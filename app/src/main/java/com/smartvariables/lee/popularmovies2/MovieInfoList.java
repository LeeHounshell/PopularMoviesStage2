package com.smartvariables.lee.popularmovies2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/*
 * source: http://stackoverflow.com/questions/10953121/android-arraylistmyobject-pass-as-parcelable
 */
public class MovieInfoList extends ArrayList<MovieInfo> implements Parcelable {
    private final static String TAG = "LEE: <" + MovieInfoList.class.getSimpleName() + ">";

    private static final long serialVersionUID = 931854767798790961L;

    public final Parcelable.Creator<MovieInfoList> CREATOR = new Parcelable.Creator<MovieInfoList>() {
        public MovieInfoList createFromParcel(Parcel in) {
            return new MovieInfoList(in);
        }

        public MovieInfoList[] newArray(int size) {
            return new MovieInfoList[size];
        }
    };

    public MovieInfoList() {
    }

    @SuppressWarnings("unused")
    public MovieInfoList(Parcel in) {
        this();
        readFromParcel(in);
    }

    protected void readFromParcel(Parcel in) {
/*
 * unused
 *
        Log.v(TAG, "readFromParcel");
        clear();
        // First we have to read the list size
        int size = in.readInt();

        for (int i = 0; i < size; i++) {
            MovieInfo r = new MovieInfo(in);
            add(r);
        }
*/
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
/*
 * unused
 *
        Log.v(TAG, "writeToParcel");
        int size = size();

        // We have to write the list size, we need him recreating the list
        dest.writeInt(size);

        for (int i = 0; i < size; i++) {
            MovieInfo r = get(i);
            r.writeToParcel(dest, -1);
        }
*/
    }

}
