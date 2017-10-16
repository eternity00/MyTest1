package com.example.lenovo.mychessfive;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by lenovo on 2017/7/31.
 */
public class MySqlite extends SQLiteOpenHelper{


    public MySqlite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.i("yang","MySqlite new");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("yang","MySqlite create");
        String aa = " table";
        String creaTTable = "create" + aa +" stu (id integer PRIMARY KEY AUTOINCREMENT NOT NULL,age integer)";
        sqLiteDatabase.execSQL(creaTTable);
        //String creaTTable = "create table user (_id integer PRIMARY KEY AUTOINCREMENT NOT NULL,name varchar,age int)";
        //db.execSQL(creaTTable);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.i("yang","MySqlite open");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i("yang","MySqlite update");
    }
}
