package com.jx.demo.utils;

import android.support.annotation.NonNull;
import android.view.View;

import butterknife.ButterKnife;

/**
 * ButterKnife辅助类，封装了一些基础功能，简化使用
 */
public final class ButterKnifeHelper {
    public static final ButterKnife.Setter<View, Boolean> ENABLED = new ButterKnife.Setter<View, Boolean>() {
        @Override
        public void set(@NonNull View view, Boolean value, int index) {
            view.setEnabled(value);
        }
    };
}
