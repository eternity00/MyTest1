package com.example.lenovo.mychessfive;

import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lenovo.mydevelopment.Multimedia;

import java.util.Vector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String CRAZYIT_ACTION = "android.intent.action.MAIN";

    SharedPreferences prefer;
    SharedPreferences.Editor edit;
    Button share_read_btn, share_write_btn, start_service_btn, stop_service_btn;

    ContentResolver contentResolver;
    Uri uu = Uri.parse("content://myprovider.dictprovider/words");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("yang", "MainActivity create");
        contentResolver = getContentResolver();
//        insert();
//        query();
//        delete();

        start_service_btn = (Button) findViewById(R.id.start_service_btn);
        stop_service_btn = (Button) findViewById(R.id.stop_service_btn);
        Button boardcast_btn = (Button) findViewById(R.id.boardcast_btn);
        boardcast_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("org.MYBROADCAST");
                intent.putExtra("msg", "第一条广播");
                intent.putExtra("msg2", "第2条广播");
                //sendBroadcast(intent);//发送普通广播，即无序广播
                sendOrderedBroadcast(intent, null);//发送有序广播，可设置权限，即优先级
            }
        });
        share_init();

        Button jump_pic = (Button) findViewById(R.id.pic_btn);
        jump_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();//一个Intent对象可以包括一个Action属性和多个Category属性
                intent.setClass(MainActivity.this, TwoActivity.class);
                //intent.setAction(MainActivity.CRAZYIT_ACTION);
                startActivity(intent);
            }
        });
        Button media_scrn_btn = (Button) findViewById(R.id.media_scrn_btn);
        media_scrn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();//一个Intent对象可以包括一个Action属性和多个Category属性
                intent.setClass(MainActivity.this, Multimedia.class);
                //intent.setAction(MainActivity.CRAZYIT_ACTION);
                startActivity(intent);
            }
        });

        //final Intent in = new Intent(this, MyService.class);//有耗时任务会异常
        final Intent in = new Intent(this, MyIntentService.class);//耗时任务事件
        in.setAction("chess.myservice");
        start_service_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(in);//service onCreate -> service onStartCommand
            }
        });
        stop_service_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(in);//service onDestroy
            }
        });
        click_init();
        service_bind();
    }

    MyService.MyBinder binder;
    private ServiceConnection conn = new ServiceConnection() {
        @Override//当activity与service连接成功时回调该方法
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("yang", "service connect");
            binder = (MyService.MyBinder) iBinder;
        }

        @Override//当activity与service断开连接时回调该方法
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("yang", "service disconnect");//当Service服务被异外销毁时调用，例如内存的资源不足时
        }
    };

    void service_bind() {//如果不绑定服务，那么就无法与其它应用程序通信
        Button bind, unbind, getServiceStatus;
        bind = (Button) findViewById(R.id.bind_btn);
        unbind = (Button) findViewById(R.id.unbind_btn);
        getServiceStatus = (Button) findViewById(R.id.getServiceStatus_btn);
        final Intent intent = new Intent(this, MyService.class);
        bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("yang", "bind");
                bindService(intent, conn, Service.BIND_AUTO_CREATE);//service onCreate -> service onBind -> service connect
            }
        });
        unbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("yang", "unbind");
                unbindService(conn);//service onUnbind -> service onDestroy
            }
        });
        getServiceStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.i("yang", "count:" + binder.getCount());
                } catch (Exception e) {
                    Log.i("yang", "getServiceStatus");
                }
            }
        });
    }

    private void click_init() {
        Button insert2 = (Button) findViewById(R.id.insert2_btn);
        Button delete2 = (Button) findViewById(R.id.delete2_btn);
        Button update2 = (Button) findViewById(R.id.update2_btn);
        Button check2 = (Button) findViewById(R.id.check2_btn);
        insert2.setOnClickListener(this);
        delete2.setOnClickListener(this);
        update2.setOnClickListener(this);
        check2.setOnClickListener(this);
        Button insert = (Button) findViewById(R.id.insert_btn);
        Button delete = (Button) findViewById(R.id.delete_btn);
        Button update = (Button) findViewById(R.id.update_btn);
        Button check = (Button) findViewById(R.id.check_btn);
        insert.setOnClickListener(this);
        delete.setOnClickListener(this);
        update.setOnClickListener(this);
        check.setOnClickListener(this);

        share_read_btn = (Button) findViewById(R.id.share_read_btn);
        share_write_btn = (Button) findViewById(R.id.share_write_btn);
        share_read_btn.setOnClickListener(this);
        share_write_btn.setOnClickListener(this);
    }

    void share_init() {
        /*  mode为操作模式，默认的模式为0或MODE_PRIVATE，还可以使用MODE_WORLD_READABLE和MODE_WORLD_WRITEABLE
mode指定为MODE_PRIVATE，则该配置文件只能被自己的应用程序访问。
mode指定为MODE_WORLD_READABLE，则该配置文件除了自己访问外还可以被其它应该程序读取。
mode指定为MODE_WORLD_WRITEABLE，则该配置文件除了自己访问外还可以被其它应该程序读取和写入*/
        prefer = getSharedPreferences("myshare", MODE_PRIVATE);
        edit = prefer.edit();
    }

    void share_write() {
        int a = (int) (Math.random() * 100);
        edit.putInt("random", a);//写入数据
        edit.commit();//提交写入的数据
        Log.i("yang", "share1 write num:" + a);
    }

    void share_read() {
        int num = prefer.getInt("random", 0);//读取数据
//        Toast.makeText(MainActivity.this, "num:" + num, Toast.LENGTH_SHORT).show();
        Log.i("yang", "share1 read num:" + num);
    }

    void insert2() {//2为直接操作数据库
        String creaTTable2 = "create table stu2 (id integer PRIMARY KEY AUTOINCREMENT NOT NULL,name char(20))";
        SQLiteDatabase db2 = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/MyDict2.db3", null);//存在则打开，不存在则创建
        try {
            db2.execSQL(creaTTable2);
        } catch (SQLiteException se) {
            System.out.println("sql create");
        }
        try {
            db2.execSQL("insert into stu2 values(null, 'lisi5')");
        } catch (SQLiteException se) {
            System.out.println("sql insert");
        }
        db2.close();//
    }

    void insert() {//通过ContentProvider操作数据库
        ContentValues v = new ContentValues();
        Integer a = new Integer(18);
        v.put("age", a);
        Uri u = contentResolver.insert(uu, v);
        Log.i("yang", "insert:" + u);
        Toast.makeText(MainActivity.this, "insert:" + a, Toast.LENGTH_SHORT).show();
    }

    void query2() {
        SQLiteDatabase db2 = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/MyDict2.db3", null);
        Cursor c = db2.rawQuery("select * from stu2 where id <= ?", new String[]{"6"});
        String s;
        Vector<Object> v = new Vector<>();

        while (c.moveToNext()) {
            int id1 = c.getInt(c.getColumnIndex("id"));
            String name1 = c.getString(c.getColumnIndex("name"));
            s = "id:" + id1 + ",name:" + name1;
            v.add(s);
            Log.i("db", "id:" + id1 + ",name:" + name1);
        }
        for (int i = 0; i < v.size(); i++) {
            String m = (String) v.get(i);
            Log.i("list", m);
        }
    }

    void query() {
        Cursor c = contentResolver.query(uu, new String[]{"age"}, null, null, null);
        Log.i("yang", "query c:" + c);//System.out: Cursor:android.content.ContentResolver$CursorWrapperInner@41cef310
        String s;
        Vector<Object> v = new Vector<>();

        while (c.moveToNext()) {
            int id1 = c.getInt(c.getColumnIndex("id"));
            String age = c.getString(c.getColumnIndex("age"));
            s = "id:" + id1 + ",age:" + age;
            v.add(s);
            Log.i("yang", "id:" + id1 + ",age:" + age);
        }
        for (int i = 0; i < v.size(); i++) {
            String m = (String) v.get(i);
            Log.i("yang", m);
        }
    }

    void delete2() {
        SQLiteDatabase db2 = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/MyDict2.db3", null);
        db2.execSQL("delete from stu2 where id>15");
//        int count = contentResolver.delete(uu, "dict", null);
//        Toast.makeText(MainActivity.this, "count:"+count, Toast.LENGTH_SHORT).show();
    }

    void delete() {//待研究
        String[] s = new String[]{"id", "2"};
        String where = "stu";
        Log.i("yang", "delete id:" + s[1]);
        int count = contentResolver.delete(uu, where, s);
        Toast.makeText(MainActivity.this, "count:" + count, Toast.LENGTH_SHORT).show();
    }

    void update2() {
        SQLiteDatabase db2 = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/MyDict2.db3", null);
        db2.execSQL("update stu2 set name='zhang5' where id=3");
    }

    void update() {
        ContentValues v = new ContentValues();
        Integer a = new Integer(18);
        v.put("age", a);
        v.get("1");
        String[] s = new String[]{"age", "33"};
        contentResolver.update(uu, v, "stu", s);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int num = prefer.getInt("random", 0);//读取数据
        Toast.makeText(MainActivity.this, "num:" + num, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.insert2_btn:
                insert2();
                break;
            case R.id.delete2_btn:
                delete2();
                break;
            case R.id.update2_btn:
                update2();
                break;
            case R.id.check2_btn:
                query2();
                break;
            case R.id.insert_btn:
                insert();
                break;
            case R.id.delete_btn:
                delete();
                break;
            case R.id.update_btn:
                update();
                break;
            case R.id.check_btn:
                query();
                break;

            case R.id.share_read_btn:
                share_read();
                break;
            case R.id.share_write_btn:
                share_write();
                break;
        }
    }
}


