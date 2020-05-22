package com.tplink.sdk.tpopensdkdemo.bean;

import com.tplink.sdk.tpopensdk.bean.IPCDeviceDefines;

/**
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * @author Li Wei
 * @ClassName: IPCDiscoverDevBean
 * @Description: Version 1.0.0, 2018-10-16, Li Wei create file.
 */

public class IPCDiscoverDevBean {
    public IPCDeviceDefines.IPCDevInfo mDevInfo;
    public boolean mIsAdded;

    public IPCDiscoverDevBean(IPCDeviceDefines.IPCDevInfo mDevInfo, boolean mIsAdded) {
        this.mDevInfo = mDevInfo;
        this.mIsAdded = mIsAdded;
    }
}
