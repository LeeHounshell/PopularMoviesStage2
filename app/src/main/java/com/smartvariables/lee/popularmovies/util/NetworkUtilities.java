package com.smartvariables.lee.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.smartvariables.lee.popularmovies.PopularMoviesStage2Application;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtilities {
    private final static String TAG = "LEE: <" + NetworkUtilities.class.getSimpleName() + ">";
    private static boolean firstTime = true;
    private static boolean isConnected = true; // assume online to begin
    private static boolean haveConnectedWifi = false;
    private static boolean haveConnectedMobile = false;

    public static void init() {
        if (firstTime) {
            firstTime = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isNetworkAvailable(); // network ops must never be on the main thread
                }
            }).start();
        }
    }

    // first call not reliable - as isNetworkAvailable() will complete in the background
    public static boolean isConnected() {
        init();
        return isConnected;
    }

    public static boolean isHaveConnectedWifi() {
        init();
        return haveConnectedWifi;
    }

    public static boolean isHaveConnectedMobile() {
        init();
        return haveConnectedMobile;
    }

    // from http://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    public static boolean isNetworkAvailable() {
        Context context = PopularMoviesStage2Application.getAppContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            if (netInfo != null) {
                for (NetworkInfo ni : netInfo) {
                    if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                        if (ni.isAvailable() && ni.isConnectedOrConnecting()) {
                            Log.v(TAG, "haveConnectedWifi = true");
                            haveConnectedWifi = true;
                        }
                    } else if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                        if (ni.isAvailable() && ni.isConnectedOrConnecting()) {
                            Log.v(TAG, "haveConnectedMobile = true");
                            haveConnectedMobile = true;
                        }
                    }
                }
            }
        }
        if (haveConnectedWifi || haveConnectedMobile) {
            return isConnected = isNetworkWorking();
        }
        return isConnected = false;
    }

    // ensure we actually have connectivity, not just an active network interface
    protected static boolean isNetworkWorking() {
        Context context = PopularMoviesStage2Application.getAppContext();
        boolean workingNetwork = false;
        URL url;
        HttpURLConnection urlConnection = null;
        String site = "http://www.google.com";
        try {
            url = new URL(site);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader isw = new InputStreamReader(in);
            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                //System.out.print(current);
            }
            workingNetwork = true;
        } catch (ConnectException e) {
            Log.e(TAG, "problem connecting to " + site + " - error=" + e);
        } catch (IOException e) {
            Log.e(TAG, "problem reading from " + site + " - error=" + e);
        } catch (Exception e) {
            Log.e(TAG, "exception while connecting to " + site + " - error=" + e);
        } finally {
            try {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e(TAG, "problem disconnecting from " + site + " - error=" + e);
            }
        }
        return workingNetwork;
    }

}
