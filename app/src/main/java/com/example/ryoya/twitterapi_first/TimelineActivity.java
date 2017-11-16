package com.example.ryoya.twitterapi_first;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Image;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class TimelineActivity extends AppCompatActivity {

    ListView listView;
    List<Tweet> tweetList = new ArrayList<>();
    TweetAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(OnRefreshListener);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClassName
                        ("com.example.ryoya.twitterapi_first","com.example.ryoya.twitterapi_first.Post_tweet_activity");
                startActivity(intent);

            }
    });

        listView = (ListView) findViewById(R.id.my_list_view);
        adapter = new TweetAdapter(this, tweetList);

        listView.setAdapter(adapter);

        getHomeTimeline();
    }

    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener = new SwipeRefreshLayout.OnRefreshListener(){
        @Override
        public void onRefresh(){
            refreshTimeline();
            adapter.notifyDataSetChanged();
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run(){
                    refreshLayout.setRefreshing(false);
                }
            },2000);

        }
    };

    private void getHomeTimeline(){
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();

        StatusesService statusesService = twitterApiClient.getStatusesService();

        Call<List<Tweet>> call = statusesService.homeTimeline(20, null, null, false, false, false, false);
        call.enqueue(new Callback<List<Tweet>>(){
            @Override
            public void success(Result<List<Tweet>> result){

                tweetList.addAll(result.data);

                adapter.notifyDataSetChanged();

                Toast toast = Toast.makeText(TimelineActivity.this, "タイムライン取得成功", Toast.LENGTH_LONG);
                toast.show();

            }

            @Override
            public void failure(TwitterException exception){
                Toast toast = Toast.makeText(TimelineActivity.this, "タイムライン取得エラー", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    private void refreshTimeline(){
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();

        StatusesService statusesService = twitterApiClient.getStatusesService();

        Tweet tweet = tweetList.get(0);
        //list.get(数値)で(数値ー１)を指定する。(size-1)で最後のtweetを指定する。

        Call<List<Tweet>> call = statusesService.homeTimeline(30, tweet.getId(), null, false, false, false, false);
        call.enqueue(new Callback<List<Tweet>>(){
            @Override
            public void success(Result<List<Tweet>> result){

                adapter.notifyDataSetChanged();
                tweetList.addAll(0, result.data);
                //リストの先頭に要素を追加する為に(0, item)で記述

                Toast toast = Toast.makeText(TimelineActivity.this, "タイムライン取得成功", Toast.LENGTH_LONG);
                toast.show();

            }

            @Override
            public void failure(TwitterException exception){
                Toast toast = Toast.makeText(TimelineActivity.this, "タイムライン取得エラー", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }



}