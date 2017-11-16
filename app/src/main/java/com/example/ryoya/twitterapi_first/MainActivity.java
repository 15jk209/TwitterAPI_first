package com.example.ryoya.twitterapi_first;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.identity.*;
import com.twitter.sdk.android.core.TwitterCore;

import static com.example.ryoya.twitterapi_first.R.id.seekBar1;


public class MainActivity extends AppCompatActivity{

    private static final String TWITTER_KEY = "BNGU8EiBiuWyQYLoGBBDmBWhZ";
    private static final String TWITTER_SECRET = "NA1W0coHKslOJ1sMqrrnuYFGI3KZhT4pt6X28QTQS28cbJM6A7";

    private TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET))
                .debug(true)
                .build(); //twitterの初期化
        Twitter.initialize(config);
        setContentView(R.layout.activity_main);

        if(TwitterCore.getInstance().getSessionManager().getActiveSession()!= null) {
            //Intent intent = new Intent(MainActivity.this, TimelineActivity.class);
            //startActivity(intent);

            Toast toast = Toast.makeText(MainActivity.this, "ログイン中", Toast.LENGTH_LONG);
            toast.show();

            Intent intent = new Intent(MainActivity.this, TimelineActivity.class);
            startActivity(intent);

        }else{

            /*タイムラインの画面に遷移する
            if文はログイン中かどうかを判定する。if文を消してコールバックの中に入れても可*/
        }


        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = result.data;

                String msg = "@" + session.getUserName() + "logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                //上記2文はログイン時に表示される文字を表示
            //ログイン成功時にコールバックする
                Intent intent = new Intent(MainActivity.this, TimelineActivity.class);
                startActivity(intent);
                //ログインしたらタイムラインの画面に遷移する
            }
            @Override
            public void failure(TwitterException exception) {
                Toast toast = Toast.makeText(MainActivity.this, "ログイン失敗", Toast.LENGTH_LONG);
                toast.show();
            //ログイン失敗時
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginButton.onActivityResult(requestCode, resultCode, data);
    }


}