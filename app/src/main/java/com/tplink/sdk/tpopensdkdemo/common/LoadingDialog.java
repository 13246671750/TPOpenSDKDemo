package com.tplink.sdk.tpopensdkdemo.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tplink.sdk.tpopensdkdemo.R;

/**
 * @author fuyuncong
 * @ClassName: LoadingDialog
 * @Description: 转圈弹框控件
 * @date 2017-08-10
 */

public class LoadingDialog extends BaseDialog {

    public static final String TAG = LoadingDialog.class.getSimpleName();

    private static final String STRING_HINT = "loading_tv";

    public static boolean DIALOG_SHOW = false;

    private RoundProgressBar mRoundProgressBar;
    private TextView mLoadingTv;

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View dialogView = inflater.inflate(R.layout.dialog_loading, container, false);

        mLoadingTv = (TextView) dialogView.findViewById(R.id.dialog_loading_tv);
        mRoundProgressBar = dialogView.findViewById(R.id.dialog_loading_progress_bar);

        String loadingText =
                getArguments() != null ? getArguments().getString(STRING_HINT, "") : null;
        setLoadingTv(loadingText);
        return dialogView;
    }

    @Override
    protected boolean showDimBehind() {
        return false;
    }

    public void setLoadingTv(String tv) {
        if (isAdded()) {
            if (TextUtils.isEmpty(tv)) {
                mLoadingTv.setVisibility(View.GONE);
            } else {
                mLoadingTv.setVisibility(View.VISIBLE);
                mLoadingTv.setText(tv);
            }
        } else {
            getArguments().putString(STRING_HINT, tv);
        }
    }

    public void dismissWithAnim() {
        mRoundProgressBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadingDialog.this.dismissAllowingStateLoss();
            }
        }, mRoundProgressBar.onComplete());
    }

    public static LoadingDialog newInstance(@Nullable String text) {
        Bundle args = new Bundle();
        args.putString(STRING_HINT, text);
        LoadingDialog fragment = new LoadingDialog();
        fragment.setArguments(args);
        return fragment;
    }
}
