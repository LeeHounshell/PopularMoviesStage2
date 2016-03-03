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

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;
import android.util.Log;

import com.smartvariables.lee.popularmovies2.data_helper.MoviesContract;
import com.smartvariables.lee.popularmovies2.util.PollingCheck;

import java.util.Map;
import java.util.Set;

// Functions and test data to make it easier to test the database and Content Provider.
public class TestUtilities extends AndroidTestCase {

    static final String TAG = "LEE: <" + TestUtilities.class.getSimpleName() + ">";

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    // setup content for a default movie row for database tests.
    static ContentValues createMovieValues(int testNumber) {
        ContentValues movieValues = new ContentValues();
        switch (testNumber) {
            case 1:
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_TITLE, "The Wizard of Oz");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_RELEASE_DATE, "1939-08-25");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POSTER_PATH, "https://image.tmdb.org/t/p/w500/tKEHoKPZv3af0Pn3poaOLHOJ6NM.jpg");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POPULARITY, 1);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_AVERAGE, 6.8);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_COUNT, 500);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_OVERVIEW, "One of the most famous musical films and the first film from Hollywood to use color. Young Dorothy finds herself in a magical world where she makes friends with a lion, a scarecrow and a tin man as they make their way along the yellow brick road to talk with the Wizard and ask for the things they miss most in their lives. The Wicked Witch of the West is the only thing that could stop them.");
                break;
            case 2:
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_TITLE, "Gone with the Wind");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_RELEASE_DATE, "1940-01-17");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POSTER_PATH, "https://image.tmdb.org/t/p/w500/2m9qsuDpAb6aBMxbKOBU1jsP9qf.jpg");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POPULARITY, 1);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_AVERAGE, 7.1);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_COUNT, 317);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_OVERVIEW, "An American classic in which a manipulative woman and a roguish man carry on a turbulent love affair in the American south during the Civil War and Reconstruction.");
                break;
            case 3:
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_TITLE, "Snow White and the Seven Dwarfs");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_RELEASE_DATE, "1937-12-21");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POSTER_PATH, "https://image.tmdb.org/t/p/w500/vGV35HBCMhQl2phhGaQ29P08ZgM.jpg");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POPULARITY, 1);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_AVERAGE, 6.4);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_COUNT, 580);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_OVERVIEW, "A beautiful girl, Snow White, takes refuge in the forest in the house of seven dwarfs to hide from her stepmother, the wicked Queen. The Queen is jealous because she wants to be known as \"the fairest in the land,\" and Snow White's beauty surpasses her own.");
                break;
            case 4:
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_TITLE, "Casablanca");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_RELEASE_DATE, "1942-11-26");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POSTER_PATH, "https://image.tmdb.org/t/p/w780/nhHsH7qUySVTY57mxf231xO7Fga.jpg");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POPULARITY, 1);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_AVERAGE, 7.5);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_COUNT, 493);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_OVERVIEW, "Casablanca is a classic and one of the most revered films of all time. Starring Humphrey Bogart and Ingrid Bergman in a love triangle in the city of Casablanca which is a refuge for many fleeing foreigners looking for a new life during the war. Political romance with a backdrop of war conflict between democracy and totalitarianism.");
                break;
            case 5:
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_TITLE, "2001: A Space Odyssey");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_RELEASE_DATE, "1968-04-05");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POSTER_PATH, "https://image.tmdb.org/t/p/w780/pckdZ29bHj11hBsV3SbVVfmCB6C.jpg");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POPULARITY, 1);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_AVERAGE, 7.5);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_COUNT, 978);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_OVERVIEW, "Humanity finds a mysterious object buried beneath the lunar surface and sets off to find its origins with the help of HAL 9000, the world's most advanced super computer.");
                break;
            case 6:
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_TITLE, "Blade Runner");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_RELEASE_DATE, "1982-06-25");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POSTER_PATH, "https://image.tmdb.org/t/p/w780/yNlVk0HnxvY5Z1raID9N6SKeFid.jpg");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POPULARITY, 1);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_AVERAGE, 7.6);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_COUNT, 1335);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_OVERVIEW, "In the smog-choked dystopian Los Angeles of 2019, blade runner Rick Deckard is called out of retirement to kill a quartet of replicants who have escaped to Earth seeking their creator for a way to extend their short life spans.");
                break;
            case 7:
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_TITLE, "Back to the Future");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_RELEASE_DATE, "1985-07-03");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POSTER_PATH, "https://image.tmdb.org/t/p/w780/x4N74cycZvKu5k3KDERJay4ajR3.jpg");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POPULARITY, 1);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_AVERAGE, 7.6);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_COUNT, 2479);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_OVERVIEW, "Eighties teenager Marty McFly is accidentally sent back in time to 1955, inadvertently disrupting his parents' first meeting and attracting his mother's romantic interest. Marty must repair the damage to history by rekindling his parents' romance and - with the help of his eccentric inventor friend Doc Brown - return to 1985.");
                break;
            case 8:
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_TITLE, "Gravity");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_RELEASE_DATE, "2013-10-04");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POSTER_PATH, "https://image.tmdb.org/t/p/w500/uPxtxhB2Fy9ihVqtBtNGHmknJqV.jpg");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POPULARITY, 1);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_AVERAGE, 7.6);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_COUNT, 2262);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_OVERVIEW, "Dr. Ryan Stone (Sandra Bullock), a brilliant medical engineer on her first Shuttle mission, with veteran astronaut Matt Kowalsky (George Clooney) in command of his last flight before retiring.");
                break;
            case 9:
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_TITLE, "WALL*E");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_RELEASE_DATE, "2008-06-22");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POSTER_PATH, "https://image.tmdb.org/t/p/w500/4qOKgcDcB0SojUIg2syzor1FeL7.jpg");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POPULARITY, 1);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_AVERAGE, 7.4);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_COUNT, 2850);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_OVERVIEW, "WALL?E is the last robot left on an Earth that has been overrun with garbage and all humans have fled to outer space. For 700 years he has continued to try and clean up the mess, but has developed some rather interesting human-like qualities. When a ship arrives with a sleek new type of robot, WALL?E thinks he's finally found a friend and stows away on the ship.");
                break;
            case 10:
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_TITLE, "Star Wars: Episode V - The Empire Strikes Back");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_RELEASE_DATE, "1980-05-17");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POSTER_PATH, "https://image.tmdb.org/t/p/w500/6u1fYtxG5eqjhtCPDx04pJphQRW.jpg");
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POPULARITY, 1);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_AVERAGE, 7.8);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_COUNT, 2466);
                movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_OVERVIEW, "The adventure continues in this Star Wars sequel. Luke Skywalker (Mark Hamill), Han Solo (Harrison Ford), Princess Leia (Carrie Fisher) and Chewbacca (Peter Mayhew) face attack by the Imperial forces and its AT-AT walkers on the ice planet Hoth. While Han and Leia escape in the Millennium Falcon, Luke travels to Dagobah in search of Yoda. Only with the Jedi master's help will Luke survive when the dark side of the Force beckons him into the ultimate duel with Darth Vader (David Prowse).");
                break;
            default:
                assertTrue("invalid value for testNumber=" + testNumber, false);
        }
        return movieValues;
    }

    static long insertMovieValues(Context context, int testNumber) {
        // insert test record into the database
        MovieDbHelper dbHelper = MovieDbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertFalse("getWriteableDatabase must be WRITABLE!", db.isReadOnly());
        ContentValues testValues = TestUtilities.createMovieValues(testNumber);
        long movieRowId = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, testValues);
        // Verify we got a row back.
        Log.v(TAG, "insertMovieValues: movieRowId=" + movieRowId);
        assertTrue("Error: Failure to insert Movie Values", movieRowId != -1);
        return movieRowId;
    }

    // setup content for a default trailer row for database tests.
    static ContentValues createTrailerValues(long movie_id, int testNumber) {
        ContentValues trailerValues = new ContentValues();
        switch (testNumber) {
            case 1: // The Wizard of Oz
                trailerValues.put(MoviesContract.TrailersEntry.FIELD_MOVIE_ID, movie_id);
                trailerValues.put(MoviesContract.TrailersEntry.FIELD_TRAILER_URL, "http://www.youtube.com/watch?v=aVG8F9D7suw");
                break;
            case 2: // Gone with the Wind
                trailerValues.put(MoviesContract.TrailersEntry.FIELD_MOVIE_ID, movie_id);
                trailerValues.put(MoviesContract.TrailersEntry.FIELD_TRAILER_URL, "http://www.youtube.com/watch?v=wUA5jB2MPCc");
                break;
            default:
                assertTrue("invalid value for testNumber", false);
        }
        return trailerValues;
    }

    static long insertTrailerValues(Context context, long movie_id, int testNumber) {
        // insert test record into the database
        MovieDbHelper dbHelper = MovieDbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertFalse("getWriteableDatabase must be WRITABLE!", db.isReadOnly());
        ContentValues testValues = TestUtilities.createTrailerValues(movie_id, testNumber);
        long trailerRowId = db.insert(MoviesContract.TrailersEntry.TABLE_NAME, null, testValues);
        Log.v(TAG, "insertTrailerValues: trailerRowId=" + trailerRowId);
        // Verify we got a row back.
        assertTrue("Error: Failure to insert Trailer Values", trailerRowId != -1);
        return trailerRowId;
    }

    // setup content for a default reviews row for database tests.
    static ContentValues createReviewsValues(long movie_id, int testNumber) {
        ContentValues reviewValues = new ContentValues();
        switch (testNumber) {
            case 1: // review: The Wizard of Oz
                reviewValues.put(MoviesContract.ReviewsEntry.FIELD_MOVIE_ID, movie_id);
                reviewValues.put(MoviesContract.ReviewsEntry.FIELD_CRITIC_NAME, "Lee");
                reviewValues.put(MoviesContract.ReviewsEntry.FIELD_CRITIC_URL, "lee.hounshell@gmail.com");
                reviewValues.put(MoviesContract.ReviewsEntry.FIELD_REVIEW_TEXT, "Dorothy Gale (Judy Garland) is an orphaned teenager who lives with her Auntie Em (Clara Blandick) and Uncle Henry (Charley Grapewin) on a Kansas farm in the early 1900s. She daydreams about going \"over the rainbow\" and does! - so can you if you see this movie! - highly recommended for family viewing");
                break;
            case 2: // review: Gone with the Wind
                reviewValues.put(MoviesContract.ReviewsEntry.FIELD_MOVIE_ID, movie_id);
                reviewValues.put(MoviesContract.ReviewsEntry.FIELD_CRITIC_NAME, "Tracy");
                reviewValues.put(MoviesContract.ReviewsEntry.FIELD_CRITIC_URL, "tracy@smartvariables.com");
                reviewValues.put(MoviesContract.ReviewsEntry.FIELD_REVIEW_TEXT, "This classic film narrates the story of Scarlett O'Hara during the American civil war. - highly recommended");
                break;
            default:
                assertTrue("invalid value for testNumber", false);
        }
        return reviewValues;
    }

    static long insertReviewValues(Context context, long movie_id, int testNumber) {
        // insert test record into the database
        MovieDbHelper dbHelper = MovieDbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertFalse("getWriteableDatabase must be WRITABLE!", db.isReadOnly());
        ContentValues testValues = TestUtilities.createReviewsValues(movie_id, testNumber);
        long reviewRowId = db.insert(MoviesContract.ReviewsEntry.TABLE_NAME, null, testValues);
        // Verify we got a row back.
        Log.v(TAG, "insertReviewValues: reviewRowId=" + reviewRowId);
        assertTrue("Error: Failure to insert Reviews Values", reviewRowId != -1);
        return reviewRowId;
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }

    void testMoviesDatabase() {
        int testNumber = 1;
        long movie_id = insertMovieValues(getContext(), testNumber);
        assertTrue("Error: Failure to insert Movie record", movie_id > 0);
        long trailer_id = insertTrailerValues(getContext(), movie_id, testNumber);
        assertTrue("Error: Failure to insert Trailer record", trailer_id > 0);
        long review_id = insertReviewValues(getContext(), movie_id, testNumber);
        assertTrue("Error: Failure to insert Review record", review_id > 0);
    }

    // Use the TestProvider utility class to test the ContentObserver callbacks using the PollingCheck class.
    // Note that this only tests that the onChange function is called; it does not test that the correct Uri is returned.
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.

            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();

            mHT.quit();
        }
    }

}
