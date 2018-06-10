package com.example.hoon.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void GoSingleMode(View view) //TODO
    {
        Intent intent = new Intent(this, individual_counter.class);
        startActivity(intent);
    }

    public void GoTeamMode(View view)   //TODO
    {

    }

    public void ShowHelper(View view)   //TODO
    {

    }
}
