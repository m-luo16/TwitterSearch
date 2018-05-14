package com.vouchr.michelle.vouchrtwitter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Michelle Luo on 2018-05-11
 * */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    private List<ListTweet> listTweets;
    private OnBottomReachedListener onBottomReachedListener;

    public Adapter(List<ListTweet> listTweets) {
        this.listTweets = listTweets;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tweets, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == listTweets.size() - 1){
            onBottomReachedListener.onBottomReached(position);
        }
        ListTweet current = listTweets.get(position);
        holder.bind(current);
    }
    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }
    @Override
    public int getItemCount() {
        return listTweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.textViewScreenName)
        public TextView textViewScreenName;
        @BindView(R.id.textViewName)
        public TextView textViewName;
        @BindView(R.id.textViewContent)
        public TextView textViewContent;
        @BindView(R.id.imageViewProfile)
        public ImageView imageViewProfile;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(ListTweet current) {
            textViewScreenName.setText(current.getScreenName());
            textViewName.setText(current.getName());
            textViewContent.setText(current.getContent());

            String url = current.getProfileImageURL();
            Context context = imageViewProfile.getContext();
            Glide.with(context).load(url).apply(RequestOptions.circleCropTransform()).into(imageViewProfile);
        }
    }
}
