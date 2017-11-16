package com.example.ryoya.twitterapi_first;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

/**
 * Created by ryoya on 2017/10/02.
 */

public class TweetAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater layoutInflater = null;
    private List<Tweet> tweetList;

    public TweetAdapter(Context context, List<Tweet> tweetList){
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tweetList = tweetList;
    }

    @Override
    public int getCount(){

        return tweetList.size();
    }

    @Override
    public Object getItem(int position){

        return tweetList.get(position).getId();
    }

    @Override
    public long getItemId(int position){
        return tweetList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        convertView = layoutInflater.inflate(R.layout.tweet_row, parent, false);

        TextView screenNameTextView = (TextView)convertView.findViewById(R.id.screen_name);
        TextView TweetTextTextView = (TextView)convertView.findViewById(R.id.tweet_text);
        TextView faboriteText=(TextView)convertView.findViewById(R.id.faborite);
        TextView RTText = (TextView)convertView.findViewById(R.id.RT);
        ImageView profileImage = (ImageView)convertView.findViewById(R.id.profile_image);

        //下記はpicassoでプロフィール画像を表示する為に書いたもの
        Picasso.with(context).load(tweetList.get(position).user.profileImageUrl).into(profileImage);
        screenNameTextView.setText(tweetList.get(position).user.name);
        TweetTextTextView.setText(tweetList.get(position).text);
        faboriteText.setText(String.valueOf(tweetList.get(position).favoriteCount));
        RTText.setText(String.valueOf(tweetList.get(position).retweetCount));

        return convertView;
    }
}
