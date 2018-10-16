package com.oops.duvoice;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ActivityVoice extends UnityPlayerActivity  {
    private EventManager asr;
    private  EventListener asrLis;
    private EventManager wakeup;

    private boolean logTime = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        // initPermission();

        asr = EventManagerFactory.create(this, "asr");
        asrLis = setEvenListener();
        asr.registerListener(asrLis); //  EventListener 中 onEvent方法

        wakeup = EventManagerFactory.create(this, "wp");
        wakeup.registerListener(setEvenListener());

    }

    public void startASR(String json) {
      //  String json = "{\"accept-audio-volume\":false,\"pid\":1536}";
        asr.send(SpeechConstant.ASR_START, json, null, 0, 0);
        printLog("输入d参数：" + json);
    }

    public void stopASR() {
        printLog("停止识别：ASR_STOP");
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0); //
    }

    public void startWakeUp(String json) {

       // String json = "{\"accept-audio-volume\":false,\"kws-file\":\"assets:\\/\\/\\/WakeUp.bin\"}";
        wakeup.send(SpeechConstant.WAKEUP_START, json, null, 0, 0);
        printLog("输入d参数：" + json);
    }

    public void stopWakeUp() {
        printLog("停止唤醒：WAKEUP_STOP");
        wakeup.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0); //
    }



    EventListener setEvenListener()
    {
        EventListener asrListener = new EventListener() {
            @Override
            public void onEvent(String name, String params, byte[] data, int offset, int length)
            {

                if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_READY)) {
                    // 引擎准备就绪，可以开始说话
                }

                else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_BEGIN)) {
                    // 检测到用户的已经开始说话
                }
                 else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_END)) {
                    // 检测到用户的已经停止说话
                }

                else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
                    // 临时识别结果, 长语音模式需要从此消息中取出结果
                }

                else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_FINISH)) {
                    // 识别结束， 最终识别结果或可能的错误,可能返回多个结果，请取第一个结果
                }
                 else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_LONG_SPEECH)) {
                    // 长语音识别结束
                }
                else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_EXIT)) {
                    // 引擎完成整个识别，空闲中
                }

                else if (name.equals(SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS)) {
                    //唤醒成功
                }
                else if (name.equals(SpeechConstant.CALLBACK_EVENT_WAKEUP_ERROR)) {
                    //唤醒失败
                }
                else if (name.equals(SpeechConstant.CALLBACK_EVENT_WAKEUP_STOPED)) {
                    //关闭唤醒词
                }




                String logTxt = "name: " + name;
                if (params != null && !params.isEmpty())
                {
                    logTxt += " ;params :" + params;

                     String _json= name + ";" + params;
                    UnityPlayer.UnitySendMessage("BDVoice", "ResultToU3D", _json);
                }
                else if (data != null)
                {
                    logTxt += " ;data length=" + data.length;
                }

                printLog(logTxt);
            }

        };
        return asrListener;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        wakeup.send(SpeechConstant.WAKEUP_STOP, "{}", null, 0, 0);
        asr.unregisterListener(asrLis);
    }

    @Override
    protected void onPause(){
        super.onPause();
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
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

    // 显示Toast消息
    public void ShowToast(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
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
}
