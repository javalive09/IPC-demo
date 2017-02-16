package com.javalive09.client_aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.javalive09.server_aidl.IMyAidlInterface;
import com.javalive09.server_aidl.MyCallBack;

public class MainActivity extends AppCompatActivity {

    private IMyAidlInterface iMyAidlInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bootService();
    }

    private void bootService() {
        Intent intent = new Intent("com.javalive09.server_aidl.MyService");
        intent.setClassName("com.javalive09.server_aidl", "com.javalive09.server_aidl.MyService");
        startService(intent);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            iMyAidlInterface =null;
        }

    };

    private MyCallBack callBack = new MyCallBack.Stub() {

        @Override
        public void onResult(final String result) throws RemoteException {
            toast(result);
        }
    };

    private void toast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "callback onResult = " + text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClick(View view) throws RemoteException {
        if(iMyAidlInterface != null) {
            switch (view.getId()) {
                case R.id.fun1:
                    iMyAidlInterface.function1();
                    break;
                case R.id.fun2:
                    iMyAidlInterface.function2();
                    break;
                case R.id.register:
                    iMyAidlInterface.registerCallBack(callBack);
                    break;
                case R.id.unregister:
                    iMyAidlInterface.unRegisterCallBack(callBack);
                    break;
            }
        }else {
            toast("not bind service, need start server client and restart client app !");
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }
}
