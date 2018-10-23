package com.oops.androidlibrary;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class TestAndroidNative {

    private static TestAndroidNative _instance;
    private static Context unityContext;
    private static Activity unityActivity;

    public static TestAndroidNative instance()
    {
        if(_instance == null)
            _instance = new TestAndroidNative();
        return _instance;
    }
    // 初始化方法，用来传入上下文
    // 这里传入 UnityPlayerActivity
    public void init(Context _context)
    {
        unityContext = _context.getApplicationContext();
        unityActivity = (Activity)_context;
    }

    public String test(String str)
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showToast("wakelock");
                acquireWakeLock();
              //  releaseWakeLock();

            }
        }, 10000);//3秒后执行TimeTask的run方法
        return "From android >>>> " + str;
    }

    public static void showToast(final String msg)
    {
        unityActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(unityContext,msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void acquireWakeLock( ) //唤醒屏幕
    {
        KeyguardManager km = (KeyguardManager) unityActivity.getSystemService(Context.KEYGUARD_SERVICE);
        final Window win = unityActivity.getWindow();


        if( km.isKeyguardLocked())//如果是锁屏状态
        {
            if (unityActivity != null && win != null)
            {
                win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
              //  releaseWakeLock( );
            }
        }

    }

    public void releaseWakeLock( )
    {
        if (unityActivity != null && unityActivity.getWindow() != null) {
            unityActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

  public  void screenOn()
    {
        KeyguardManager km = (KeyguardManager) unityActivity.getSystemService(Context.KEYGUARD_SERVICE);
     //   if( km.isKeyguardLocked())//如果是锁屏状态
     //   {
            PowerManager pm = (PowerManager) unityActivity.getSystemService(Context.POWER_SERVICE);
            if (!pm.isScreenOn()) {
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
                wl.acquire();
                wl.release();
            }
      //  }


    }
}
