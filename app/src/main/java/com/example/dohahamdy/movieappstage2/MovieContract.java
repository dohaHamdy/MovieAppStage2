package com.example.dohahamdy.movieappstage2;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by DOHA HAMDY on 4/23/2017.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY="com.example.dohahamdy.movieappstage2";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE="movie";



    public static final class MovieEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI=
                BASE_CONTENT_URI
                        .buildUpon()
                        // .appendQueryParameter("api_key",APIKEY)
                        .appendPath(PATH_MOVIE)
                        .build();

        public static final String TABLE_NAME="movie";
        public static final String COLUMN_TITLE="title";
        public static final String COLUMN_RATE="rate";
        public static final String COLUMN_OVERVIEW="overview";
        public static final String COLUMN_IMG="image";
        public static final String COLUMN_DATE="date";
        public static final String COLUMN_ID="movieID";
    }
}

