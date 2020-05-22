package com.tplink.sdk.tpopensdkdemo.device;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tplink.sdk.tpopensdk.bean.IPCDeviceDefines;
import com.tplink.sdk.tpopensdk.openctx.IPCReqData;
import com.tplink.sdk.tpopensdk.openctx.IPCReqListener;
import com.tplink.sdk.tpopensdk.openctx.IPCReqResponse;
import com.tplink.sdk.tpopensdkdemo.R;

import java.util.ArrayList;

public class StorageInfoActivity extends DeviceSettingDetailInfoActivity
        implements StorageInfoListAdapter.OnLoopStatusListener, StorageInfoListAdapter.OnFormatSDListener{
    ArrayList<IPCDeviceDefines.IPCStorageInfo> mSDList;
    private StorageInfoListAdapter mSDListAdapter;
    private RecyclerView mSDInfoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_storage_info);
        initView();
    }

    @Override
    public void initData() {
        super.initData();
        mSDList = mDev.getSDList();
        mSDListAdapter = new StorageInfoListAdapter(this, mSDList, this, this);
    }

    @Override
    public void initView() {
        super.initView();
        mSDInfoListView = findViewById(R.id.sdinfo_list_view);
        mSDInfoListView.setAdapter(mSDListAdapter);
        mSDInfoListView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onSetLoopStatus(int position, int status) {
        mDevCtx.reqSetStorageLoopStatus(mDev, new IPCReqListener() {
            @Override
            public int callBack(final IPCReqResponse ipcReqResponse) {
                commonCallBack(ipcReqResponse);
                return 0;
            }
        }, status);
        showLoading(null);
    }

    @Override
    public void onFormatSD(int position) {
        mDevCtx.reqFormatSD(mDev, new IPCReqListener() {
            @Override
            public int callBack(final IPCReqResponse ipcReqResponse) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (ipcReqResponse.mError == 0) {
                            IPCReqData<Integer> data = (IPCReqData<Integer>) ipcReqResponse;
                            if (data.mData != 100) {
                                showToast(getString(R.string.storage_info_format_percent) + data.mData);
                            } else {
                                dismissLoading();
                                showToast(getString(R.string.storage_info_format_success));
                            }
                        } else {
                            dismissLoading();
                            showToast(getString(R.string.storage_info_format_failure));
                        }
                    }
                });
                if (ipcReqResponse.mError == 0) {
                    IPCReqData<Integer> data = (IPCReqData<Integer>) ipcReqResponse;
                    return data.mData == 100 ? 0 : -1;
                } else {
                    return 0;
                }
            }
        }, mSDList.get(0).getStrDiskName());
        showLoading(null);
    }
}
