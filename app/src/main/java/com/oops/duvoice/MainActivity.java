package com.oops.duvoice;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.oops.baiduvoice.DataCallback;
import com.unity3d.player.UnityPlayerActivity;
import com.unity3d.player.UnityPlayer;
import com.oops.baiduvoice.BaiDuVoice;

public class MainActivity extends UnityPlayerActivity implements DataCallback {
    BaiDuVoice baiDuVoice;
    private boolean logTime = true;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        baiDuVoice = BaiDuVoice.instance();
        baiDuVoice.init(MainActivity.this);
        baiDuVoice.dataCallback(this);
/*
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

              //  screenOn();
            }
        }, 10000);
        */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baiDuVoice.release();

    }


    @Override
    public String callback(String s)//返回识别结果
    {
        recResult(s);
        return s;
    }

    public void startASR(String json)
    {
        baiDuVoice.startASR(json);
    }

    public void stopASR() {
        baiDuVoice.stopASR();
    }

    public void cancelASR(){baiDuVoice.cancelASR();}

    public void startWakeUp(String json) {
        baiDuVoice.startWakeUp(json);
    }

    public void stopWakeUp() {
        baiDuVoice.stopWakeUp();
    }

    public void release()
    {
        baiDuVoice.release();
    }

    public void recResult(String json)//返回结果
    {
        callUnity(json);
    }

    public void logRec(String text)//debug
    {
        printLog(text);
    }

    public void callUnity(String msg)//传到unity
    {
        UnityPlayer.UnitySendMessage("BDVoice", "ResultToU3D", msg);
    }

    private void printLog(String text) {
        if (logTime) {
            text += "  ;time=" + System.currentTimeMillis();
        }
        text += "\n";
        Log.i(getClass().getName(), text);
        //  txtLog.append(text + "\n");

         UnityPlayer.UnitySendMessage("BDVoice", "SetText", text);
    }

    public void showToast(final String msg)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,msg, Toast.LENGTH_LONG).show();
            }
        });
    }



    /**
     * android 6.0 以上需要动态申请权限
     */
    /*
    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }
*/
}
