package com.example.dohahamdy.movieappstage2;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class MovieDetailFragment extends Fragment {
    private TextView mDate;
    private ImageView mImg;
    private TextView mRate;
    private TextView mView;
    private MovieData movies;
    private Button mfavorate;
    private Button mReviewBtn;
    private Button mTrailerBtn;
    private RecyclerView mRevires;

    private RecyclerView.LayoutManager mLayoutManger;

    private ReviewAdapter adapt;
    private int mPosition=RecyclerView.NO_POSITION;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mDate = (TextView) view.findViewById(R.id.date);
        mImg = (ImageView) view.findViewById(R.id.img);
        mRate = (TextView) view.findViewById(R.id.rate);
        mView = (TextView) view.findViewById(R.id.overView);
        mfavorate=(Button) view.findViewById(R.id.favBtn);
        mReviewBtn=(Button)view.findViewById(R.id.getReviews);
        mTrailerBtn=(Button) view.findViewById(R.id.getVideo);
        mRevires=(RecyclerView) view.findViewById(R.id.recycleReviews);
        adapt=new ReviewAdapter(getContext());
        mRevires.setAdapter(adapt);


        mLayoutManger=new GridLayoutManager(getContext(),1);
        mRevires.setLayoutManager(mLayoutManger);
        mRevires.setHasFixedSize(true);


        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra("movies")) {
                movies = intent.getParcelableExtra("movies");
                mDate.setText(movies.getDate());
                mView.setText(movies.getOverview());
                mRate.setText(movies.getRate());

                Picasso.with(getContext()).load(Networking.IMGURL + movies.getImg()).into(mImg);
            }
        }
        mfavorate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ContentValues value=new ContentValues();

                value.put(MovieContract.MovieEntry.COLUMN_DATE,movies.getDate());
                value.put(MovieContract.MovieEntry.COLUMN_IMG,movies.getImg());
                value.put(MovieContract.MovieEntry.COLUMN_TITLE,movies.getTitle());
                value.put(MovieContract.MovieEntry.COLUMN_OVERVIEW,movies.getOverview());
                value.put(MovieContract.MovieEntry.COLUMN_RATE,movies.getRate());
                value.put(MovieContract.MovieEntry.COLUMN_ID,movies.getId());
                String where=MovieContract.MovieEntry.COLUMN_ID+" = ? ";
                String [] whereArgs={movies.getId()};
                Cursor cursor=
                        getActivity()
                                .getContentResolver()
                                .query(MovieContract.MovieEntry.CONTENT_URI,
                                        null,
                                        where,
                                        whereArgs,
                                        null);
                Log.d("onClick: ","befor if");
                if(!cursor.moveToNext()) {
                    Log.d("onClick: ","inside if");
                    getActivity()
                            .getContentResolver()
                            .insert(MovieContract.MovieEntry.CONTENT_URI,
                                    value);
                }
                cursor.close();
            }
        });
        mReviewBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                new FetchReviewData().execute(movies.getId()+"/reviews");

            }
        });
        mTrailerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchTrialerData().execute(movies.getId()+"/videos");


            }
        });
        return view;
    }
    public void watchYoutubeVideo(String id){
        Intent intent = new Intent(Intent.ACTION_VIEW,
        Uri.parse(Networking.YOUTUBELINK + id));
        startActivity(intent);

    }
    public class FetchTrialerData extends AsyncTask<String,Void,MovieTrialerData> {

        @Override
        protected MovieTrialerData doInBackground(String... params) {
            URL requestUrl=Networking.buildTheUrl(params[0]);
            try {
                String jsonresponse=Networking.getResponse(requestUrl);
                MovieTrialerData trialDataJSON= Networking.trialerJson.getSimpleData(getContext(),jsonresponse);
                Log.d("DetailFrag Background: ",trialDataJSON.getType());
                return trialDataJSON;
            }catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(MovieTrialerData datas) {
            if(datas !=null){
                Log.d("getTrialer: ","type: "+datas.getType()+"  key"+datas.getKey());
                watchYoutubeVideo(datas.getKey());
            }
        }

    }
    public class FetchReviewData extends AsyncTask<String,Void,List<ReviewData>> {

        @Override
        protected List<ReviewData> doInBackground(String... params) {
            URL requestUrl=Networking.buildTheUrl(params[0]);
            try {
                String jsonresponse=Networking.getResponse(requestUrl);
                List<ReviewData> reviewDataJSON= Networking.ReviewJson.getSimpleData(getContext(),jsonresponse);
                Log.d("DetailFrag Background: ",reviewDataJSON.get(0).getAuther());
                return reviewDataJSON;
            }catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ReviewData> datas) {
            if(datas !=null){
                showReviewData();
                //Toast.makeText(MainActivity.this,onemovie,Toast.LENGTH_LONG);

                adapt.setReviewData(datas);
                //  mMovieData.append((onemovie+"\n\n\n"));
            }
            else {
                showErrorMsg();

            }
        }

    }
    private void loadMovieData(String typeOfSearch){
        showReviewData();
        new FetchReviewData().execute(typeOfSearch);
    }

    private void showReviewData(){
        mRevires.setVisibility(View.VISIBLE);
    }

    private void showErrorMsg(){
        mRevires.setVisibility(View.INVISIBLE);
    }

}