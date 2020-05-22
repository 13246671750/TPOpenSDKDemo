package com.tplink.sdk.tpopensdkdemo.player;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tplink.applibs.util.TPByteArrayJNI;
import com.tplink.sdk.tpopensdk.TPOpenSDK;
import com.tplink.sdk.tpopensdk.TPPlayer;
import com.tplink.sdk.tpopensdk.TPSDKContext;
import com.tplink.sdk.tpopensdk.common.TPSDKCommon;
import com.tplink.sdk.tpopensdk.openctx.IPCDevice;
import com.tplink.sdk.tpopensdk.openctx.IPCDeviceContext;
import com.tplink.sdk.tpopensdkdemo.R;
import com.tplink.sdk.tpopensdkdemo.common.TPApplication;
import com.tplink.sdk.tpopensdkdemo.common.TPUtils;
import com.tplink.sdk.tpopensdkdemo.common.TPViewUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PlayerActivity extends AppCompatActivity {
    public static final String EXTRA_DEVICE = "EXTRA_DEVICE";
    TPPlayer mPlayer = null;
    File mRootDir = Environment.getExternalStorageDirectory();
    File mSdkDir = new File(mRootDir, "TPOpenSDK");

    protected Handler mMainHandler = new Handler();
    protected String mRecordUri, mDownloadUri;
    // FIXME: 2018/10/18 channelId&listType is custom
    protected int mChannelId = -1, mListType = 1;

    protected boolean mIsGetVideoPort, mIsSetScanTour;
    protected boolean mIsPlay;
    protected boolean mIsPause;
    protected boolean mIsSoundOn = true;
    protected boolean mIsClear;
    protected boolean mIsRecord;
    protected boolean mIsCruise;
    protected IPCDevice mDevice;
    protected TPSDKContext mSDKContext;
    protected IPCDeviceContext mDeviceContext;

    protected TextView mTitleLeft;
    protected TextView mTitleCenter;
    protected TextView mTitleRight;
    protected ViewGroup mViewHolder;
    protected TextView mTimeTv, mFlowTv, mRecordDurationTv;

    protected ImageView mPlayBtn, mSoundBtn, mQualityBtn, mOrientationBtn, mSnapshotBtn, mRecordBtn;

    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.player_orientation_btn:
                reqOrientation();
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        if (!mSdkDir.exists() && !mSdkDir.mkdir()) {
            Log.e("TAG", "sdk dir create fail!");
        }

        initData();

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mIsPlay)
            play();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mIsPlay)
            stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setContentView(getContentViewId());
        initView();
        TPUtils.fullScreen(this, TPUtils.isLandscape(this));
    }

    protected int getContentViewId() {
        return 0;
    }

    protected void initData() {
        mDevice = getIntent().getParcelableExtra(EXTRA_DEVICE);
        mSDKContext = TPApplication.INSTANCE.getSDKContext();
        mDeviceContext = mSDKContext.getDevCtx();
        initStatus();
    }

    protected void initView() {
        if (!TPUtils.isLandscape(this))
            initTitleBar();

        mViewHolder = findViewById(R.id.player_view_holder);
        // need to replace container when set orientation
        if (mPlayer != null) {
            mPlayer.setViewHolder(mViewHolder);
        }
    }

    protected void initTitleBar() {
        mTitleLeft = findViewById(R.id.title_bar_left_tv);
        mTitleCenter = findViewById(R.id.title_bar_center_tv);
        mTitleRight = findViewById(R.id.title_bar_right_tv);
    }

    protected void updateBtnStatus(ImageView v, boolean enable) {
    }

    protected void updateBtnStatus() {
        LinearLayout[] groups = new LinearLayout[]{
                findViewById(R.id.player_tab_bar),
                findViewById(R.id.player_controller_btn_top),
                findViewById(R.id.player_controller_btn_bottom)
        };

        for (int i = 0; i < groups.length; i++) {
            if (groups[i] != null) {
                for (int j = 0; j < groups[i].getChildCount(); j++) {
                    View v = groups[i].getChildAt(j);
                    if (v instanceof ImageView)
                        updateBtnStatus((ImageView) v, true);
                }
            }
        }
    }

    protected void togglePlay() {
        if (mIsPlay) {
            stop();
        } else {
            play();
        }
    }

    protected void toggleRecord() {
        if (mPlayer == null)
            return;
        if (mIsRecord) {
            mPlayer.stopRecord();
            Toast.makeText(this, "record uri: " + mRecordUri, Toast.LENGTH_SHORT).show();
        } else {
            mRecordUri = mSdkDir.toString() + getString(R.string.prefix_record) + System.currentTimeMillis() / 1000 + getString(R.string.suffix_mp4);
            mPlayer.startRecord(mRecordUri);
        }
        mIsRecord = !mIsRecord;
    }

    protected void toggleCruise() {
        if (mPlayer == null)
            return;
        if (mIsCruise) {
            stopCruise();
        } else {
            if (mIsSetScanTour) {
                startCruise();
            } else {
                Log.e("TAG", "Error! not set scanTour");
            }
        }
    }

    protected void toggleSound() {
        if (mPlayer == null)
            return;
        if (mIsSoundOn) {
            mPlayer.turnOffSound();
        } else {
            mPlayer.turnOnSound();
        }
        mIsSoundOn = !mIsSoundOn;
    }

    protected void toggleQuality() {
        if (mPlayer == null)
            return;
        if (mIsClear) {
            mPlayer.changeQuality(TPSDKCommon.Quality.QUALITY_FLUENCY);
        } else {
            mPlayer.changeQuality(TPSDKCommon.Quality.QUALITY_CLEAR);
        }
        mIsClear = !mIsClear;
    }


    protected void startCruise() {
    }

    protected void stopCruise() {
    }

    protected void play() {
        initPlayer();
    }

    protected void stop() {
        if (mPlayer == null)
            return;
        mPlayer.stop();
        releasePlayer();
        initStatus();
        updateBtnStatus();
    }

    protected void onPlayTimeChange(long playTime) {
    }

    private void initStatus() {
        mIsPlay = false;
        mIsClear = false;
        mIsRecord = false;
        mIsSoundOn = true;
        mIsPause = false;
    }

    private void initPlayer() {
        mPlayer = TPOpenSDK.getInstance()
                .createPlayer(this)
                .setViewHolder(mViewHolder);
        mPlayer.setPlayerCallback(new TPPlayer.PlayerCallback() {
            @Override
            public int onPlayStatusChange(int status, int errorCode) {
                Log.v("TAG", "onPlayStatusChange:: status = " + status + "; errorCode = " + errorCode);
                return 0;
            }

            @Override
            public int onRecordStatusChange(int status, int errorCode, String filePath) {
                Log.v("TAG", "onRecordStatusChange:: status = " + status + "; errorCode = " + errorCode + "; filePath = " + filePath);
                return 0;
            }

            @Override
            public int onSnapshot(int errorCode, String filePath) {
                Log.v("TAG", "onSnapshot:: errorCode = " + errorCode + "; filePath = " + filePath);
                return 0;
            }

            @Override
            public int onPlayTimeUpdate(final long playTime) {
                Log.v("TAG", "onPlayTimeUpdate:: playTime = " + playTime);
                Date date = new Date(playTime * 1000);
                SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.player_time_formatter));
                final String text = sdf.format(date);
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        TPViewUtils.setText(mTimeTv, text);
                        onPlayTimeChange(playTime);
                    }
                });

                return 0;
            }

            @Override
            public int onRecordDurationUpdate(final long duration) {
                Log.v("TAG", "onRecordDurationUpdate:: duration = " + duration);
                if (mRecordDurationTv != null) {
                    mRecordDurationTv.post(new Runnable() {
                        @Override
                        public void run() {
                            TPViewUtils.setText(mRecordDurationTv, duration + "");
                        }
                    });
                }
                return 0;
            }

            @Override
            public int onDataStatistics(final long dataSize, final double dataSpeed) {
                Log.v("TAG", "onDataStatistics:: dataSize = " + dataSize + "; dataSpeed = " + dataSpeed);
                if (mFlowTv != null) {
                    mFlowTv.post(new Runnable() {
                        @Override
                        public void run() {
                            TPViewUtils.setText(mFlowTv, String.format("%1$.2f", dataSpeed) + "/" + dataSize);
                        }
                    });
                }
                return 0;
            }

            @Override
            public int onChangeQuality(int quality) {
                Log.v("TAG", "onChangeQuality:: quality = " + quality);
                return 0;
            }

            @Override
            public int onDataRecv(TPByteArrayJNI tpByteArrayJNI) {
               // Log.v("TAG", "onDataRecv:: pointer = " + tpByteArrayJNI.getBufferPointer() + "; size = " + tpByteArrayJNI.size());
                return 0;
            }
        });
    }


    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void reqOrientation() {
        if (TPUtils.isLandscape(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
}
