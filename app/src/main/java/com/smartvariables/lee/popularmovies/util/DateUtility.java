package com.smartvariables.lee.popularmovies.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtility {
    private final static String TAG = "LEE: <" + DateUtility.class.getSimpleName() + ">";

    public static String getReadableDate(String date) {
        Log.v(TAG, "getReadableDate - date=" + date);
        if (date != null && date.length() > 0) {
            try {
                SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date formDate = fromFormat.parse(date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(formDate);
                if (calendar.get(Calendar.YEAR) > 1900 && calendar.get(Calendar.YEAR) < 2100) {
                    Log.v(TAG, "getReadableDate: formDate=" + formDate);
                    SimpleDateFormat toFormat = new SimpleDateFormat("MMMM dd, yyyy");
                    date = toFormat.format(formDate);
                    Log.v(TAG, "getReadableDate: date=" + date);
                } else {
                    Log.v(TAG, "getReadableDate: invalid release year");
                    date = "";
                }
            } catch (ParseException e) {
                Log.e(TAG, "the date seems invalid. e=" + e);
            }
        } else {
            date = "";
        }
        return date;
    }

}
