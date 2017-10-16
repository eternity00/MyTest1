package com.example.lenovo.mychessfive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        Log.i("yang", "onReceive," + "Intent Action:" + intent.getAction() + " -- msg:" + intent.getStringExtra("msg"));
        Log.i("yang", "msg2:" + intent.getStringExtra("msg2"));
        Bundle b = new Bundle() ;
        b.putString("first", "first BroadCastReceiver 存入的消息");
    }
}
