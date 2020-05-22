package com.tplink.sdk.tpopensdkdemo.player;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tplink.foundation.TPLog;
import com.tplink.sdk.tpopensdk.bean.IPCDeviceDefines;
import com.tplink.sdk.tpopensdk.common.TPSDKCommon;
import com.tplink.sdk.tpopensdk.openctx.IPCDevice;
import com.tplink.sdk.tpopensdk.openctx.IPCReqData;
import com.tplink.sdk.tpopensdk.openctx.IPCReqListener;
import com.tplink.sdk.tpopensdk.openctx.IPCReqResponse;
import com.tplink.sdk.tpopensdkdemo.R;
import com.tplink.sdk.tpopensdkdemo.common.TPUtils;
import com.tplink.sdk.tpopensdkdemo.common.TPViewUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * @author caizhenghe
 * @ClassName: PlaybackActivity
 * @Description: Version 1.0.0, 2018-09-30, caizhenghe create file.
 */

public class PlaybackActivity extends PlayerActivity {
    private static final int GRID_VIEW_SPAN_COUNT = 3;

    private DatePickerDialog mDatePickerDialog;
    private int mClientId = -1, mClientIdForDownload = -1, mEventType = -1;
    private long mStartTime, mEndTime;
    private Calendar mCalendar = new GregorianCalendar();
    private List<IPCDeviceDefines.IPCSearchVideo> mSearchVideoList = new ArrayList<>();
    private List<IPCDeviceDefines.IPCSearchMedia> mSearchMediaList = new ArrayList<>();
    private VideoAdapter mAdapter;
    private int mProgress;

    private RecyclerView mVideoRecyclerView;
    private TextView mDateTv, mStartTimeTv, mDurationTv;
    private SeekBar mSeekBar;

    public static void startActivity(Context context, IPCDevice device) {
        Intent intent = new Intent(context, PlaybackActivity.class);
        intent.putExtra(EXTRA_DEVICE, device);
        context.startActivity(intent);
    }

    @Override
    public void doClick(View v) {
        super.doClick(v);
        switch (v.getId()) {
            case R.id.title_bar_left_tv:
                finish();
                break;
            case R.id.title_bar_right_tv:
                reqDownloadFirstMedia();
                break;
            case R.id.player_play_btn:
                if (mIsPlay) {
                    if (mIsPause) {
                        resume();
                    } else {
                        pause();
                    }
                } else {
                    Toast.makeText(this, "Error! Player is not init, choose video first", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.player_date_pick_tv:
                if (mDatePickerDialog != null && !mDatePickerDialog.isShowing())
                    mDatePickerDialog.show();
                break;
        }
        if (v instanceof ImageView)
            updateBtnStatus((ImageView) v, true);
    }

    @Override
    protected void initData() {
        super.initData();

        // FIXME: 2018/10/18 just adapt ipc
        mChannelId = 0;

        // get clientId
        reqClient(false);
        // when we catch client id for play, can not catch a client id for download in the same time.
        //reqClient(true);

        // DatePickerDialog
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mDatePickerDialog = new DatePickerDialog(this);
            mDatePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    TPLog.d("TAG", "onDateSet: year = " + year + "; month = " + month + "; day = " + dayOfMonth);
                    // stop player
                    stop();
                    // update date
                    mDateTv.setText(String.format(getString(R.string.date_format), year, month + 1, dayOfMonth));
                    mCalendar.set(year, month, dayOfMonth);
                    // research video & refresh list
                    reqSearchVideo();
                }
            });
        } else {
            Log.e("TAG", "sdk version is lower than 24, cannot create dialog!");
        }

        // Adapter
        mAdapter = new VideoAdapter(this, mSearchVideoList, new VideoAdapter.OnItemClickListener() {
            @Override
            public void callback(int position) {
                // stop player
                stop();
                // update start/end time
                IPCDeviceDefines.IPCSearchVideo bean = mSearchVideoList.get(position);
                mStartTime = bean.getStartTime();
                mEndTime = bean.getEndTime();
                mEventType = bean.getType();
                mStartTimeTv.setText(String.format(getString(R.string.time_format), 0, 0, 0));
                mDurationTv.setText(String.format(getString(R.string.time_format), 0, 0, 0));
                // update max value in seek bar
                mSeekBar.setProgress(0);
                mSeekBar.setMax((int) (mEndTime - mStartTime));
                // start playback
                play();
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_playback;
    }

    @Override
    protected void initView() {
        super.initView();

        if (TPUtils.isLandscape(this))
            return;

        mDateTv = findViewById(R.id.player_date_pick_tv);
        TPViewUtils.setText(mDateTv,
                String.format(getString(R.string.date_format),
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH) + 1,
                        mCalendar.get(Calendar.DATE)));
        mStartTimeTv = findViewById(R.id.player_seek_start_time_tv);
        mDurationTv = findViewById(R.id.player_seek_duration_tv);
        mSeekBar = findViewById(R.id.player_seek_bar);
        mSeekBar.setProgress(mProgress);
        mSeekBar.setMax((int) (mEndTime - mStartTime));
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!mIsPlay)
                    return;
                // update current duration
                mDurationTv.setText(TPUtils.getDuration(PlaybackActivity.this, mStartTime, progress + mStartTime));
                mProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // seek
                mPlayer.seek(mStartTime + mProgress);
            }
        });

        mPlayBtn = findViewById(R.id.player_play_btn);
        mOrientationBtn = findViewById(R.id.player_orientation_btn);
        mVideoRecyclerView = findViewById(R.id.player_video_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, GRID_VIEW_SPAN_COUNT, GridLayoutManager.VERTICAL, false);
        if (mVideoRecyclerView != null) {
            mVideoRecyclerView.setLayoutManager(gridLayoutManager);
            mVideoRecyclerView.setAdapter(mAdapter);
        }

        // refresh all btn status
        updateBtnStatus();
    }

    @Override
    protected void initTitleBar() {
        super.initTitleBar();
        mTitleCenter.setText(getString(R.string.playback));
        mTitleLeft.setVisibility(View.VISIBLE);
        mTitleLeft.setText(getString(R.string.preview));
        if (mDevice.supportDownloadVideo(mListType, mChannelId)) {
            mTitleRight.setVisibility(View.VISIBLE);
            mTitleRight.setText(getString(R.string.download));
        }
    }

    @Override
    protected void updateBtnStatus(ImageView v, boolean enable) {
        v.setEnabled(enable);
        switch (v.getId()) {
            case R.id.player_play_btn:
                if (mIsPlay && !mIsPause) {
                    v.setImageResource(enable ? R.drawable.tabbar_pause : R.drawable.tabbar_pause_dis);
                } else {
                    v.setImageResource(enable ? R.drawable.tabbar_play : R.drawable.tabbar_play_dis);
                }
                break;
        }
    }

    @Override
    protected void play() {
        super.play();
        if (!isVideoSelected()) {
            Toast.makeText(this, "Error! mEventType = " + mEventType
                    + "; mClientId = " + mClientId + "; mStartTime = " + mStartTime
                    + "; mEndTime = " + mEndTime, Toast.LENGTH_SHORT).show();
        } else {
            mIsPlay = true;
            updateBtnStatus(mPlayBtn, true);
            mPlayer.startPlayback(mDevice, mClientId, mChannelId, mEventType, mStartTime, mEndTime);
        }
    }

    @Override
    protected void onPlayTimeChange(long playTime) {
        super.onPlayTimeChange(playTime);
        if (playTime >= mStartTime && playTime <= mEndTime) {
            if (mSeekBar != null) {
                mSeekBar.setProgress((int) (playTime - mStartTime));
            }
        }
    }

    private void pause() {
        mPlayer.pause();
        mIsPause = true;
        updateBtnStatus(mPlayBtn, true);
    }

    private void resume() {
        mPlayer.resume();
        mIsPause = false;
        updateBtnStatus(mPlayBtn, true);
    }


    private void reqClient(final boolean isDownload) {
        mDeviceContext.reqGetClientID(mDevice, new IPCReqListener() {
            @Override
            public int callBack(IPCReqResponse ipcReqResponse) {
                if (ipcReqResponse.mError == 0) {
                    IPCReqData<Integer> data = (IPCReqData<Integer>) ipcReqResponse;

                    // search video
                    if (isDownload) {
                        mClientIdForDownload = data.mData;
                        reqSearchMedia();
                    } else {
                        mClientId = data.mData;
                        reqSearchVideo();
                    }
                } else {
                    TPLog.e("TAG", "Get client ID error!");
                }
                return 0;
            }
        });
    }

    private void reqSearchMedia() {
        if (mClientIdForDownload == -1)
            return;
        Calendar startCalendar = (Calendar) mCalendar.clone();
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        Calendar endCalendar = (Calendar) mCalendar.clone();
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.SECOND, 59);

        ArrayList<Integer> listType = new ArrayList<>();
        listType.add(mListType);

        /** mediaType: 0：视频 1：消息图片 2：普通图片 */
        ArrayList<Integer> mediaType = new ArrayList<>();
        mediaType.add(TPSDKCommon.MediaType.IPC_DOWNLOADER_MEDIA_TYPE_VIDEO);

        ArrayList<Integer> channelId = new ArrayList<>();
        channelId.add(mChannelId);

        mDeviceContext.reqSearchMedia(mDevice, new IPCReqListener() {
            @Override
            public int callBack(IPCReqResponse ipcReqResponse) {
                if (ipcReqResponse.mError == 0) {
                    IPCReqData<HashMap<Integer, ArrayList<IPCDeviceDefines.IPCSearchMedia>>> result =
                            (IPCReqData<HashMap<Integer, ArrayList<IPCDeviceDefines.IPCSearchMedia>>>) ipcReqResponse;
                    mSearchMediaList.clear();
                    if (result.mData != null && result.mData.get(mChannelId) != null) {
                        mSearchMediaList.addAll(result.mData.get(mChannelId));
                    }
                } else {
                    TPLog.e("TAG", "Search Media error!");
                }
                return 0;
            }
        }, mClientIdForDownload, startCalendar.getTimeInMillis() / 1000, endCalendar.getTimeInMillis() / 1000, listType, mediaType, channelId);
    }

    private void reqDownloadFirstMedia() {
        if (mSearchMediaList.size() <= 0 || mClientIdForDownload == -1) {
            Toast.makeText(this, "Media list is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        mDownloadUri = mSdkDir.toString() + getString(R.string.prefix_download) + System.currentTimeMillis() / 1000 + getString(R.string.suffix_mp4);
        mDeviceContext.reqDownloadMedia(mDevice, new IPCReqListener() {
            @Override
            public int callBack(IPCReqResponse ipcReqResponse) {
                /**
                 * 下载状态详情查看{@link IPCDeviceDefines.DownloadStatus}
                 */
                int downloadStatus = ipcReqResponse.mError; // 下载状态
                int downlaodProgress = ipcReqResponse.mRval; // 下载进度
                // TODO: 2018/10/25 根据下载进度和状态相应的更新UI
                if (downloadStatus == TPSDKCommon.DownloadStatus.IPC_DOWNLOAD_STATUS_COMPLETED) {
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PlaybackActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return 0;
            }
        }, mClientIdForDownload, mSearchMediaList.get(0), mDownloadUri, mChannelId);
    }

    private void reqSearchVideo() {
        if (mClientId == -1)
            return;

        mDeviceContext.reqSearchVideo(mDevice, new IPCReqListener() {
            @Override
            public int callBack(IPCReqResponse ipcReqResponse) {
                if (ipcReqResponse.mError == 0) {
                    IPCReqData<HashMap<Integer, ArrayList<IPCDeviceDefines.IPCSearchVideo>>> result =
                            (IPCReqData<HashMap<Integer, ArrayList<IPCDeviceDefines.IPCSearchVideo>>>) ipcReqResponse;
                    mSearchVideoList.clear();
                    if (result.mData != null && result.mData.get(mChannelId) != null) {
                        mSearchVideoList.addAll(result.mData.get(mChannelId));
                    }
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.update(mSearchVideoList);
                        }
                    });
                } else {
                    TPLog.e("TAG", "Search video error!");
                }
                return 0;
            }
        }, mClientId, mCalendar.getTimeInMillis() / 1000, mChannelId);
    }

    private boolean isVideoSelected() {
        return mEventType != -1 && mClientId != -1 && mStartTime < mEndTime;
    }

}
