package com.tplink.sdk.tpopensdkdemo.device;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tplink.sdk.tpopensdk.TPOpenSDK;
import com.tplink.sdk.tpopensdk.openctx.IPCDevice;
import com.tplink.sdk.tpopensdk.openctx.IPCDeviceContext;
import com.tplink.sdk.tpopensdk.openctx.IPCReqListener;
import com.tplink.sdk.tpopensdk.openctx.IPCReqResponse;
import com.tplink.sdk.tpopensdkdemo.R;
import com.tplink.sdk.tpopensdkdemo.common.BaseActivity;

public class DeviceAddByManualActivity extends BaseActivity {
    IPCDeviceContext mDevCtx = null;
    IPCDevice mDev = null;
    private EditText mIPEdt;
    private EditText mPortEdt;
    private EditText mPwdEdt;
    private TextView mAddDeviceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_device_add_by_manual);
        initView();
    }

    private void initData() {
        mDevCtx = TPOpenSDK.getInstance().getSDKContext().getDevCtx();
    }

    private void initView() {
        ((TextView) findViewById(R.id.title_bar_center_tv)).setText(R.string.manual_add);

        mIPEdt = findViewById(R.id.ip_edit);
        mPortEdt = findViewById(R.id.port_edit);
        mPwdEdt = findViewById(R.id.password_edit);
        mAddDeviceBtn = findViewById(R.id.add_dev_btn);
        mAddDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIPEdt.getText().toString().isEmpty() || mPortEdt.getText().toString().isEmpty()) {
                    showToast(getString(R.string.enter_common_invalid));
                    return;
                }
                mDev = new IPCDevice(mDevCtx.initDev(mIPEdt.getText().toString(), Integer.valueOf(mPortEdt.getText().toString())));
                mDevCtx.reqLogin(mDev, mPwdEdt.getText().toString(), new IPCReqListener() {
                    @Override
                    public int callBack(final IPCReqResponse ipcReqResponse) {
                        if (ipcReqResponse.mError == 0) {
                            mAddDeviceBtn.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(DeviceAddByManualActivity.this, "reqLogin success!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            mDevCtx.reqConnectDev(mDev, new IPCReqListener() {
                                @Override
                                public int callBack(final IPCReqResponse ipcReqResponse) {
                                    if (ipcReqResponse.mError == 0) {
                                        mAddDeviceBtn.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                dismissLoading();
                                                Toast.makeText(DeviceAddByManualActivity.this, "reqConnectDev success!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        DeviceActivity.DeviceList.add(mDev);
                                        finish();
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dismissLoading();
                                                showToast("reqResponse error: " + ipcReqResponse.mError + "\n reqResponse rval: " + ipcReqResponse.mRval);
                                            }
                                        });
                                    }
                                    return 0;
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismissLoading();
                                    showToast("reqResponse error: " + ipcReqResponse.mError + "\n reqResponse rval: " + ipcReqResponse.mRval);
                                }
                            });
                        }
                        return 0;
                    }
                });
            showLoading(null);
            }
        });
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DeviceAddByManualActivity.class);
        context.startActivity(intent);
    }
}
