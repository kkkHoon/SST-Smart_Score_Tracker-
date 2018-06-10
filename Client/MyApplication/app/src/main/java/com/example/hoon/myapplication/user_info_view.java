package com.example.hoon.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by hoon on 2017-11-27.
 */

public class user_info_view extends AppCompatActivity {

    private user_info selected_user;
    private TextView name;
    private ImageButton log_btn;
    private ImageButton video_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_view);
        name = (TextView)findViewById(R.id.user_name);
        log_btn = (ImageButton)findViewById(R.id.history_btn);
        video_btn = (ImageButton)findViewById(R.id.play_btn) ;
        Bundle bundle = getIntent().getExtras();
        selected_user = bundle.getParcelable("user_info");

        set_view();
    }

    private void set_view(){
        name.setText(selected_user.getName());
        log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO connect with personal user's log class(not implemented yet!)
            }
        });
        video_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO connect with personal user's video class(not implemented yet!)
            }
        });
    }
}
