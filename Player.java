package com.example.tuhv.sdreadmusic;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
///////////////////////////////////////////////////////////////////////////////
////Note chưa là được tự chuyển bài xem lại Run và mp.setonComblectlistener////
//////////////////////////////////////////////////////////////////////////////

public class Player extends AppCompatActivity implements View.OnClickListener{
    static MediaPlayer mp;
    ArrayList<File> nhaccuatoi;

    SeekBar sb;
    Thread updateSeekBar;
    Button btPlay, btFF, btFB, btNxt, btPv;

    int position;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btPlay = (Button)findViewById(R.id.btPlay);
        btFB = (Button)findViewById(R.id.btFB);
        btFF = (Button)findViewById(R.id.btFF);
        btNxt = (Button)findViewById(R.id.btNxt);
        btPv = (Button)findViewById(R.id.btPv);

        btPlay.setOnClickListener(this);
        btPv.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btNxt.setOnClickListener(this);
        btFB.setOnClickListener(this);

        sb = (SeekBar)findViewById(R.id.seekBar);
        //chạy ứng dụng

        updateSeekBar = new Thread(){
            @Override
            public void run() {
                int totalDuration = mp.getDuration();
                int currentPosition = 0;

                while (currentPosition < totalDuration){

                    try {
                        sleep(700);
                        currentPosition = mp.getCurrentPosition();
                        sb.setProgress(currentPosition);
                        if(currentPosition == totalDuration && currentPosition == mp.getCurrentPosition()){
                            mp.release();
                            position = (position+1)%nhaccuatoi.size();
                            uri = Uri.parse(nhaccuatoi.get(position).toString());
                            mp = MediaPlayer.create(getApplicationContext(), uri);
                            mp.start();
                            sb.setMax(mp.getDuration());
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sb.setMax(mp.getDuration());
                }
                super.run();
            }
        };
        if (mp!=null){
            mp.stop();
            mp.release();
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        nhaccuatoi = (ArrayList) b.getParcelableArrayList("danhsachnhac");
        position = b.getInt("pos", 0);

        uri = Uri.parse(nhaccuatoi.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), uri);
        mp.start();
        //chạy khi hoàn thành
        //chưa hiểu về vấn đề này

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                try {
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                uri = Uri.parse(nhaccuatoi.get(position + 1).toString());
                mp = MediaPlayer.create(getApplicationContext(), uri);
                mp.start();
                sb.setMax(mp.getDuration());

            }
        });
        sb.setMax(mp.getDuration());
        updateSeekBar.start();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mp.seekTo(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                mp.seekTo(seekBar.getProgress());

            }
        });


    }
    //
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btPlay:
                if (mp.isPlaying()){
                    btPlay.setText(">");
                    mp.pause();
                }else{
                    btPlay.setText("||");
                    mp.start();
                }
                break;
            case R.id.btFF:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.btFB:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.btNxt:
                mp.stop();
                mp.release();
                position = (position+1)%nhaccuatoi.size();
                uri = Uri.parse(nhaccuatoi.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), uri);
                mp.start();
                sb.setMax(mp.getDuration());
                break;
            case R.id.btPv:
                mp.stop();
                mp.release();
                position = (position-1< 0)? nhaccuatoi.size():position -1;
//                if (position-1 <0){
//                    position = nhaccuatoi.size() - 1;
//                }else {
//                    position = position -1;
//                }

                Uri uri = Uri.parse(nhaccuatoi.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(),uri);
                mp.start();
                sb.setMax(mp.getDuration());
                break;

        }

    }
}
