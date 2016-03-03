package com.smartvariables.lee.popularmovies2.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.smartvariables.lee.popularmovies2.MovieViewHolder;

public class NotifyImageView extends ImageView {
    private final static String TAG = "LEE: <" + NotifyImageView.class.getSimpleName() + ">";

    private boolean mImageChanged;
    private NotifyImageHolder mHolder;
    private boolean mImageFinished;
    private boolean mMonitoring;

    public NotifyImageView(Context context) {
        super(context);
        mMonitoring = false;
        init();
    }

    public NotifyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMonitoring = false;
        init();
    }

    public NotifyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMonitoring = false;
        init();
    }

    protected void init() {
        if (! mMonitoring) {
            mImageChanged = false;
            mImageFinished = false;
            mHolder = null;
            monitorPreDraw();
        }
    }

    // so we can tell when the image finishes loading..
    // from: http://stackoverflow.com/questions/17606140/how-to-get-when-an-imageview-is-completely-loaded-in-android
    protected void monitorPreDraw() {
        //Log.v(TAG, "monitorPreDraw");
        final NotifyImageView thePosterImage = this;
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                //Log.v(TAG, "onPreDraw");
                try {
                    mMonitoring = true;
                    return true; //note, that "true" is important, since you don't want drawing pass to be canceled
                } finally {
                    getViewTreeObserver().removeOnPreDrawListener(this); // we don't need any further notifications
                    thePosterImage.buildDrawingCache();
                    mImageFinished = true;
                    mMonitoring = false;
                }
            }
        });
    }

    public void setNotifyImageHolder(NotifyImageHolder holder) {
        this.mHolder = holder;
    }

    public boolean isImageChanged() {
        return mImageChanged;
    }

    public boolean isImageFinished() {
        return mImageFinished;
    }

    public void notifyOff() {
        Log.v(TAG, "notifyOff");
        mHolder = null;
        mImageFinished = false;
        mImageChanged = false;
    }

    public void notifyOn(MovieViewHolder holder) {
        Log.v(TAG, "notifyOn");
        init();
        mHolder = holder;
    }

    // the change notify happens here..
    @Override
    public void setImageDrawable(Drawable noPosterImage) {
        Log.v(TAG, "setImageDrawable");
        super.setImageDrawable(noPosterImage);
        if (mHolder != null && mImageFinished) {
            mImageFinished = false; // we send a single change-notification only
            final NotifyImageView theNotifyImageView = this;

            theNotifyImageView.post(new Runnable() {
                @Override
                public void run() {
                    if (mHolder != null) {
                        int width = getMeasuredWidth();
                        int height = getMeasuredHeight();
                        Log.v(TAG, "*** NEW IMAGE DATA! - width=" + width + ", height=" + height);
                        mImageChanged = true;
                        mHolder.notifyImageChanged(theNotifyImageView, width, height);
                    }
                }
            });

        }
    }

}

