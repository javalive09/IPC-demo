package com.javalive09.server_messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by peter on 2017/2/16.
 */

public class MyService extends Service {

    private static final int FUN1 = 0X000001;
    private static final int FUN2 = 0X000002;
    private static final int CALLBACK = 0X000004;
    private static final String CONTENT = "content";

    private Set<Messenger> activityMessengers = new HashSet<>();
    private Messenger serviceMessenger = new Messenger(new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                switch (msg.what) {
                    case FUN1:
                        method1();
                        activityMessengers.add(msg.replyTo);
                        toClient("method1");
                        break;
                    case FUN2:
                        method2();
                        toClient("method2");
                        break;
                }
            }
    });

    //发送数据给客户端
    private void toClient(String result) {
        Message message = Message.obtain();
        message.what = CALLBACK;
        Bundle bundle = new Bundle();
        bundle.putString(CONTENT, result);
        message.setData(bundle);
        try {
            for(Messenger activityMessenger : activityMessengers) {
                activityMessenger.send(message);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceMessenger.getBinder();
    }

    private void method1() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                toClient("method1()");
            }
        }, 5000);
    }

    private void method2() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                toClient("method2()");
            }
        }, 5000);
    }


}
