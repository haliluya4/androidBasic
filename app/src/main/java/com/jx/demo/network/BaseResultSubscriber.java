package com.jx.demo.network;

import com.google.gson.JsonParseException;
import com.jx.framework.base.BaseView;
import com.jx.framework.log.XLogManager;
import com.jx.demo.R;
import com.jx.demo.base.GoToLoginView;
import com.jx.demo.network.exception.BusinessException;
import com.jx.demo.network.exception.ReLoginException;
import com.jx.demo.network.exception.ServerDataInvalidException;
import com.jx.demo.utils.RxConvertHelper;

import org.json.JSONException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.HttpException;
import rx.Subscriber;

/**
 * 结果订阅者基类，将一些通用异常进行了封装，业务上可以直接new一个，实现业务异常方法。
 * 如果需要处理所有错误情况，也可直接复写onError(Throwable e)方法，但要记得调super的。
 */
public abstract class BaseResultSubscriber<T> extends Subscriber<T> {
    private final BaseView mBaseView;

    public BaseResultSubscriber(BaseView view) {
        this.mBaseView = view;
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(final Throwable e) {
        if (e instanceof UnknownHostException) {
            // 无法解析服务器域名
            XLogManager.e("Network", "服务器异常 " + e.getMessage(), e);
            notifyOtherError(R.string.dns_error);
        } else if (e instanceof IOException) {
            // 网络连接异常
            XLogManager.e("Network", "网络连接异常 " + e.getMessage(), e);
            notifyOtherError(R.string.network_error);
        } else if (e instanceof HttpException) {
            // http请求返回错误码
            XLogManager.e("Network", "服务器异常 " + e.getMessage(), e);
            notifyOtherError(R.string.server_error);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException
                || e instanceof NumberFormatException) {
            XLogManager.e("Network", "服务器数据异常", e);
            notifyOtherError(R.string.response_error);
        } else if (e instanceof ReLoginException) {
            XLogManager.e("Network", "Token失效 " + e.getMessage());
            onReLoginError();
            if (mBaseView instanceof GoToLoginView) {
                // 切到登录页面
                RxConvertHelper.callView(() -> ((GoToLoginView) mBaseView).gotoLogin());
            } else {
                onOtherError(e.getMessage());
            }
        } else if (e instanceof ServerDataInvalidException) {
            // 服务端数据异常
            XLogManager.e("Network", "服务端数据异常, 类 "
                    + ((ServerDataInvalidException) e).getResultClass()
                    + " , 原因 " + e.getMessage());
            notifyOtherError(R.string.response_error_with_reason, e.getMessage());
        } else if (e instanceof BusinessException) {
            // 业务异常
            onBusinessError((BusinessException) e);
        } else {
            // 未知异常
            XLogManager.e("Network", "未知异常", e);
            notifyOtherError(R.string.unknown_error);
        }
    }

    private void notifyOtherError(int string, Object... data) {
        String tips;
        if (data == null || data.length == 0) {
            tips = mBaseView.getContext().getString(string);
        } else {
            tips = mBaseView.getContext().getString(string, data);
        }
        onOtherError(tips);
    }

    protected void onBusinessError(BusinessException e) {
        // 待具体实现覆盖
    }

    /**
     * 如果有业务做了并行操作，在监听到该异常后，需要停止所有后续处理，由框架自主切换页面
     */
    protected void onReLoginError() {
        // 待具体实现覆盖
    }

    protected void onOtherError(String msg) {
        // 待具体实现覆盖
    }
}
