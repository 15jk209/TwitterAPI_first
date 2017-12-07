package com.example.ryoya.twitterapi_first;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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
        Button button1 = (Button) findViewById(R.id.angry);
        Button button2 = (Button) findViewById(R.id.sad);
        Button button3 = (Button) findViewById(R.id.fun);
        Button button4 = (Button) findViewById(R.id.enjoy);


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
                    String seek1 = String.valueOf(seekBar1.getProgress()+1);
                    fos.write(seek1.getBytes());
                    String seek2 = String.valueOf(seekBar2.getProgress()+1);
                    fos.write(seek2.getBytes());
                    String seek3 = String.valueOf(seekBar3.getProgress()+1);
                    fos.write(seek3.getBytes());
                    String seek4 = String.valueOf(seekBar4.getProgress()+1);
                    fos.write(seek4.getBytes());

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
                finish();

                FileInputStream fis = null;
                try{
                    fis = openFileInput(file);
                    byte buffer[] = new byte[100];

                    fis.read(buffer);

                    String str = new String(buffer).trim();//trim()は先頭または最後の空白を切り取る

                    Toast.makeText(Post_tweet_activity.this, str + "が登録されています", Toast.LENGTH_SHORT).show();
                }catch (IOException e){
                    e.printStackTrace();
                    Toast.makeText(Post_tweet_activity.this, "読み込みに失敗しました" , Toast.LENGTH_SHORT).show();
                }finally{
                    try{
                        if(fis != null)
                            fis.close();
                    }catch(IOException e){
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
