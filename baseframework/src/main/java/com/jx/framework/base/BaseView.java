package com.jx.framework.base;

import android.content.Context;

public interface BaseView<T> {

    /**
     * 返回上下文对象
     * @return 上下文对象
     */
    Context getContext();

    /**
     * 提示信息
     * @param tip 待显示内容
     */
    void showTip(String tip);
}
