package com.example.dohahamdy.movieappstage2;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by DOHA HAMDY on 4/23/2017.
 */

public class Networking {

    final static String BASEURL="https://api.themoviedb.org/3/movie/";

    final static String APIKEY="07eb9eb4668d8e779e3a30e4f9755e1e";
    final public static String IMGURL="http://image.tmdb.org/t/p/w185/";
    final public static String YOUTUBELINK="http://www.youtube.com/watch?v=";

    final static String VIDEOS="videos";


    public static URL buildTheUrl(String typeOfSearch){
        Uri bult=Uri.parse(BASEURL+typeOfSearch)
                .buildUpon()
                .appendQueryParameter("api_key",APIKEY)
                .build();
        URL url=null;
        try{
            url=new URL(bult.toString());

        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }
    public static String getResponse(URL url)throws IOException {

        HttpsURLConnection connection=(HttpsURLConnection)url.openConnection();

        connection.setConnectTimeout(5000);
        try {
            InputStream in=connection.getInputStream();
            Scanner scan=new Scanner(in);
            scan.useDelimiter("\\A");
            boolean hasinput=scan.hasNext();
            if (hasinput){
                return scan.next();
            }else{
                return null;
            }
        }
        finally {
            connection.disconnect();
        }
    }

    /**
     * Created by DOHA HAMDY on 4/22/2017.
     */

    public static class ReviewJson {
        public static List<ReviewData> getSimpleData(Context context, String jsonString)
                throws JSONException {
            List<ReviewData> reviewData=new ArrayList<>();

            JSONObject json = new JSONObject(jsonString);
            JSONArray ReviewArray = json.getJSONArray("results");

            for (int i = 0; i < ReviewArray.length(); i++) {
                JSONObject oneReview=ReviewArray.getJSONObject(i);
                ReviewData review=new ReviewData();
                review.setAuther(oneReview.getString("author"));
                review.setContent(oneReview.getString("content"));
                review.setUrl(oneReview.getString("url"));
                reviewData.add(review);
            }
            return reviewData;
        }
    }



    public static class trialerJson{
        public static MovieTrialerData getSimpleData(Context context, String jsonString)
                throws JSONException {
            JSONObject json = new JSONObject(jsonString);
            JSONArray MovieTrialerArray = json.getJSONArray("results");
            for (int i = 0; i < MovieTrialerArray.length(); i++) {
                JSONObject oneMovieTrialer = MovieTrialerArray.getJSONObject(i);
                MovieTrialerData trialer = new MovieTrialerData();

                trialer.setKey(oneMovieTrialer.getString("key"));
                trialer.setType(oneMovieTrialer.getString("type"));
                Log.d("inside foor",trialer.getType());

                if (trialer.getType() == "Trailer")
                    Log.d("getSimpleData: ", "FOUND");
                    return trialer;

            }
            Log.d( "getSimpleData: ",":(");
            return null;
        }
    }
}

