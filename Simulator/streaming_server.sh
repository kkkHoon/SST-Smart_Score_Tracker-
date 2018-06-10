#!/bin/sh

# basic RTSP streaming server script
# need vlc module(precondition)
# client can access video on raspberry pi IP adress and port 9000
# android application videoView class suport getting rtsp signal easily.
# To kill server, kill all cvlc: killall cvlc


sudo raspivid -n -w 1280 -h 720 -b 4500000 -fps 30 -vf -hf -t 0 -o - | cvlc -vvv stream:///dev/stdin --sout '#rtp{sdp=rtsp://:9000/}' :demux=h264




