package com.tplink.sdk.tpopensdkdemo.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.tplink.foundation.TPLog;

/**
 * Copyright (C), 2017, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * @author caizhenghe
 * @ClassName: TouchButton
 * @Description: Version 1.0.0, 2017-09-23, caizhenghe create file.
 */

@SuppressLint("AppCompatCustomView")
public class TouchButton extends ImageView{
    public static final String TAG = TouchButton.class.getSimpleName();
    private OnUpdateButtonStatus mCallback;

    public interface OnUpdateButtonStatus {
        void onReleaseButton(View v);

        void onTouchButton(View v);
    }

    public TouchButton(Context context) {
        this(context, null);
    }

    public TouchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCallback(OnUpdateButtonStatus callback) {
        mCallback = callback;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        // TPLog.d(TAG, "onTouch");
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                TPLog.d(TAG, "ACTION_DOWN");
                if (!isEnabled()) {
                    return false;
                }
                updatePressStatus(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                int y = (int) event.getX();
                if (x < 0 || y < 0 || x > getWidth() || y > getHeight()) {
                    updatePressStatus(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                TPLog.d(TAG, "ACTION_UP");
                updatePressStatus(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                TPLog.d(TAG, "ACTION_CANCEL");
                updatePressStatus(false);
                break;
        }
        return true;
    }

    private void updatePressStatus(boolean isPress) {
        if (isPress) {
            setPressed(true);
            if (mCallback != null) {
                mCallback.onTouchButton(this);
            }
        } else {
            setPressed(false);
            if (mCallback != null) {
                mCallback.onReleaseButton(this);
            }
        }
    }
}
