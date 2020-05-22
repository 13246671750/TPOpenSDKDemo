package com.tplink.sdk.tpopensdkdemo.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tplink.sdk.tpopensdk.openctx.IPCDeviceContext;
import com.tplink.sdk.tpopensdk.openctx.IPCOnboardContext;
import com.tplink.sdk.tpopensdk.openctx.IPCReqListener;
import com.tplink.sdk.tpopensdk.openctx.IPCReqResponse;
import com.tplink.sdk.tpopensdkdemo.R;
import com.tplink.sdk.tpopensdkdemo.common.BaseActivity;

public class DeviceAddBySmartConfigActivity extends BaseActivity {

    private IPCDeviceContext mDevCtx;
    private IPCOnboardContext mOnboardCtx;
    private EditText mQrcodeEdt;
    private EditText mSSIDEdt;
    private EditText mPwdEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_device_add_by_smart_config);
        initView();
    }

    private void initData() {
        mDevCtx = mSDKCtx.getDevCtx();
        mOnboardCtx = mSDKCtx.getOnboardCtx();
    }

    private void initView() {
        findViewById(R.id.add_btn).setOnClickListener(this);
        mQrcodeEdt = findViewById(R.id.qrcode_edt);
        mSSIDEdt = findViewById(R.id.ssid_edt);
        mPwdEdt = findViewById(R.id.smartconfig_pwd_edt);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_btn:
                mOnboardCtx.setOnboardWifiInfo(mSSIDEdt.getText().toString(), mPwdEdt.getText().toString());
                mOnboardCtx.onboardSetQRCode(mQrcodeEdt.getText().toString(), 0);
                mOnboardCtx.reqSmartConfig(new IPCReqListener() {
                    @Override
                    public int callBack(final IPCReqResponse ipcReqResponse) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissLoading();
                                if (ipcReqResponse.mError == 0) {
                                    showToast(getString(R.string.smartconfig_success));
                                } else {
                                    showToast(getString(R.string.smartconfig_failure));
                                }
                            }
                        });
                        return 0;
                    }
                }, 90);
                showLoading(null);
                break;
        }
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, DeviceAddBySmartConfigActivity.class);
        activity.startActivity(intent);
    }
}
