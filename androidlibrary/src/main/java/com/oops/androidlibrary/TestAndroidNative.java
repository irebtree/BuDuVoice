package com.oops.androidlibrary;

import android.app.Activity;
import android.content.Context;
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
    public static void init(Context _context)
    {
        unityContext = _context.getApplicationContext();
        unityActivity = (Activity)_context;
    }

    public String test(String str)
    {
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
}
