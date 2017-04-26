package com.example.dohahamdy.movieappstage2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.net.URL;
import java.util.List;


public class MainFragment extends Fragment
        implements MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor>{

    public static String Shared="previos";
    public static String TAG="mainFragment";
    //private TextView mYextError;
    private ProgressBar mLoading;
    private RecyclerView mcyclerView;
    private RecyclerView.LayoutManager mLayoutManger;
    private MovieAdapter adapt;
    private int mPosition=RecyclerView.NO_POSITION;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private Bundle mBundleRecycleState;
    SharedPreferences sharedpreferences;

    Parcelable list;
    private static final int ID_MOVIE_LOADER=44;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_main, container, false);


        //mYextError=(TextView) view.findViewById(R.id.movie_data);
        //new FetchMovieData().execute();
        mLoading=(ProgressBar)view.findViewById(R.id.loading);
        mcyclerView=(RecyclerView) view.findViewById(R.id.cyclerView);


        adapt=new MovieAdapter( getContext()/*,this*/);
        mcyclerView.setAdapter(adapt);

        mLayoutManger=new GridLayoutManager(getContext(),1);
        mcyclerView.setLayoutManager(mLayoutManger);
        mcyclerView.setHasFixedSize(true);

        showLoading();
        Log.d( "onCreateView: ","befor if");

        sharedpreferences=getActivity().getPreferences(getActivity().MODE_PRIVATE);
        if(!sharedpreferences.contains(MainFragment.Shared)) {
            Log.d( "onCreateView: ","not contain");
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(MainFragment.Shared, "populer");
            editor.commit();
        }
        String typeOfSearch = sharedpreferences.getString(MainFragment.Shared, "populer");
        Log.d( "onCreateView: ",typeOfSearch);
        if(typeOfSearch=="favorate")
        {
            getActivity().getSupportLoaderManager().initLoader(ID_MOVIE_LOADER,null,this);
        }
        else
        {
            loadMovieData(typeOfSearch);
        }

        return view;
    }
    @Override
    public void onClick(MovieData OneMovie) {
        Intent intent=new Intent(getContext(),getClass());
        intent.putExtra("movies",OneMovie);

        startActivity(intent);
    }

    private void loadMovieData(String typeOfSearch){
        showMovieData();
        new FetchMovieData().execute(typeOfSearch);

    }

    private void showMovieData(){
        mLoading.setVisibility(View.INVISIBLE);
        mcyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMsg(){
        mcyclerView.setVisibility(View.INVISIBLE);
        mLoading.setVisibility(View.VISIBLE);
    }

    private void showLoading(){
        mcyclerView.setVisibility(View.INVISIBLE);
        mLoading.setVisibility(View.VISIBLE);
    }

    public class FetchMovieData extends AsyncTask<String,Void,List<MovieData>> {
        @Override
        protected List<MovieData> doInBackground(String... params) {

            URL requestUrl= Networking.buildTheUrl(params[0]);
            try {
                String jsonresponse=Networking.getResponse(requestUrl);
                List<MovieData> movieDataJSON= MovieDbHelper.MovieJson.getSimpleData(getContext(),jsonresponse);
                return movieDataJSON;
            }catch (Exception e){
                return null;
            }


        }

        @Override
        protected void onPostExecute(List<MovieData> movieData) {
            if(movieData !=null){
                showMovieData();
                //Toast.makeText(MainActivity.this,onemovie,Toast.LENGTH_LONG);

                adapt.setMovieData(movieData);
                //  mMovieData.append((onemovie+"\n\n\n"));
            }
            else {
                showErrorMsg();

            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main_list,menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.populer){
            adapt.setMovieData(null);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(MainFragment.Shared, "popular");
            editor.commit();

            loadMovieData("popular");
            // getActivity().getSupportLoaderManager().initLoader(ID_MOVIE_LOADER,null,this);

            return true;
        }
        if (id==R.id.TOP) {
            adapt.setMovieData(null);

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(MainFragment.Shared, "top_rated");
            editor.commit();

            loadMovieData("top_rated");
//            getActivity().getSupportLoaderManager().initLoader(ID_MOVIE_LOADER,null,this);

            return true;
        }
        if (id==R.id.favorate){

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(MainFragment.Shared, "favorate");
            editor.commit();


            adapt.setMovieData(null);
            //loadMovieData("favorate");
            getActivity().getSupportLoaderManager().initLoader(ID_MOVIE_LOADER,null,this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id){
            case ID_MOVIE_LOADER:
                Uri MovieQueryUri= MovieContract.MovieEntry.CONTENT_URI;
                return new CursorLoader(getContext(),
                        MovieQueryUri,
                        MovieData.MAIN_MOVIE_PROJECTSION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implwmwnted: "+id);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        adapt.swapCursur(null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        adapt.swapCursur(data);
        if(mPosition==RecyclerView.NO_POSITION)
            mPosition=0;
        mcyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount()!=0)
            showMovieData();
    }

}

