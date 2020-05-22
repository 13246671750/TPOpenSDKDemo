package com.tplink.sdk.tpopensdkdemo.common;

import android.app.Application;

import com.tplink.sdk.tpopensdk.TPOpenSDK;
import com.tplink.sdk.tpopensdk.TPSDKContext;

/**
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * @author caizhenghe
 * @ClassName: TPApplication
 * @Description: Version 1.0.0, 2018-10-12, caizhenghe create file.
 */

public class TPApplication extends Application {
    public static TPApplication INSTANCE;
    private TPSDKContext mSDKContext;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public TPSDKContext getSDKContext() {
        if (mSDKContext == null) {
            synchronized (this) {
                if (mSDKContext == null) {
                    mSDKContext = TPOpenSDK.getInstance().getSDKContext();
                }
            }
        }
        return mSDKContext;
    }


}
