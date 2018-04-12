package com.jx.demo.business.login;

import com.jx.framework.base.BasePresenter;
import com.jx.framework.base.BaseView;

/**
 * 登录协议，定义了登录功能View层与Presenter层的接口
 */
interface LoginContract {

    /**
     * 所有对该接口的调用都需要保证在UI线程（除了BaseView.getContext）
     */
    interface View extends BaseView<Presenter> {

        /**
         * 设置登录信息
         *
         * @param userName 用户名
         * @param password 密码
         */
        void setLoginInfo(String userName, String password);

        /**
         * 设置是否允许点击登录
         *
         * @param isEnabled true-允许
         */
        void setLoginAvailability(boolean isEnabled);

        /**
         * 跳转到功能选择界面
         */
        void showNext();

        /**
         * 提示登录失败信息
         *
         * @param msg 提示内容
         */
        void showLoginFail(String msg);

        void showLoading();

        void hideLoading();
    }

    /**
     * 所有对该接口的调用都需要保证在非UI线程
     */
    interface Presenter extends BasePresenter {

        /**
         * 登录
         *
         * @param userName 用户名
         * @param password 密码
         */
        void login(String userName, String password);

        /**
         * 重置状态，用于注销或登录失效后重新启动登录页时重置数据及页面状态
         */
        void reset();
    }
}
