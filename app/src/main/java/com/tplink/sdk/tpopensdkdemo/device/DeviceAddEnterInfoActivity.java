package com.tplink.sdk.tpopensdkdemo.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.tplink.sdk.tpopensdkdemo.R;
import com.tplink.sdk.tpopensdkdemo.common.BaseActivity;

public class DeviceAddEnterInfoActivity extends BaseActivity {

    public static final int REQUEST_CODE_DEVICE_ADD_INPUT_INFO = 100;
    public static String DEVICE_ADD_ENTER_INFO = "enter_info";
    public static final String INTO_TYPE= "type";
    public static final int RESULT_CODE_PWD = 1;
    public static final int RESULT_CODE_PORT = 2;

    private EditText mInfoEdt;
    private int iType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_device_add_enter_pwd);
        initView();
    }

    private void initData() {
        iType = getIntent().getIntExtra(INTO_TYPE, RESULT_CODE_PWD);
    }

    private void initView() {
        findViewById(R.id.title_bar_left_tv).setVisibility(View.GONE);
        findViewById(R.id.title_bar_center_tv).setVisibility(View.GONE);

        mInfoEdt = findViewById(R.id.pwd_edit);
        mInfoEdt.setHint(iType == RESULT_CODE_PORT ? R.string.enter_port : R.string.enter_password);
        if (iType == RESULT_CODE_PORT) {
            mInfoEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(DEVICE_ADD_ENTER_INFO, mInfoEdt.getText().toString());
                setResult(iType, intent);
                finish();
            }
        });
    }

    public static void startActivity(Activity activity, int type) {
        Intent intent = new Intent(activity, DeviceAddEnterInfoActivity.class);
        intent.putExtra(INTO_TYPE, type);
        activity.startActivityForResult(intent, DeviceAddEnterInfoActivity.REQUEST_CODE_DEVICE_ADD_INPUT_INFO);
    }
}
