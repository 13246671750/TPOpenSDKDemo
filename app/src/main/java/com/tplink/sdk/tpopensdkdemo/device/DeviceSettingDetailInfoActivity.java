package com.tplink.sdk.tpopensdkdemo.device;

import android.app.Activity;
import android.content.Intent;

import com.tplink.sdk.tpopensdk.openctx.IPCDevice;
import com.tplink.sdk.tpopensdk.openctx.IPCDeviceContext;
import com.tplink.sdk.tpopensdkdemo.common.BaseActivity;

/**
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * @author Li Wei
 * @ClassName: DeviceSettingDetailInfoActivity
 * @Description: Version 1.0.0, 2018-10-19, Li Wei create file.
 */

public class DeviceSettingDetailInfoActivity extends BaseActivity {
    public static final String DEVICE_INFO = "DEV";
    public IPCDevice mDev;
    IPCDeviceContext mDevCtx;

    public void initData() {
        mDevCtx = mSDKCtx.getDevCtx();
        mDev = getIntent().getParcelableExtra(DEVICE_INFO);
    }

    public void initView() {

    }

    public static void startActivity(Activity activity, Class targetClass, IPCDevice dev) {
        Intent intent = new Intent(activity, targetClass);
        intent.putExtra(DEVICE_INFO, dev);
        activity.startActivity(intent);
    }
}
