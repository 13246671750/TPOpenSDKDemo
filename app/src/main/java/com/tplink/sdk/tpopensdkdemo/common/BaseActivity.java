package com.tplink.sdk.tpopensdkdemo.common;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tplink.sdk.tpopensdk.TPOpenSDK;
import com.tplink.sdk.tpopensdk.TPSDKContext;
import com.tplink.sdk.tpopensdk.openctx.IPCReqResponse;

/**
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * @author Li Wei
 * @ClassName: BaseActivity
 * @Description: Version 1.0.0, 2018-10-18, Li Wei create file.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = BaseActivity.class.getSimpleName();

    protected TPSDKContext mSDKCtx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSDKCtx = TPOpenSDK.getInstance().getSDKContext();
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 显示转圈等待框
     *
     * @param text 等待框内的提示文字，可以为null
     */
    public void showLoading(@Nullable String text) {
        LoadingDialog loadingDialog = getLoadingDialog();
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.newInstance(text);
        } else {
            loadingDialog.setLoadingTv(text);
        }
        if (!loadingDialog.isAdded()) {
            loadingDialog.show(getFragmentManager(), LoadingDialog.TAG);
            LoadingDialog.DIALOG_SHOW = true;
        }
    }

    private LoadingDialog getLoadingDialog() {
        Fragment fragment = getFragmentManager().findFragmentByTag(LoadingDialog.TAG);
        if (fragment instanceof LoadingDialog) {
            return (LoadingDialog) fragment;
        }
        return null;
    }

    /**
     * dismiss转圈等待框
     */
    public void dismissLoading() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(LoadingDialog.DIALOG_SHOW) {
                    final LoadingDialog loadingDialog = getLoadingDialog();
                    if (loadingDialog != null
                            && !loadingDialog.isRemoving()
                            && !isFinishing()) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                LoadingDialog.DIALOG_SHOW = false;
                                loadingDialog.dismissAllowingStateLoss();
                            }
                        });
                    }
                }
            }
        }).start();
    }

    public void showToast(String info) {
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }

    /***
     * 通用的回调结果处理
     * @param response
     */
    public void commonCallBack(final IPCReqResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissLoading();
                if (response.mError == 0) {
                    onReqSuccess(response);
                } else {
                    showToast("response Error: " + response.mError + "\n Rval: " + response.mRval);
                }
          }
        });
    }

    /***
     * 成功的回调结果处理，可重写
     * @param response
     */
    public void onReqSuccess(IPCReqResponse response) {
        showToast("success");
    }
}
