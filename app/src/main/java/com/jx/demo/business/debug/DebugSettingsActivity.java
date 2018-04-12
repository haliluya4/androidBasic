package com.jx.demo.business.debug;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.jx.framework.log.XLogManager;
import com.jx.demo.R;
import com.jx.demo.network.Api;
import com.jx.demo.utils.Settings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * 调试界面，可通过以下命令打开（如果是默认包名，后面的前缀可以省略）
 * adb shell am start -n {applicationId}/com.jx.demo.business.debug.DebugSettingsActivity
 */
public class DebugSettingsActivity extends Activity {

    private static final String LOG_TAG = "DebugSettings";

    @BindView(R.id.cb_debug_mode)
    CheckBox mForceDebugMode;

    @BindView(R.id.cb_dev_device)
    CheckBox mDevDevice;

    @BindView(R.id.sp_env)
    Spinner mEnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_settings);
        ButterKnife.bind(this);
        initData();
    }

    // 所有环境枚举的数组
    private Api.Environment[] mEnvs;

    private void initData() {
        mForceDebugMode.setChecked(Settings.isForceDebug());
        mDevDevice.setChecked(Settings.isDevDevice());

        mEnvs = Api.Environment.values();
        String[] envList = new String[mEnvs.length];
        for (int i = 0; i < mEnvs.length; i++) {
            envList[i] = mEnvs[i].name();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, envList);
        mEnv.setAdapter(adapter);
        mEnv.setSelection(getCurrentEnvIndex());
    }

    /**
     * 获取当前环境索引
     */
    private int getCurrentEnvIndex() {
        for (int i = 0; i < mEnvs.length; i++) {
            if (Api.sEnvironment == mEnvs[i]) {
                return i;
            }
        }
        return 0;
    }

    @OnCheckedChanged(R.id.cb_debug_mode)
    void onDebugStateChanged(CompoundButton buttonView, boolean isChecked) {
        Settings.setIsForceDebug(isChecked);
    }

    @OnCheckedChanged(R.id.cb_dev_device)
    void onDebugDeviceStateChanged(CompoundButton buttonView, boolean isChecked) {
        Settings.setIsDevDevice(isChecked);
    }

    @OnItemSelected(R.id.sp_env)
    void onEnvItemSelected(int position) {
        if (mEnvs == null) {
            return;
        }
        if (position < 0 || position >= mEnvs.length) {
            XLogManager.e(LOG_TAG, "Invalid env index: " + position);
            return;
        }

        int old = getCurrentEnvIndex();
        if (position != old) {
            Settings.setCurrentEnv(mEnvs[position]);
        } else {
            XLogManager.d(LOG_TAG, "No change");
        }
    }
}
