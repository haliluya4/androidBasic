package com.jx.demo.business.login;

import android.text.TextUtils;

import com.jx.framework.log.XLogManager;
import com.jx.demo.R;
import com.jx.demo.business.login.service.LoginService;
import com.jx.demo.business.login.service.entity.LoginResult;
import com.jx.demo.business.login.service.entity.UserInfoResult;
import com.jx.demo.network.BaseResult;
import com.jx.demo.network.BaseResultSubscriber;
import com.jx.demo.network.RequestParamsHelper;
import com.jx.demo.network.RxResultHelper;
import com.jx.demo.network.RxSchedulersHelper;
import com.jx.demo.network.ServiceManager;
import com.jx.demo.network.exception.BusinessException;
import com.jx.demo.utils.DataCenter;
import com.jx.demo.utils.EncryptionDecryption;
import com.jx.demo.utils.RxConvertHelper;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * 登录功能业务层
 */
public class LoginPresenter implements LoginContract.Presenter {
    private static final String TAG = LoginPresenter.class.getName();

    private final LoginContract.View mLoginView;

    LoginPresenter(LoginContract.View view) {
        mLoginView = view;
        initData();
    }

    /**
     * 同步/异步初始化数据.
     * 无大量io/计算的，可以用同步，否则需要通过RxConvertHelper
     * 如果是异步初始化，需要定义相关的状态，当初始化完毕后才允许后续操作.
     */
    private void initData() {
        String loginAccount = DataCenter.getAccount();
        if (!TextUtils.isEmpty(loginAccount)) {
            RxConvertHelper.callView(() -> mLoginView.setLoginInfo(loginAccount, ""));
        }
    }

    // 登录相关状态
    private enum LoginState {
        IDLE, LOGINING, GETUSERINFO, LOGINED
    }

    private LoginState mState = LoginState.IDLE;

    private Subscription mLoginTask = null;

    @Override
    public void subscribe() {
    }

    @Override
    public void unSubscribe() {
        // 取消进行中的任务
        if (mLoginTask != null && !mLoginTask.isUnsubscribed()) {
            mLoginTask.unsubscribe();
        }
        mLoginTask = null;
        synchronized (LoginState.class) {
            // 如果还处于登录中，取消登录
            if (mState == LoginState.LOGINING) {
                mState = LoginState.IDLE;
                RxConvertHelper.callView(() -> mLoginView.hideLoading());
            }
        }
    }

    @Override
    public void login(String userName, String password) {
        synchronized (LoginState.class) {
            if (mState == LoginState.IDLE) {
                mState = LoginState.LOGINING;
                // 禁用点击和输入
                RxConvertHelper.callView(() -> mLoginView.showLoading());
                // 调用登录接口
                Map<String, Object> params = RequestParamsHelper.createNew();
                params.put("username", userName);
                params.put("password", EncryptionDecryption.md5(password));
                mLoginTask = ServiceManager.getInstance(LoginService.class).login(params)
                        .compose(RxSchedulersHelper.ioComputation())
                        .compose(RxResultHelper.handleResult())
                        .flatMap(new Func1<LoginResult, Observable<BaseResult<UserInfoResult>>>() {
                            @Override
                            public Observable<BaseResult<UserInfoResult>> call(LoginResult loginResult) {
                                synchronized (LoginState.class) {
                                    if (mState == LoginState.LOGINING) {
                                        // 保存token
                                        DataCenter.setAccessToken(loginResult.getAccessToken());
                                        DataCenter.setAccount(userName);

                                        // 获取用户信息
                                        mState = LoginState.GETUSERINFO;
                                        Map<String, Object> params = RequestParamsHelper.createNew();
                                        return ServiceManager.getInstance(LoginService.class).getUserInfo(params);
                                    } else {
                                        XLogManager.e(TAG, "登录回调太晚，当前状态 = " + mState.name());
                                    }
                                }
                                return null;
                            }
                        })
                        .compose(RxSchedulersHelper.ioComputation())
                        .compose(RxResultHelper.handleResult())
                        .subscribe(new BaseResultSubscriber<UserInfoResult>(mLoginView) {
                            @Override
                            public void onNext(UserInfoResult userInfoResult) {
                                synchronized (LoginState.class) {
                                    if (mState == LoginState.GETUSERINFO) {
                                        // 本地保存用户昵称
                                        DataCenter.setNickName(userInfoResult.getName());
                                        RxConvertHelper.callView(() -> {
                                            mLoginView.showNext();
                                            mLoginView.hideLoading();
                                        });
                                        mState = LoginState.LOGINED;
                                        mLoginTask = null;
                                    } else {
                                        XLogManager.e(TAG, "登录回调太晚，当前状态 = " + mState.name());
                                    }
                                }
                            }

                            @Override
                            protected void onBusinessError(BusinessException e) {
                                super.onBusinessError(e);
                                synchronized (LoginState.class) {
                                    if (mState == LoginState.LOGINING || mState == LoginState.GETUSERINFO) {
                                        mLoginTask = null;
                                        // 提示
                                        RxConvertHelper.callView(() -> {
                                            mLoginView.showLoginFail(e.getMessage());
                                            mLoginView.hideLoading();
                                        });

                                        // 更新状态
                                        mState = LoginState.IDLE;
                                    } else {
                                        XLogManager.e(TAG, "登录业务失败回调太晚，当前状态 = " + mState.name());
                                    }
                                }
                            }

                            @Override
                            protected void onOtherError(String msg) {
                                super.onOtherError(msg);
                                synchronized (LoginState.class) {
                                    if (mState == LoginState.LOGINING || mState == LoginState.GETUSERINFO) {
                                        mLoginTask = null;
                                        // 提示
                                        RxConvertHelper.callView(() -> {
                                            mLoginView.showLoginFail(msg);
                                            mLoginView.hideLoading();
                                        });

                                        // 更新状态
                                        mState = LoginState.IDLE;
                                    } else {
                                        XLogManager.e(TAG, "登录其他失败回调太晚，当前状态 = " + mState.name());
                                    }
                                }
                            }

                        });
            } else {
                XLogManager.e(TAG, "登录方法调用状态异常，当前状态 = " + mState.name());
                CrashReport.postCatchedException(new RuntimeException("登录方法调用状态异常，当前状态 = " + mState.name()));
            }
        }
    }

    @Override
    public void reset() {
        // 取消进行中的任务
        if (mLoginTask != null && !mLoginTask.isUnsubscribed()) {
            mLoginTask.unsubscribe();
        }
        mLoginTask = null;
        synchronized (LoginState.class) {
            // 重置状态与页面数据
            if (mState != LoginState.IDLE) {
                mState = LoginState.IDLE;
                RxConvertHelper.callView(() -> {
                    String loginAccount = DataCenter.getAccount();
                    if (TextUtils.isEmpty(loginAccount)) {
                        loginAccount = "";
                    }
                    mLoginView.setLoginInfo(loginAccount, "");
                    mLoginView.hideLoading();
                });
            }
            // 提示登录过期
            RxConvertHelper.callView(() -> mLoginView.showTip(mLoginView.getContext().getString(R.string.token_invalid)));
        }
    }
}
