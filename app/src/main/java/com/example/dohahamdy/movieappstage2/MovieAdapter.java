package com.example.dohahamdy.movieappstage2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DOHA HAMDY on 4/23/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{
    private List<MovieData> moviesdetails;

    Context mContext;

    //  final private MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler{
        void onClick(MovieData OneMovie);
    }
    // private Cursor mCursor;

    public MovieAdapter(@NonNull Context context/*,MovieAdapterOnClickHandler clickHandler*/){
        mContext=context;

        //    mClickHandler=clickHandler;
    }

    public class MovieAdapterViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener {
        //  public final TextView mMovieText
        public ImageView mImageView;

        MovieAdapterViewHolder(View view) {

            super(view);
            mImageView = (ImageView) view.findViewById(R.id.poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent intent = new Intent(context, MovieDetailActivity.class);
            intent.putExtra("movies", moviesdetails.get(getAdapterPosition()));
            context.startActivity(intent);
        }
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {

        //   mCursor.moveToPosition(position);
        MovieData dataForOneMovie=moviesdetails.get(position);
        // String Image=mCursor.getInt(1);

        Picasso.with(mContext)
                .load(Networking.IMGURL+dataForOneMovie.getImg())
                .into(holder.mImageView);
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list,parent,false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public int getItemCount() {

        if (null==moviesdetails)
            return 0;
        return moviesdetails.size();
    }


    List<MovieData> convertCursurToList(Cursor cursor)
    {
        List<MovieData>movies=new ArrayList<>();
        if(cursor !=null)
        {
            if(cursor.moveToFirst()){
                do{
                    MovieData movie=new MovieData();
                    movie.setTitle(cursor.getString(MovieData.INDEX_MOVIE_TITLE));
                    movie.setRate(cursor.getString(MovieData.INDEX_MOVIE_RATE));
                    movie.setOverview(cursor.getString(MovieData.INDEX_MOVIE_OVERVIEW));
                    movie.setImg(cursor.getString(MovieData.INDEX_MOVIE_IMG));
                    movie.setDate(cursor.getString(MovieData.INDEX_MOVIE_DATE));
                    movie.setId(cursor.getString(MovieData.INDEX_MOVIE_ID));
                    Log.d( "convertCursurToList: ",movie.getTitle());
                    movies.add(movie);
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
        return movies;
    }
    public void swapCursur(Cursor newCursor){
        setMovieData(convertCursurToList(newCursor));
        notifyDataSetChanged();
    }

    public void setMovieData(List<MovieData> moviesData){
        moviesdetails=moviesData;
        notifyDataSetChanged();
    }

}

