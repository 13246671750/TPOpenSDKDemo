package com.tplink.sdk.tpopensdkdemo.device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tplink.sdk.tpopensdk.openctx.IPCDevice;
import com.tplink.sdk.tpopensdk.openctx.IPCDeviceContext;
import com.tplink.sdk.tpopensdk.openctx.IPCReqListener;
import com.tplink.sdk.tpopensdk.openctx.IPCReqResponse;
import com.tplink.sdk.tpopensdkdemo.R;
import com.tplink.sdk.tpopensdkdemo.common.BaseActivity;
import com.tplink.sdk.tpopensdkdemo.player.PreviewActivity;

import java.util.ArrayList;

/**
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * @author caizhenghe
 * @ClassName: DeviceActivity
 * @Description: Version 1.0.0, 2018-09-30, caizhenghe create file.
 */

public class DeviceActivity extends BaseActivity implements DeviceListAdapter.OnDeviceClickListner,
        DeviceListAdapter.OnDeviceSetClickListener, DeviceListAdapter.onNVRMoreClickListener {

    private RecyclerView mDeviceListView;
    private DeviceListAdapter mDeviceListAdapter;
    public static ArrayList<IPCDevice> DeviceList;
    private IPCDeviceContext mDevCtx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_device);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDeviceListAdapter.notifyDataSetChanged();
    }

    private void initData() {
        /* UI线程应该不做太多阻塞操作，appRepStart暂时操作不算多，先同步启动 */
        mSDKCtx.appReqStart(true, null);
        mDevCtx = mSDKCtx.getDevCtx();
    }

    private void initView() {
        findViewById(R.id.title_bar_left_tv).setVisibility(View.GONE);
        findViewById(R.id.title_bar_center_tv).setVisibility(View.GONE);
        TextView mRightTv = findViewById(R.id.title_bar_right_tv);
        mRightTv.setText(getString(R.string.device_add_device));
        mRightTv.setVisibility(View.VISIBLE);
        findViewById(R.id.title_bar_right_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceAddActivity.startActivity(DeviceActivity.this);
            }
        });
        mDeviceListView = findViewById(R.id.device_list_recyclerview);
        DeviceList = new ArrayList<>();
        mDeviceListAdapter = new DeviceListAdapter(this, DeviceList, this, this, this);
        mDeviceListView.setAdapter(mDeviceListAdapter);
        mDeviceListView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onDeviceClicked(int position) {
        PreviewActivity.startActivity(this, DeviceList.get(position));
    }

    @Override
    public void onDevSetClick(int position) {
        final int iPosition = position;
        mDevCtx.reqLoadSetting(DeviceList.get(iPosition), new IPCReqListener() {
            @Override
            public int callBack(final IPCReqResponse ipcReqResponse) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoading();
                        if (ipcReqResponse.mError == 0) {
                            DeviceSettingActivity.startActivity(DeviceActivity.this, DeviceList.get(iPosition));
                        }
                        else {
                           showToast("response: error: " + ipcReqResponse.mError + "\nrval: " + ipcReqResponse.mRval);
                        }
                    }
                });
                return 0;
            }
        });
        showLoading(null);
    }

    @Override
    public void onNVRMoreClick(int position) {
        DevChannelActivity.startActivity(this, DeviceList.get(position));
    }
}
