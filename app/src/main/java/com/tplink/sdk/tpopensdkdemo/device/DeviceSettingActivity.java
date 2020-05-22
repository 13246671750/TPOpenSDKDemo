package com.tplink.sdk.tpopensdkdemo.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tplink.sdk.tpopensdk.bean.IPCDeviceDefines;
import com.tplink.sdk.tpopensdk.openctx.IPCDevice;
import com.tplink.sdk.tpopensdk.openctx.IPCDeviceContext;
import com.tplink.sdk.tpopensdk.openctx.IPCReqListener;
import com.tplink.sdk.tpopensdk.openctx.IPCReqResponse;
import com.tplink.sdk.tpopensdkdemo.R;
import com.tplink.sdk.tpopensdkdemo.common.BaseActivity;

public class DeviceSettingActivity extends BaseActivity implements View.OnClickListener{

    public static final String IPC_DEVICE = "device";
    private IPCDeviceContext mDevCtx;
    private IPCDevice mDev;
    private int mClickID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_device_setting);
    }

    private void initData() {
        mDevCtx = mSDKCtx.getDevCtx();
        mDev = getIntent().getParcelableExtra(IPC_DEVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        ((TextView)findViewById(R.id.title_bar_center_tv)).setText(R.string.dev_settings);

        TextView nameTv = findViewById(R.id.alias_layout).findViewById(R.id.module_capability_tv);
        nameTv.setText(mDev.getAlias());
        findViewById(R.id.alias_layout).setOnClickListener(this);

        ((TextView)findViewById(R.id.dev_about_layout).findViewById(R.id.module_capability_tv)).setText(R.string.dev_about);

        findViewById(R.id.dev_about_layout).setOnClickListener(this);

        // TODO: 2018/10/18 常量定义

        if (mDev.supportDevAlarm(1, -1)) {
            // showCapability(R.id.support_DevAlarm_layout, R.string.DevAlarm);
        }

        if (mDev.supportRecordPlan(1, -1)) {
            showCapability(R.id.support_RecordPlan_layout, R.string.RecordPlan);
        }

        if (mDev.supportLocalStorage(1, -1)) {
            showCapability(R.id.support_LocalStorage_layout, R.string.LocalStorage);
        }

        /*
        if (mDev.supportVoiceCallMode(1, -1)) {
            showCapability(R.id.support_VoiceCallMode_layout, R.string.VoiceCallMode);
        }
        */

        if (mDev.supportMsgPush(1, -1)) {
            //showCapability(R.id.support_MsgPush_layout, R.string.MsgPush);
        }

        if (mDev.supportTimingReboot(1, -1)) {
            showCapability(R.id.support_TimingReboot_layout, R.string.TimingReboot);
        }

        if (mDev.supportSpeakerVolume(1, -1)) {
            showCapability(R.id.support_SpeakerVolume_layout, R.string.SpeakerVolume);
        }

        if (mDev.supportWifiConnectionInfo(1, -1)) {
            // showCapability(R.id.support_WifiConnectionInfo_layout, R.string.WifiConnectionInfo);
            // IPCDeviceDefines.IPCWifiConnectInfo wifiConnectInfo = mDev.getWifiConnectInfo();
            /* ((TextView)findViewById(R.id.support_WifiConnectionInfo_layout)
                    .findViewById(R.id.support_capability_tv)).setText(
                            wifiConnectInfo.getStrSsid() + " " + wifiConnectInfo.getLinktype() + " " + wifiConnectInfo.getRssi());
            */
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dev_about_layout:
                DeviceAboutActivity.startActivity(DeviceSettingActivity.this, DeviceAboutActivity.class, mDev);
                break;
            case R.id.alias_layout:
                ModifyDevInfoActivity.startActivity(DeviceSettingActivity.this, ModifyDevInfoActivity.CHANGE_ALIAS, mDev);
                break;
            case R.id.support_DevAlarm_layout:
                // TODO: 2018/10/18
                break;
            case R.id.support_RecordPlan_layout:
                mClickID = R.id.support_RecordPlan_layout;
                mDevCtx.reqGetRecordPlan(mDev, new IPCReqListener() {
                    @Override
                    public int callBack(IPCReqResponse ipcReqResponse) {
                        commonCallBack(ipcReqResponse);
                        IPCDeviceDefines.IPCRecordPlan recordPlan = mDev.getRecordPlan();
                        return 0;
                    }
                }, -1);
                showLoading(null);
                break;
            case R.id.support_LocalStorage_layout:
                StorageInfoActivity.startActivity(DeviceSettingActivity.this, StorageInfoActivity.class, mDev);
                break;
            case R.id.support_MsgPush_layout:
                // TODO: 2018/10/18
                break;
            case R.id.support_TimingReboot_layout:
                mClickID = R.id.support_TimingReboot_layout;
                mDevCtx.reqGetTimingReboot(mDev, new IPCReqListener() {
                    @Override
                    public int callBack(IPCReqResponse ipcReqResponse) {
                        commonCallBack(ipcReqResponse);
                        return 0;
                    }
                });
                showLoading(null);
                break;
            case R.id.support_SpeakerVolume_layout:
                VolumeSetActivity.startActivity(DeviceSettingActivity.this, VolumeSetActivity.class, mDev);
                break;
            case R.id.support_WifiConnectionInfo_layout:
                // TODO: 2018/10/18
                break;
        }
    }

    @Override
    public void onReqSuccess(IPCReqResponse response) {
        switch (mClickID) {
            case R.id.support_TimingReboot_layout:
                TimeRebootActivity.startActivity(DeviceSettingActivity.this, TimeRebootActivity.class, mDev);
                break;
            case R.id.support_RecordPlan_layout:
                RecordPlanActivity.startActivity(DeviceSettingActivity.this, RecordPlanActivity.class, mDev);
                break;
        }
    }

    private void showCapability(final int ResID, final int strResID) {
        findViewById(ResID).setVisibility(View.VISIBLE);
        ((TextView)findViewById(ResID)
                .findViewById(R.id.module_capability_tv)).setText(strResID);
        findViewById(ResID).setOnClickListener(DeviceSettingActivity.this);
    }

    public static void startActivity(Activity activity, IPCDevice dev) {
        Intent intent = new Intent(activity, DeviceSettingActivity.class);
        intent.putExtra(IPC_DEVICE, dev);
        activity.startActivity(intent);
    }
}
