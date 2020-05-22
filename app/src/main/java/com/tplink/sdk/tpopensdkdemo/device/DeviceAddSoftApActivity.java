package com.tplink.sdk.tpopensdkdemo.device;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tplink.sdk.tpopensdk.bean.IPCDeviceDefines;
import com.tplink.sdk.tpopensdk.common.TPSDKCommon;
import com.tplink.sdk.tpopensdk.openctx.IPCDevice;
import com.tplink.sdk.tpopensdk.openctx.IPCDeviceContext;
import com.tplink.sdk.tpopensdk.openctx.IPCReqData;
import com.tplink.sdk.tpopensdk.openctx.IPCReqListener;
import com.tplink.sdk.tpopensdk.openctx.IPCReqResponse;
import com.tplink.sdk.tpopensdkdemo.R;
import com.tplink.sdk.tpopensdkdemo.common.BaseActivity;
import com.tplink.sdk.tpopensdkdemo.common.TPUtils;

import java.util.ArrayList;

public class DeviceAddSoftApActivity extends BaseActivity implements WifiListAdapter.OnWifiClickListener{

    private RecyclerView mWifiListView;
    private TextView mGetWifiListTv;
    private EditText mPwdEdt;
    private WifiListAdapter mWifiListAdapter;
    private ArrayList<IPCDeviceDefines.IPCOnboardWifiInfo> mWifiList;
    private IPCDeviceContext mDevCtx;
    private IPCDevice mTempDev;
    private WifiManager mWifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_device_add_soft_ap);
        initView();
    }

    private void initData() {
        mWifiList = new ArrayList<>();
        mWifiListAdapter = new WifiListAdapter(mWifiList, this, this);
        mDevCtx = mSDKCtx.getDevCtx();
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    private void initView() {
        mPwdEdt = findViewById(R.id.wifi_pwd_edt);
        mGetWifiListTv = findViewById(R.id.get_wifi_list_btn);
        mGetWifiListTv.setOnClickListener(this);
        mWifiListView = findViewById(R.id.wifi_list_view);
        mWifiListView.setAdapter(mWifiListAdapter);
        mWifiListView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onWifiClick(int position) {
        IPCDeviceDefines.IPCOnboardWifiInfo wifiInfo = mWifiList.get(position);
        wifiInfo.setPassword(mPwdEdt.getText().toString());
        mDevCtx.reqSendWifiToDev(mTempDev, new IPCReqListener() {
            @Override
            public int callBack(IPCReqResponse ipcReqResponse) {
                if (ipcReqResponse.mError == TPSDKCommon.ErrorCode.IPC_EC_OK) {
                    IPCReqData<IPCDeviceDefines.IPCSendWifiResult> data = (IPCReqData<IPCDeviceDefines.IPCSendWifiResult>) ipcReqResponse;
                    if (data.mData.getSupportAp() == 0) {
                        /* 不支持Ap的设备只能通过自动发现来确认手机是否联网成功 */
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissLoading();
                                showToast(getString(R.string.softap_no_ap_tip));
                            }
                        });
                        return 0;
                    }
                    mDevCtx.reqGetDevConnectStatus(mTempDev, new IPCReqListener() {
                        @Override
                        public int callBack(IPCReqResponse ipcReqResponse) {
                            if (ipcReqResponse.mError == 0) {
                                final IPCReqData<Integer> data = (IPCReqData<Integer>) ipcReqResponse;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (data.mData == 0) {
                                            dismissLoading();
                                            /* 已连上路由器wifi */
                                            showToast(getString(R.string.softap_connect_wifi_success));
                                        } else {
                                            dismissLoading();
                                            showToast(getString(R.string.softap_connect_wifi_error));
                                        }
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissLoading();
                                        showToast(getString(R.string.softap_get_wifi_connect_info_error));
                                    }
                                });
                            }
                            return 0;
                        }
                    }, data.mData.getConnectTime());
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissLoading();
                            showToast(getString(R.string.softap_send_wifi_error));
                        }
                    });
                }
                return 0;
            }
        }, wifiInfo);
        showLoading(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_wifi_list_btn:
                mTempDev = new IPCDevice(mDevCtx.initDev(
                        TPUtils.intToIp(mWifiManager.getDhcpInfo().gateway), 80));
                mDevCtx.reqDevScanWifi(mTempDev, new IPCReqListener() {
                    @Override
                    public int callBack(final IPCReqResponse ipcReqResponse) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissLoading();
                                if (ipcReqResponse.mError == TPSDKCommon.ErrorCode.IPC_EC_OK) {
                                    IPCReqData<ArrayList<IPCDeviceDefines.IPCOnboardWifiInfo>> data = (IPCReqData<ArrayList<IPCDeviceDefines.IPCOnboardWifiInfo>>) ipcReqResponse;
                                    mWifiList.addAll(data.mData);
                                    mWifiListAdapter.notifyDataSetChanged();
                                } else {
                                    showToast(getString(R.string.softap_send_wifi_error));
                                }
                            }
                        });
                        return 0;
                    }
                });
                showLoading(null);
                break;
        }
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, DeviceAddSoftApActivity.class);
        activity.startActivity(intent);
    }
}
