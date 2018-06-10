package com.example.hoon.myapplication;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;

import static android.os.SystemClock.elapsedRealtime;

/**
 * Created by hoon on 2017-11-26.
 */

public class user_info implements Parcelable {

    private String user_name;
    private int user_score;
    private int user_max_score;

    private long start_time = 0;
    private long stop_time = 0;
    private long base_value = 0;
    private boolean end = false;
    private boolean turn = false;


    public user_info() {
        user_name = "noname";
        user_score = 0;
        user_max_score = 0;
    }

    protected user_info(Parcel in) {
        user_name = in.readString();
        user_score = in.readInt();
        user_max_score = in.readInt();

        start_time = in.readLong();
        stop_time = in.readLong();
        end = in.readByte() != 0;
        turn = in.readByte() != 0;
    }

    public static final Creator<user_info> CREATOR = new Creator<user_info>() {
        @Override
        public user_info createFromParcel(Parcel in) {
            return new user_info(in);
        }

        @Override
        public user_info[] newArray(int size) {
            return new user_info[size];
        }
    };

    public String getName() {
        return user_name;
    }
    public int getScore() {
        return user_score;
    }
    public int getMaxScore() {
        return user_max_score;
    }

    public void setName(String name) {
        user_name = name;
    }
    public void setScore(int score) {
        user_score = score;
    }
    public void setMaxScore(int score) {
        user_max_score = score;
    }

    public void plusScore() {
        if(!end) {
            user_score += 1;
            if (user_max_score == user_score)
                end = true;
        }
    }
    public void minusScore() {
        if(!end && user_score >= 1)
            user_score -= 1;
    }

    public boolean isEnd() { return end; }

    public void setTurn(boolean temp){
        if(turn != temp){
            turn = temp;
        }
    }

    public boolean getTurn() {
        return turn;
    }
    public void setStart_time(long time) { this.start_time = time; }
    public long getStop_time() { return this.stop_time; }
    public void setStop_time(long time) { this.stop_time = time; }
    public long getBase_value() { return  this.base_value; }
    public void setBase_value(long value) { this.base_value = value; }
    //public void setPlus_time(long time) { this.plus_time = time; }

    /*
    public void restart(){
        if(myView != null){
            Chronometer chronometer = (Chronometer)myView.findViewById(R.id.item_timer);
            chronometer.stop();
            chronometer.start();
        }
    }
    public void startTimer(){
        if (myView != null){
            Log.d("bbb","MyView is not null!");
            Chronometer chronometer = (Chronometer)myView.findViewById(R.id.item_timer);
            chronometer.setBase(start_time + (elapsedRealtime() - stop_time));
            start_time = chronometer.getBase();
            chronometer.start();
        }
    }

    public void stopTimer(){
        if(myView != null){
            Chronometer chronometer = (Chronometer)myView.findViewById(R.id.item_timer);
            stop_time = elapsedRealtime();
            chronometer.stop();
        }
    }
    */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(user_name);
        parcel.writeInt(user_score);
        parcel.writeInt(user_max_score);

        parcel.writeLong(start_time);
        parcel.writeLong(stop_time);
        parcel.writeByte((byte) (end ? 1 : 0));
        parcel.writeByte((byte) (turn ? 1 : 0));
    }
}
