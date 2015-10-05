/*
 * code uses 'themoviedbapi' https://github.com/holgerbrandl/themoviedbapi/
 */
package com.smartvariables.lee.popularmovies;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.smartvariables.lee.popularmovies.util.NotifyImageView;

public class MovieListAdapter
        extends ArrayAdapter<MovieInfo> {
    private final static String TAG = "LEE: <" + MovieListAdapter.class.getSimpleName() + ">";

    private static MovieInfoList sMovieList;
    private MainActivity mActivity;
    private int mResource;

    public MovieListAdapter(
            MainActivity activity,
            int resource,
            MovieInfoList sMovieList) {
        super(activity, resource, sMovieList);
        //Log.v(TAG, "MovieListAdapter");
        mActivity = activity;
        mResource = resource;
        MovieListAdapter.sMovieList = sMovieList;
    }

    public static MovieInfoList getMovieList() {
        return sMovieList;
    }

    public void setMovieData(MovieInfoList movieList) {
        Log.v(TAG, "===> setMovieData - movieList.size()=" + movieList.size() + " <===");
        if (movieList.size() == 0) {
            PerformAsyncMovieSearchTask.reset();
        }
        MovieListAdapter.sMovieList = movieList;
        //Log.v(TAG, "notifyDataSetChanged");
        notifyDataSetChanged();
        final MainActivity activity = MainActivity.getTheActivity();
        if (activity != null && movieList.size() > 0) {
            Handler handler = activity.getHandler();
            if (handler != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        hideNoDataImageView(activity);
                    }
                });
            }
        }
    }

    public static void hideNoDataImageView(MainActivity activity) {
        ImageView noDataImage = (ImageView) activity.findViewById(R.id.nodata_image);
        if (noDataImage != null) {
            Log.v(TAG, "*** NODATA IMAGE is GONE");
            noDataImage.setVisibility(View.GONE);
        }
        TextView noDataText = (TextView) activity.findViewById(R.id.nodata_text);
        if (noDataText != null) {
            Log.v(TAG, "*** NODATA TEXT is GONE");
            noDataText.setVisibility(View.GONE);
        }
    }

    @Override
    public int getCount() {
        if (sMovieList == null) {
            return 0;
        }
        return sMovieList.size();
    }

    @Override
    public View getView(
            int position,
            View convertView,
            ViewGroup parent) {
        View row = convertView;
        if (sMovieList.size() <= position) {
            Log.e(TAG, "getView: mMovieList[] is not initialized yet! - position=" + position);
            return row;
        }
        MovieViewHolder holder = null;
        boolean needToCreateNewHolder = false;
        if (row != null) {
            holder = (MovieViewHolder) row.getTag();
            MovieInfo oldMovieInfo = holder.getMovie();
            MovieInfo newMovieInfo = sMovieList.get(position);
            if (oldMovieInfo == null || newMovieInfo == null) {
                needToCreateNewHolder = true;
            }
            else if (! oldMovieInfo.equals(newMovieInfo)) {
                needToCreateNewHolder = true;
                // the oldMovieInfo may be loading/saving an Internet movie poster.. if so, we just let it finish
                // we don't try and reuse the container due to race conditions during loading
                if (holder.getMovieImageViewState() == MovieViewHolder.MovieImageViewState.IMAGE_OK) {
                    Log.v(TAG, "we can reuse this containter - it is finished loading - new movie="+newMovieInfo.getTitle());
                    needToCreateNewHolder = false;
                    holder.re_initializeWithMovie(newMovieInfo);
                }
            }
        }
        if (row == null || needToCreateNewHolder) {
            LayoutInflater inflater = ((Activity) mActivity).getLayoutInflater();
            row = inflater.inflate(mResource, parent, false);
            holder = new MovieViewHolder(
                    mActivity, // MainActivity
                    sMovieList.get(position),
                    (RatingBar) row.findViewById(R.id.rating_bar),
                    (TextView) row.findViewById(R.id.release_date),
                    (NotifyImageView) row.findViewById(R.id.poster_image),
                    (ImageView) row.findViewById(R.id.no_poster_image),
                    (TextView) row.findViewById(R.id.movie_title),
                    (TextView) row.findViewById(R.id.plot_synopsis),
                    (TextView) row.findViewById(R.id.missing_art)
            );
            row.setTag(holder);
            holder.getMovie().setMovieViewHolder(holder);
        }
        Log.v(TAG, "getView - movie="+holder.getMovie().getTitle());
        return row;
    }

}
