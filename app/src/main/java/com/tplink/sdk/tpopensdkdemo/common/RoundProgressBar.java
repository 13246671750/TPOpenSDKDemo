package com.tplink.sdk.tpopensdkdemo.common;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.tplink.foundation.TPViewUtils;
import com.tplink.sdk.tpopensdkdemo.R;

/**
 * @author zhoucongcong
 * @ClassName: RoundProgressBar
 * @Description: 圆环进度条，可配置是否显示数字,目前使用场景是播放窗口中间的加载图案和LoadingDialog。fork from IPC project
 * @date 2017-05-26
 */
public class RoundProgressBar extends ProgressBar {
    private static final String TAG = RoundProgressBar.class.getSimpleName();

    private static final int START_ANGLE = -90;
    private static final String CENTER_COLOR = "#00000000";
    private static final String RING_COLOR = "#FF1E262C";
    private static final String PROGRESS_COLOR = "#FF409AFF";
    private static final int CIRCLE_RADIUS = 20;
    private static final int RING_WIDTH = 5;

    /**
     * 圆弧的起始角度，参考canvas.drawArc方法
     */
    private int mStartAngle;

    /**
     * 圆形内半径
     */
    private int mRadius;

    /**
     * 进度条的宽度
     */
    private int mRingWidth;

    /**
     * 圆形内部填充色
     */
    private int mCenterColor;

    /**
     * 进度条背景色
     */
    private int mRingColor;

    /**
     * 进度条的颜色
     */
    private int mProgressColor;

    /**
     * 图案结束时的Animator
     */
    private ValueAnimator mEndValueAnimator;

    /**
     * 进度圈显示百分比
     */
    private float mProgressPercent;

    /**
     * 进度圈出发点角度
     */
    private float mActiveStartAngle;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    /******************
     * external interface
     ******************/
    /**
     * 进行完成动画，返回为动画需要时间
     * 目前形式为进度转满
     *
     * @return
     */
    public long onComplete() {
        if (mValueAnimator != null) {
            if (mValueAnimator.isRunning()) {
                mValueAnimator.cancel();
            }
        }
        long duration = (long) (1000 - 10 * mProgressPercent);
        mEndValueAnimator = ValueAnimator.ofFloat(mProgressPercent, 100);
        mEndValueAnimator.setDuration(duration);
        mEndValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mProgressPercent = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mEndValueAnimator.start();
        //加上时延，避免用户觉得突兀
        return duration + 200;
    }

    /**
     * 通过可见性控制动画，避免空转
     *
     * @param changedView
     * @param visibility
     */
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility != View.VISIBLE) {
            TPViewUtils.cancelAnimator(mEndValueAnimator);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        TPViewUtils.cancelAnimator(mEndValueAnimator);
    }

    /******************
     * init
     ******************/
    @Override
    protected void initData(Context context, AttributeSet attrs) {
        super.initData(context, attrs);

        // 获取自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
        for (int i = 0; i < a.length(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.RoundProgressBar_startAngle:
                    mStartAngle = a.getInteger(attr, START_ANGLE);
                    break;
                case R.styleable.RoundProgressBar_centerColor:
                    mCenterColor = a.getColor(attr, Color.parseColor(CENTER_COLOR));
                    break;
                case R.styleable.RoundProgressBar_progressColor:
                    mProgressColor = a.getColor(attr, Color.parseColor(PROGRESS_COLOR));
                    break;
                case R.styleable.RoundProgressBar_ringColor:
                    mRingColor = a.getColor(attr, Color.parseColor(RING_COLOR));
                    break;
                case R.styleable.RoundProgressBar_textColor:
                    mTextColor = a.getColor(attr, Color.parseColor(TEXT_COLOR));
                    break;
                case R.styleable.RoundProgressBar_textSize:
                    mTextSize = (int) a.getDimension(attr, TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE,
                            getResources().getDisplayMetrics()));
                    break;
                case R.styleable.RoundProgressBar_isTextDisplay:
                    mIsTextDisplay = a.getBoolean(attr, true);
                    break;
                case R.styleable.RoundProgressBar_radius:
                    mRadius = (int) a.getDimension(attr, TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, CIRCLE_RADIUS,
                            getResources().getDisplayMetrics()
                    ));
                    break;
                case R.styleable.RoundProgressBar_ringWidth:
                    mRingWidth = (int) a.getDimension(attr, TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, RING_WIDTH,
                            getResources().getDisplayMetrics()
                    ));
                    break;
                default:
                    break;
            }
        }
        a.recycle();
    }

    @Override
    protected void initPaint() {
        super.initPaint();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setStrokeWidth(0);
    }

    @Override
    protected void initValueAnimator() {
        super.initValueAnimator();
        if (mValueAnimator != null) {
            if (!mValueAnimator.isRunning()) {
                mValueAnimator.start();
            }
        } else {
            mValueAnimator = ValueAnimator.ofFloat(0, 2);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float animatorValue = (float) valueAnimator.getAnimatedValue();
                    mActiveStartAngle = getStartAngle(animatorValue);
                    mProgressPercent = 95 - Math.abs(1 - animatorValue) * 75;
                    invalidate();
                }
            });
            mValueAnimator.setDuration(2000);
            mValueAnimator.setInterpolator(new LinearInterpolator());
            mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mValueAnimator.start();
        }
    }

    /******************
     * drawing
     ******************/
    @Override
    protected float[] getTextOriginPoint(float textWidth, float textHeight) {
        float originX = getWidth() / 2 - textWidth / 2;
        float originY = getHeight() / 2 - textHeight / 2;
        return new float[]{originX, originY};
    }

    @Override
    protected void drawProgressPattern(Canvas canvas) {
        // 获取圆心坐标
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        /**
         * 画圆心颜色
         */
        if (mCenterColor != 0) {
            drawCenterCircle(canvas, cx, cy);
        }

        /**
         * 画外层大圆
         */
        drawOuterCircle(canvas, cx, cy);

        /**
         * 画进度圆弧
         */
        drawProgress(canvas, cx, cy);
    }



    private void drawProgress(Canvas canvas, int cx, int cy) {
        mPaint.setColor(mProgressColor);
        mPaint.setStrokeWidth(mRingWidth);
        mPaint.setStyle(Paint.Style.STROKE);

        RectF mRectF = new RectF(cx - mRadius, cy - mRadius, cx + mRadius, cy + mRadius);
        canvas.drawArc(mRectF, mActiveStartAngle, getProgressArcAngle(mProgressPercent), false, mPaint);
    }

    private void drawOuterCircle(Canvas canvas, int cx, int cy) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mRingColor);
        mPaint.setStrokeWidth(mRingWidth);
        canvas.drawCircle(cx, cy, mRadius, mPaint);
    }

    private void drawCenterCircle(Canvas canvas, int cx, int cy) {
        mPaint.setColor(mCenterColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cx, cy, mRadius, mPaint);
    }


    private float getStartAngle(float animatorValue) {
        return mStartAngle + 360 * (animatorValue - (int) animatorValue);
    }

    /**
     * 线段长度从20%到95%往复
     *
     * @return
     */
    private float getProgressArcAngle(float percentage) {
        return percentage * 360 / 100;
    }
}
