package com.example.lenovo.mychessfive;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Timer;
import java.util.TimerTask;

public class TwoActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView iv;
    Button btn1, btn2, btn3, btn4;
    MyImage img = new MyImage();
    TextView tv;
    public MyImage fun1() {
        return img;
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            int i = (int) msg.obj;
            if (i == 2) {
                img.index_inc();
                change_img();
            }
        }
    }

    private void change_img(){
        iv.setImageResource(img.get_imgid());//int resId
        tv.setText(""+img.get_index());
    }

    Handler handler;
    ProgressBar progress;
    int prog = 0;
    SharedPreferences prefer2;
    SharedPreferences.Editor edit2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        handler = new MyHandler();
        progress = (ProgressBar) findViewById(R.id.progressBar2);
        progress.setMax(100);
        progress.setProgress(20);

        prefer2 = getSharedPreferences("myshare", MODE_PRIVATE);
        edit2 = prefer2.edit();

        tv = (TextView) findViewById(R.id.num_tv);
        iv = (ImageView) findViewById(R.id.girl_iv);
//        timer = new Timer();
        SeekBar sb = (SeekBar) findViewById(R.id.light_sb);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i == 100)
                    i = 125;
                iv.setImageAlpha(2 * i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (prog != 100)
                    prog += 10;
                progress.setProgress(prog);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                Toast.makeText(TwoActivity.this, "progress:" + progress, Toast.LENGTH_SHORT).show();
            }
        });
        init_btn();
    }

    private void init_btn() {
        btn1 = (Button) findViewById(R.id.play_btn);
        btn2 = (Button) findViewById(R.id.stop_btn);
        btn3 = (Button) findViewById(R.id.pre_btn);
        btn4 = (Button) findViewById(R.id.next_btn);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        Button share_read_btn, share_write_btn;
        share_read_btn = (Button) findViewById(R.id.share2_read_btn);
        share_write_btn = (Button) findViewById(R.id.share2_write_btn);
        share_read_btn.setOnClickListener(this);
        share_write_btn.setOnClickListener(this);
    }

    MyThread th = new MyThread();
    Timer timer = null;//为后续判断做准备
    Integer timer_stop;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_btn:
                if (timer == null) {//不判断的话，每次点击都会开一个定时器发消息，图片切换速度每次加倍
                    timer = new Timer();
                    timer_stop = 0;
                    th.start();//开启线程 暂时用不到
                    timer.schedule(new MyTimerTask(timer, img, handler, timer_stop), 0, 200);
                }
                break;
            case R.id.stop_btn:
                if (timer != null) {//不判断的话，直接点击会死机
                    timer.cancel();//停止定时器   还应该在退出当前应用在取消一次
                    timer.purge();//清除定时器
                    timer = null;
                }
                break;
            case R.id.pre_btn:
                //iv.setImageDrawable(getDrawable(R.mipmap.ic_launcher));//Drawable rawable
                //iv.setImageURI(Uri.parse("drawable/abc.png"));//Uri uri
                img.index_dec();
                change_img();
                Toast.makeText(TwoActivity.this, "R.drawable.abc:" + img.get_imgid(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.next_btn:
                img.index_inc();
                change_img();
                Toast.makeText(TwoActivity.this, "R.drawable.tu1:" + img.get_imgid(), Toast.LENGTH_SHORT).show();
                break;

            case R.id.share_read_btn:
                int num = prefer2.getInt("random", 0);//读取数据
//        Toast.makeText(MainActivity.this, "num:" + num, Toast.LENGTH_SHORT).show();
                Log.i("yang", "share2 read num:" + num);

                break;
            case R.id.share_write_btn:
                int a = (int) (Math.random() * 100);
                edit2.putInt("random", a);//写入数据
                edit2.commit();//提交写入的数据
                Log.i("yang", "share2 write num:" + a);
                break;
        }
    }
}

class MyTimerTask extends TimerTask {
    private Timer timer;
    private MyImage img;
    private Handler handler = null;
    private Integer timer_stop;

    MyTimerTask(Timer timer, MyImage img, Handler handler, Integer timer_stop) {
        this.timer = timer;
        this.img = img;
        this.handler = handler;
        this.timer_stop = timer_stop;
    }

    @Override
    public void run() {
        if (timer_stop == 1)
            timer.cancel();//即便写在run方法的开头，后面的代码也会执行完后才会停止定时器
        Message msg = new Message();
        msg.obj = 2;
        handler.sendMessage(msg);
    }
}

class MyThread extends Thread {

    MyThread() {

    }

    @Override
    public void run() {
        //super.run();
    }
}

class MyImage {
    private int[] img_buff = new int[]{R.drawable.abc, R.drawable.tu1};
    private int img_index = 0;

    public int get_imgid() {
        return img_buff[img_index];
    }

    public void index_inc() {
        if (img_index >= img_buff.length - 1)
            img_index = 0;
        else
            img_index++;
    }

    public void index_dec() {
        if (img_index <= 0)
            img_index = img_buff.length - 1;
        else
            img_index--;
    }

    public int get_index() {
        return img_index;
    }

    public void set_index(int index) {
        this.img_index = index;
    }
}

