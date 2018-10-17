package com.oops.baiduvoice;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ActivityRecog extends Activity implements EventListener {

    protected TextView txtResult;
    protected TextView txtLog;
    protected Button btn;
    protected Button stopBtn;

    private EventManager asr;

    private boolean logTime = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
       // initPermission();
        //initView();

        asr = EventManagerFactory.create(this, "asr");
        asr.registerListener(this); //  EventListener 中 onEvent方法

    }

    public void startASR() {
       String json = "{\"accept-audio-volume\":false,\"pid\":1536}";
        asr.send(SpeechConstant.ASR_START, json, null, 0, 0);
        printLog("输入d参数：" + json);
    }

    public void stopASR() {
        printLog("停止识别：ASR_STOP");
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0); //
    }

    public void startWakeUp() {

        Map<String, Object> params = new TreeMap<String, Object>();

        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        // "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下

        String json = null; // 这里可以替换成你需要测试的json
        json = new JSONObject(params).toString();

        //json = "{\"accept-audio-volume\":false,\"kws-file\":\"assets:\\/\\/\\/WakeUp.bin\"}";
        asr.send(SpeechConstant.WAKEUP_START, json, null, 0, 0);
        printLog("输入d参数：" + json);
    }

    public void stopWakeUp() {
        printLog("停止唤醒：WAKEUP_STOP");
        asr.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0); //
    }

    //   EventListener  回调方法
    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        String logTxt = "name: " + name;


        if (params != null && !params.isEmpty()) {
            logTxt += " ;params :" + params;
        }

        if (name.equals(SpeechConstant.ASR_START)) {

        }

        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
           //识别结果
        }
        if (name.equals(SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS)) {

        }
        else if (data != null) {
            logTxt += " ;data length=" + data.length;
        }
        printLog(logTxt);
    }



    private void printLog(String text) {
        if (logTime) {
            text += "  ;time=" + System.currentTimeMillis();
        }
        text += "\n";
        Log.i(getClass().getName(), text);
      //  txtLog.append(text + "\n");

       // UnityPlayer.UnitySendMessage("BDVoice", "SetText", text);
    }




}
