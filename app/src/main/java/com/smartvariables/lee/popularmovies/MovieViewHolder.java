package com.smartvariables.lee.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.smartvariables.lee.popularmovies.util.AppCompatActivityBase;
import com.smartvariables.lee.popularmovies.util.BitmapUtility;
import com.smartvariables.lee.popularmovies.util.DateUtility;
import com.smartvariables.lee.popularmovies.util.NetworkUtilities;
import com.smartvariables.lee.popularmovies.util.NotifyImageHolder;
import com.smartvariables.lee.popularmovies.util.NotifyImageView;
import com.smartvariables.lee.popularmovies.util.Tmdb_API_KEY;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//import com.bumptech.glide.Glide;

// UI data holder for one movie item
public class MovieViewHolder implements
        MovieViewHolderInterface,
        NotifyImageHolder {
    private final static String TAG = "LEE: <" + MovieViewHolder.class.getSimpleName() + ">";

    private AppCompatActivityBase mActivityBase;
    private MovieInfo mMovie;
    private Uri mMovieInfoUri;
    private RatingBar mRatingBar;
    private MovieViewHolder.MovieImageViewState mMovieImageViewState;
    private TextView mReleaseDateTextView;
    private NotifyImageView mPosterImageView;
    private ImageView mNoImageView;
    private TextView mMovieTitleTextView;
    private TextView mPlotSynopsis;
    private TextView mMissingArtTextView;
    private WebView mMovieReviewsWebView;
    private int mTextColor;
    private int mBackgroundColor;
    private int mDelayUntilExpectedUpdate;
    private boolean noImage;

    public final static int MIN_IMAGE_SIZE = 32;

    //-------------------------------------------------------
    // Possible Poster-Image State-Machine Transitions:
    //
    // [start] -> TRY_SQLITE_QUERY
    // TRY_SQLITE_QUERY -> TRY_INTERNET | SQLITE_FOUND
    // TRY_INTERNET -> INTERNET_REQUESTED
    // INTERNET_REQUESTED -> INTERNET_RESPONSE_RECEIVED
    // INTERNET_RESPONSE_RECEIVED -> SQLITE_STORE | TRY_INTERNET
    // SQLITE_STORE -> IMAGE_OK | TRY_INTERNET
    // SQLITE_FOUND -> IMAGE_OK
    // IMAGE_OK -> [end]
    //
    public enum MovieImageViewState {
        TRY_SQLITE_QUERY, SQLITE_FOUND, TRY_INTERNET, INTERNET_REQUESTED, INTERNET_RESPONSE_RECEIVED, SQLITE_STORE, IMAGE_OK
    }

    public MovieViewHolder(
            AppCompatActivityBase activityBase,
            MovieInfo movie,
            RatingBar ratingBar,
            TextView releaseDateTextView,
            NotifyImageView posterImageView,
            ImageView noPosterImageView,
            TextView movieTitleTextView,
            TextView plotSynopsis,
            TextView missingArtTextView) {
        Log.v(TAG, "new MovieViewHolder - movie=" + movie.getTitle());
        noImage = true;
        setActivityBase(activityBase);
        setRatingBar(ratingBar);
        setReleaseDateTextView(releaseDateTextView);
        setPosterImageView(posterImageView);
        setNoImageView(noPosterImageView);
        setMovieTitleTextView(movieTitleTextView);
        setPlotSynopsis(plotSynopsis);
        setMissingArtTextView(missingArtTextView);
        setMovieReviewsWebView(null);
        initializeWithMovie(movie);
    }

    public void re_initializeWithMovie(MovieInfo movie) {
        Log.w(TAG, "*** re_initializeWithMovie: movie=" + movie);
        hideMissingPosterImageAndText();
        movie.setPoster(null);
        movie.setPosterWidth(0);
        movie.setPosterHeight(0);
        initializeWithMovie(movie);
    }

    public void initializeWithMovie(MovieInfo movie) {
        getPosterImageView().notifyOff();
        setMovie(movie);
        setMovieImageViewState(MovieImageViewState.TRY_SQLITE_QUERY);
        Log.w(TAG, "initializeWithMovie: state=TRY_SQLITE_QUERY - movie=" + getMovie().getTitle());

        if (movie.getPosterPath() != null) {
            Log.w(TAG, "initializeWithMovie: found the movie poster path! - movie=" + getMovie().getTitle());
            setMovieInfoUri(
                    Uri.parse(Tmdb_API_KEY.getBaseUrl())
                            .buildUpon()
                            .appendEncodedPath(getSizeParameter())
                            .appendEncodedPath(getMovie().getPosterPath())
                            .build());
        } else {
            Log.w(TAG, "initializeWithMovie: unable to get movie poster path! - movie=" + getMovie().getTitle());
            setMovieInfoUri(null);
        }

        setBackgroundColor(Color.TRANSPARENT);
        setTextColor(Color.WHITE);
        setDelayUntilExpectedUpdate(300);

/*
        if (movie.isLoadedParcelable()) {
            Log.v(TAG, "initializeWithMovie: FROM PARCELABLE -------------------------------------------- FROM PARCELABLE");
            //if (getPosterImageView() != null && movie.getPoster() != null) {
            //    setMovieImageViewState(MovieImageViewState.IMAGE_OK);
            //    Log.v(TAG, "initializeWithMovie: ---> marking PARCELABLE image as state=IMAGE_OK.. - movie=" + getMovie().getTitle() + " <---");
            //} else
            {
                Log.v(TAG, "initializeWithMovie: ---> WE NEED TO RELOAD THE POSTER IMAGE.. - movie=" + getMovie().getTitle() + " <---");
                reloadPosterImage();
            }
        } else
*/
        {
            Log.v(TAG, "initializeWithMovie: *** TRY LOADING THE MOVIE INFO FROM SQL FIRST ***");
            prepareHolderAndQueryMovie();
        }

        // display the movie data
        if (getRatingBar() != null) {
            float rating = getMovie().getVoteAverage() / (float) 2.0;
            getRatingBar().setRating(rating);
        }
        if (getReleaseDateTextView() != null) {
            getReleaseDateTextView().setText(DateUtility.getReadableDate(trim(movie.getReleaseDate())));
        }
        if (getMovieTitleTextView() != null) {
            getMovieTitleTextView().setText(trim(movie.getTitle()));
        }
        if (getPlotSynopsis() != null) {
            getPlotSynopsis().setText(trim(getMovie().getOverview()));
        }
    }

    public void reloadPosterImage() {
        if (getMovieImageViewState() == MovieImageViewState.TRY_SQLITE_QUERY) {
            Log.v(TAG, "reloadPosterImage: RELOAD THE POSTER-IMAGE - query SQLite for the poster image - movie=" + getMovie().getTitle());
            if (!prepareHolderAndQueryMovie()) {
                setMovieImageViewState(MovieImageViewState.TRY_INTERNET);
                Log.w(TAG, "reloadPosterImage: state=TRY_INTERNET - movie=" + getMovie().getTitle());
            }
        }
        if (getMovieImageViewState() != MovieImageViewState.INTERNET_REQUESTED
                && getMovieImageViewState() != MovieImageViewState.IMAGE_OK
                && getMovieImageViewState() != MovieImageViewState.SQLITE_FOUND) {
            Log.v(TAG, "reloadPosterImage: query INTERNET for the poster image - movie=" + getMovie().getTitle());
            queryPosterImageFromOnlineMovieService();
        }
    }

    // setup a holder to be reused by the display adapter..
    public boolean prepareHolderAndQueryMovie() {
        Log.v(TAG, "prepareHolderAndQueryMovie - state=" + getMovieImageViewState() + ", movie=" + getMovie().getTitle());
        boolean found = false;
        switch (getMovieImageViewState()) {
            case INTERNET_REQUESTED: {
                Log.v(TAG, "prepareHolderAndQueryMovie: case INTERNET_REQUESTED - movie=" + getMovie().getTitle());
                break;
            }
            case TRY_SQLITE_QUERY: {
                Log.v(TAG, "prepareHolderAndQueryMovie: case TRY_SQLITE_QUERY - movie=" + getMovie().getTitle());
                if (getMovie().querySqliteMoviePoster(this) == true) {
                    setMovieImageViewState(MovieImageViewState.SQLITE_FOUND);
                    updatePosterFromMovieInfo();
                    Log.v(TAG, "==> prepareHolderAndQueryMovie: new state=SQLITE_FOUND - FOUND IMAGE in SQLITE - movie=" + getMovie().getTitle());
                    found = true;
                    showPosterImage();
                    break;
                }
                Log.v(TAG, "prepareHolderAndQueryMovie: the poster image is not in SQLITE.. state=TRY_INTERNET - movie=" + getMovie().getTitle());
                // due to Threaded nature, state may have changed..
                if (getMovieImageViewState() == MovieImageViewState.IMAGE_OK) {
                    showPosterImage();
                    break;
                }
                setMovieImageViewState(MovieImageViewState.TRY_INTERNET);
                showPlaceholderCoverImage();
                // fall into TRY_INTERNET case below
            }
            case TRY_INTERNET: {
                Log.v(TAG, "prepareHolderAndQueryMovie: case TRY_INTERNET - movie=" + getMovie().getTitle());
                queryPosterImageFromOnlineMovieService();
                break;
            }
            default: {
                Log.v(TAG, "prepareHolderAndQueryMovie - ALREADY PREPARED - state=" + getMovieImageViewState() + " - movie=" + getMovie().getTitle());
                break;
            }
        }
        return found;
    }

    public void updatePosterFromMovieInfo() {
        if (! MainActivity.isTwoPane()) {
            if (getPosterImageView() != null) {

                getPosterImageView().post(new Runnable() {
                    @Override
                    public void run() {
                        Log.v(TAG, "---> INTERNAL POSTER UPDATE <--- movie=" + getMovie().getTitle());
                        getPosterImageView().setImageBitmap(getMovie().getPoster());
                    }
                });

            }
        }
    }

    // implement NotifyImageHolder interface
    // synchronize a valid poster-image display to SQLite poster image-copy
    public void notifyImageChanged(final NotifyImageView thePosterImage, final int width, final int height) {
        if (width < MIN_IMAGE_SIZE || height < MIN_IMAGE_SIZE) {
            Log.v(TAG, "===> RE-REQUEST INVALID IMAGE - notifyImageChanged: state=INTERNET_RESPONSE_RECEIVED - movie=" + getMovie().getTitle());
            setMovieImageViewState(MovieImageViewState.TRY_INTERNET);
            queryPosterImageFromOnlineMovieService();
            return;
        }
        if (getMovieImageViewState() == MovieImageViewState.IMAGE_OK) {
            Log.w(TAG, "notifyImageChanged: ALREADY IMAGE_OK! - IGNORED IMAGE UPDATE - movie=" + getMovie().getTitle());
            return;
        }
        Log.v(TAG, "===> notifyImageChanged: state=INTERNET_RESPONSE_RECEIVED - movie=" + getMovie().getTitle());
        thePosterImage.notifyOff();
        setMovieImageViewState(MovieImageViewState.INTERNET_RESPONSE_RECEIVED);
        if (getPosterImageView() != null) {
            final MovieViewHolder holder = this;
            Handler handler = getHandler();
            if (handler != null) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        // first save the new image..
                        final Bitmap posterImage;
                        Drawable drawable = thePosterImage.getDrawable();
                        if (drawable != null) {
                            posterImage = BitmapUtility.drawableToBitmap(drawable);
                        } else {
                            thePosterImage.buildDrawingCache();
                            posterImage = thePosterImage.getDrawingCache();
                        }
                        Log.v(TAG, "*** UPDATING THE POSTER IMAGE BITMAP.. *** - movie=" + getMovie().getTitle());
                        getMovie().setPoster(posterImage);
                        getMovie().setPosterWidth(width);
                        getMovie().setPosterWidth(height);
                        // next make sure the database is up-to-date with this poster image

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.v(TAG, "================================================================================");
                                Log.v(TAG, "*** SAVE/UPDATE THE POSTER IN SQLITE! - movie=" + getMovie().getTitle() + ", current state=" + getMovieImageViewState());
                                Log.v(TAG, "================================================================================");
                                if (MovieInfo.updateSqliteMoviePoster(holder, posterImage)) {
                                    Log.v(TAG, "*** SAVE/UPDATE THE POSTER IN SQLITE OK! - movie=" + getMovie().getTitle());
                                    updatePosterFromMovieInfo();
                                } else {
                                    Log.w(TAG, "*** UNABLE TO SAVE/UPDATE THE POSTER - TRY_INTERNET!! - movie=" + getMovie().getTitle());
                                    setMovieImageViewState(MovieImageViewState.TRY_INTERNET);
                                    queryPosterImageFromOnlineMovieService();
                                }
                                Log.v(TAG, "================================================================================");
                            }
                        }).start();

                        showPosterImage();
                    }
                });
            }
        } else {
            Log.w(TAG, "notifyImageChanged: NO POSTER IMAGE VIEW! - IGNORED IMAGE UPDATE - movie=" + getMovie().getTitle());
        }
    }

    public boolean queryPosterImageFromOnlineMovieService() {
        if (getMovieInfoUri() != null && NetworkUtilities.isConnected()) {
            Log.v(TAG, "===> queryPosterImageFromOnlineMovieService - movie=" + getMovie().getTitle() + " <===");
            setDelayUntilExpectedUpdate(9000);
            Log.v(TAG, "Internet: request=" + getMovieInfoUri() + " - movie=" + getMovie().getTitle());
            try {
                if (getMovie() != null) {
                    getMovie().reset();
                }
                getPosterImageView().notifyOn(this); // so we get the callback 'notifyImageChanged'
                // remember to add to build.grade dependencies: compile 'com.squareup.picasso:picasso:2.5.2'
                Picasso.with(getActivityBase())
                        .load(this.getMovieInfoUri())
                        .into(this.getPosterImageView());
/*
 * USE OPTIONAL 'GLIDE' IMAGE SUPPORT LIBRARY (instead of Picasso)
 *
                // 'Glide' an alternate image loading library could be used instead of Picasso..
                // add to build.grade dependencies: compile 'com.github.bumptech.glide:glide:3.6.1'
                Glide.with(getActivityBase()).load(getMovieInfoUri()).asBitmap().into(getPosterImageView());
 */
                setMovieImageViewState(MovieImageViewState.INTERNET_REQUESTED);
                Log.v(TAG, "queryPosterImageFromOnlineMovieService: ==> state=INTERNET_REQUESTED - movie=" + getMovie().getTitle() + " <==");
                return true;
            } catch (Exception e) {
                Log.e(TAG, "*** EXCEPTION while loading Internet Poster Image - error=" + e);
            }
        }
        showPlaceholderCoverImage();
        return false;
    }

    // note: also converts null to empty String
    protected static String trim(String toTrim) {
        if (toTrim != null) {
            return toTrim.trim();
        }
        return "";
    }

    protected String getReadableDate(String releaseDate) {
        if (releaseDate != null && releaseDate.length() > 0) {
            try {
                SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = fromFormat.parse(releaseDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                if (calendar.get(Calendar.YEAR) > 1900 && calendar.get(Calendar.YEAR) < 2100) {
                    Log.v(TAG, "getReadableDate: date=" + date);
                    SimpleDateFormat toFormat = new SimpleDateFormat("MMMM, dd yyyy");
                    releaseDate = toFormat.format(date);
                    Log.v(TAG, "getReadableDate: releaseDate=" + releaseDate);
                } else {
                    Log.v(TAG, "getReadableDate: invalid release year");
                    releaseDate = "";
                }
            } catch (ParseException e) {
                Log.e(TAG, "the release date seems invalid. e=" + e);
            }
        }
        return releaseDate;
    }

    protected String getSizeParameter() {
        // determine based on device screen size
        WindowManager wm = (WindowManager) getActivityBase().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        //int height = size.y;
        if (width <= 400) {
            return "w92";
        } else if (width <= 800) {
            return "w154";
        } else if (width <= 1280) {
            return "w185";
        } else {
            return "w342";
        }
    }

    public AppCompatActivityBase getActivityBase() {
        return mActivityBase;
    }

    public void setActivityBase(AppCompatActivityBase activityBase) {
        mActivityBase = activityBase;
    }

    public MovieInfo getMovie() {
        return mMovie;
    }

    public void setMovie(MovieInfo movie) {
        mMovie = movie;
    }

    public Uri getMovieInfoUri() {
        return mMovieInfoUri;
    }

    public void setMovieInfoUri(Uri movieInfoUri) {
        mMovieInfoUri = movieInfoUri;
    }

    public RatingBar getRatingBar() {
        return mRatingBar;
    }

    public void setRatingBar(RatingBar ratingBar) {
        mRatingBar = ratingBar;
    }

    public MovieViewHolder.MovieImageViewState getMovieImageViewState() {
        return mMovieImageViewState;
    }

    public void setMovieImageViewState(MovieViewHolder.MovieImageViewState movieImageViewState) {
        mMovieImageViewState = movieImageViewState;
    }

    public TextView getReleaseDateTextView() {
        return mReleaseDateTextView;
    }

    public void setReleaseDateTextView(TextView releaseDateTextView) {
        mReleaseDateTextView = releaseDateTextView;
    }

    public NotifyImageView getPosterImageView() {
        return mPosterImageView;
    }

    public void setPosterImageView(NotifyImageView posterImageView) {
        mPosterImageView = posterImageView;
        if (mPosterImageView != null) {
            mPosterImageView.setNotifyImageHolder(this);
        }
    }

    public ImageView getNoImageView() {
        return mNoImageView;
    }

    public void setNoImageView(ImageView noImageView) {
        mNoImageView = noImageView;
    }

    public TextView getMissingArtTextView() {
        return mMissingArtTextView;
    }

    public void setMissingArtTextView(TextView missingArtTextView) {
        mMissingArtTextView = missingArtTextView;
    }

    public boolean isNoImage() {
        return noImage;
    }

    public void setNoImage(boolean noImage) {
        this.noImage = noImage;
    }

    public TextView getMovieTitleTextView() {
        return mMovieTitleTextView;
    }

    public void setMovieTitleTextView(TextView movieTitleTextView) {
        mMovieTitleTextView = movieTitleTextView;
    }

    public TextView getPlotSynopsis() {
        return mPlotSynopsis;
    }

    public void setPlotSynopsis(TextView plotSynopsis) {
        mPlotSynopsis = plotSynopsis;
    }

    public WebView getMovieReviewsWebView() {
        return mMovieReviewsWebView;
    }

    public void setMovieReviewsWebView(WebView reviewsWebView) {
        mMovieReviewsWebView = reviewsWebView;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    public int getDelayUntilExpectedUpdate() {
        return mDelayUntilExpectedUpdate;
    }

    public void setDelayUntilExpectedUpdate(int delayUntilExpectedUpdate) {
        mDelayUntilExpectedUpdate = delayUntilExpectedUpdate;
    }

    public String getReviews() {
        if (getMovie() != null) {
            return getMovie().getReviews();
        }
        return "";
    }

    // show the Poster Image!
    protected void showPosterImage() {
        Handler handler = getHandler();
        if (handler != null) {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.v(TAG, "showPosterImage: movie=" + getMovie().getTitle());
                    hideMissingPosterImageAndText();
                    if (getPosterImageView() != null) {
                        Bitmap posterBitmap = getMovie().getPoster();
                        if (posterBitmap != null) {
                            if (posterBitmap.getWidth() > MIN_IMAGE_SIZE && posterBitmap.getHeight() > MIN_IMAGE_SIZE && posterBitmap.getByteCount() > MIN_IMAGE_SIZE) {
                                Log.v(TAG, "showPosterImage: *** SHOW GOOD IMAGE! *** - getMovie().getPoster() - movie=" + getMovie().getTitle());
                                getPosterImageView().setImageBitmap(posterBitmap);
                                Log.v(TAG, "showPosterImage: updated PosterImageView with new bitmap. width=" + posterBitmap.getWidth() + ", height=" + posterBitmap.getHeight());
                            }
                            else {
                                Log.v(TAG, "===> RE-REQUEST IMAGE - showPosterImage for BAD IMAGE - movie=" + getMovie().getTitle());
                                setMovieImageViewState(MovieImageViewState.TRY_INTERNET);
                                queryPosterImageFromOnlineMovieService();
                            }
                        }
                        else {
                            Log.v(TAG, "showPosterImage: INVALID POSTER - USE PLACEHOLDER - movie=" + getMovie().getTitle());
                            showPlaceholderCoverImage();
                        }
                    }
                }
            });

        }
    }

    protected void hideMissingPosterImageAndText() {
        setNoImage(false);
        if (getNoImageView() != null) {
            getNoImageView().setVisibility(View.GONE); // hide noimage image
        }
        if (getMissingArtTextView() != null) {
            getMissingArtTextView().setVisibility(View.GONE);
        }
    }

    // show a placeholder for the image..
    protected void showPlaceholderCoverImage() {
        Handler handler = getHandler();
        if (handler != null) {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    // show the noimage image with title text overlay
                    Log.w(TAG, "showPlaceholderCoverImage: movie="+getMovie().getTitle());
                    setNoImage(true); // showing 'NOIMAGE'
                    if (getMissingArtTextView() != null) {
                        getMissingArtTextView().setBackgroundColor(getBackgroundColor());
                        getMissingArtTextView().setText(trim(getMovie().getTitle()));
                        getMissingArtTextView().setTextColor(getTextColor());
                        getMissingArtTextView().setVisibility(View.VISIBLE);
                    }
                    if (getNoImageView() != null) {
                        getNoImageView().setVisibility(View.VISIBLE); // show noimage image
                    }
                }
            });

        }
    }

    protected Handler getHandler() {
        Handler handler = null;
        AppCompatActivityBase activityBase = getActivityBase();
        if (activityBase != null) {
            handler = activityBase.getHandler();
        }
        return handler;
    }

}
