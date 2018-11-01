package com.oops.myapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;

import com.oops.baiduvoice.BaiDuVoice;
import com.oops.baiduvoice.DataCallback;
import com.oops.duvoice.R;

import java.util.List;

public class MainActivity extends Activity implements DataCallback {
    protected Button btn1;
    protected TextView textView;
    //private Context mContext;

    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;

    BaiDuVoice baiDuVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.Btn1);
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btn1Click();
            }
        });
        textView = (TextView)findViewById(R.id.textView);
        init();

        acquireWakeLock(MainActivity.this);
    }

    void btn1Click()
    {
        textView.setText("btn 1clicl");
    }
    private void init() {

        baiDuVoice = BaiDuVoice.instance();
        baiDuVoice.init(MainActivity.this);
        baiDuVoice.dataCallback(this);
        startWakeUp("");

          mPostDelayed(7000);

    }

    //终止服务
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    //终止服务
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    public void startWakeUp(String json) {
        json = "{\"accept-audio-volume\":false,\"kws-file\":\"assets:\\/\\/\\/WakeUp.bin\"}";
        baiDuVoice.startWakeUp(json);
    }

    public void startASR(String json)
    {
        json = "{\"accept-audio-volume\":false,\"pid\":1536}";
        logTxt += json;
        baiDuVoice.startASR(json);
    }
    String logTxt;
    @Override
    public String callback(String s)//返回识别结果
    {
        String name="";
        String[] strs=s.split(";");
        if(strs.length >0)
        {
            name =strs[0];
            if(name.equals("wp.data") )
            {
                //baiDuVoice.screenOn();
              //  baiDuVoice.moveTaskToFront();
               // startASR("");
                moveTaskToFront2();
            }
        }
logTxt += s;
textView.setText(logTxt);
        return s;
    }

    void moveTaskToFront2()//唤醒后台到前台
    {
       // Log.i("aaaa","22222");
        if(isAppOnForeground() == false) {

            Intent resultIntent = new Intent(Intent.ACTION_MAIN); // 启动栈顶的activity
            resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resultIntent.setClass(this, MainActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        }

    }

    void moveTaskToFront()//唤醒后台到前台
    {
        if(isAppOnForeground() == false)
        {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE) ;
            am.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);
        }
    }

    public boolean isAppOnForeground() //判断程序是否前台
    {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    private void acquireWakeLock(Activity activity) {

        KeyguardManager km = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        if( km.isKeyguardLocked())//如果是锁屏状态
        {
            if (activity != null && activity.getWindow() != null)
            {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
              //  releaseWakeLock(MainActivity.this);
            }
        }


    }

    private void releaseWakeLock(Activity activity) {
        if (activity != null && activity.getWindow() != null) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }


    void screenOn()
    {
        KeyguardManager km = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        if( km.isKeyguardLocked())//如果是锁屏状态
        {
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            if (!pm.isScreenOn()) {
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
                wl.acquire();
                wl.release();




            }
        }
    }

void mPostDelayed(int delay)
{

    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
            /**
             *要执行的操作
             */
            textView.setText("fff22ff");
           // screenOn();
            //screenOn1();
           // acquireWakeLock(MainActivity.this);
            //releaseWakeLock(MainActivity.this);
            moveTaskToFront2();
          //  moveTaskToFront();

        }
    }, delay);//3秒后执行TimeTask的run方法


}


}
