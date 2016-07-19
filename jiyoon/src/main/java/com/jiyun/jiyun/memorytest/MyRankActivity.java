package com.jiyun.jiyun.memorytest;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.facebook.Profile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MyRankActivity extends AppCompatActivity {

    // 서버에 점수, 게임기록을 보네기 위해서..
    private static final String SERVER_ADDRESS = "http://52.192.119.174";
    private static final String phpLoaction_myScore = "/memory_test/getMyScore.php?";
    private static final String phpLoaction_myRank = "/memory_test/getMyRank.php?";
    private static final String phpLoaction_myTryCount = "/memory_test/getMyTryCount.php?";
    private static final String phpLoaction_mySuccessCount = "/memory_test/getMySuccessCount.php?";

    // 정보 뿌려줄 텍스트 뷰
    TextView myName;
    TextView myRecord;
    TextView myRank;
    TextView myTryCount;
    TextView mySuccessCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rank);

        String user_name = Profile.getCurrentProfile().getName().toString().replaceAll(" ", "");
        try {
            user_name = URLEncoder.encode(user_name, "UTF-8");
        }catch (Exception e){
        }

        //텍스트뷰와 관련하여
        myName = (TextView)findViewById(R.id.myName);
        myRecord = (TextView)findViewById(R.id.myRecord);
        myRank = (TextView)findViewById(R.id.myRank);
        myTryCount = (TextView)findViewById(R.id.myTryCount);
        mySuccessCount = (TextView)findViewById(R.id.mySuccessCount);

        //정보 받아올 url
        String URL_myRecord = SERVER_ADDRESS + phpLoaction_myScore + "id=" + user_name;
        String URL_myTryCount = SERVER_ADDRESS + phpLoaction_myTryCount + "id=" + user_name;
        String URL_mySuccessCount = SERVER_ADDRESS + phpLoaction_mySuccessCount + "id=" + user_name;
        String URL_myRank = SERVER_ADDRESS + phpLoaction_myRank + "id=" + user_name;

        //정보 서버로부터 받아서 텍스트뷰 세팅하는 함수.
        myName.setText(Profile.getCurrentProfile().getName().toString().replaceAll(" ", ""));
        getMyInformation(URL_myRecord,myRecord);
        getMyInformation(URL_myTryCount,myTryCount);
        getMyInformation(URL_mySuccessCount,mySuccessCount);
        getMyInformation(URL_myRank,myRank);
    }


    // 내 정보 가져오는 함수, url과 버튼을 인자로..
    private void getMyInformation(final String stringUrl, final TextView targetTextView) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        runOnUiThread(new Runnable() {
            public void run() {
                try {

                    URL url = new URL(stringUrl);

                    HttpURLConnection con = null;
                    BufferedReader bufferedReader;
                    String result;
                    StringBuilder sb = new StringBuilder();
                    con = (HttpURLConnection) url.openConnection();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while ((result = bufferedReader.readLine()) != null) {
                        sb.append(result);
                    }
                    result = sb.toString();
                    if(targetTextView == myRecord){ // 기록의 경우에는 받아온 데이터 변환해야함.
                        targetTextView.setText(ConverStringToTime(result));
                    }
                    else {
                        targetTextView.setText(result);
                    }
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
            }
        });
    }

    private String ConverStringToTime(String text){   // 숫자를 받아서 시간초로 보기좋게 바꿔주는 함수. 예) 856 => 08:56
        StringBuffer sb = new StringBuffer(text);
        if(sb.length()<4){  // 10초 미만.. 예) 856
            sb.insert(1,":");   // 8:56
            sb.insert(0,"0");   // 08:56
        }
        else{   // 10초 이상 예) 1256
            sb.insert(2,":");   // 12:56
        }
        return sb.toString();
    }
}

