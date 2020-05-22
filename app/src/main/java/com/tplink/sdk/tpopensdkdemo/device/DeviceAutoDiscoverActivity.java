package com.tplink.sdk.tpopensdkdemo.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tplink.sdk.tpopensdk.bean.IPCDeviceDefines.IPCDevInfo;
import com.tplink.sdk.tpopensdk.openctx.IPCDevice;
import com.tplink.sdk.tpopensdk.openctx.IPCDeviceContext;
import com.tplink.sdk.tpopensdk.openctx.IPCDiscoverContext;
import com.tplink.sdk.tpopensdk.openctx.IPCReqData;
import com.tplink.sdk.tpopensdk.openctx.IPCReqListener;
import com.tplink.sdk.tpopensdk.openctx.IPCReqResponse;
import com.tplink.sdk.tpopensdkdemo.R;
import com.tplink.sdk.tpopensdkdemo.bean.IPCDiscoverDevBean;
import com.tplink.sdk.tpopensdkdemo.common.BaseActivity;

import java.util.ArrayList;

public class DeviceAutoDiscoverActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, DeviceAutoAddListAdapter.AddDeviceListener,
        View.OnClickListener {
    private IPCDiscoverContext mDiscoverCtx;
    private IPCDeviceContext mDevCtx;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecycleView;
    private Handler mHandler;

    private DeviceAutoAddListAdapter mDevListAdatper;
    private ArrayList<IPCDiscoverDevBean> mDevInfoList;
    private IPCDevice mToAddDev;
    private int mAddPosition;
    private boolean mModifyPort;
    private int mPort;
    private String mPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_device_auto_discover);
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DeviceAddEnterInfoActivity.REQUEST_CODE_DEVICE_ADD_INPUT_INFO:
                if (resultCode == DeviceAddEnterInfoActivity.RESULT_CODE_PWD) {
                    mPwd = data.getStringExtra(DeviceAddEnterInfoActivity.DEVICE_ADD_ENTER_INFO);
                    addDevice(mToAddDev);
                }
                if (resultCode == DeviceAddEnterInfoActivity.RESULT_CODE_PORT) {
                    mPort = Integer.valueOf(data.getStringExtra(DeviceAddEnterInfoActivity.DEVICE_ADD_ENTER_INFO));
                    addDevice(mToAddDev);
                }
                break;
        }
    }

    /************
     * common
     ***********/
    private void initData() {
        mDiscoverCtx = mSDKCtx.getDiscoverCtx();
        mDevCtx = mSDKCtx.getDevCtx();
        mDevInfoList = new ArrayList<>();
        mDevListAdatper = new DeviceAutoAddListAdapter(this, mDevInfoList, this);
        mHandler = new Handler();
        mModifyPort = false;
        mPwd = "123456";
        mPort = 80;
    }

    private void initView() {
        ((TextView)findViewById(R.id.title_bar_center_tv)).setText(R.string.auto_discover);

        mSwipeRefreshLayout = findViewById(R.id.device_add_auto_discover_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.text_blue_dark);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                startDiscover();
            }
        });
        mRecycleView = findViewById(R.id.device_auto_discover_recyclerview);
        mRecycleView.setAdapter(mDevListAdatper);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        startDiscover();
    }

    @Override
    public void onAddDeviceClicked(final int position) {
        IPCDevice dev = new IPCDevice(mDevCtx.initDevWithInfo(mDevInfoList.get(position).mDevInfo));
        mToAddDev = dev;
        mAddPosition = position;
        addDevice(dev);
    }

    private void addDevice(final IPCDevice dev) {
        mDevCtx.reqLogin(dev, mPwd, new IPCReqListener() {
            @Override
            public int callBack(final IPCReqResponse ipcReqResponse) {
                if (ipcReqResponse.mError == 0) {
                    mDevCtx.reqConnectDev(dev, new IPCReqListener() {
                        @Override
                        public int callBack(IPCReqResponse ipcReqResponse) {
                            if (ipcReqResponse.mError == 0) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissLoading();
                                        DeviceActivity.DeviceList.add(dev);
                                        mDevInfoList.get(mAddPosition).mIsAdded = true;
                                        mDevListAdatper.notifyItemChanged(mAddPosition);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissLoading();
                                    }
                                });
                            }
                            return 0;
                        }
                    });
                } else if (ipcReqResponse.mRval == -40401) {
                    // TODO: 底层的常量应该在SDK中暴露
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dismissLoading();
                            DeviceAddEnterInfoActivity.startActivity(DeviceAutoDiscoverActivity.this,
                                    DeviceAddEnterInfoActivity.RESULT_CODE_PWD);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissLoading();
                            if (!mModifyPort) {
                                // TODO: 2018/10/21 底层login暂时没有提供修改端口的方法
                                /* 可能是端口错误，提示修改端口号 */
                                mModifyPort = true;
                                DeviceAddEnterInfoActivity.startActivity(DeviceAutoDiscoverActivity.this,
                                        DeviceAddEnterInfoActivity.RESULT_CODE_PORT);
                            } else {
                                showToast("response: error: " + ipcReqResponse.mError + "\nrval: " + ipcReqResponse.mRval);
                            }
                        }
                    });
                }
                return 0;
            }
        });
        showLoading(null);
    }

    private void startDiscover() {
        mDevInfoList.clear();
        mDevListAdatper.notifyDataSetChanged();
        mDiscoverCtx.reqScannedList(new IPCReqListener() {
            @Override
            public int callBack(IPCReqResponse ipcReqResponse) {
                final IPCReqResponse response = ipcReqResponse;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.mError == 0) {
                            final IPCReqData<ArrayList<IPCDevInfo>> reqData = (IPCReqData<ArrayList<IPCDevInfo>>)response;
                            for(IPCDevInfo dev : reqData.mData) {
                                mDevInfoList.add(new IPCDiscoverDevBean(dev, false));
                            }
                            mDevListAdatper.notifyDataSetChanged();
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
                return 0;
            }
        }, 5000, "0.0.0.0");
    }

    /****************
     * Entrance
     ****************/
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, DeviceAutoDiscoverActivity.class);
        activity.startActivity(intent);
    }
}
