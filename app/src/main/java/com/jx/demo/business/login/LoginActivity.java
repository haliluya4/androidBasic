package com.jx.demo.business.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jx.demo.R;
import com.jx.demo.business.second.SecondActivity;
import com.jx.demo.utils.ButterKnifeHelper;
import com.jx.demo.utils.RxConvertHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录界面
 */
public class LoginActivity extends Activity implements LoginContract.View {
    private LoginContract.Presenter mPresenter;

    @BindView(R.id.username)
    EditText mUserNameInput;
    @BindView(R.id.password)
    EditText mPasswordInput;
    @BindView(R.id.sign_in_button)
    LinearLayout mLoginButton;
    @BindView(R.id.login_progress)
    ProgressBar mLoginProgress;

    @BindViews({R.id.username, R.id.password, R.id.sign_in_button})
    List<View> mOperateElements;

    private Dialog waitDialog;

    private static final long KEY_THRESHOLD = 100;  // 按键最快100ms触发一次

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mPresenter = new LoginPresenter(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 重新登录，重置状态
        mPresenter.reset();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.subscribe();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unSubscribe();
    }

    @OnClick(R.id.sign_in_button)
    void onLoginClick() {
        String userName = mUserNameInput.getText().toString();
        String password = mPasswordInput.getText().toString();
        // 简单的内容校验，归View层负责
        if (userName.trim().length() == 0) {
            showTip(getString(R.string.prompt_username));
            return;
        } else if (password.length() == 0) {
            showTip(getString(R.string.prompt_password));
            return;
        }
        RxConvertHelper.callPresenter(() -> mPresenter.login(userName, password));
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showTip(String tip) {
        Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setLoginInfo(String userName, String password) {
        mUserNameInput.setText(userName);
        mPasswordInput.setText(password);
        // 光标在最后
        Selection.setSelection(mUserNameInput.getText(), userName.length());
        Selection.setSelection(mPasswordInput.getText(), password.length());
    }

    @Override
    public void setLoginAvailability(boolean isEnabled) {
        mLoginProgress.setVisibility(isEnabled ? View.GONE : View.VISIBLE);
        // 批量切换是否可用状态，防止登录中用户修改内容，重复点击
        ButterKnife.apply(mOperateElements, ButterKnifeHelper.ENABLED, isEnabled);
    }

    @Override
    public void showNext() {
        // TODO 换成自己的下一个界面
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    @Override
    public void showLoginFail(String msg) {
        showTip(msg);
    }

    @Override
    public void showLoading() {
        // TODO 换成自己的实现
    }

    @Override
    public void hideLoading() {
        // TODO 换成自己的实现
    }
}

