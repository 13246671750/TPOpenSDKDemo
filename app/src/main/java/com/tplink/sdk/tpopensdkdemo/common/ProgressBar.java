package com.tplink.sdk.tpopensdkdemo.common;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tplink.foundation.TPLog;
import com.tplink.foundation.TPViewUtils;

/**
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * @author caizhenghe
 * @ClassName: ProgressBar
 * @Description: Version 1.0.0, 2018-05-03, caizhenghe create file.
 */

public class ProgressBar extends View{
    private static final String TAG  = ProgressBar.class.getSimpleName();
    protected static final String TEXT_COLOR = "#FFFFFF";
    protected static final int TEXT_SIZE = 30;
    /**
     * 文字大小
     */
    protected int mTextSize;

    /**
     * 文字颜色
     */
    protected int mTextColor;

    /**
     * 文字是否需要显示
     */
    protected boolean mIsTextDisplay;

    /**
     * 默认进度
     */
    protected int mProgress = 0;

    /**
     * 目标进度值，即动画的终点
     */
    protected int mTargetProgress;

    /**
     * 控制文字变更的Animator
     */
    protected ValueAnimator mTextValueAnimator;

    /**
     * 绘制图形的画笔
     */
    protected Paint mPaint;
    /**
     * 绘制文字的画笔
     */
    protected Paint mTextPaint;

    /**
     * 控制加载动画的Animator
     */
    protected ValueAnimator mValueAnimator;

    public ProgressBar(Context context) {
        this(context, null);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化参数
        initData(context, attrs);
        // 初始化画笔设置
        initPaint();
    }

    /******************
     * external interface
     ******************/
    public synchronized int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        setProgress(progress, true);
    }

    public boolean getIsTextDisplay() {
        return mIsTextDisplay;
    }

    public void setIsTextDisplay(boolean isTextDisplay) {
        mIsTextDisplay = isTextDisplay;
        invalidate();
    }

    public synchronized void setProgressWithTextAnimation(int progress) {
        //不可见，不加载动画。场景是加载太快，播放画面已经显示，这时候收到底层回调，即不再新建ObjectAnimator
        if (getVisibility() != View.VISIBLE) {
            mTargetProgress = progress;
            return;
        }

        //progress变小直接跳小，不进行动画
        if (progress <= mTargetProgress) {
            setProgress(progress);
            return;
        }

        if (progress < 0) {
            progress = 0;
        } else if (progress > 100) {
            progress = 100;
        }

        if (mTextValueAnimator != null) {
            mTextValueAnimator.cancel();
            mTextValueAnimator.setIntValues(mTargetProgress, progress);
        } else {
            mTextValueAnimator = ObjectAnimator.ofInt(this, "progressByAnimator", mTargetProgress, progress);
        }
        mTextValueAnimator.setDuration(50 * (progress - mTargetProgress));
        mTextValueAnimator.start();
        mTargetProgress = progress;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initValueAnimator();
    }

    /**
     * 通过可见性控制动画，避免空转
     *
     * @param changedView
     * @param visibility
     */
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE) {
            initValueAnimator();
        } else {
            TPViewUtils.cancelAnimator(mValueAnimator, mTextValueAnimator);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        TPViewUtils.cancelAnimator(mValueAnimator, mTextValueAnimator);
    }

    protected void initData(Context context, AttributeSet attrs){}

    protected void initPaint(){}

    protected void initValueAnimator(){}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.EXACTLY) {
            if (heightMode == MeasureSpec.UNSPECIFIED) {
                TPLog.e(TAG, "UNSPECIFIED! please check height measure mode");
            }
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);

            if (widthSize == 0 || heightSize == 0) {
                TPLog.e(TAG, "Measure Error! width or height size is 0");
            }
            // force height,width equals to min(height,width)
//            widthSize = heightSize = Math.min(heightSize, widthSize);
            // force measure mode to EXACTLY
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        } else {
            TPLog.e(TAG, "UNSPECIFIED! please check width measure mode");

        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawProgressPattern(canvas);
        drawProgressText(canvas);
    }

    /**
     * 绘制进度条图案
     * @param canvas
     */
    protected void drawProgressPattern(Canvas canvas) {}

    protected float[] getTextOriginPoint(float textWidth, float textHeight){return null;}

    /**
     * 绘制进度条文字
     * @param canvas
     */
    private void drawProgressText(Canvas canvas) {
        if (!mIsTextDisplay) {
            return;
        }
        String textContent = getProgress() + "%";
        float textWidth = mTextPaint.measureText(textContent);
        // ascent = 推荐最高线ascentLine - 基准线baseLine < 0
        float ascent = mTextPaint.getFontMetrics().ascent;
        float[] originTextPoint = getTextOriginPoint(textWidth, mTextPaint.getFontMetrics().descent - ascent);
        canvas.drawText(textContent, originTextPoint[0], originTextPoint[1] - ascent, mTextPaint);
    }

    private synchronized void setProgress(int progress, boolean manually) {
        if (manually && mTextValueAnimator != null) {
            mTextValueAnimator.cancel();
        }
        if (progress < 0) {
            progress = 0;
        } else if (progress > 100) {
            progress = 100;
        }
        mProgress = progress;
        // 进度改变时，需要通过invalidate方法进行重绘，但是因为已经有ValueAnimator绘制转圈动画，故而不再重复调用

        if (manually) {
            mTargetProgress = progress;
        }
    }

    /**
     * 线段长度从20%到95%往复
     *
     * @return
     */
    private float getProgressArcAngle(float percentage) {
        return percentage * 360 / 100;
    }

    /**
     * 属性动画的set方法，不可删除。
     * 用反射调用。
     *
     * @param progress
     */
    protected void setProgressByAnimator(int progress) {
        setProgress(progress, false);
    }
}
