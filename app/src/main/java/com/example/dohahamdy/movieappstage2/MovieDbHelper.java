package com.example.dohahamdy.movieappstage2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DOHA HAMDY on 4/23/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="movie.db";
    private static final int DATABASE_VERSION=1;
    public MovieDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE=
                "CREATE TABLE "+ MovieContract.MovieEntry.TABLE_NAME+" ("+
                        MovieContract.MovieEntry._ID            +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        MovieContract.MovieEntry.COLUMN_TITLE   +" TEXT, "+
                        MovieContract.MovieEntry.COLUMN_RATE    +" TEXT, "+
                        MovieContract.MovieEntry.COLUMN_OVERVIEW+" TEXT, "+
                        MovieContract.MovieEntry.COLUMN_DATE    +" TEXT, "+
                        MovieContract.MovieEntry.COLUMN_IMG     +" TEXT, "+
                        MovieContract.MovieEntry.COLUMN_ID      +" TEXT "+
                        ");";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Created by DOHA HAMDY on 4/13/2017.
     */

    public static class MovieJson {

        public static List<MovieData> getSimpleData(Context context, String jsonString)
                throws JSONException {

            //String[] parseMovieData = null;
            List<MovieData> movieData=new ArrayList<>();
            JSONObject json = new JSONObject(jsonString);
            JSONArray MovieArray = json.getJSONArray("results");

            //parseMovieData = new String[MovieArray.length()];
            //movieData=new MovieData[MovieArray.length()];
            for (int i = 0; i < MovieArray.length(); i++) {
                JSONObject oneMovie=MovieArray.getJSONObject(i);
                MovieData movie=new MovieData();
                movie.setTitle(oneMovie.getString("title"));
                movie.setImg(oneMovie.getString("poster_path"));
                movie.setOverview(oneMovie.getString("overview"));
                movie.setRate(oneMovie.getString("vote_average"));
                movie.setDate(oneMovie.getString("release_date"));
                movie.setId(oneMovie.getString("id"));
                movieData.add(movie);
                //parseMovieData[i]=movieData[i].getImg()+ movieData[i].getTitle();
            }
            return movieData;
        }
    }
}

