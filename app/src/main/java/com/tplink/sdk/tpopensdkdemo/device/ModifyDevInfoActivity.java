package com.tplink.sdk.tpopensdkdemo.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tplink.sdk.tpopensdk.openctx.IPCDevice;
import com.tplink.sdk.tpopensdk.openctx.IPCReqListener;
import com.tplink.sdk.tpopensdk.openctx.IPCReqResponse;
import com.tplink.sdk.tpopensdkdemo.R;

public class ModifyDevInfoActivity extends DeviceSettingDetailInfoActivity {

    public final static String CHANGE_TYPE = "type";
    public final static int CHANGE_ALIAS = 1;
    public final static int CHANGE_PASSWORD = 2;

    private EditText mEdt;
    private int mType;
    private int mReqSetPwdID;
    private int mReqLoginID;
    private int mReqConnectDevID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_modify_alias);
        initView();
    }

    @Override
    public void initData() {
        super.initData();
        mType = getIntent().getIntExtra(CHANGE_TYPE, 0);
    }

    @Override
    public void initView() {
        super.initView();
        TextView rightTv = findViewById(R.id.title_bar_right_tv);
        rightTv.setOnClickListener(this);
        rightTv.setText(R.string.common_save);
        rightTv.setVisibility(View.VISIBLE);
        TextView centerTv = findViewById(R.id.title_bar_center_tv);
        switch (mType) {
            case CHANGE_ALIAS:
                centerTv.setText(R.string.modify_alias);
                break;
            case CHANGE_PASSWORD:
                centerTv.setText(R.string.setting_modify_admin_password);
                break;
        }
        mEdt = findViewById(R.id.alias_edt);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_right_tv:
                switch (mType) {
                    case CHANGE_ALIAS:
                        mDevCtx.reqModifyAlias(mDev, new IPCReqListener() {
                            @Override
                            public int callBack(IPCReqResponse ipcReqResponse) {
                                commonCallBack(ipcReqResponse);
                                return 0;
                            }
                        }, mEdt.getText().toString());
                        showLoading(null);
                        break;
                    case CHANGE_PASSWORD:
                        mReqSetPwdID = mDevCtx.reqSetPassword(mDev, new IPCReqListener() {
                            @Override
                            public int callBack(IPCReqResponse ipcReqResponse) {
                                commonCallBack(ipcReqResponse);
                                return 0;
                            }
                        }, mDev.getUserName(), mEdt.getText().toString());
                        showLoading(null);
                        break;
                }
                break;
        }
    }

    @Override
    public void onReqSuccess(IPCReqResponse response) {
        if (response.mReqID == mReqSetPwdID) {
            mReqLoginID = mDevCtx.reqLogin(mDev, mEdt.getText().toString(), new IPCReqListener() {
                @Override
                public int callBack(IPCReqResponse ipcReqResponse) {
                    commonCallBack(ipcReqResponse);
                    return 0;
                }
            });
            if (mReqLoginID > 0) {
                showLoading(null);
            }
        } else if (response.mReqID == mReqLoginID) {
            mReqConnectDevID = mDevCtx.reqConnectDev(mDev, new IPCReqListener() {
                @Override
                public int callBack(IPCReqResponse ipcReqResponse) {
                    commonCallBack(ipcReqResponse);
                    return 0;
                }
            });
            if (mReqConnectDevID > 0) {
                showLoading(null);
            }
        } else if (response.mReqID == mReqConnectDevID) {
            super.onReqSuccess(response);
        }
    }

    public static void startActivity(Activity activity, int type, IPCDevice dev) {
        Intent intent = new Intent(activity, ModifyDevInfoActivity.class);
        intent.putExtra(DEVICE_INFO, dev);
        intent.putExtra(CHANGE_TYPE, type);
        activity.startActivity(intent);
    }
}
