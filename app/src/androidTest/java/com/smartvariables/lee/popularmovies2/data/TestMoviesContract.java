/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smartvariables.lee.popularmovies2.data;

import android.net.Uri;
import android.test.AndroidTestCase;

import com.smartvariables.lee.popularmovies2.data_helper.MoviesContract;

// Test the MoviesContract
public class TestMoviesContract extends AndroidTestCase {

    long TEST_MOVIE = 1;
    long TEST_TRAILER = 1;
    long TEST_REVIEW = 1;

    public void testBuildMovieUri() {
        Uri movieUri = MoviesContract.MoviesEntry.buildMovieUri(TEST_MOVIE);
        assertNotNull("Error: Null Movie Uri returned.", movieUri);
        assertEquals("Error: Movie _ID not properly appended to the end of the Uri",
                Long.toString(TEST_MOVIE), movieUri.getLastPathSegment());
        assertEquals("Error: Movie Uri doesn't match our expected result", movieUri.toString(),
                "content://com.smartvariables.lee.popularmovies2.data.popularmoviesprovider/movies/" + TEST_MOVIE);
    }

    public void testBuildTrailerUri() {
        Uri trailerUri = MoviesContract.TrailersEntry.buildTrailerUri(TEST_TRAILER);
        assertNotNull("Error: Null Trailer Uri returned.", trailerUri);
        assertEquals("Error: Trailer _ID not properly appended to the end of the Uri",
                Long.toString(TEST_TRAILER), trailerUri.getLastPathSegment());
        assertEquals("Error: Trailer Uri doesn't match our expected result", trailerUri.toString(),
                "content://com.smartvariables.lee.popularmovies2.data.popularmoviesprovider/trailers/" + TEST_TRAILER);
    }

    public void testBuildReviewUri() {
        Uri reviewUri = MoviesContract.ReviewsEntry.buildReviewUri(TEST_REVIEW);
        assertNotNull("Error: Null Review Uri returned.", reviewUri);
        assertEquals("Error: Review _ID not properly appended to the end of the Uri",
                Long.toString(TEST_REVIEW), reviewUri.getLastPathSegment());
        assertEquals("Error: Trailer Uri doesn't match our expected result", reviewUri.toString(),
                "content://com.smartvariables.lee.popularmovies2.data.popularmoviesprovider/reviews/" + TEST_REVIEW);
    }

}
