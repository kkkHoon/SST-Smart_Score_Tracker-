package com.example.hoon.myapplication;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.os.SystemClock.elapsedRealtime;

/**
 * Created by hoon on 2017-11-26.
 */

public class users_info implements Parcelable {

    private ArrayList<user_info> user_list = new ArrayList<>();
    private ArrayList<user_info> order_list = new ArrayList<>();
    private user_info player;
    private int order = 0;   // 0 ~ n-1
    private int length = 0;  // 1 ~ n

    public int getLength() {
        return length;
    }

    public int getOrder() { return order; }

    public void addUser(user_info user) {
        if(order_list.size() == 0) {
            player = user;
            player.setTurn(true);
            //player.startTimer();  //It does not work for first player TT
        }
        user_list.add(user);
        order_list.add(user);
        length += 1;
    }

    public void deleteUser(user_info user){  // maybe it would not be used, just to match (add - delete concept)
        if(order_list.contains(user)){
            user_list.remove(user);
            order_list.remove(user);
            length -= 1;
        }
    }

    public user_info getUser(int position) throws Exception{
        if (position >= 0 && position < user_list.size())
            return user_list.get(position);
        throw new Exception();
    }

    public user_info getPlayer() {
        return player;
    }

    public void finish_user(user_info user){
        if(order_list.contains(user))
            order_list.remove(user);
    }

    public void nextOrder() {
        if (player != null) {
            long current_time = elapsedRealtime();

            player.setTurn(false);
            player.setStop_time(current_time);

            int position = order_list.indexOf(player);
            player = (user_info) ((length == (position + 1)) ? order_list.get(0) : order_list.get(position + 1));
            player.setTurn(true);
            player.setBase_value(player.getBase_value() +(elapsedRealtime() - player.getStop_time()));
        }
    }


    public void prevOrder() {
        if (player != null) {
            long current_time = elapsedRealtime();

            player.setTurn(false);
            player.setStop_time(current_time);

            int position = order_list.indexOf(player);
            player = (user_info) ((position == 0) ? order_list.get(length - 1) : order_list.indexOf(position - 1));
            player.setTurn(true);
            player.setBase_value(player.getBase_value() +(elapsedRealtime() - player.getStop_time()));
        }
    }

    public users_info() {}

    protected users_info(Parcel in) {
        in.readTypedList(user_list,user_info.CREATOR);
        in.readTypedList(order_list,user_info.CREATOR);
        in.readParcelable(user_info.class.getClassLoader());
        in.readInt();
        in.readInt();
    }  //TODO

    public static final Creator<users_info> CREATOR = new Creator<users_info>() {
        @Override
        public users_info createFromParcel(Parcel in) {
            return new users_info(in);
        }

        @Override
        public users_info[] newArray(int size) {
            return new users_info[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(user_list);
        parcel.writeTypedList(order_list);
        parcel.writeParcelable(player, 0);
        parcel.writeInt(order);
        parcel.writeInt(length);
    }
}
