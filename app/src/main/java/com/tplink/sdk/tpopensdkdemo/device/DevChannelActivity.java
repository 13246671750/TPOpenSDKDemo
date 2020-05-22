package com.tplink.sdk.tpopensdkdemo.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.tplink.sdk.tpopensdk.openctx.IPCDevice;
import com.tplink.sdk.tpopensdk.openctx.IPCDeviceChannel;
import com.tplink.sdk.tpopensdkdemo.R;
import com.tplink.sdk.tpopensdkdemo.common.BaseActivity;

import java.util.ArrayList;

public class DevChannelActivity extends BaseActivity implements
        DevChannelListAdapter.OnChannelClickListner, DevChannelListAdapter.OnChannelSetClickListener{

    private static final String DEVICE = "device";
    private RecyclerView mChannelListView;
    private DevChannelListAdapter mChannelListAdapter;
    private ArrayList<IPCDeviceChannel> mChannelList;
    private IPCDevice mDev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_dev_channel);
        initView();
    }

    private void initData() {
        mDev = getIntent().getParcelableExtra(DEVICE);
        mChannelList = mDev.getChannelList();
        mChannelListAdapter = new DevChannelListAdapter(this, mChannelList, this, this);
    }

    private void initView() {
        TextView channleTv = findViewById(R.id.title_bar_center_tv);
        channleTv.setText(R.string.dev_channel);

        mChannelListView = findViewById(R.id.channel_list_recyclerview);
        mChannelListAdapter = new DevChannelListAdapter(this, mChannelList, this, this);
        mChannelListView.setAdapter(mChannelListAdapter);
        mChannelListView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onChannelClicked(int position) {
        // TODO: 2018/10/30 通道预览
    }

    public void onChannelSetClick(int position) {
        ChannelInfoActivity.startActivity(this, mDev, mChannelList.get(position).getID());
    }

    public static void startActivity(Activity activity, IPCDevice dev) {
        Intent intent = new Intent(activity, DevChannelActivity.class);
        intent.putExtra(DEVICE, dev);
        activity.startActivity(intent);
    }
}
