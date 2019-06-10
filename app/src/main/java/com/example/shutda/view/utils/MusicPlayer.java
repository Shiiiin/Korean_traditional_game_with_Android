package com.example.shutda.view.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.shutda.R;
import com.google.android.gms.flags.Singletons;

public class MusicPlayer {

        private static MusicPlayer instance;
        private static MediaPlayer mp;


        private static SoundPool soundPool; //소리 효과음 코드^^
        private int die,half,check,call; // 소리 효과음 코드^^


        public static MusicPlayer getInstance(Context context){

            if(instance == null){
                instance = new MusicPlayer(context);
            }

            return instance;
        }


        public MusicPlayer(Context context){

        //배경음 코드
        mp = MediaPlayer.create(context, R.raw.bgm);
        mp.setLooping(true);
        mp.start();

        //효과음 시작

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();

        half = soundPool.load(context, R.raw.half,1);
        call = soundPool.load(context, R.raw.call,1);
        die = soundPool.load(context, R.raw.die,1);
        check = soundPool.load(context, R.raw.check,1);
        }//Constructor 끝


        public void halfsound(){
                soundPool.play(half,1,1,0,0,1);
        }

        public void callsound(){
            soundPool.play(call,1,1,0,0,1);
        }

        public void diesound(){
            soundPool.play(die,1,1,0,0,1);
        }

        public void checksound(){
            soundPool.play(check,1,1,0,0,1);
        }

        public static void MusicTurnOff(){
//            soundPool.release();
//            soundPool.autoPause();
//            soundPool = null;
        }

        public void MusicTurnOn(){

        }



}
