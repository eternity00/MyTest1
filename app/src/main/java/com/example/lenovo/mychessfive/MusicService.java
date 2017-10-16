package com.example.lenovo.mychessfive;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.lenovo.mydevelopment.Multimedia;

import java.io.IOException;

/**
 * Created by lenovo on 2017/9/7.
 */
public class MusicService extends Service {

    MyReceiver3 serviceReceiver;
    AssetManager am;
    //String[] musics = new String[]{"wish.mp3", "promish.mp3", "beautiful.mp3"};
    int[] musics = new int[]{R.raw.wish, R.raw.promish, R.raw.beautiful};
    MediaPlayer mPlayer;
    int status = 0x11;//0x11代表没有播放，0x12代表正在播放，0x13代表暂停
    int current = 0;//记录当前正在播放的音乐

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("yang", "Music service");
        am = getAssets();
        serviceReceiver = new MyReceiver3();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Multimedia.CTL_ACTION);
        registerReceiver(serviceReceiver, filter);

        mPlayer = new MediaPlayer() ;
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                current++;
                if (current >= 3) {
                    current = 0;
                }
                Log.i("yang", "current:" + current);
                Intent in = new Intent(Multimedia.UPDATE_ACTION);
                in.putExtra("current", current);
                sendBroadcast(in);
                prepareAndPlay(musics[current]);
            }
        });
    }

    public class MyReceiver3 extends BroadcastReceiver {//CTL_ACTION

        @Override
        public void onReceive(Context context, Intent intent) {
            int control = intent.getIntExtra("control", -1);
            switch (control) {
                case 1://播放或暂停
                    if (status == 0x11) {
                        prepareAndPlay(musics[current]);
                        status = 0x12;
                    } else if (status == 0x12) {
                        mPlayer.pause();
                        status = 0x13;
                    } else if (status == 0x13) {
                        mPlayer.start();
                        status = 0x12;
                    }
                    break;
                case 2://停止声音
                    if (status == 0x12 || status == 0x13) {//如果原来正在播放或暂停
                        mPlayer.stop();
                        status = 0x11;
                    }
                    break;
            }
            Log.i("yang", "control status:" + status);
            Intent in = new Intent(Multimedia.UPDATE_ACTION);
            in.putExtra("update", status);
            in.putExtra("current", current);
            sendBroadcast(in);
        }
    }

    private void prepareAndPlay2(String music) {//有问题 错的
        try {
            Log.i("yang", "prepare try");
            //AssetFileDescriptor afd = am.openFd(music);//打开指定音乐文件
            Log.i("yang", "prepare 0");
            //mPlayer.reset();
            Log.i("yang", "prepare 1");
            //mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());//使用MediaPlayer加载指定的声音文件
            mPlayer.setDataSource(music);
            Log.i("yang", "prepare 2");
            mPlayer.prepare();//准备声音
            mPlayer.start();//播放
            Log.i("yang", "prepare");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepareAndPlay(int music) {
        mPlayer = MediaPlayer.create(this, music);
        mPlayer.start();
    }
}
