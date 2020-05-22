package com.tplink.sdk.tpopensdkdemo.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tplink.sdk.tpopensdk.openctx.IPCDevice;
import com.tplink.sdk.tpopensdk.openctx.IPCDeviceChannel;
import com.tplink.sdk.tpopensdkdemo.R;

public class ChannelInfoActivity extends DeviceSettingDetailInfoActivity {
    public static final String CHANNEL_INFO = "CHANNEL";

    private int mChannelID;
    private IPCDeviceChannel mChannel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_channel_info);
        initView();
    }

    @Override
    public void initData() {
        super.initData();
        mChannelID = getIntent().getIntExtra(CHANNEL_INFO, -1);
        mChannel = mDev.getChannelList().get(mChannelID);
    }

    @Override
    public void initView() {
        super.initView();
        ((TextView)findViewById(R.id.title_bar_center_tv)).setText(R.string.channel_info);
        ((TextView)findViewById(R.id.HardwareVersion_layout).findViewById(R.id.about_name_tv)).setText(R.string.setting_about_hardware_info);
        ((TextView)findViewById(R.id.SoftwareVersion_layout).findViewById(R.id.about_name_tv)).setText(R.string.setting_about_software_info);
        ((TextView)findViewById(R.id.Alias_layout).findViewById(R.id.about_name_tv)).setText(R.string.channel_name);

        ((TextView)findViewById(R.id.HardwareVersion_layout).findViewById(R.id.about_value_tv)).setText(mChannel.getHardwareVersion());
        ((TextView)findViewById(R.id.Alias_layout).findViewById(R.id.about_value_tv)).setText(mChannel.getAlias());
        ((TextView)findViewById(R.id.SoftwareVersion_layout).findViewById(R.id.about_value_tv)).setText(mChannel.getSoftwareVersion());
    }

    public static void startActivity(Activity activity, IPCDevice dev, int channelID) {
        Intent intent = new Intent(activity, ChannelInfoActivity.class);
        intent.putExtra(DEVICE_INFO, dev);
        intent.putExtra(CHANNEL_INFO, channelID);
        activity.startActivity(intent);
    }
}
