package com.smartvariables.lee.popularmovies2.util;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class AppCompatActivityBase extends AppCompatActivity {

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler = null;
    }

    public Handler getHandler() {
        return mHandler;
    }

}
