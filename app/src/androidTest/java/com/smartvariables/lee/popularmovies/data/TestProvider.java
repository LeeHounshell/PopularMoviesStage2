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

package com.smartvariables.lee.popularmovies.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.Log;

import com.smartvariables.lee.popularmovies.data.movies.MoviesColumns;
import com.smartvariables.lee.popularmovies.data.reviews.ReviewsColumns;
import com.smartvariables.lee.popularmovies.data.trailers.TrailersColumns;
import com.smartvariables.lee.popularmovies.data_helper.MoviesContract;

// test for basic functionality to be implemented correctly.
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();
    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;

    static ContentValues[] createBulkInsertMovieValues() {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++) {
            ContentValues movieValues = TestUtilities.createMovieValues(i + 1);
            returnContentValues[i] = movieValues;
        }
        return returnContentValues;
    }

    // Delete all records from all database tables using the ContentProvider.
    // Also query the ContentProvider to make sure that the database is successfully deleted,
    // By deleting Movies, associated Reviews and Trailers will CASCADE delete.
    public void deleteAllRecordsFromProvider() {

        mContext.getContentResolver().delete(
                MoviesColumns.CONTENT_URI,
                null,
                null
        );


        // make sure everything is deleted - note any reviews or trailers should be gone too

        Cursor cursor = mContext.getContentResolver().query(
                MoviesColumns.CONTENT_URI, // table
                null, // all columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // order by
        );
        assertEquals("Error: Records not deleted from Movies table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                TrailersColumns.CONTENT_URI, // table
                null, // all columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // order by
        );
        assertEquals("Error: Records not deleted from Trailers table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                ReviewsColumns.CONTENT_URI, // table
                null, // all columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // order by
        );
        assertEquals("Error: Records not deleted from Reviews table during delete", 0, cursor.getCount());
        cursor.close();

    }

    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    // we want each test to start with a clean slate, so run deleteAllRecords in setUp before each test.
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    // make sure that the content provider is registered correctly.
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the PopularMoviesProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                PopularMoviesProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: MovieProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + PopularMoviesProvider.AUTHORITY,
                    providerInfo.authority, PopularMoviesProvider.AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // the provider isn't registered correctly.
            assertTrue("Error: MovieProvider not registered at " + mContext.getPackageName(), false);
        }
    }

    public void verifyExpectedType(String type, Uri uri) {
        PopularMoviesProvider provider = new PopularMoviesProvider();
        String expectedType = provider.getType(uri);
        assertEquals("Error: the passed CONTENT_URI 'type' should match 'expectedType'", type, expectedType);
    }

    // verify that the ContentProvider returns the correct type for each type of URI that it can handle.
    public void testGetType() {
        Uri uri = MoviesColumns.CONTENT_URI;
        String type = mContext.getContentResolver().getType(uri);
        verifyExpectedType(type, uri);
        uri = TrailersColumns.CONTENT_URI;
        type = mContext.getContentResolver().getType(uri);
        verifyExpectedType(type, uri);
        uri = ReviewsColumns.CONTENT_URI;
        type = mContext.getContentResolver().getType(uri);
        verifyExpectedType(type, uri);
    }

    // Test uses the database directly to insert a row then uses the ContentProvider to read that data back.
    public void testBasicMovieInsertAndQuery() {
        // insert our test records into the database
        MovieDbHelper dbHelper = MovieDbHelper.getInstance(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertFalse("getWriteableDatabase must be WRITABLE!", db.isReadOnly());

        int testNumber = 2;
        ContentValues movieValues = TestUtilities.createMovieValues(testNumber);
        long movieRowId = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, movieValues);
        assertTrue("Unable to Insert MoviesEntry into the Database", movieRowId != -1);
        db.close();

        // Test the basic content provider query
        Cursor movieCursor = mContext.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI, // table
                null, // all columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // order by
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicMovieQuery", movieCursor, movieValues);

        // Has the NotificationUri been set correctly? --- we can only test this easily against API
        // level 19 or greater because getNotificationUri was added in API level 19.
        if (Build.VERSION.SDK_INT >= 19) {
            assertEquals("Error: Movie Query did not properly set NotificationUri",
                    movieCursor.getNotificationUri(), MoviesContract.MoviesEntry.CONTENT_URI);
        }
    }

    // Test uses the database directly to insert multiple rows then uses the ContentProvider to read that data back.
    public void testComplexMovieInsertAndQuery() {
        // insert our test records into the database
        MovieDbHelper dbHelper = MovieDbHelper.getInstance(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertFalse("getWriteableDatabase must be WRITABLE!", db.isReadOnly());

        ContentValues movieValues1 = TestUtilities.createMovieValues(1);
        long movieRowId1 = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, movieValues1);
        assertTrue("Unable to Insert MoviesEntry #1 into the Database", movieRowId1 != -1);

        ContentValues movieValues2 = TestUtilities.createMovieValues(2);
        long movieRowId2 = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, movieValues2);
        assertTrue("Unable to Insert MoviesEntry #2 into the Database", movieRowId2 != -1);

        // Test the basic content provider query
        String orderBy = "_ID ASC";
        Cursor movieCursor = mContext.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI,
                null, // all columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                orderBy // ORDER BY _ID ASC
        );

        // Make sure we get the correct cursor out of the database
        movieCursor.moveToFirst();
        TestUtilities.validateCurrentRecord("testComplexMovieQuery #1", movieCursor, movieValues1);
        movieCursor.moveToNext();
        TestUtilities.validateCurrentRecord("testComplexMovieQuery #2", movieCursor, movieValues2);

        // Has the NotificationUri been set correctly? --- we can only test this easily against API
        // level 19 or greater because getNotificationUri was added in API level 19.
        if (Build.VERSION.SDK_INT >= 19) {
            assertEquals("Error: Movie Query did not properly set NotificationUri",
                    movieCursor.getNotificationUri(), MoviesContract.MoviesEntry.CONTENT_URI);
        }

        db.close();
    }

    // Use the provider to insert a record and then update it.
    public void testUpdateMovie() {
        // Create a new map of values, where column names are the keys
        int testNumber = 1;
        ContentValues movieValues = TestUtilities.createMovieValues(testNumber);

        Uri movieUri = mContext.getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, movieValues);
        long movieRowId = ContentUris.parseId(movieUri);

        // Verify we got a row back.
        assertTrue(movieRowId != -1);
        Log.d(LOG_TAG, "New movie row id: " + movieRowId);

        ContentValues updatedValues = new ContentValues(movieValues);
        updatedValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POPULARITY, 9.3);
        updatedValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_OVERVIEW, "a test movie overview.");

        // Create a cursor with observer to make sure that the content provider is notifying observers as expected
        Cursor movieCursor = mContext.getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        movieCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                MoviesContract.MoviesEntry.CONTENT_URI, updatedValues, MoviesContract.MoviesEntry._ID + "=?",
                new String[]{Long.toString(movieRowId)});
        assertEquals(count, 1);

        // Did the content observer get called?
        // if this fails, the insert operation isn't calling getActivityBase().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        movieCursor.unregisterContentObserver(tco);
        movieCursor.close();

        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI,
                null, // projection
                MoviesContract.MoviesEntry._ID + "=?", // where
                new String[]{Long.toString(movieRowId)}, // Values for the "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testUpdateMovie. Error validating movie entry update.", cursor, updatedValues);
        cursor.close();
    }

    // Ensure insert notifications work and that we can still query specific data after adding multiple records
    public void testQueryAndInsertNotify() {
        int testNumber = 1;
        ContentValues movieContentValues1 = TestUtilities.createMovieValues(testNumber);

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MoviesContract.MoviesEntry.CONTENT_URI, true, tco);
        Uri movieUri1 = mContext.getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, movieContentValues1);

        // Did the content observer get called?
        // if this fails, the insert movie operation isn't calling getActivityBase().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long movieRowId1 = ContentUris.parseId(movieUri1);
        // Verify we got a row back.
        assertTrue(movieRowId1 != -1);

        // the data should be inserted.. now verify it.
        Cursor movieCursor1 = mContext.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI,
                null, // all columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertNotifyAndQuery. Error validating MoviesEntry insert1.", movieCursor1, movieContentValues1);

        // Create trailer values for the movie
        ContentValues trailerValues1 = TestUtilities.createTrailerValues(movieRowId1, testNumber);
        assertTrue(trailerValues1 != null);

        // Insert trailer ContentValues into database and get a trailer row ID back
        Uri trailerUri1 = mContext.getContentResolver().insert(MoviesContract.TrailersEntry.CONTENT_URI, trailerValues1);
        long trailerRowId1 = ContentUris.parseId(trailerUri1);
        Log.v(LOG_TAG, "testMovieTables: trailerRowId=" + trailerRowId1);
        assertTrue(trailerRowId1 != -1);

        // Fantastic.  Now that we have one movie and a trailer, add another
        testNumber = 2;
        ContentValues movieContentValues2 = TestUtilities.createMovieValues(testNumber);
        // The TestContentObserver is a one-shot class
        tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MoviesContract.MoviesEntry.CONTENT_URI, true, tco);

        Uri movieUri2 = mContext.getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, movieContentValues2);
        assertTrue(movieUri2 != null);

        long movieRowId2 = ContentUris.parseId(movieUri2);
        assertTrue(movieRowId2 != -1);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        Cursor movieCursor2 = mContext.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI,  // Table
                null, // all columns.
                "field_movie_title=?", // cols for "where" clause
                new String[]{"Gone with the Wind"}, // values for "where" clause
                null  // order by
        );

        TestUtilities.validateCursor("testInsertNotifyAndQuery. Error validating MoviesEntry insert2.", movieCursor2, movieContentValues2);

        // Create trailer values for the second movie
        ContentValues trailerValues2 = TestUtilities.createTrailerValues(movieRowId2, testNumber);
        assertTrue(trailerValues2 != null);

        // Insert trailer ContentValues into database and get a trailer row ID back
        Uri trailerUri2 = mContext.getContentResolver().insert(MoviesContract.TrailersEntry.CONTENT_URI, trailerValues2);
        long trailerRowId2 = ContentUris.parseId(trailerUri2);
        assertTrue(trailerRowId2 != -1);
    }

    public void testDeleteRecords() {
        testQueryAndInsertNotify();

        // Register a content observer for movie delete.
        TestUtilities.TestContentObserver movieObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MoviesContract.MoviesEntry.CONTENT_URI, true, movieObserver);

        // Register a content observer for trailer delete.
        TestUtilities.TestContentObserver trailerObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MoviesContract.TrailersEntry.CONTENT_URI, true, trailerObserver);

        // Register a content observer for review delete.
        TestUtilities.TestContentObserver reviewObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MoviesContract.ReviewsEntry.CONTENT_URI, true, reviewObserver);

        deleteAllRecordsFromProvider();

        try {
            trailerObserver.waitForNotificationOrFail();
        } catch (junit.framework.AssertionFailedError e1) {
            Log.i(LOG_TAG, "expected timeout for trailerObserver");
        }

        try {
            reviewObserver.waitForNotificationOrFail();
        } catch (junit.framework.AssertionFailedError e2) {
            Log.i(LOG_TAG, "expected timeout for reviewObserver");
        }

        movieObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(movieObserver);
        mContext.getContentResolver().unregisterContentObserver(trailerObserver);
        mContext.getContentResolver().unregisterContentObserver(reviewObserver);
    }

    // Test to bulkInsert some movies.
    public void testBulkInsert() {
        ContentValues[] bulkInsertContentValues = createBulkInsertMovieValues();
        assertEquals(BULK_INSERT_RECORDS_TO_INSERT, bulkInsertContentValues.length);

        // register a content observer for our bulk insert.
        TestUtilities.TestContentObserver bulkMovieObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MoviesContract.MoviesEntry.CONTENT_URI, true, bulkMovieObserver);

        int insertCount = mContext.getContentResolver().bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI, bulkInsertContentValues);
        Log.v(LOG_TAG, "testBulkInsert: insertCount=" + insertCount);

        // Did the content observer get called?
        // if this fails, the insert operation isn't calling getActivityBase().getContentResolver().notifyChange(uri, null);
        bulkMovieObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(bulkMovieObserver);

        assertEquals(BULK_INSERT_RECORDS_TO_INSERT, insertCount);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI, // table
                null, // all columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                MoviesContract.MoviesEntry._ID + " ASC"  // ORDER BY _ID ASCENDING
        );

        // we should have as many records in the database as we've inserted
        assertEquals(BULK_INSERT_RECORDS_TO_INSERT, cursor.getCount());

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext()) {
            TestUtilities.validateCurrentRecord("testBulkInsert. Error validating MoviesEntry #" + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }

}
