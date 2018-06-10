package com.example.hoon.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by hoon on 2017-11-26.
 */

public class fragment_scoreboard extends Fragment{

    private ListView listView;
    private adapter_percentage ListAdapter;
    private users_info user_list;

    public fragment_scoreboard() {

    }

    public void setData(users_info temp) {
        user_list = temp;
        ListAdapter = new adapter_percentage(user_list, getActivity());
        listView.setAdapter(ListAdapter);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scoreboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView)view.findViewById(R.id.listview);
    }

    public void plusEvent() {
        user_info player = user_list.getPlayer();
        player.plusScore();
        ListAdapter.notifyDataSetChanged();
        listView.setAdapter(ListAdapter);
    }

    public void zeroEvent() {
        user_list.nextOrder();
        ListAdapter.notifyDataSetChanged();
        listView.setAdapter(ListAdapter);
    }

    public void minusEvent() {
        user_info player = user_list.getPlayer();
        player.minusScore();
        user_list.nextOrder();
        ListAdapter.notifyDataSetChanged();
        listView.setAdapter(ListAdapter);
    }

    public void prevEvent() {
        user_list.prevOrder();
        ListAdapter.notifyDataSetChanged();
        listView.setAdapter(ListAdapter);
    }
    //여기서 어떤 method()를 만들어서 상위 activity에서 자신의 view안의 있는 fragment의 참조를 얻어서 method call()을 해야할 것 같다.
}
