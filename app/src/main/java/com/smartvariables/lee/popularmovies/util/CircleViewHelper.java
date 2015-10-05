package com.smartvariables.lee.popularmovies.util;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartvariables.lee.popularmovies.DetailActivity;
import com.smartvariables.lee.popularmovies.DetailActivityFragment;
import com.smartvariables.lee.popularmovies.MainActivity;
import com.smartvariables.lee.popularmovies.MainActivityFragment;
import com.smartvariables.lee.popularmovies.R;

public class CircleViewHelper {
    private static CircleViewTarget sTarget = null;
    private static AppCompatActivityBase sActivityBase = null;
    private static CircleFragment sFragmentBase = null;

    public enum CircleViewTarget {
        MASTER, DETAIL
    }

    private static void init() {
        if (sTarget != null) {
            switch (sTarget) {

                case MASTER: {
                    sActivityBase = MainActivity.getTheActivity();
                    sFragmentBase = MainActivityFragment.getTheFragment();
                    break;
                }

                case DETAIL: {
                    sActivityBase = DetailActivity.getTheActivity();
                    sFragmentBase = DetailActivityFragment.getTheFragment();
                    break;
                }
            }
        }
    }

    public static void onDestroy() {
        sTarget = null;
        sActivityBase = null;
        sFragmentBase = null;
    }

    public static void showCircleView(CircleViewTarget target) {
        sTarget = target;
        init();
        if (sActivityBase != null && sFragmentBase != null) {
            Handler handler = sActivityBase.getHandler();
            if (handler != null) {
                final AppCompatActivityBase activityBaseFinal = sActivityBase;
                final CircleFragment fragmentBaseFinal = sFragmentBase;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ImageView noDataImage = (ImageView) activityBaseFinal.findViewById(R.id.nodata_image);
                        if (noDataImage != null) {
                            noDataImage.setVisibility(View.GONE);
                        }
                        fragmentBaseFinal.showCircleView();
                    }
                });
            }
        }
    }

    public static void initializeCircleViewValue(final float circleMax) {
        init();
        if (sActivityBase != null && sFragmentBase != null) {
            Handler handler = sActivityBase.getHandler();
            if (handler != null) {
                final CircleFragment fragmentBaseFinal = sFragmentBase;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        fragmentBaseFinal.initializeCircleViewValue(circleMax);
                    }
                });
            }
        }
    }

    public static void setCircleViewValue(final float value) {
        init();
        if (sActivityBase != null && sFragmentBase != null) {
            Handler handler = sActivityBase.getHandler();
            if (handler != null) {
                final CircleFragment fragmentBaseFinal = sFragmentBase;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        fragmentBaseFinal.setCircleViewValue(value);
                    }
                });
            }
        }
    }

    public static void hideCircleView(final boolean doDelay, final boolean noData) {
        init();
        if (sActivityBase != null && sFragmentBase != null) {
            Handler handler = sActivityBase.getHandler();
            if (handler != null) {
                int delay = 1000; // spin the circle for 1 secs
                if (doDelay) {
                    delay = 2000; // spin the circle for 2 secs if 'doDelay'
                }
                final AppCompatActivityBase activityBaseFinal = sActivityBase;
                final CircleFragment fragmentBaseFinal = sFragmentBase;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fragmentBaseFinal.hideCircleView();
                        if (noData) {
                            TextView noDataText = (TextView) activityBaseFinal.findViewById(R.id.nodata_text);
                            if (noDataText != null) {
                                noDataText.setVisibility(View.VISIBLE);
                            }
                            ImageView noDataImage = (ImageView) activityBaseFinal.findViewById(R.id.nodata_image);
                            if (noDataImage != null) {
                                noDataImage.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }, delay);
            }
        }
    }

}
