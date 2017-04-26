package com.example.dohahamdy.movieappstage2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by DOHA HAMDY on 4/23/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder>{
    private List<ReviewData> dataList;

    Context mContext;

    public class ReviewAdapterViewHolder extends  RecyclerView.ViewHolder{
        public TextView mauther;
        public TextView mlink;
        public TextView mreview;

        public  ReviewAdapterViewHolder(View view){
            super(view);
            mauther=(TextView)view.findViewById(R.id.auther);
            mlink=(TextView)view.findViewById(R.id.link);
            mreview=(TextView)view.findViewById(R.id.review);

        }

    }

    public ReviewAdapter(@NonNull Context context){
        mContext=context;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.review_item,parent,false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        ReviewData reviewForOneMovie=dataList.get(position);
        holder.mauther.setText( reviewForOneMovie.getAuther().toCharArray(),0,reviewForOneMovie.getAuther().length());

        holder.mlink.setText( reviewForOneMovie.getUrl().toCharArray(),0,reviewForOneMovie.getUrl().length());

        holder.mreview.setText( reviewForOneMovie.getContent().toCharArray(),0,reviewForOneMovie.getContent().length());
    }

    @Override
    public int getItemCount() {
        if(dataList==null)
            return 0;
        return dataList.size();
    }
    public void setReviewData(List<ReviewData> reviewData){
        dataList=reviewData;
        notifyDataSetChanged();
    }
}
