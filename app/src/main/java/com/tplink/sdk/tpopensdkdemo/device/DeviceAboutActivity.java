package com.tplink.sdk.tpopensdkdemo.device;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tplink.sdk.tpopensdk.openctx.IPCReqListener;
import com.tplink.sdk.tpopensdk.openctx.IPCReqResponse;
import com.tplink.sdk.tpopensdkdemo.R;

public class DeviceAboutActivity extends DeviceSettingDetailInfoActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_device_about);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        ((TextView)findViewById(R.id.title_bar_center_tv)).setText(R.string.dev_about);

        ((TextView)findViewById(R.id.userName_layout).findViewById(R.id.about_name_tv)).setText(R.string.setting_about_recorder_manager_name);
        ((TextView)findViewById(R.id.password_layout).findViewById(R.id.about_name_tv)).setText(R.string.setting_about_recorder_manager_password);
        ((TextView)findViewById(R.id.Model_layout).findViewById(R.id.about_name_tv)).setText(R.string.setting_about_device_model);
        ((TextView)findViewById(R.id.HardwareVersion_layout).findViewById(R.id.about_name_tv)).setText(R.string.setting_about_hardware_info);
        ((TextView)findViewById(R.id.SoftwareVersion_layout).findViewById(R.id.about_name_tv)).setText(R.string.setting_about_software_info);
        ((TextView)findViewById(R.id.Address_layout).findViewById(R.id.about_name_tv)).setText(R.string.setting_about_recorder_ip_address);
        ((TextView)findViewById(R.id.VideoPort_layout).findViewById(R.id.about_name_tv)).setText(R.string.setting_about_recorder_port);
        // TODO: 2018/10/19 端口号不应该是video_port

        ((TextView)findViewById(R.id.userName_layout).findViewById(R.id.about_value_tv)).setText(mDev.getUserName());
        ((TextView)findViewById(R.id.password_layout).findViewById(R.id.about_value_tv)).setText(mDev.getPassword());
        ((TextView)findViewById(R.id.Model_layout).findViewById(R.id.about_value_tv)).setText(mDev.getModel());
        ((TextView)findViewById(R.id.HardwareVersion_layout).findViewById(R.id.about_value_tv)).setText(mDev.getHardwareVersion());
        ((TextView)findViewById(R.id.Address_layout).findViewById(R.id.about_value_tv)).setText(mDev.getAddress());
        ((TextView)findViewById(R.id.VideoPort_layout).findViewById(R.id.about_value_tv)).setText(String.valueOf(mDev.getVideoPort()));
        ((TextView)findViewById(R.id.SoftwareVersion_layout).findViewById(R.id.about_value_tv)).setText(mDev.getSoftwareVersion());

        if (mDev.supportChangePassword(1, -1)) {
            TextView changePwdTv = findViewById(R.id.password_layout).findViewById(R.id.about_operate_tv);
            changePwdTv.setText(R.string.setting_modify_admin_password);
            changePwdTv.setOnClickListener(this);
            changePwdTv.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.reset_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_operate_tv:
                ModifyDevInfoActivity.startActivity(DeviceAboutActivity.this, ModifyDevInfoActivity.CHANGE_PASSWORD, mDev);
                break;
            case R.id.reset_tv:
                mDevCtx.reqReset(mDev, new IPCReqListener() {
                    @Override
                    public int callBack(IPCReqResponse ipcReqResponse) {
                        commonCallBack(ipcReqResponse);
                        return 0;
                    }
                });
                showLoading(null);
                break;
        }
    }
}
