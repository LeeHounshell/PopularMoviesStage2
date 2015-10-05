package com.smartvariables.lee.popularmovies.util;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.smartvariables.lee.popularmovies.R;

import at.grabner.circleprogress.AnimationState;
import at.grabner.circleprogress.AnimationStateChangedListener;
import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;

public class CircleFragment extends Fragment {
    private final static String TAG = "LEE: <" + CircleFragment.class.getSimpleName() + ">";

    private CircleProgressView mCircleView;
    private boolean mShowUnit;

    public void initCircleView(View view) {
        Log.v(TAG, "initCircleView (View)");
        mCircleView = (CircleProgressView) view.findViewById(R.id.circle_view);
        mShowUnit = false;
    }

    public void initCircleView(CircleProgressView circleView) {
        Log.v(TAG, "initCircleView (CircleProgressView)");
        mCircleView = circleView; // needed to get around a problem with Fragments and findViewById
        mShowUnit = false;
    }

    public void showCircleView() {
        Log.v(TAG, "showCircleView");
        if (mCircleView == null) {
            Log.w(TAG, "showCircleView: CircleProgressView issue");
            return;
        }
        mCircleView.post(new Runnable() {
            @Override
            public void run() {

                mShowUnit = false;
                mCircleView.setVisibility(View.VISIBLE);
                Log.w(TAG, "showCircleView: CircleView VISIBLE");
                mCircleView.setAutoTextSize(true); // enable auto text size, previous values are overwritten
                mCircleView.setUnitScale(0.9f); // if you want the calculated text sizes to be bigger/smaller
                mCircleView.setTextScale(0.9f); // if you want the calculated text sizes to be bigger/smaller
                mCircleView.setTextColor(Color.RED);
                mCircleView.setText("Loading.."); //shows the given text in the circle view
                mCircleView.setTextMode(TextMode.TEXT); // Set text mode to text to show text
                mCircleView.spin(); // start spinning
                mCircleView.setShowTextWhileSpinning(true); // Show/hide text in spinning mode

                mCircleView.setAnimationStateChangedListener(

                        new AnimationStateChangedListener() {
                            @Override
                            public void onAnimationStateChanged(AnimationState _animationState) {
                                if (mCircleView != null) {
                                    switch (_animationState) {
                                        case IDLE:
                                        case ANIMATING:
                                        case START_ANIMATING_AFTER_SPINNING:
                                            mCircleView.setTextMode(TextMode.PERCENT); // show percent if not spinning
                                            mCircleView.setShowUnit(mShowUnit);
                                            break;
                                        case SPINNING:
                                            mCircleView.setTextMode(TextMode.TEXT); // show text while spinning
                                            mCircleView.setShowUnit(false);
                                        case END_SPINNING:
                                            break;
                                        case END_SPINNING_START_ANIMATING:
                                            break;

                                    }
                                }
                            }
                        }

                );

            }
        });
    }

    public void initializeCircleViewValue(float value) {
        if (mCircleView != null) {
            mShowUnit = true;
            mCircleView.setUnit("%");
            mCircleView.setShowUnit(mShowUnit);
            mCircleView.setTextMode(TextMode.PERCENT); // Shows current percent of the current value from the max value
            mCircleView.setMaxValue(value);
            mCircleView.setValue(0);
            Log.w(TAG, "initializeCircleViewValue: CircleView MAX=" + value);
        }
    }

    public void setCircleViewValue(float value) {
        if (mCircleView != null) {
            mCircleView.setValue(value);
            Log.w(TAG, "setCircleViewValue: CircleView value=" + value);
            if (value == mCircleView.getMaxValue()) {
                hideCircleView();
            }
        }
    }

    public void hideCircleView() {
        if (mCircleView != null) {
            mCircleView.stopSpinning();
            mCircleView.setVisibility(View.GONE);
            Log.w(TAG, "hideCircleView: CircleView HIDDEN");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCircleView = null;
        CircleViewHelper.onDestroy();
    }

    /*
     * called when on back pressed to the current fragment that is returned
     */
    public void onBackPressed()
    {
        Log.v(TAG, "onBackPressed");
    }

}
