package com.tplink.sdk.tpopensdkdemo.common;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.tplink.sdk.tpopensdk.common.TPSDKCommon;
import com.tplink.sdk.tpopensdkdemo.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * @author caizhenghe
 * @ClassName: TPUtils
 * @Description: Version 1.0.0, 2018-10-12, caizhenghe create file.
 */

public class TPUtils {
    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static void fullScreen(Activity activity, boolean enable) {
        if (enable) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }
    }


    public static ViewGroup getContentView(Activity activity) {
        ViewGroup content = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        return (ViewGroup) content.getChildAt(0);
    }

    public static String getEventType(Context context, int eventType) {
        String type = "";
        switch (eventType) {
            case TPSDKCommon.PlayBackType.TPPLAYER_PLAYBACK_EVENT_TIMING:
                type = context.getString(R.string.timing_record);
                break;
            case TPSDKCommon.PlayBackType.TPPLAYER_PLAYBACK_EVENT_MOTION:
                type = context.getString(R.string.motion_detection);
                break;

        }
        return type;
    }

    public static String getTime(Context context, long timeMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat(context.getString(R.string.time_format_sdf));
        Date date = new Date(timeMillis);
        return sdf.format(date);
    }

    public static String getDuration(Context context, long startTime/* s */, long endTime) {
        if (startTime > endTime)
            return "";
        long duration = endTime - startTime;
        int hour, minute, second;
        hour = (int) (duration / 60 / 60);
        minute = (int) (duration / 60 % 60);
        second = (int) (duration % 60);

        return String.format(context.getString(R.string.time_format), hour, minute, second);
    }

    public static long getSeekTime(int process, int max, long startTime, long endTime) {
        float percent = process / (float)max;
        long currentDuration = (long) ((endTime - startTime) * percent);
        return currentDuration + startTime;
    }

    public static int getProcess(long startTime, long endTime, long seekTime, int max){
        float percent = (seekTime - startTime) / (float)(endTime - startTime);
        return (int) (max * percent);
    }

    /**
     * 将int的IP值转成字符串格式的
     */
    public static String intToIp(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
