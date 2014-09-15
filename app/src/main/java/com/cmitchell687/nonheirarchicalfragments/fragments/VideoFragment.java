package com.cmitchell687.nonheirarchicalfragments.fragments;

import android.app.Fragment;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.cmitchell687.nonheirarchicalfragments.R;

public class VideoFragment extends Fragment implements View.OnClickListener {

    private boolean mIsExpanded = false;

    private LinearLayout mView;
    private VideoView mVideoView;

    private int mHeight;
    private int mWidth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = (LinearLayout) inflater.inflate(R.layout.special, container);
        mView.setOnClickListener(this);

        mVideoView = (VideoView) mView.findViewById(R.id.video_player);

        mVideoView.setVideoURI(Uri.parse("http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8"));

        return mView;
    }

    @Override
    public void onResume() {
        mVideoView.start();

        super.onResume();
    }

    @Override
    public void onPause() {
        mVideoView.pause();

        super.onPause();
    }

    public void setContainerSize(int height, int width) {
        mHeight = height;
        mWidth = width;
    }

    @Override
    public void onClick(View view) {
        if (mIsExpanded) {
            collapse(mView);
            getActivity().getActionBar().show();
            mIsExpanded = false;

        } else {
            expand(mView);
            getActivity().getActionBar().hide();
            mIsExpanded = true;
        }
    }

    private void expand(final View v) {

        final int startHeight = v.getHeight();
        final int startWidth = v.getWidth();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = calcSize(startHeight, mHeight, interpolatedTime);
                v.getLayoutParams().width = calcSize(startWidth, mWidth, interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int)(mHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    private int calcSize(int start, int stop, float interpolatedTime) {
        return (int)((start * (1-interpolatedTime)) + (stop * interpolatedTime));
    }

    private void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Resources res = getResources();
        final int finalHeight = (int)res.getDimension(R.dimen.video_player_height);
        final int finalWidth = (int)res.getDimension(R.dimen.video_player_width);

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = calcSize(mHeight, finalHeight, interpolatedTime);
                v.getLayoutParams().width = calcSize(mWidth, finalWidth, interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
