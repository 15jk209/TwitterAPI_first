package com.example.ryoya.twitterapi_first;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import retrofit2.Call;


public class Post_tweet_activity extends AppCompatActivity {

    private String file = "tweet_file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_tweet_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText editText = (EditText) findViewById(R.id.edit_text);
        final SeekBar seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        final SeekBar seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        final SeekBar seekBar3 = (SeekBar) findViewById(R.id.seekBar3);
        final SeekBar seekBar4 = (SeekBar) findViewById(R.id.seekBar4);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postTweet(editText.getText().toString());

                //下記はデータ(Edittext)を永続化(保存)するためのもの
                FileOutputStream fos = null;

                try{
                    fos = openFileOutput(file, Context.MODE_PRIVATE);
                    fos.write(editText.getText().toString().getBytes());
                    fos.write(seekBar1.getProgress());
                    fos.write(seekBar2.getProgress());
                    fos.write(seekBar3.getProgress());
                    fos.write(seekBar4.getProgress());

                }catch(IOException e){
                    e.printStackTrace();
                }finally {
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void postTweet(String message){
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();

        Call<Tweet> call = statusesService.update(message, null, false, null, null, null, false, null, null);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                Toast.makeText(getApplicationContext(), "post seccess", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getApplicationContext(), "post failure", Toast.LENGTH_LONG).show();
            }
        });

        /* new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                Toast.makeText(getApplicationContext(), "post success", Toast.LENGTH_LONG).show();
            }

            public void failure(TwitterException exception) {
                Toast.makeText(getApplicationContext(), "post fail", Toast.LENGTH_LONG).show();
            }
        });*/

    }

}
