package com.example.lenovo.mychessfive;

import android.app.IntentService;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by lenovo on 2017/8/28.
 */

public class MyIntentService extends IntentService{

    public MyIntentService(String name) {
        super(name);
    }
    public MyIntentService(){
        super("MyIntentService");
    }
    //IntentService会使用单独的线程来执行该方法的代码
    @Override
    protected void onHandleIntent(Intent intent) {
        //该方法内可以执行任何耗时任务，比如下载文件等，此处只是让线程暂停20s
        long endTime = System.currentTimeMillis() + 20 * 1000;
        Log.i("yang", "onHandleIntent");
        while(System.currentTimeMillis() < endTime){
            synchronized (this){//同步
                try {
                    wait(endTime - System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i("yang", "--耗时任务完成--");
    }

    @Override
    public void onDestroy() {//关闭服务时会被调用，但关闭服务后onHandleIntent不会终止
        super.onDestroy();
        Log.i("yang", "onDestroy MyIntentService");
    }
}
