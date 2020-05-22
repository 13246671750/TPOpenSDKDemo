package com.tplink.sdk.tpopensdkdemo.device;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tplink.sdk.tpopensdk.bean.IPCDeviceDefines;
import com.tplink.sdk.tpopensdk.openctx.IPCReqListener;
import com.tplink.sdk.tpopensdk.openctx.IPCReqResponse;
import com.tplink.sdk.tpopensdkdemo.R;

public class TimeRebootActivity extends DeviceSettingDetailInfoActivity {

    IPCDeviceDefines.IPCRebootInfo mRebootInfo;
    TextView mEnableTv;
    TextView mDayTv;
    TextView mTimeTv;
    TextView mModifyTimeTv;
    TextView mModifyDayTv;
    EditText mEdt;
    int mEnable;
    int mRebootDay;
    String mRebootTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_time_reboot);
        initView();
    }

    public void initData() {
        super.initData();
        mRebootInfo = mDev.getTimingRebootInfo();
        mEnable = mRebootInfo.getEnabled();
        mRebootDay = mRebootInfo.getiDay();
        mRebootTime = mRebootInfo.getStrTime();
    }

    public void initView() {
        ((TextView)findViewById(R.id.title_bar).findViewById(R.id.title_bar_center_tv)).setText(R.string.time_reboot_detail);
        mEnableTv = findViewById(R.id.time_reboot_tv);
        mEnableTv.setText(getString(R.string.reboot_status) + getString(mEnable == 1 ? R.string.set_open : R.string.set_close));
        mEnableTv.setOnClickListener(this);
        mDayTv = findViewById(R.id.time_reboot_day_tv);
        mDayTv.setText(getString(R.string.reboot_day) + String.valueOf(mRebootInfo.getiDay()));
        mDayTv.setOnClickListener(this);
        mTimeTv = findViewById(R.id.time_reboot_time_tv);
        mTimeTv.setText(getString(R.string.reboot_time) + mRebootInfo.getStrTime());
        mTimeTv.setOnClickListener(this);
        findViewById(R.id.time_reboot_confirm_tv).setOnClickListener(this);
        findViewById(R.id.time_reboot_now_tv).setOnClickListener(this);
        mEdt = findViewById(R.id.enter_time_edt);
        mModifyTimeTv = findViewById(R.id.time_confirm_tv);
        mModifyTimeTv.setOnClickListener(this);
        mModifyDayTv = findViewById(R.id.day_confirm_tv);
        mModifyDayTv.setOnClickListener(this);
        findViewById(R.id.time_reboot_now_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_reboot_tv:
                mEnable = mEnable == 1 ? 0 : 1;
                mEnableTv.setText(getString(R.string.reboot_status) + getString(mEnable == 1 ? R.string.set_open : R.string.set_close));
                break;
            case R.id.time_reboot_day_tv:
                mEdt.setVisibility(View.VISIBLE);
                mModifyDayTv.setVisibility(View.VISIBLE);
                mModifyTimeTv.setVisibility(View.GONE);
                break;
            case R.id.time_reboot_time_tv:
                mEdt.setVisibility(View.VISIBLE);
                mModifyDayTv.setVisibility(View.GONE);
                mModifyTimeTv.setVisibility(View.VISIBLE);
                break;
            case R.id.time_confirm_tv:
                mRebootTime = mEdt.getText().toString();
                mTimeTv.setText(getString(R.string.reboot_time) + mEdt.getText().toString());
                mEdt.setVisibility(View.GONE);
                mModifyDayTv.setVisibility(View.GONE);
                mModifyTimeTv.setVisibility(View.GONE);
                break;
            case R.id.day_confirm_tv:
                try {
                    mRebootDay = Integer.valueOf(mEdt.getText().toString());
                } catch (Exception e) {
                    showToast(getString(R.string.enter_common_invalid));
                }
                mDayTv.setText(getString(R.string.reboot_day) + mEdt.getText().toString());
                mEdt.setVisibility(View.GONE);
                mModifyDayTv.setVisibility(View.GONE);
                mModifyTimeTv.setVisibility(View.GONE);
                break;
            case R.id.time_reboot_confirm_tv:
                mDevCtx.reqSetTimingReboot(mDev, new IPCReqListener() {
                            @Override
                            public int callBack(final IPCReqResponse response) {
                                commonCallBack(response);
                                return 0;
                            }
                        }, mEnable, mRebootDay, mRebootTime);
                showLoading(null);
                break;
            case R.id.time_reboot_now_tv:
                mDevCtx.reqReboot(mDev, new IPCReqListener() {
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
