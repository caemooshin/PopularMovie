package com.example.y_v_d.popularmovie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.y_v_d.popularmovie.data.MovieContract.MovieEntry;

    public class MovieDBHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "movieDb.db";

        // If you change the database schema, you must increment the database version
        private static final int VERSION = 1;


        // Constructor
        MovieDBHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {

            // Create tasks table (careful to follow SQL formatting rules)
            final String CREATE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME
                    + " (" +
                    MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY," +
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH + " TEXT NOT NULL " +
                    " );";
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
            onCreate(db);
        }
    }
