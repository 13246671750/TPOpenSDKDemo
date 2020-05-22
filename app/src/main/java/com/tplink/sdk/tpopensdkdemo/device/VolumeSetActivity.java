package com.tplink.sdk.tpopensdkdemo.device;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tplink.sdk.tpopensdk.openctx.IPCReqListener;
import com.tplink.sdk.tpopensdk.openctx.IPCReqResponse;
import com.tplink.sdk.tpopensdkdemo.R;

public class VolumeSetActivity extends DeviceSettingDetailInfoActivity {

    private EditText mMicroEdt;
    private EditText mSpeakerEdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_volume_set);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        mMicroEdt = findViewById(R.id.microphone_volume_edt);
        mSpeakerEdt = findViewById(R.id.speaker_volume_edt);
        findViewById(R.id.set_microphone_volume_tv).setOnClickListener(this);
        findViewById(R.id.set_speaker_volume_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_microphone_volume_tv:
                int volume = 0;
                try {
                    volume = Integer.valueOf(mMicroEdt.getText().toString());
                } catch (Exception e) {
                    showToast(getString(R.string.enter_common_invalid));
                    return;
                }
                mDevCtx.reqSetMicrophoneVolume(mDev, new IPCReqListener() {
                    @Override
                    public int callBack(IPCReqResponse ipcReqResponse) {
                        commonCallBack(ipcReqResponse);
                        return 0;
                    }
                }, volume);
                showLoading(null);
                break;
            case R.id.set_speaker_volume_tv:
                int speakerVolume = 0;
                try {
                    speakerVolume = Integer.valueOf(mSpeakerEdt.getText().toString());
                } catch (Exception e) {
                    showToast(getString(R.string.enter_common_invalid));
                    return;
                }
                mDevCtx.reqSetSpeakerVolume(mDev, new IPCReqListener() {
                    @Override
                    public int callBack(IPCReqResponse ipcReqResponse) {
                        commonCallBack(ipcReqResponse);
                        return 0;
                    }
                }, speakerVolume);
                showLoading(null);
                break;
        }
    }
}
