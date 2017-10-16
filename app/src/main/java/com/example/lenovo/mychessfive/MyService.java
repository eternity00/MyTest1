package com.example.lenovo.mychessfive;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by lenovo on 2017/8/23.
 */
public class MyService extends Service {
    private int count;
    private boolean quit;

    private MyBinder binder = new MyBinder();
    public class MyBinder extends Binder{
        public int getCount(){
            return count;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("yang", "service onBind");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("yang", "service onUnbind");
        return super.onUnbind(intent);
    }

    @Override//被创建时回调
    public void onCreate() {
        super.onCreate();
        Log.i("yang", "service onCreate");
        new Thread(){
            @Override
            public void run() {
                super.run();
                while(!quit){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                }
            }
        }.start();
    }

    @Override//被启动时回调
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("yang", "service onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override//Service被关闭之前回调
    public void onDestroy() {
        super.onDestroy();
        this.quit = true;
        Log.i("yang", "service onDestroy");
    }
}
