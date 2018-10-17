package com.oops.baiduvoice;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.oops.*;
public class BaiDuVoice {
    private static BaiDuVoice _instance;

    // 暂存 UnityPlayerActivity 的 Context
    private static Context unityContext;
    // 暂存 UnityPlayerActivity 的 Activity
    private static Activity unityActivity;

    private EventManager asr;
    private EventListener asrLis;
    private EventManager wakeup;

    public static BaiDuVoice instance()
    {

        if(_instance == null)
            _instance = new BaiDuVoice();
        return _instance;
    }
    // 初始化方法，用来传入上下文
    // 这里传入 UnityPlayerActivity
    public void init(Context _context)
    {
        unityContext = _context.getApplicationContext();
        unityActivity = (Activity)_context;

        asr = EventManagerFactory.create(unityContext, "asr");
        asrLis = setEvenListener();
        asr.registerListener(asrLis); //  EventListener 中 onEvent方法

        wakeup = EventManagerFactory.create(unityContext, "wp");
        wakeup.registerListener(setEvenListener());
    }

    public void unregisterASRListener()
    {
        asr.unregisterListener(asrLis);
    }

    public void startASR(String json) {
        //  String json = "{\"accept-audio-volume\":false,\"pid\":1536}";
        asr.send(SpeechConstant.ASR_START, json, null, 0, 0);
    }

    public void stopASR() {
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0); //
    }

    public void cancelASR()
    {
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);

    }

    public void startWakeUp(String json) {

        // String json = "{\"accept-audio-volume\":false,\"kws-file\":\"assets:\\/\\/\\/WakeUp.bin\"}";
        wakeup.send(SpeechConstant.WAKEUP_START, json, null, 0, 0);

    }

    public void stopWakeUp() {
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
                    //  UnityPlayer.UnitySendMessage("BDVoice", "ResultToU3D", _json);
                    dataCallback.callback(_json);
                }
                else if (data != null)
                {
                    logTxt += " ;data length=" + data.length;
                }


            }

        };
        return asrListener;

    }
    DataCallback dataCallback;
    public void dataCallback(DataCallback d)
   {
      dataCallback = d;
      // d.callback("");
    }
    public String test(String str)
    {
        return "From android >>>> " + str;
    }


}
