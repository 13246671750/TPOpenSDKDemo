package com.tplink.sdk.tpopensdkdemo.common;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * @author caizhenghe
 * @ClassName: TPViewUtils
 * @Description: Version 1.0.0, 2018-10-12, caizhenghe create file.
 */

public class TPViewUtils {

    public static void setText(TextView view, String text) {
        if (view != null) {
            view.setText(text);
        }
    }

    public static void setVisible(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    public static void setImageSrc(ImageView view, @DrawableRes int res) {
        if (view != null) {
            view.setImageResource(res);
        }
    }

    public static void setColor(View view, @ColorInt int color) {
        if (view != null) {
            view.setBackgroundColor(color);
        }
    }
}
