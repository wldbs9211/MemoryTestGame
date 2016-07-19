package com.jiyun.jiyun.memorytest;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Profile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Random;


public class GameActivity extends AppCompatActivity {

    // 서버에 점수, 게임기록을 보네기 위해서..
    private static final String SERVER_ADDRESS = "http://52.192.119.174";
    private static final String phpLoaction_score = "/memory_test/ranking_register.php?";
    private static final String phpLoaction_last_play = "/memory_test/last_play_date_log.php?";
    private boolean loggedIn;

    // 스톱워치의 상태를 위한 상수
    final static int IDLE = 0;
    final static int RUNNING = 1;
    final static int CANNOTCLICK = 2;

    // 시간과 관련한 변수
    int mStatus = IDLE;//처음 상태는 IDLE
    long mBaseTime;

    // 랜덤으로 숫자 뿌려주기 위한 변수들..
    int random_number_arr[] = new int[10];
    boolean isAlreadySet[] = new boolean[10];

    // 게임 버튼과 시간정보
    Button GameStartButton; // 스타트 버튼... 누른 후에는 리스타트로 바뀌어야겠지?
    TextView TimeTextView;  // 시간 정보를 알려줄것임.. 얼마나 지났나..?

    //게임 버튼과 관련하여
    Button GameButton1;
    Button GameButton2;
    Button GameButton3;
    Button GameButton4;
    Button GameButton5;
    Button GameButton6;
    Button GameButton7;
    Button GameButton8;
    Button GameButton9;

    //게임 진행과 관련하여
    int count = 1;  // 숫자 몇까지 눌렀는지? 정답 비교에 사용할 예정(누른 숫자가 이 숫자여야함.)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        loggedIn = getIntent().getExtras().getBoolean("loggedIn");   // 페이스북 로그인 여부
        System.out.println("로그인 되었음? : " + loggedIn);

        GameStartButton = (Button)findViewById(R.id.GameStartButton);
        TimeTextView = (TextView)findViewById(R.id.TimeTextView);

        //게임 버튼과 관련하여
        GameButton1 = (Button)findViewById(R.id.GameButton1);
        GameButton2 = (Button)findViewById(R.id.GameButton2);
        GameButton3 = (Button)findViewById(R.id.GameButton3);
        GameButton4 = (Button)findViewById(R.id.GameButton4);
        GameButton5 = (Button)findViewById(R.id.GameButton5);
        GameButton6 = (Button)findViewById(R.id.GameButton6);
        GameButton7 = (Button)findViewById(R.id.GameButton7);
        GameButton8 = (Button)findViewById(R.id.GameButton8);
        GameButton9 = (Button)findViewById(R.id.GameButton9);

        // 게임 스타트 버튼 이벤트
        GameStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(mStatus){
                    //IDLE상태이면
                    case IDLE:
                        System.out.println("로그인되었나? : " + loggedIn);
                        if(loggedIn == true)    // 페북 계정 로그인 된 상태라면
                            update_last_play();     // 게임 횟수 증가
                        mStatus = CANNOTCLICK;  // 상태는 버튼클릭 불가 상태로... 카운트다운 함수 내에서 5초뒤 상태 바꿔줌
                        countDown();    // 5초 카운트 다운

                        //start를 restart로 바꾸고
                        GameStartButton.setText("ReStart");
                        // 여기서는 이제 텍스트뷰에 숫자를 랜덤으로 보여주는 알고리즘을 구현...
                        setNumber();    // 텍스트뷰 숫자 세팅
                        count = 1;
                        break;

                    //버튼이 실행상태이면 Restart를 누른거임
                    case RUNNING:
                        //핸들러 메시지를 없애고
                        mTimer.removeMessages(0);
                        //텍스트뷰 초기화
                        TimeTextView.setText("00:00:00");
                        GameStartButton.setText("Game Start");
                        //상태는 IDLE로 해야겠지
                        mStatus = IDLE;
                        break;

                    case CANNOTCLICK:   // 클릭불가상태에서는 클릭해도 아무일 없도록... 카운트 다운 중 클릭하면 또 카운트다운 돌아가는 것 막는 용도.
                        break;
                }
            }
        });
    }

    // 아래 버튼 눌렀을 때 이벤트...
    public void GameButtonClicked(View v) {
        if(mStatus == IDLE || mStatus == CANNOTCLICK) // 게임 중이 아닌데 버튼 눌리면 무시.
            return;
        switch (v.getId()){
            case R.id.GameButton1:
                if(isCorrectAnswer(0)==true){   // 맞은 경우
                    count++;
                    if(count == 10) success();
                }
                else{   // 틀린 경우
                    selectWrongAnswer();
                }
                break;
            case R.id.GameButton2:
                if(isCorrectAnswer(1)==true){
                    count++;
                    if(count == 10) success();
                }
                else{
                    selectWrongAnswer();
                }
                break;
            case R.id.GameButton3:
                if(isCorrectAnswer(2)==true){
                    count++;
                    if(count == 10) success();
                }
                else{
                    selectWrongAnswer();
                }
                break;
            case R.id.GameButton4:
                if(isCorrectAnswer(3)==true){
                    count++;
                    if(count == 10) success();
                }
                else{
                    selectWrongAnswer();
                }
                break;
            case R.id.GameButton5:
                if(isCorrectAnswer(4)==true){
                    count++;
                    if(count == 10) success();
                }
                else{
                    selectWrongAnswer();
                }
                break;
            case R.id.GameButton6:
                if(isCorrectAnswer(5)==true){
                    count++;
                    if(count == 10) success();
                }
                else{
                    selectWrongAnswer();
                }
                break;
            case R.id.GameButton7:
                if(isCorrectAnswer(6)==true){
                    count++;
                    if(count == 10) success();
                }
                else{
                    selectWrongAnswer();
                }
                break;
            case R.id.GameButton8:
                if(isCorrectAnswer(7)==true){
                    count++;
                    if(count == 10) success();
                }
                else{
                    selectWrongAnswer();
                }
                break;
            case R.id.GameButton9:
                if(isCorrectAnswer(8)==true){
                    count++;
                    if(count == 10) success();
                }
                else{
                    selectWrongAnswer();
                }
                break;
        }
    }

    //스톱워치는 위해 핸들러를 만든다.
    Handler mTimer = new Handler(){
        //핸들러는 기본적으로 handleMessage에서 처리한다.
        public void handleMessage(android.os.Message msg) {
            //텍스트뷰를 수정해준다.
            TimeTextView.setText(getEllapse());
            //메시지를 다시 보낸다.
            mTimer.sendEmptyMessage(0);//0은 메시지를 구분하기 위한 것
        };
    };

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        mTimer.removeMessages(0);//메시지를 지워서 메모리릭 방지
        super.onDestroy();
    }

    // 지난 시간 구해주는 함수
    String getEllapse(){
        long now = SystemClock.elapsedRealtime();
        long ell = now - mBaseTime;//현재 시간과 지난 시간을 빼서 ell값을 구하고
        //아래에서 포맷을 바꾸고 리턴해준다.
        String sEll = String.format("%02d:%02d:%02d", ell / 1000 / 60, (ell/1000)%60, (ell %1000)/10);
        return sEll;
    }

    // 랜덤으로 숫자 세팅
    void setNumber(){
        int count = 0;  // 9개 숫자가 모두 세팅되었는지... 확인하기 위해

        for(int i = 0; i<10; i++){  // 처음엔 모든 숫자가 아직 선택되지 않았다.
            isAlreadySet[i] = false;
        }

        // 1~9의 숫자를 배열에 랜덤으로 넣는다.
        while(true){
            if(count == 9)
                break;
            Random Random_number = new Random();
            int temp_number = Random_number.nextInt(9) + 1;    // 파라미터보다 작은 숫자들만 나옴/ 이 경우엔 1~9만 출력 됨.
            //System.out.println(temp_number);

            if(isAlreadySet[temp_number] == true)   // 이미 세팅 되어있는 숫자면 버려야겠지?
                continue;
            else{                                   // 세팅 안된 숫자라면..
                isAlreadySet[temp_number] = true;   // 세팅했다고 해주고,
                random_number_arr[count] = temp_number; // 배열에 넣어주고
                count++;    // 카운트 올린다
            }
        }

        // 텍스트뷰 바꿔줌
        makeButtonTextVisible();
    }

    // 누른 버튼이 정답인지?
    boolean isCorrectAnswer(int clickedButtonNumber){
        if(random_number_arr[clickedButtonNumber] == count)
            return true;
        else
            return false;
    }

    void selectWrongAnswer(){
        //핸들러 메시지를 없애고
        mTimer.removeMessages(0);
        //텍스트뷰 초기화
        TimeTextView.setText("00:00:00");
        GameStartButton.setText("Try Again");
        //상태 변경
        mStatus = IDLE;
        //정답 보여줌
        makeButtonTextVisible();
    }

    void success(){
        //핸들러 메시지를 없애고
        mTimer.removeMessages(0);
        //텍스트뷰 초기화
        GameStartButton.setText("Success!");
        //상태 변경
        mStatus = IDLE;
        //정답 보여줌
        makeButtonTextVisible();
        //점수 등록
        if(loggedIn == true)    // 페북 계정 로그인 된 상태라면
            register_score();
    }

    void makeButtonTextInvisible(){
        GameButton1.setText("");
        GameButton2.setText("");
        GameButton3.setText("");
        GameButton4.setText("");
        GameButton5.setText("");
        GameButton6.setText("");
        GameButton7.setText("");
        GameButton8.setText("");
        GameButton9.setText("");
    }

    void makeButtonTextVisible(){
        GameButton1.setText("" + random_number_arr[0]);
        GameButton2.setText("" + random_number_arr[1]);
        GameButton3.setText("" + random_number_arr[2]);
        GameButton4.setText("" + random_number_arr[3]);
        GameButton5.setText("" + random_number_arr[4]);
        GameButton6.setText("" + random_number_arr[5]);
        GameButton7.setText("" + random_number_arr[6]);
        GameButton8.setText("" + random_number_arr[7]);
        GameButton9.setText("" + random_number_arr[8]);
    }
    void countDown(){
        new CountDownTimer(5 * 1000, 900){  // 두번째인자 1000으로 하면 숫자가 좀 그럼..
            int countDown = 5;
            @Override
            public void onTick(long millisUntilFinished) { // 총 시간과 주기
                TimeTextView.setText("" + countDown);
                countDown --;
            }

            @Override
            public void onFinish() {    // 5초가 지나면
                //베이스 시간 세팅
                mBaseTime = SystemClock.elapsedRealtime();
                //핸들러로 메시지를 보낸다.
                mTimer.sendEmptyMessage(0);
                // 텍스트 안보이게 한다.
                makeButtonTextInvisible();
                //상태를 RUNNING으로 바꾼다.
                mStatus = RUNNING;  // 게임 동작
            }
        }.start();  // 타이머 시작
    }

    // 점수 등록하는 함수
    private void register_score() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    System.out.println("걸린시간 : " + Integer.parseInt(TimeTextView.getText().toString().replaceAll(":", "")));

                    String user_ID = Profile.getCurrentProfile().getName().toString().replaceAll(" ", "");
                    user_ID = URLEncoder.encode(user_ID,"UTF-8");

                    URL url = new URL(SERVER_ADDRESS + phpLoaction_score
                            + "id=" + user_ID + "&"   // 로그인된 페이스북 계정으로 랭킹에 등록 시도
                            + "score=" + Integer.parseInt(TimeTextView.getText().toString().replaceAll(":", "")));

                    //System.out.println(url);

                    HttpURLConnection con = null;
                    BufferedReader bufferedReader;
                    String result;
                    StringBuilder sb = new StringBuilder();
                    con = (HttpURLConnection) url.openConnection();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
            }
        });
    }

    // 마지막 플레이 시간 갱신
    private void update_last_play() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    Calendar c = Calendar.getInstance();

                    String user_ID = Profile.getCurrentProfile().getName().toString().replaceAll(" ", "");
                    user_ID = URLEncoder.encode(user_ID,"UTF-8");

                    URL url = new URL(SERVER_ADDRESS + phpLoaction_last_play
                            + "id=" + user_ID + "&"
                            + "last_play_date=" + c.getTime().toString().replaceAll(" ", "-"));

                    HttpURLConnection con = null;
                    BufferedReader bufferedReader;
                    con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
            }
        });
    }
}


