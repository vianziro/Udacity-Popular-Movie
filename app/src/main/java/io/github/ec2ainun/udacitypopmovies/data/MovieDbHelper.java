/*
* Copyright (C) 2016 The Android Open Source Project
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

package io.github.ec2ainun.udacitypopmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import io.github.ec2ainun.udacitypopmovies.data.MovieContract.MovieEntry;


public class MovieDbHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "moviesDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 2;
    private static final String DATABASE_CREATE_MOVIE ="CREATE TABLE "  + MovieEntry.TABLE_NAME + " (" +
            MovieEntry._ID                + " INTEGER PRIMARY KEY, " +
            MovieEntry.COLUMN_MOVIEID + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_MOVIETITLE + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_MOVIEOVERVIEW + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_MOVIEPOSTERPATH + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_MOVIERELEASEDATE + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_MOVIEVOTEAVERAGE + " TEXT NOT NULL);";

    private static final String DATABASE_ALTER_MOVIE_1 = "ALTER TABLE "
            + MovieEntry.TABLE_NAME + " ADD COLUMN " + MovieEntry.COLUMN_MOVIEFAV + " TEXT;";


    // Constructor
    MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    /**
     * Called when the tasks database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_MOVIE);
    }


    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(DATABASE_ALTER_MOVIE_1);
        }
    }
}
