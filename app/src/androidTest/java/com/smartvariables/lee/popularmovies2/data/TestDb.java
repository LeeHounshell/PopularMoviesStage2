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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.smartvariables.lee.popularmovies2.data_helper.MoviesContract;

import java.util.HashSet;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_FILE_NAME);
    }

    // delete the Sqlite database prior to each test
    public void setUp() {
        deleteTheDatabase();
    }

    protected void checkAllColumns(Cursor cursor, HashSet<String> columnsHashSet) {
        Log.v(LOG_TAG, "begin with columnsHashSet=" + columnsHashSet);
        int columnNameIndex = cursor.getColumnIndex("name");
        do {
            String columnName = cursor.getString(columnNameIndex);
            Log.v(LOG_TAG, "found column " + columnName);
            columnsHashSet.remove(columnName);
        } while (cursor.moveToNext());

        // if this fails, it means that the database doesn't contain all of the required columns for table being tested
        Log.v(LOG_TAG, "(should be empty) columnsHashSet=" + columnsHashSet);
        assertTrue("Error: The database doesn't contain all of the required columns!", columnsHashSet.isEmpty());
    }

    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MoviesContract.MoviesEntry.TABLE_NAME);
        tableNameHashSet.add(MoviesContract.TrailersEntry.TABLE_NAME);
        tableNameHashSet.add(MoviesContract.ReviewsEntry.TABLE_NAME);

        mContext.deleteDatabase(MovieDbHelper.DATABASE_FILE_NAME);
        MovieDbHelper helper = MovieDbHelper.getInstance(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: This means that the database has not been created correctly", cursor.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(cursor.getString(0));
        } while (cursor.moveToNext());
        cursor.close();
        cursor = null;

        // if this fails, it means that the database doesn't contain all tables
        assertTrue("Error: Your database is missing tables!", tableNameHashSet.isEmpty());

        Log.v(LOG_TAG, "begin testing table columns..");
        // Build a HashSet of all of the column names we want to look for
        HashSet<String> allColumnsHashSet;

        //------------------------------
        // columns for the movies table
        allColumnsHashSet = new HashSet<String>();
        allColumnsHashSet.add(MoviesContract.MoviesEntry._ID);
        allColumnsHashSet.add(MoviesContract.MoviesEntry.FIELD_MOVIE_OVERVIEW);
        allColumnsHashSet.add(MoviesContract.MoviesEntry.FIELD_MOVIE_POPULARITY);
        allColumnsHashSet.add(MoviesContract.MoviesEntry.FIELD_MOVIE_POSTER_PATH);
        allColumnsHashSet.add(MoviesContract.MoviesEntry.FIELD_MOVIE_RELEASE_DATE);
        allColumnsHashSet.add(MoviesContract.MoviesEntry.FIELD_MOVIE_TITLE);
        allColumnsHashSet.add(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_AVERAGE);
        allColumnsHashSet.add(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_COUNT);

        // now, do our tables contain the correct columns?
        Cursor moviesCursor = db.rawQuery("PRAGMA table_info(" + MoviesContract.MoviesEntry.TABLE_NAME + ")", null);
        assertTrue("Error: This means that we were unable to query the database for 'movies' table information.", moviesCursor.moveToFirst());
        checkAllColumns(moviesCursor, allColumnsHashSet);
        moviesCursor.close();

        //------------------------------
        // columns for the trailers table
        allColumnsHashSet = new HashSet<String>();
        allColumnsHashSet.add(MoviesContract.TrailersEntry._ID);
        allColumnsHashSet.add(MoviesContract.TrailersEntry.FIELD_MOVIE_ID);
        allColumnsHashSet.add(MoviesContract.TrailersEntry.FIELD_TRAILER_URL);

        // now, do our tables contain the correct columns?
        Cursor trailersCursor = db.rawQuery("PRAGMA table_info(" + MoviesContract.TrailersEntry.TABLE_NAME + ")", null);
        assertTrue("Error: This means that we were unable to query the database for 'trailers' table information.", trailersCursor.moveToFirst());
        checkAllColumns(trailersCursor, allColumnsHashSet);
        trailersCursor.close();

        //------------------------------
        // columns for the reviews table
        allColumnsHashSet = new HashSet<String>();
        allColumnsHashSet.add(MoviesContract.ReviewsEntry._ID);
        allColumnsHashSet.add(MoviesContract.ReviewsEntry.FIELD_MOVIE_ID);
        allColumnsHashSet.add(MoviesContract.ReviewsEntry.FIELD_CRITIC_NAME);
        allColumnsHashSet.add(MoviesContract.ReviewsEntry.FIELD_CRITIC_URL);
        allColumnsHashSet.add(MoviesContract.ReviewsEntry.FIELD_REVIEW_TEXT);

        // now, do our tables contain the correct columns?
        Cursor reviewsCursor = db.rawQuery("PRAGMA table_info(" + MoviesContract.ReviewsEntry.TABLE_NAME + ")", null);
        assertTrue("Error: This means that we were unable to query the database for 'reviews' table information.", reviewsCursor.moveToFirst());
        checkAllColumns(reviewsCursor, allColumnsHashSet);
        reviewsCursor.close();

        db.close();
    }

    protected void validateDatabase(SQLiteDatabase db, String tableName, ContentValues someValues) {

        // Query the database and receive a Cursor back
        Cursor someCursor = db.query(
                tableName, // Table to Query
                null,      // leaving "columns" null just returns all the columns.
                null,      // cols for "where" clause
                null,      // values for "where" clause
                null,      // columns to group by
                null,      // columns to filter by row groups
                null       // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue("Error: No Records returned from query for table " + tableName, someCursor.moveToFirst());

        // Validate the Query
        TestUtilities.validateCurrentRecord("testInsertReadDb row failed to validate for " + tableName, someCursor, someValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from query for table " + tableName, someCursor.moveToNext());

        someCursor.close();
    }

    // test that we can insert and query the database tables
    public void testMovieTables() {
        // First insert the movie, and then use the movieRowId to insert a trailer.
        // Then use the movieRowId to insert a movie review.

        int testNumber = 1;
        ContentValues movieValues = TestUtilities.createMovieValues(testNumber);
        long movieRowId = insertAndVerifyMovie(movieValues);
        Log.v(LOG_TAG, "testMovieTables: movieRowId=" + movieRowId);

        // Make sure we have a valid row ID.
        assertFalse("Error: Movie Not Inserted Correctly", movieRowId == -1L);

        // If there's an error in the SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        MovieDbHelper dbHelper = MovieDbHelper.getInstance(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertFalse("getWriteableDatabase must be WRITABLE!", db.isReadOnly());

        // Create trailer values for the movie
        ContentValues trailerValues = TestUtilities.createTrailerValues(movieRowId, testNumber);
        assertTrue(trailerValues != null);

        // Insert trailer ContentValues into database and get a trailer row ID back
        long trailerRowId = db.insert(MoviesContract.TrailersEntry.TABLE_NAME, null, trailerValues);
        Log.v(LOG_TAG, "testMovieTables: trailerRowId=" + trailerRowId);
        assertTrue(trailerRowId != -1);

        // Create review values for the movie
        ContentValues reviewValues = TestUtilities.createReviewsValues(movieRowId, testNumber);
        assertTrue(reviewValues != null);

        // Insert review ContentValues into database and get a review row ID back
        long reviewRowId = db.insert(MoviesContract.ReviewsEntry.TABLE_NAME, null, reviewValues);
        Log.v(LOG_TAG, "testMovieTables: reviewRowId=" + reviewRowId);
        assertTrue(reviewRowId != -1);

        validateDatabase(db, MoviesContract.MoviesEntry.TABLE_NAME, movieValues);
        validateDatabase(db, MoviesContract.TrailersEntry.TABLE_NAME, trailerValues);
        validateDatabase(db, MoviesContract.ReviewsEntry.TABLE_NAME, reviewValues);

        dbHelper.close(); // Close database
    }


    protected long insertAndVerifyMovie(ContentValues movieValues) {
        MovieDbHelper dbHelper = MovieDbHelper.getInstance(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertFalse("getWriteableDatabase must be WRITABLE!", db.isReadOnly());

        // Insert ContentValues into database and get a movie row ID back
        long movieRowId;
        movieRowId = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, movieValues);
        Log.v(LOG_TAG, "insertAndVerifyMovie: movieRowId=" + movieRowId);

        // Verify we got a row back.
        assertTrue(movieRowId != -1);

        // verify the data inserted properly..
        Cursor cursor = db.query(
                MoviesContract.MoviesEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back from the query
        assertTrue("Error: No Records returned from movie query", cursor.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        TestUtilities.validateCurrentRecord("Error: Movie Query Validation Failed", cursor, movieValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from movie query", cursor.moveToNext());

        // Close Cursor and Database
        cursor.close();
        db.close();
        return movieRowId;
    }

}
