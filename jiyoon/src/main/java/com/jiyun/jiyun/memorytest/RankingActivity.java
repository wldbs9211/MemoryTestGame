package com.jiyun.jiyun.memorytest;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RankingActivity extends AppCompatActivity {

    ArrayList<ListViewRankingData> datas= new ArrayList<ListViewRankingData>();
    ListView listview;
    private int itemCount = 0;
    private String SERVER_ADDRESS="http://52.192.119.174";
    private static final String phpLocation_getCount = "/memory_test/getRankingCount.php";   // 랭킹 카운트
    private static final String phpLocation_getID = "/memory_test/getRankingID.php";         // 랭킹 아이디
    private static final String phpLocation_getScore = "/memory_test/getRankingScore.php";      // 랭킹 스코어
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        getInformationFromServer(); // 서버로부터 데이터 받음
    }

    private void getInformationFromServer() {
        class GetInformation extends AsyncTask<String, Void, Boolean> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RankingActivity.this, "Downloading Ranking", "Please wait...", true, true);
            }

            @Override
            protected Boolean doInBackground(String... param) {
                String uri_getCount = SERVER_ADDRESS + phpLocation_getCount;    // 랭킹 기록 몇개나 있는지..
                String uri_getID = SERVER_ADDRESS + phpLocation_getID + "?row=";
                String uri_getScore = SERVER_ADDRESS + phpLocation_getScore + "?row=";

                try {
                    URL url_getCount = new URL(uri_getCount);   // 먼저 서버로부터 랭킹 몇개나 있는지 받고
                    String count = getTextFromURL(url_getCount);
                    itemCount = Integer.parseInt(count.replaceAll("[\\D]",""));;    // string to int, remove ""   (example ""30"", when i receive value)

                    for(int i = 0; i < itemCount; i++){         // 처음부터 끝까지 하나씩 아이디,스코어 받아온다.
                        String uri_getIDAddedRow = uri_getID + i;
                        String uri_getScoreAddedRow = uri_getScore + i;

                        URL url_getCode = new URL(uri_getIDAddedRow);   // 아이디
                        URL url_getScore = new URL (uri_getScoreAddedRow);  // 스코어

                        datas.add(new ListViewRankingData(String.valueOf(i+1) , getTextFromURL(url_getCode), ConverStringToTime(getTextFromURL(url_getScore)))); // 리스트뷰에 하나씩 넣음
                    }
                    return true;   // Success..
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
                return false; // return false when wrong something
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if(result == true) {
                    //Toast.makeText(getApplicationContext(), "Download Success", Toast.LENGTH_LONG).show();

                    listview = (ListView) findViewById(R.id.RankingList);
                    ListViewRankingDataAdapter adapter = new ListViewRankingDataAdapter(getLayoutInflater(), datas);
                    listview.setAdapter(adapter);
                    loading.dismiss();
                }
                else
                    Toast.makeText(getApplicationContext(), "Download Fail", Toast.LENGTH_LONG).show();
            }
        }
        GetInformation gi = new GetInformation();
        gi.execute();
    }

    private String getTextFromURL(URL url){
        //System.out.println("url : " + url);
        HttpURLConnection con = null;
        BufferedReader bufferedReader;
        try {
            con = (HttpURLConnection) url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String result;
            StringBuilder sb = new StringBuilder();
            while ((result = bufferedReader.readLine()) != null) {
                sb.append(result);
            }
            result = sb.toString();
            //System.out.println("result : " + result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "-1"; // when fail
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