package com.javalive09.client_messenger;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int FUN1 = 0X000001;
    private static final int FUN2 = 0X000002;
    private static final int CALLBACK = 0X000004;
    private static final String CONTENT = "content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bootService();
    }

    private void bootService() {
        Intent intent = new Intent("com.javalive09.server_messenger.MyService");
        intent.setClassName("com.javalive09.server_messenger", "com.javalive09.server_messenger.MyService");
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fun1:
                invokeService(FUN1);
                break;
            case R.id.fun2:
                invokeService(FUN2);
                break;
        }
    }

    private void invokeService(int code) {
        Message message = Message.obtain();
        message.what = code;
        message.replyTo = activityMessenger;
        try {
            mService.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //service messenger 发送消息到服务器
    private Messenger mService;

    //activity messenger 接受服务端传来的消息
    private Messenger activityMessenger = new Messenger(new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CALLBACK:
                    Bundle bundle = msg.getData();
                    final String result = bundle.getString(CONTENT);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
            }
        }

    });

    ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = new Messenger(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
        }
    };


}
