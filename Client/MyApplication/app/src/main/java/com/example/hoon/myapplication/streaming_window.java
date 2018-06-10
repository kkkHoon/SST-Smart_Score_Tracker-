package com.example.hoon.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.VideoView;

/**
 * Created by hoon on 2018-04-21.
 */

public class streaming_window extends PopupWindow {

    View contents;
    VideoView videoview;
    Button closeBtn;
    String VideoURL = "rtsp://192.168.0.15:9000/";
    ProgressDialog pDialog;
    MediaPlayer.OnPreparedListener listener;
    MediaController mediaController;
    Uri video;

    public streaming_window(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        contents = contentView;

        pDialog = new ProgressDialog(contents.getContext());
        pDialog.setTitle("Streaming from Raspberry pi");
        pDialog.setMessage("Buferring...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);

        listener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                pDialog.dismiss();
                videoview.start();
            }
        };
    }

    public void setURL(String url){
        VideoURL = url;
    }
    public String getURL(){
        return VideoURL;
    }
    public void setView(){
        videoview = (VideoView)contents.findViewById(R.id.videoView);
        closeBtn = (Button)contents.findViewById(R.id.close);

        try{
            mediaController = new MediaController(contents.getContext());
            mediaController.setAnchorView(videoview);
            video = Uri.parse(VideoURL);
            videoview.setMediaController(mediaController);
            videoview.setVideoURI(video);
            videoview.setOnPreparedListener(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(View parent, int gravity, int x, int y){
        this.showAtLocation(parent, gravity, x, y);
    }

    public void showAtLocation(View parent, int gravity, int x, int y, Context above) {
        super.showAtLocation(parent, gravity, x, y);

        pDialog = new ProgressDialog(contents.getContext());
        pDialog.setTitle("Streaming from Raspberry pi");
        pDialog.setMessage("Buferring...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        try{
            mediaController = new MediaController(above);
            mediaController.setAnchorView(videoview);
            video = Uri.parse(VideoURL);
            videoview.setMediaController(mediaController);
            videoview.setVideoURI(video);
            videoview.setOnPreparedListener(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        videoview.requestFocus();
    }
}
