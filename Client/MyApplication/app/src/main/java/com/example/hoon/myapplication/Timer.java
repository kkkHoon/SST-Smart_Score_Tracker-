package com.example.hoon.myapplication;

import android.content.Context;
import android.util.Log;
import android.widget.Chronometer;

import static android.os.SystemClock.elapsedRealtime;

/**
 * Created by hoon on 2017-12-12.
 */

public class Timer extends Chronometer{

    private long start_time = 0;
    private long stop_time = 0;

    public Timer(android.content.Context context) { super(context); }

    public Timer(android.content.Context context, android.util.AttributeSet attrs) { super(context,attrs); }

    public Timer(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) { super(context,attrs,defStyleAttr); }

    //public Timer(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) { super(context,attrs,defStyleAttr,defStyleRes); }

    public void startTimer(){
        Log.d("bbb","MyView is not null!");
        stopTimer();
        setBase(start_time + (elapsedRealtime() - stop_time));
        start_time = getBase();
        start();
    }

    public void stopTimer(){
        stop_time = elapsedRealtime();
        stop();
    }
}
