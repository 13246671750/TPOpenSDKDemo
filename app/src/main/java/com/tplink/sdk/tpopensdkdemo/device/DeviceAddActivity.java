package com.tplink.sdk.tpopensdkdemo.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tplink.sdk.tpopensdkdemo.R;
import com.tplink.sdk.tpopensdkdemo.common.BaseActivity;

public class DeviceAddActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_device_add);
        initView();
    }

    private void initData() {

    }

    private void initView() {
        ((TextView)findViewById(R.id.title_bar_center_tv)).setText(R.string.device_add_device);
        TextView autoTv = findViewById(R.id.auto_discover_tv);
        autoTv.setText(R.string.auto_discover);
        autoTv.setOnClickListener(this);

        TextView manualTv = findViewById(R.id.Manual_tv);
        manualTv.setText(R.string.manual_add);
        manualTv.setOnClickListener(this);

        TextView smartconfigTv = findViewById(R.id.smartconfig_tv);
        smartconfigTv.setText(R.string.smartconfig);
        smartconfigTv.setOnClickListener(this);

        TextView softapTv = findViewById(R.id.softap_tv);
        softapTv.setText(R.string.softAp);
        softapTv.setOnClickListener(this);

        TextView voiceConfigTv = findViewById(R.id.voice_config_tv);
        voiceConfigTv.setText(R.string.voice_config);
        voiceConfigTv.setOnClickListener(this);

        TextView wifiDirectTv = findViewById(R.id.wifiDirect_tv);
        wifiDirectTv.setText(R.string.wifidirect);
        wifiDirectTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.auto_discover_tv:
                DeviceAutoDiscoverActivity.startActivity(this);
                break;
            case R.id.Manual_tv:
                DeviceAddByManualActivity.startActivity(this);
                break;
            case R.id.smartconfig_tv:
                DeviceAddBySmartConfigActivity.startActivity(this);
                break;
            case R.id.softap_tv:
                DeviceAddSoftApActivity.startActivity(this);
                break;
            case R.id.voice_config_tv:
                break;
        }
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, DeviceAddActivity.class);
        activity.startActivity(intent);
    }
}
