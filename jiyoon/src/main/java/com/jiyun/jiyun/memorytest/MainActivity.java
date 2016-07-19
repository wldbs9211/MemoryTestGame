package com.jiyun.jiyun.memorytest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    // 로그인 여부 체크
    boolean loggedIn = false;

    // 버튼과 관련
    Button StartButton;
    Button RankingButton;
    Button ExitButton;

    // 페이스북 연동과 관련
    private CallbackManager callbackManager = null;
    private AccessTokenTracker accessTokenTracker = null;
    private com.facebook.login.widget.LoginButton loginButton = null;

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            Toast.makeText(getApplicationContext(), loginResult.getAccessToken().getUserId(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(), "User sign in canceled!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        // 페북 연동 관련
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        accessTokenTracker.startTracking();

        // 푸시 관련
        FirebaseMessaging.getInstance().subscribeToTopic("memory_test");

        // 스타트 버튼 누르면 게임 화면으로 전환한다
        StartButton=(Button)findViewById(R.id.StartButton);
        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loggedIn = (Profile.getCurrentProfile() != null);
                Intent start_intent = new Intent(MainActivity.this,GameActivity.class);
                start_intent.putExtra("loggedIn",loggedIn);
                startActivity(start_intent);
            }
        });

        // 랭킹 버튼 누르면 랭킹 화면으로 전환한다
        RankingButton=(Button)findViewById(R.id.RankingButton);
        RankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ranking_intent = new Intent(MainActivity.this,RankingActivity.class);
                startActivity(ranking_intent);
            }
        });

        // 내 랭킹 버튼 누르면 내 랭킹 화면으로 전환한다
        RankingButton=(Button)findViewById(R.id.MyRankButton);
        RankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loggedIn = (Profile.getCurrentProfile() != null);
                if(loggedIn == false){  // 로그인 후에만 내 정보 보기 가능
                    Toast.makeText(getApplicationContext(), "Please login first.", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent myRank_intent = new Intent(MainActivity.this, MyRankActivity.class);
                    startActivity(myRank_intent);
                }
            }
        });

        // 종료 버튼 누르면 종료
        ExitButton=(Button)findViewById(R.id.ExitButton);
        ExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}


