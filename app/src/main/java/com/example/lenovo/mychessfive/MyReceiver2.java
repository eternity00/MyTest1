package com.example.lenovo.mychessfive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by lenovo on 2017/9/6.
 */
public class MyReceiver2 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("yang", "onReceive2," + "Intent Action:" + intent.getAction() + " -- msg:" + intent.getStringExtra("msg"));
        Log.i("yang", "msg2:" + intent.getStringExtra("msg2"));
        Bundle b = getResultExtras(true);
        String first = b.getString("first");
        Log.i("yang", "Receiver2 first Broadcast 存入的消息:" + first);
    }
}
