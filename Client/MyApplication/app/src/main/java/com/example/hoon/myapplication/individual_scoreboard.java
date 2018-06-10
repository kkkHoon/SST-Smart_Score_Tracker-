package com.example.hoon.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URI;

import static android.os.SystemClock.elapsedRealtime;

/**
 * Created by hoon on 2017-11-26.
 */

public class individual_scoreboard extends AppCompatActivity {

    Handler handler = new Handler();
    private static final int EXIT = 0;
    private static final int TIMESET = 1;

    private Handler timer_handler;
    TextView timerText;
    TextView moneyText;
    fragment_scoreboard fragment_in_view;
    users_info test;
    int count;
    final int minue_price = 100;

    //For streaming service
    String VideoURL = "rtsp://192.168.0.15:9000/";
    Uri video;
    Dialog streamingDialog;
    VideoView videoView;
    Button close_btn;
    View.OnClickListener btn_listener;
    MediaController controller;
    MediaPlayer.OnPreparedListener media_listener;

    // For event listener information
    Socket socket;
    String IP = "192.168.0.15";
    int PORT = 5555;
    BufferedReader reader;
    BufferedWriter writer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_scoreboard);
        timerText = (TextView)findViewById(R.id.timer_text);
        moneyText = (TextView) findViewById(R.id.money_text);
        fragment_in_view = (fragment_scoreboard)getFragmentManager().findFragmentById(R.id.container);

        Intent intent = getIntent();
        count = intent.getIntExtra("count",1);
        makeTestInput();

        // manual order changing buttons for exception error
        ImageButton prev_btn = (ImageButton)findViewById(R.id.prev_btn);
        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_in_view.prevEvent();
            }
        });
        ImageButton next_btn = (ImageButton)findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_in_view.zeroEvent();
            }
        });

        // Timer for game(how long they played game)
        timerThread timer = new timerThread();
        timer.setDaemon(true);
        timer.start();
        A_Minute();

        // Initialize setting for streaming service
        init_streamingService();
    }

    private void makeTestInput() {
        //temporary test inpt data
        test = new users_info();
        user_info[] test_case = new user_info[6];

        user_info temp1 = new user_info();
        temp1.setMaxScore(20);
        temp1.setName("KIM");
        test_case[0] = temp1;
        user_info temp2 = new user_info();
        temp2.setMaxScore(5);
        temp2.setName("LEE");
        test_case[1] = temp2;
        user_info temp3 = new user_info();
        temp3.setMaxScore(10);
        temp3.setName("AHN");
        test_case[2] = temp3;
        user_info temp4 = new user_info();
        temp4.setMaxScore(15);
        temp4.setName("YOU");
        test_case[3] = temp4;
        user_info temp5 = new user_info();
        temp5.setMaxScore(30);
        temp5.setName("HWANG");
        test_case[4] = temp5;
        user_info temp6 = new user_info();
        temp6.setMaxScore(10);
        temp6.setName("CHOI");
        test_case[5] = temp6;

        for(int k=0; k<count; k++){
            long time = elapsedRealtime();
            test_case[k].setStart_time(time);
            test_case[k].setStop_time(time);
            test_case[k].setBase_value(time);
            test.addUser(test_case[k]);
        }
        //temporary test input data end!
        fragment_in_view.setData(test);
    }
    @Override
    protected void onStart() {
        super.onStart();

        Thread connectToServer = new Thread(connect_runnable);
        connectToServer.start();

        try{
            connectToServer.join();
        }catch (InterruptedException e) { e.printStackTrace(); }

        if(socket != null && socket.isConnected()){
            EventListnerThread listnerThread = new EventListnerThread();
            listnerThread.start();
        }
        else{
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer_handler != null){
            Message msg = timer_handler.obtainMessage(EXIT);
            timer_handler.sendMessage(msg);
        }
    }

    private void init_streamingService(){
        streamingDialog = new Dialog(this);
            streamingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            streamingDialog.setContentView(R.layout.streaming_popup);
            videoView = (VideoView) streamingDialog.findViewById(R.id.videoView);
            close_btn = (Button) streamingDialog.findViewById(R.id.close);
                btn_listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        streamingDialog.dismiss();
                    }
                };
            close_btn.setOnClickListener(btn_listener);

        controller = new MediaController(this);
        try{
            controller.setAnchorView(videoView);
            video = Uri.parse(VideoURL);
            videoView.setMediaController(controller);
            videoView.setVideoURI(video);
            videoView.requestFocus();
                media_listener = new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        videoView.start();
                    }
                };
            videoView.setOnPreparedListener(media_listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void streaming_start(View view){
        streamingDialog.show();
    }

    private void A_Minute(){
        if (timer_handler != null){
            Message msg = timer_handler.obtainMessage(TIMESET);
            timer_handler.sendMessage(msg);
        }
    }

    public class timerThread extends Thread{

        private int hour, min;

        public timerThread() {
            this.hour = 0;
            this.min = 0;

            timer_handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case EXIT:
                            timer_handler.getLooper().quit();
                            break;
                        case TIMESET:
                            if (min == 59){
                                hour += 1;
                                min = 0;
                            }
                            else
                                min += 1;
                            moneyText.setText( ((60*hour + min) * minue_price) + " 원");
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    String value = String.format("%02d",hour) + " : " + String.format("%02d",min);
                                    timerText.setText(value);
                                    A_Minute();
                                }
                            },60000);
                            break;
                    }
                }
            };
        }

        @Override
        public void run() {
            Looper.prepare();
            Looper.loop();
        } // run() end
    }//timer Thread class end

    private Runnable connect_runnable = new Runnable() {
        @Override
        public void run() {
            try{
                socket = new Socket(IP,PORT);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException e) { e.printStackTrace();}
        }
    };

    public class EventListnerThread extends Thread {
        @Override
        public void run() {
            while(true){
                try {
                    String msg = reader.readLine();
                    if (msg.equals("Event A")){      // 잘 쳤다.
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                fragment_in_view.plusEvent();
                            }
                        });
                    }
                    else if (msg.equals("Event B")){    // 못 쳤다.
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                fragment_in_view.zeroEvent();
                            }
                        });
                    }
                    else if (msg.equals("Event C")){    // 패널티
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                fragment_in_view.minusEvent();
                            }
                        });
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Who are you...!", Toast.LENGTH_LONG).show();
                } catch (IOException e) {e.printStackTrace();}
            }
        }
    }
}
