package com.example.lenovo.mydevelopment;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lenovo.mychessfive.MusicService;
import com.example.lenovo.mychessfive.MyService;
import com.example.lenovo.mychessfive.R;

/**
 * Created by lenovo on 2017/8/29.
 */

public class Multimedia extends AppCompatActivity implements View.OnClickListener {

    public static final String UPDATE_ACTION = "org.UPDATE_ACTION";
    public static final String CTL_ACTION = "org.CTL_ACTION";
    TextView title, author;
    Button audio_start_btn, audio_stop_btn, audio_pre_btn, audio_next_btn;
    int status = 0x11;//0x11代表没有播放，0x12代表正在播放，0x13代表暂停

    MusicReceiver musicReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_media);

        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }

        audio_init();

        //注册BroadcastReceiver
        musicReceiver = new MusicReceiver() ;
        IntentFilter filter = new IntentFilter(UPDATE_ACTION);
        registerReceiver(musicReceiver, filter);

        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
    }

    void audio_init() {
        audio_start_btn = (Button) findViewById(R.id.audio_start_btn);
        audio_stop_btn = (Button) findViewById(R.id.audio_stop_btn);
        audio_pre_btn = (Button) findViewById(R.id.audio_pre_btn);
        audio_next_btn = (Button) findViewById(R.id.audio_next_btn);
        audio_start_btn.setOnClickListener(this);
        audio_stop_btn.setOnClickListener(this);
        audio_pre_btn.setOnClickListener(this);
        audio_next_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(CTL_ACTION);
        switch (view.getId()) {
            case R.id.audio_start_btn:
                intent.putExtra("control", 1);
                break;
            case R.id.audio_stop_btn:
                intent.putExtra("control", 2);
                break;
            case R.id.audio_pre_btn:
                break;
            case R.id.audio_next_btn:
                break;
            default:
                break;
        }
        sendBroadcast(intent);
    }

    public class MusicReceiver extends BroadcastReceiver{//UPDATE_ACTION

        @Override
        public void onReceive(Context context, Intent intent) {
            int update = intent.getIntExtra("update", -1);
            int current = intent.getIntExtra("current", -1);
            if(current >= 0){

            }
            switch(update){
                case 0x11:
                    audio_start_btn.setText("Start");
                    status = 0x11;
                    break;
                case 0x12:
                    audio_start_btn.setText("Pause");
                    status = 0x12;
                    break;
                case 0x13:
                    audio_start_btn.setText("Start");
                    status = 0x13;
                    break;
            }
        }
    }
}
