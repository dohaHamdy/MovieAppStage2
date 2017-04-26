package com.example.dohahamdy.movieappstage2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by DOHA HAMDY on 4/23/2017.
 */

public class MovieProvider extends ContentProvider {

    public static final int CODEMOVIE=100;
    private MovieDbHelper dbHelper;

    private static final UriMatcher SURI_MATCHER=buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        final UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
        final String authoriy=MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authoriy, MovieContract.PATH_MOVIE,CODEMOVIE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper=new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(
            @NonNull Uri uri,
            @NonNull ContentValues[] values) {
        final SQLiteDatabase db= dbHelper.getWritableDatabase();

        switch (SURI_MATCHER.match(uri)) {

            case CODEMOVIE:
                db.beginTransaction();
                int rowInserted=0;
                try{
                    for (ContentValues value :values){
                        long _id=db.insert(MovieContract.MovieEntry.TABLE_NAME,null,value);
                        if(_id!=-1){
                            rowInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                if(rowInserted>0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowInserted;
            default:
                return super.bulkInsert(uri,values);
        }

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int update(
            @NonNull Uri uri,
            @Nullable ContentValues values,
            @Nullable String selection,
            @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public Uri insert(
            @NonNull Uri uri,
            @Nullable ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = SURI_MATCHER.match(uri);
        Uri returnUri;
        switch (match) {
            case CODEMOVIE:

                long id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {

                    returnUri = ContentUris.withAppendedId(uri, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(
            @NonNull Uri uri,
            @Nullable String selection,
            @Nullable String[] selectionArgs) {
        int numRowDeleted;
        if(selection==null)
            selection="1";

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (SURI_MATCHER.match(uri)){

            case CODEMOVIE:
                db.beginTransaction();
                numRowDeleted=db.delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                db.endTransaction();
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        if (numRowDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return numRowDeleted;
    }

    @Nullable
    @Override
    public Cursor query(
            @NonNull Uri uri,
            @Nullable String[] projection,
            @Nullable String selection,
            @Nullable String[] selectionArgs,
            @Nullable String sortOrder) {
        Cursor cursor;

        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        switch(SURI_MATCHER.match(uri)){

            case CODEMOVIE: {
                db.beginTransaction();
                cursor = db.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                db.endTransaction();
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }
}
