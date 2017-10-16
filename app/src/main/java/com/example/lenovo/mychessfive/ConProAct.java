package com.example.lenovo.mychessfive;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.UserDictionary;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Vector;

public class ConProAct extends ContentProvider {

    private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int WORDS = 1;
    private static final int WORD = 2;
    private MySqlite dbOpenHealper;

    static {
        matcher.addURI("myprovider.dictprovider", "words", WORDS);//
        matcher.addURI("myprovider.dictprovider", "word/#", WORD);
    }

    @Override
    public boolean onCreate() {
        Log.i("yang", "ContentProvider create");
        dbOpenHealper = new MySqlite(this.getContext(), "MyDict.db", null, 1);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Log.i("yang", "query" + "uri:" + uri + "strings:" + strings + "s:" + s + "strings1:" + strings1 + "s1:" + s1);
        SQLiteDatabase db = dbOpenHealper.getReadableDatabase();
        switch (matcher.match(uri)){
            case WORDS:
                System.out.println("query words++");
                Cursor c = db.rawQuery("select * from stu where id <= ?", new String[]{"6"});
                return c;
                //return db.query("dict", strings, s, strings1, null, null, s1);
            case WORD:
                System.out.println("query word++");
                long id = ContentUris.parseId((uri));
                String whereclause = UserDictionary.Words._ID + "=" + id;
                if(s != null && !"".equals(s)){
                    whereclause = whereclause + " and " + s;
                }
                return db.query("dict", strings, whereclause, strings1, null, null, s1);
            default:
                System.out.println("query default++");
                throw new IllegalArgumentException("未知uri:" + uri);
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        System.out.println("getType");
        switch (matcher.match(uri)) {
            case WORDS:
                return "vnd.android.cursor.dir/org.crazyit.dict";
            case WORD:
                return "vnd.android.cursor.item/org.crazyit.dict";
            default:
                throw new IllegalArgumentException("未知uri:" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Log.i("yang", "ContentProvider insert uri:" + uri + "contentValues:" + contentValues);
        SQLiteDatabase db = dbOpenHealper.getReadableDatabase();//会调用onOpen函数

        switch (matcher.match(uri)){
            case WORDS:
                long rowId = db.insert("stu", "age", contentValues);
                if(rowId > 0){
                    Log.i("yang", "insert rowId:" + rowId);
                    Uri wordUri = ContentUris.withAppendedId(uri, rowId);//在已有的uri后面追加id
                    getContext().getContentResolver().notifyChange(wordUri, null);//通知数据已经改变
                    return wordUri;
                }
                break;
            case WORD:
                Log.i("yang","word");
                break;
            default:
                throw new IllegalArgumentException("未知uri:" + uri);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        Log.i("yang", "ContentProvider delete");
        SQLiteDatabase db = dbOpenHealper.getReadableDatabase();

        String drop_table = "drop table dict";
        //db.execSQL(drop_table);
        int num = 0;
        switch(matcher.match(uri)){
            case WORDS:
                //num = db.delete("stu", s, strings);
                String a = "delete from " + s + " where " + strings[0] + "=" + strings[1];
                Log.i("yang", a);
                db.execSQL(a);
                break;
            case WORD:
                long id = ContentUris.parseId(uri);
                String whereClause = UserDictionary.Words._ID + "=" + id;

                if(s != null && !s.equals("")){
                    whereClause = whereClause + " and " + s;
                }
                num = db.delete("dict", whereClause, strings);
                break;
            default:
                throw new IllegalArgumentException("未知uri:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return num;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase db = dbOpenHealper.getWritableDatabase();
        int num = 0;
        switch (matcher.match(uri)){
            case WORDS:
                num = db.update("dict", contentValues, s, strings);
                break;
            case WORD:
                long id = ContentUris.parseId(uri);
                String whereClause = UserDictionary.Words._ID + "=" + id;
                if(s != null && !s.equals("")){
                    whereClause = whereClause + " and " + s;
                }
                num = db.update("dict", contentValues, whereClause, strings);
                break;
            default:
                throw new IllegalArgumentException("未知uri:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return num;
    }
}
