package com.example.dohahamdy.movieappstage2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DOHA HAMDY on 4/23/2017.
 */

public class MovieData implements Parcelable {

    String img;
    String overview;
    String rate;
    String date;
    String title;


    String id;

    public static final String[] MAIN_MOVIE_PROJECTSION={
            MovieContract.MovieEntry.COLUMN_DATE,
            MovieContract.MovieEntry.COLUMN_IMG,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RATE,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_ID,
    };

    public static final int INDEX_MOVIE_DATE=0;
    public static final int INDEX_MOVIE_IMG=1;
    public static final int INDEX_MOVIE_OVERVIEW=2;
    public static final int INDEX_MOVIE_RATE=3;
    public static final int INDEX_MOVIE_TITLE=4;
    public static final int INDEX_MOVIE_ID=5;


    public MovieData(){}

    protected MovieData(Parcel in) {
        img = in.readString();
        overview = in.readString();
        rate = in.readString();
        date = in.readString();
        title = in.readString();
        id=in.readString();
    }

    public static final Creator<MovieData> CREATOR = new Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };

    public String getTitle() {
        return title;
    }
    public String getImg() {
        return img;
    }
    public String getOverview() {
        return overview;
    }
    public String getRate() {
        return rate;
    }
    public String getDate() {
        return date;
    }
    public String getId() {
        return id;
    }



    public void setTitle(String title) {
        this.title = title;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }
    public void setRate(String rate) {
        this.rate = rate;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(img);
        parcel.writeString(overview);
        parcel.writeString(rate);
        parcel.writeString(date);
        parcel.writeString(title);
        parcel.writeString(id);
    }
}
