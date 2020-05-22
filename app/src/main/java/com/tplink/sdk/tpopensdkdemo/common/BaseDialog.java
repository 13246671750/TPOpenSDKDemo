package com.tplink.sdk.tpopensdkdemo.common;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author fuyuncong
 * @ClassName: BaseDialog
 * @Description: dialog的基类，包含一些基础设置
 * @date 2017-08-09
 */

public abstract class BaseDialog extends DialogFragment {

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            // 要在onCreateView之前设置才生效
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        return createView(inflater, container);
    }

    protected abstract View createView(LayoutInflater inflater, @Nullable ViewGroup container);

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            //此属性在onStart之前设置无效
            dialog.setCancelable(getCancelable());
            dialog.setCanceledOnTouchOutside(getCanceledOnTouchOutside());
            setLayoutParams(dialog);
        }
    }

    protected void setLayoutParams(Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            if (!showDimBehind()) {
                //dim这个参数要在onStart里设置才生效，在onCreateView之前无效
                wlp.dimAmount = 0f;
            }
            wlp.gravity = Gravity.CENTER;
            window.setAttributes(wlp);
        }
    }

    /**
     * 是否可以点击Back键进行取消，默认为false
     * 不同表现请Override
     *
     * @return 点击back键是否消失
     */
    protected boolean getCancelable() {
        return false;
    }

    /**
     * 是否可以点击Dialog以外区域进行取消，默认为false
     * 不同表现请Override
     *
     * @return 点击dialog外区域是否消失
     */
    protected boolean getCanceledOnTouchOutside() {
        return false;
    }

    /**
     * 是否将背景置暗
     *
     * @return
     */
    protected boolean showDimBehind() {
        return true;
    }
}
