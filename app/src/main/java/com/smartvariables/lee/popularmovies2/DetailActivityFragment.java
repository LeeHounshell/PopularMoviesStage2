package com.smartvariables.lee.popularmovies2;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.smartvariables.lee.popularmovies2.data.trailers.TrailersColumns;
import com.smartvariables.lee.popularmovies2.data_helper.MoviesContract;
import com.smartvariables.lee.popularmovies2.util.AppCompatActivityBase;
import com.smartvariables.lee.popularmovies2.util.BitmapUtility;
import com.smartvariables.lee.popularmovies2.util.CircleFragment;
import com.smartvariables.lee.popularmovies2.util.NetworkUtilities;
import com.smartvariables.lee.popularmovies2.util.NotifyImageView;
import com.smartvariables.lee.popularmovies2.util.Tmdb_API_KEY;

import at.grabner.circleprogress.CircleProgressView;


/**
 * Movie details layout contains title, release date, movie poster, popularity average, and plot synopsis.
 */
public class DetailActivityFragment
        extends CircleFragment
        implements PerformReviewSearchContext {
    private final static String TAG = "LEE: <" + DetailActivityFragment.class.getSimpleName() + ">";

    private static DetailActivityFragment sDetailActivityFragment;

    private final static String review_html_head =
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" " +
                    "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                    "<head>" +
                    "<meta name=\"generator\" content=\"HTML Tidy for Linux (vers 25 March 2009), see www.w3.org\" />" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />" +
                    "<title></title>" +
                    "<style type=\"text/css\">" +
                    "/*<![CDATA[*/" +
                    " a.c3 {font-size: 30px; color: rgb(46,139,87)}" +
                    " span.c2 {color: #2E8B57}" +
                    " div.c1 {text-align: left}" +
                    "/*]]>*/" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class=\"c1\">";

    private final static String review_html_head_reviews =
            "<h1>Reviews</h1>" +
                    "<ol>";

    private final static String review_html_head_noreviews =
            "<h1>No Reviews</h1>" +
                    "<ol>";

    private final static String review_html_foot =
            "</ol>" +
                    "</div>" +
                    "<p>" +
                    "<a href=\"http://validator.w3.org/check?uri=referer\">" +
                    "<img src=\"http://www.w3.org/Icons/valid-xhtml10\"" +
                    " alt=\"Valid XHTML 1.0 Transitional\" height=\"31\" width=\"88\" />" +
                    "</a>" +
                    "</p>" +
                    "<br />" +
                    "<br />" +
                    "</body>" +
                    "</html>";

    public static final String MOVIE_DETAIL_KEY = "mDetail";
    public static final String ARG_ITEM_ID = "item_id";

    private View mDetailsView;
    private FloatingActionButton mFavoriteButton;
    private FloatingActionButton mReviewsButton;
    private ScrollView mScrollView;
    private MovieViewHolder mDetailHolder;
    private Intent mShareIntent;
    private ShareActionProvider mShareActionProvider;
    private String mReviews;
    private View mFragmentMovieDetail;
    private CircleProgressView mCircleView;
    private Drawable mFavoriteDrawable;
    private Drawable mNotFavoriteDrawable;
    private boolean mFirstTime = true;

    public final static String THE_DETAIL_POSTER_JPG = "the_movie_poster.jpg";

    public DetailActivityFragment() {
        Log.v(TAG, "DetailActivityFragment - DEFAULT CONSTRUCTOR - UNINITIALIZED");
    }

    public static DetailActivityFragment getTheFragment() {
        if (MainActivity.isTwoPane()) {
            AppCompatActivityBase activityBase = (DetailActivity.getTheActivity() == null)
                    ? MainActivity.getTheActivity() : DetailActivity.getTheActivity();
            Fragment detailFragment = activityBase.getSupportFragmentManager().findFragmentByTag(MainActivity.DETAIL_FRAGMENT_TAG);
            sDetailActivityFragment = (DetailActivityFragment) detailFragment;
        }
        return sDetailActivityFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        sDetailActivityFragment = this;
        setHasOptionsMenu(true);
        mFavoriteButton = null;
        mReviewsButton = null;
        mScrollView = null;
        if (savedInstanceState != null) {
            // recover state..
            if (savedInstanceState.containsKey(MOVIE_DETAIL_KEY)) {
                try {
                    MovieInfo movieInfo = savedInstanceState.getParcelable(MOVIE_DETAIL_KEY);
                    Log.v(TAG, "*** RECOVER DETAIL *** using saved movie detail=" + movieInfo);
                    mDetailHolder = createDetailHolder(movieInfo);
                    if (mDetailHolder != null) {
                        Log.v(TAG, "*** RECOVER DETAIL *** mDetailHolder IMAGE STATE=" + mDetailHolder.getMovieImageViewState());
                        if (mDetailHolder.getMovieImageViewState() != MovieViewHolder.MovieImageViewState.SQLITE_FOUND
                                && mDetailHolder.getMovieImageViewState() != MovieViewHolder.MovieImageViewState.IMAGE_OK) {
                            Log.v(TAG, "*** RECOVERING POSTER IMAGE DETAIL ***");
                            mDetailHolder.reloadPosterImage();
                            mReviews = mDetailHolder.getReviews();
                            Log.v(TAG, "*** RECOVER REVIEWS *** using saved reviews=" + mReviews);
                        }
                    }
                } catch (OutOfMemoryError e) {
                    Log.w(TAG, "*** out of memory trying to load 'movie detail' from Parcelable..");
                    getActivity().onBackPressed();
                }
            } else {
                Log.v(TAG, "no 'detail' in savedInstanceState");
            }
        }
        // master-detail..
        if (mDetailHolder == null && getArguments() != null && getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            Log.v(TAG, "*** create default detail ***");
            mDetailHolder = createDetailHolder(ARG_ITEM_ID);
        }
    }

    protected static void watchYoutubeVideo(String url) {
        Log.v(TAG, "watchYoutubeVideo: url=" + url);
        try {
            if (DetailActivity.getTheActivity() != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                Activity detailsActivity = DetailActivity.getTheActivity();
                detailsActivity.startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "unable to play youtube video: " + url);
        }
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        if (MainActivity.isTwoPane()) {
            Log.v(TAG, "onDestroy: in two-pane mode - call onBackPressed for MainActivity");
            final MainActivity mainActivity = MainActivity.getTheActivity();
            if (mainActivity != null) {
                Log.v(TAG, "onDestroy: call onBackPressed for MainActivity");
                mainActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mainActivity.onBackPressed();
                        }
                        catch (IllegalStateException e) {
                            Log.w(TAG, "was the device just rotated?");
                        }
                    }
                });
            }
        }
        super.onDestroy();
        PerformAsyncMovieSearchTask.reset();
        sDetailActivityFragment = null;
        mDetailsView = null;
        mScrollView = null;
        mDetailHolder = null;
        mShareIntent = null;
        mShareActionProvider = null;
        mFavoriteButton = null;
        mReviewsButton = null;
        mReviews = null;
        mFragmentMovieDetail = null;
        mCircleView = null;
        mFavoriteDrawable = null;
        mNotFavoriteDrawable = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mDetailHolder != null) {
            MovieInfo movieInfo = mDetailHolder.getMovie();
            if (movieInfo != null) {
                movieInfo.setReviews(mReviews);
                outState.putParcelable(MOVIE_DETAIL_KEY, movieInfo);
            }
        }
        super.onSaveInstanceState(outState);
        Log.v(TAG, "--> onSaveInstanceState");
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        mDetailsView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // FIXME: seems I am unable to locate a working Two-Pane View for the detail CircleView display!
        // not sure why this doesn't work when passed to PerformAsyncReviewSearchTask as the 'view' parameter
        // works fine if not using master-detail TwoPane mode:

        mFragmentMovieDetail = mDetailsView.findViewById(R.id.fragment_movie_detail);
        mCircleView = (CircleProgressView) mFragmentMovieDetail.findViewById(R.id.circle_view);

        // handle master-detail fragments..
        Bundle arguments = getArguments();
        if (arguments != null) {
            Log.v(TAG, "onCreateView: we have Bundle arguments..");
            MovieInfo movieInfo = arguments.getParcelable(MOVIE_DETAIL_KEY);
            if (movieInfo != null) {
                Log.v(TAG, "onCreateView: movieInfo="+movieInfo);
                if (mDetailHolder == null) {
                    Log.v(TAG, "onCreateView: need to createDetailHolder - movie=" + movieInfo.getTitle());
                    mDetailHolder = createDetailHolder(movieInfo);
                } else {
                    Log.v(TAG, "onCreateView: just setMovie - movie=" + movieInfo.getTitle());
                    mDetailHolder.setMovie(movieInfo);
                }
            }
        }

        mFavoriteButton = (FloatingActionButton) mDetailsView.findViewById(R.id.favorite_button);
        mFavoriteDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.star_on);
        mNotFavoriteDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.star_off);

        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, "CLICK! (favorite_star)");
                // change the Favorite setting for this movie..
                MovieInfo theMovie = DetailActivity.getMovie();
                if (theMovie == null) {
                    Log.w(TAG, "unable to locate Movie details!");
                    return;
                }
                theMovie.setFavorite(! theMovie.getFavorite());
                String favorited = theMovie.getFavorite()
                        ? getActivity().getResources().getString(R.string.favorite)
                        : getActivity().getResources().getString(R.string.not_favorite);
                Toast.makeText(getActivity(), favorited, Toast.LENGTH_SHORT).show();
                long movieRowId = updateFavoriteButton();
                Log.v(TAG, "FAVORITE: movieRowId=" + movieRowId);
                // we may need to invalidate the movie list if viewing Favorites..
                String sortOrder = null;
                MainActivity activity = MainActivity.getTheActivity();
                if (activity != null) {
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
                    String defaultSortCriteria = getResources().getString(R.string.sort_popular);
                    sortOrder = sharedPrefs.getString("sortBy", defaultSortCriteria);
                }
                boolean dirtyMovieList = (sortOrder != null && sortOrder.equals("favorites.desc"));
                if (dirtyMovieList) {
                    Log.v(TAG, "HAVE DIRTY MOVIE LIST! changed movieRowId=" + movieRowId);
                    MainActivityFragment fragment = MainActivityFragment.getTheFragment();
                    if (fragment != null) {
                        fragment.setmDirtyMovieList(true);
                    }
                }
            }
        });

        mScrollView = (ScrollView) mDetailsView.findViewById(R.id.scrollView1);
        mReviewsButton = (FloatingActionButton) mDetailsView.findViewById(R.id.reviews_button);

        if (mDetailHolder != null) {
            if (mDetailHolder.getMovieReviewsWebView() == null) {
                Log.v(TAG, "set the MovieReviewsWebView");
                mDetailHolder.setMovieReviewsWebView((WebView) mDetailsView.findViewById(R.id.movie_reviews));
                if (mReviews != null && mReviews.length() > 0) {
                    Log.v(TAG, "*** REVIEWS BUTTON is VISIBLE");
                    mReviewsButton.setVisibility(View.VISIBLE); // show reviews button if no poster image
                    Log.v(TAG, "show the reviews button");
                }
            }
        }

        mReviewsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, "CLICK! (reviews)");
                // show reviews for this movie
                // use AsyncTask to check Internet and populate the reviews
                if (!mFirstTime && MainActivity.isTwoPane()) {
                    String search_reviews = getActivity().getResources().getString(R.string.search_reviews);
                    Toast.makeText(getActivity(), search_reviews, Toast.LENGTH_SHORT).show();
                }
                DetailActivityFragment fragment = DetailActivityFragment.getTheFragment();
                if (fragment != null && mDetailHolder != null) {
                    PerformAsyncReviewSearchTask loadReviewsTask = new PerformAsyncReviewSearchTask((PerformReviewSearchContext) fragment, mCircleView, mFirstTime);
                    loadReviewsTask.execute(Tmdb_API_KEY.getKey(), Long.toString(mDetailHolder.getMovie().getTmdbMovieId()), Long.toString(mDetailHolder.getMovie().getMovieRowId()), (mFirstTime ? "noscroll" : "scrollok"));
                    mFirstTime = false;
                } else {
                    Log.w(TAG, "*** UNABLE TO LOAD REVIEWS ***");
                }
            }
        });

        mReviewsButton.post(new Runnable() {
            @Override
            public void run() {
                if (mReviewsButton != null) {
                    Log.v(TAG, "*** AUTOMATICALLY 'CLICK' THE REVIEWS BUTTON..");
                    mReviewsButton.invalidate();
                    mReviewsButton.performClick();
                }
            }
        });

        return mDetailsView;
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
        long movieRowId = -1L;
        if (DetailActivity.getMovie() != null) {
            if (mDetailHolder == null) {
                mDetailHolder = createDetailHolder(DetailActivity.getMovie());
            }
            movieRowId = updateFavoriteButton();
            Log.v(TAG, "onResume: movieRowId="+movieRowId);
            if (! NetworkUtilities.isConnected()){
                Log.v(TAG, "onResume: not connected - disable the trailer Spinner");
                Spinner trailersSpinner = (Spinner) mDetailsView.findViewById(R.id.trailers);
                Log.v(TAG, "*** TRAILERS SPINNER is GONE");
                trailersSpinner.setVisibility(View.GONE);
            }
            else {
                if (movieRowId != -1L) {
                    Log.v(TAG, "onResume: showSpinnerWithTrailers(movieRowId="+movieRowId+")");
                    showSpinnerWithTrailers(movieRowId);
                }
            }
        }
        else {
            Log.e(TAG, "onResume: THE MOVIE-INFO IS NOT INITIALIZED");
        }
        if (mDetailHolder == null) {
            Log.e(TAG, "onResume: THE DETAIL-HOLDER IS NOT INITIALIZED");
        }
    }

    public long updateFavoriteButton() {
        Log.v(TAG, "updateFavoriteButton");
        long movieRowId = -1L;
        if (mFavoriteButton != null && mFavoriteDrawable != null && mNotFavoriteDrawable != null) {
            final Context context = PopularMoviesStage2Application.getAppContext();
            MovieInfo movie = DetailActivity.getMovie();
            if (movie != null) {
                if (movie.getFavorite()) {
                    mFavoriteButton.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mFavoriteButton != null) {
                                mFavoriteButton.setImageDrawable(mFavoriteDrawable);
                                Log.w(TAG, "updateFavoriteButton: FAVORITE");
                                if (MainActivity.isTwoPane()) {
                                    Log.w(TAG, "updateFavoriteButton: isTwoPane - refresh()");
                                    MainActivityFragment.getTheFragment().refresh(); // refresh the movie-list
                                }
                            }
                        }
                    });
                } else {
                    mFavoriteButton.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mFavoriteButton != null) {
                                mFavoriteButton.setImageDrawable(mNotFavoriteDrawable);
                                Log.w(TAG, "updateFavoriteButton: NOT FAVORITE");
                                if (MainActivity.isTwoPane()) {
                                    Log.w(TAG, "updateFavoriteButton: isTwoPane - refresh()");
                                    MainActivityFragment.getTheFragment().refresh(); // refresh the movie-list
                                }
                            }
                        }
                    });
                }
                movieRowId = movie.getMovieRowId();
            } else {
                Log.w(TAG, "*** updateFavoriteButton: unable to locate MovieInfo");
                mFavoriteButton.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mFavoriteButton != null) {
                            mFavoriteButton.setImageDrawable(mNotFavoriteDrawable);
                            Log.w(TAG, "*** updateFavoriteButton: NOT FAVORITE");
                            if (MainActivity.isTwoPane()) {
                                Log.w(TAG, "updateFavoriteButton: isTwoPane - refresh()");
                                MainActivityFragment.getTheFragment().refresh(); // refresh the movie-list
                            }
                        }
                    }
                });
            }
        }
        return movieRowId;
    }

    protected void showSpinnerWithTrailers(final long movieRowId) {
        Log.v(TAG, "showSpinnerWithTrailers()");
        Context context = PopularMoviesStage2Application.getAppContext();
        Cursor cursor = context.getContentResolver().query(
                TrailersColumns.CONTENT_URI, // table
                null, // all columns.
                MoviesContract.TrailersEntry.FIELD_MOVIE_ID + "=?", // where
                new String[]{Long.toString(movieRowId)}, // Values for the "where" clause
                null  // order by
        );
        if (cursor.getCount() > 0) {
            Log.v(TAG, "found " + cursor.getCount() + " trailers");
            cursor.moveToFirst();
            String trailers[] = new String[cursor.getCount() + 1];
            String trailerUrls[] = new String[cursor.getCount() + 1];
            int index = 0;
            while (cursor.isAfterLast() == false) {
                int idx = cursor.getColumnIndex(MoviesContract.TrailersEntry.FIELD_TRAILER_URL);
                String trailerUrl = cursor.getString(idx);
                Log.v(TAG, "*** trailerUrl=" + trailerUrl);
                if (index == 0) {
                    // fix problem with the spinner 1st display item.. which is not selectable initially
                    trailers[0] = (cursor.getCount() > 1) ? "Trailers.." : "Trailer..";
                    trailerUrls[0] = trailerUrl;
                    ++index;
                }
                trailers[index] = "Play Trailer " + index;
                trailerUrls[index] = trailerUrl;
                ++index;
                cursor.moveToNext();
            }
            cursor.close();
            setupSpinnerWithTrailers(trailers, trailerUrls, index);
        } else {
            cursor.close();
        }
    }

    protected void setupSpinnerWithTrailers(final String trailers[], final String trailerUrls[], final int index) {
        Log.v(TAG, "setupSpinnerWithTrailers");
        Spinner trailersSpinner = (Spinner) mDetailsView.findViewById(R.id.trailers);
        if (index == 0) {
            Log.v(TAG, "setupSpinnerWithTrailers: *** TRAILERS SPINNER is GONE");
            trailersSpinner.setVisibility(View.GONE);
            return;
        }
        AppCompatActivityBase activityBase = (DetailActivity.getTheActivity() == null)
                ? MainActivity.getTheActivity() : DetailActivity.getTheActivity();
        if (activityBase == null) {
            Log.e(TAG, "setupSpinnerWithTrailers: *** UNABLE TO GET ACTIVITY BASE ***");
            return;
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                activityBase,
                R.layout.spinner_item,
                trailers);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        trailersSpinner.setAdapter(dataAdapter);
        Log.v(TAG, "setupSpinnerWithTrailers: *** TRAILERS SPINNER is VISIBLE");
        trailersSpinner.setVisibility(View.VISIBLE);

        // keep the trailersSpinner from firing when first instantiated..
        // see http://stackoverflow.com/questions/2562248/how-to-keep-onitemselected-from-firing-off-on-a-newly-instantiated-spinner
        trailersSpinner.post(new Runnable() {
            public void run() {

                if (mDetailHolder == null) {
                    Log.w(TAG, "*** mDetailsView is null ***");
                    return;
                }
                Spinner spinner = (Spinner) mDetailsView.findViewById(R.id.trailers);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Spinner spinner = (Spinner) mDetailsView.findViewById(R.id.trailers);
                        int index = spinner.getSelectedItemPosition();
                        if (index == 0) {
                            Log.w(TAG, "setupSpinnerWithTrailers: ignoring selection of trailersSpinner title"); // workaround for trailersSpinner bug
                            return;
                        }
                        Log.v(TAG, "setupSpinnerWithTrailers: SELECTED TRAILER #" + index + ": " + trailerUrls[index]);
                        watchYoutubeVideo(trailerUrls[index]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Log.v(TAG, "setupSpinnerWithTrailers: SELECTED NOTHING");
                    }

                });
            }
        });

    }

    private MovieViewHolder createDetailHolder(String argItemId) {
        Log.v(TAG, "createDetailHolder - argItemId=" + argItemId);
        MovieInfo movieInfo = new MovieInfo(argItemId);
        return createDetailHolder(movieInfo);
    }

    public View getDetailView() {
        View view = mDetailsView;
        Log.v(TAG, "getDetailView: mDetailsView=" + mDetailsView);
        if (MainActivity.isTwoPane()) {
            Fragment detailFragment = getActivity().getSupportFragmentManager().findFragmentByTag(MainActivity.DETAIL_FRAGMENT_TAG);
            if (detailFragment != null) {
                view = detailFragment.getView();
                Log.v(TAG, "getDetailView: using View from getSupportFragmentManager.. - view="+view);
            }
        }
        if (view == null) {
            view = getView();
            Log.v(TAG, "getDetailView: view is null. instead using getView() - view=" + view);
        }
        return view;
    }

    protected MovieViewHolder createDetailHolder(MovieInfo movieInfo) {
        Log.v(TAG, "createDetailHolder - movieInfo title="+movieInfo.getTitle());
        DetailActivity.setMovie(movieInfo);
        AppCompatActivityBase activityBase = (DetailActivity.getTheActivity() == null)
                ? MainActivity.getTheActivity() : DetailActivity.getTheActivity();
        View view = getDetailView();
        if (view == null) {
            Log.e(TAG, "*** UNABLE TO CREATE MOVIE-VIEW-HOLDER! - activityBase=" + activityBase + ", view=" + view + " ***");
            return null;
        }
        MovieViewHolder detailHolder = new MovieViewHolder(
                activityBase, // MainActivity or DetailActivity
                movieInfo,
                (RatingBar) getDetailView().findViewById(R.id.rating_bar),
                (TextView) getDetailView().findViewById(R.id.release_date),
                (NotifyImageView) getDetailView().findViewById(R.id.poster_image),
                (ImageView) getDetailView().findViewById(R.id.no_poster_image),
                (TextView) getDetailView().findViewById(R.id.movie_title),
                (TextView) getDetailView().findViewById(R.id.plot_synopsis),
                (TextView) getDetailView().findViewById(R.id.missing_art)
        );
        Log.v(TAG, "set the image holder so notify after drawing is completed");
        detailHolder.getPosterImageView().setNotifyImageHolder(detailHolder);
        if (detailHolder.getMovieReviewsWebView() == null) {
            Log.v(TAG, "set the MovieReviewsWebView");
            detailHolder.setMovieReviewsWebView((WebView) getDetailView().findViewById(R.id.movie_reviews));
        }
        return detailHolder;
    }

    /*
     * useful: http://stackoverflow.com/questions/19118051/unable-to-cast-action-provider-to-share-action-provider
     */
    @Override
    public void onCreateOptionsMenu(
            Menu menu,
            MenuInflater inflater) {
        Log.v(TAG, "--> onCreateOptionsMenu <--");
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_poster_details, menu);
        final MenuItem item = menu.findItem(R.id.menu_item_share);
        Log.v(TAG, "onCreateOptionsMenu: *** SETUP the ShareActionProvider");
        if (mDetailHolder != null) {
            prepareActionSend();
        }
        Log.v(TAG, "onCreateOptionsMenu: use the mShareActionProvider to set/change the share intent.");
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(mShareIntent);
        } else {
            Log.w(TAG, "onCreateOptionsMenu: *** no ShareActionProvider!");
        }
    }

    /*
     * setup for sending email of movie details as a share Intent
     * useful: http://stackoverflow.com/questions/7661875/how-to-use-share-image-using-sharing-intent-to-share-images-in-android
     */
    protected void prepareActionSend() {
        Log.v(TAG, "prepareActionSend");
        Log.v(TAG, "prepareActionSend: possibly share this movie=" + mDetailHolder.getMovie());
        mShareIntent = new Intent();
        mShareIntent.setAction(Intent.ACTION_SEND);
        mShareIntent.setType("text/html");
        mShareIntent.setAction(Intent.ACTION_SEND);

        mShareIntent.putExtra(Intent.EXTRA_SUBJECT, mDetailHolder.getMovie().getTitle());
        Log.v(TAG, "finish setup for the share Intent..");
        String movieInfo = mDetailHolder.getMovie().toString();
        String trailers = "";
        String[] trailerInfo = mDetailHolder.getMovie().getTrailers();
        if (trailerInfo != null) {
            StringBuffer buf_trailers = new StringBuffer();
            int number = 0;
            for (String trailer : trailerInfo) {
                buf_trailers.append(htmlTrailerLink(trailer, ++number) + "\n<br>");
            }
            String trailer_head = getResources().getString(R.string.trailer_head);
            trailers = (buf_trailers.length() > 0) ? (trailer_head + "\n<br>" + buf_trailers) : "";
        }
        String check_out = getResources().getString(R.string.check_out);
        String app_name = getResources().getString(R.string.app_name);
        String email_body = "<body>" + check_out + " '" + app_name + "':\n<br>\n<br>" + movieInfo + "\n<br>\n<br>" + trailers + "</body>";
        Log.v(TAG, "prepareActionSend: set Share-Intent email_body=" + email_body);
        mShareIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(email_body));

        // master-detail attention for the detail view here..
        final boolean showTrailers = (trailers.length() > 0);

        if (mDetailHolder == null) {
            Log.w(TAG, "prepareActionSend: *** UNABLE TO INITIALIZE *** - mDetailHolder=" + mDetailHolder);
            return;
        }
        NotifyImageView imageView = mDetailHolder.getPosterImageView();
        if (imageView == null) {
            Log.w(TAG, "prepareActionSend: *** UNABLE TO INITIALIZE *** - getPosterImageView()=" + imageView);
            return;
        }

        imageView.post(new Runnable() {
            @Override
            public void run() {
                if (mDetailHolder == null) {
                    Log.v(TAG, "*** the mDetailHolder is null ***");
                    return;
                }
                Log.v(TAG, "*** SETUP THE SHARE INTENT AND POSTER IMAGE DISPLAY ***");
                // useful: http://stackoverflow.com/questions/8306623/get-bitmap-attached-to-imageview
                NotifyImageView imageView = mDetailHolder.getPosterImageView();
                if (imageView != null) {
                    imageView.notifyOff();
                    imageView.setDrawingCacheEnabled(true);
                    imageView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    imageView.layout(0, 0,
                            imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
                    imageView.buildDrawingCache(true);
                    Bitmap poster = Bitmap.createBitmap(imageView.getDrawingCache());
                    imageView.setDrawingCacheEnabled(false);
                    if (poster != null) {
                        mDetailHolder.getMovie().setPoster(poster); // stops the detail poster from resizing to 'small size' on rotate
                        mDetailHolder.updatePosterFromMovieInfo();
                        Uri posterUri = BitmapUtility.getUriForFile(THE_DETAIL_POSTER_JPG);
                        Log.v(TAG, "prepareActionSend: posterUri=" + posterUri + ", width=" + poster.getWidth() + ", height=" + poster.getHeight());
                        //mShareIntent.setType("image/jpeg");
                        //mShareIntent.setType("image/*");
                        mShareIntent.setType("application/image");
                        mShareIntent.putExtra(Intent.EXTRA_STREAM, posterUri);
                        BitmapUtility.saveOneBitmapToFlash(poster, THE_DETAIL_POSTER_JPG);
                    }
                }

                if (showTrailers) {
                    Log.v(TAG, "*** SETUP THE TRAILERS SPINNER DISPLAY ***");
                    long movieRowId = mDetailHolder.getMovie().getMovieRowId();
                    Log.v(TAG, "ok.. so show some Trailer links in the Spinner.. movieRowId=" + movieRowId);
                    showSpinnerWithTrailers(movieRowId);
                }
            }
        });

    }

    protected String htmlTrailerLink(String trailer, int number) {
        if (number == 1) {
            return "<a href=\"" + trailer + "\">trailer</a>";
        }
        return "<a href=\"" + trailer + "\">trailer #" + number + "</a>";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "onOptionsItemSelected");
        int id = item.getItemId();
        switch (id) {
            case R.id.home: {
                Log.v(TAG, "case R.id.home - UP PRESSED");
                getActivity().onBackPressed();
                return true;
            }
            case R.id.menu_item_share: {
                Log.v(TAG, "case R.id.menu_item_share");
                Log.v(TAG, "share it..");
                // use a share Intent
                startActivity(Intent.createChooser(mShareIntent, "Share movie.."));
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // 'PerformReviewSearchContext' interface method
    @Override
    public void setReviewsList(MovieReviewInfoList reviewList, final boolean scrollok) {
        Log.v(TAG, "*** setReviewsList ***");
        boolean showingReview = false;
        if (reviewList != null) {
            StringBuilder theReviewsBuf = new StringBuilder(review_html_head);
            theReviewsBuf.append((reviewList.size() == 0) ? review_html_head_noreviews : review_html_head_reviews);
            for (MovieReviewInfo review : reviewList) {
                Log.v(TAG, "===> setReviewsList review=" + review);
                theReviewsBuf.append("<li>" + review.toString() + "</li>");
            }
            if (theReviewsBuf.length() > 0) {
                theReviewsBuf.append(review_html_foot);
                showingReview = showReviews(new String(theReviewsBuf), scrollok);
            }
        }
        if (!showingReview) {
            // hide the reviews button.  there is nothing to see
            if (mDetailHolder != null && mReviewsButton != null) {
                mReviewsButton.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mDetailHolder != null) {
                            if (mDetailHolder.getMovieReviewsWebView() != null) {
                                Log.v(TAG, "*** REVIEWS WEBVIEW is GONE");
                                mDetailHolder.getMovieReviewsWebView().setVisibility(View.GONE);
                            }
                            Log.v(TAG, "*** REVIEWS BUTTON is GONE");
                            mReviewsButton.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }
    }

    // 'PerformReviewSearchContext' interface method
    @Override
    public CircleFragment getCircleFragment() {
        return this;
    }

    // 'PerformReviewSearchContext' interface method
    @Override
    public MovieReviewInfoList getDefaultReviewListForMovie(long movieRowId) {
        MovieReviewInfoList reviewInfoList = null;
        return reviewInfoList;
    }

    protected boolean showReviews(String theReviews, final boolean scrollDown) {
        Log.v(TAG, "showReviews: postDelayed");
        boolean showingReview = false;
        mReviews = theReviews;
        if (mDetailHolder != null && mDetailHolder.getMovieReviewsWebView() != null) {
            WebView reviewsWebView = mDetailHolder.getMovieReviewsWebView();
            reviewsWebView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mDetailHolder != null) {
                        Log.v(TAG, "showReviews");
                        mDetailHolder.getMovieReviewsWebView().setBackgroundColor(Color.TRANSPARENT);
                        mDetailHolder.getMovieReviewsWebView().loadData(mReviews, "text/html; charset=UTF-8", "UTF-8");
                        Log.v(TAG, "*** REVIEWS WEBVIEW is VISIBLE");
                        mDetailHolder.getMovieReviewsWebView().setVisibility(View.VISIBLE);
                        if (scrollDown) {
                            scrollDown();
                        }
                    }
                }
            }, 100);
            showingReview = true;
        } else {
            Log.w(TAG, "UNABLE TO SHOW REVIEWS - *** the MovieReviewsWebView is null! ***");
        }
        return showingReview;
    }

    protected void scrollDown() {
        if (mScrollView != null) {
            Log.v(TAG, "scrollDown");
            final int position = mDetailHolder.getPosterImageView().getBottom() + 50;

            // scroll down to show the reviews..
            mScrollView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mScrollView != null) {
                        mScrollView.setSmoothScrollingEnabled(true);
                        mScrollView.smoothScrollBy(0, position);
                    }
                }
            }, 1000);

        }
    }

}
