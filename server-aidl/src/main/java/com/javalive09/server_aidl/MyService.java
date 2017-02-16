package com.javalive09.server_aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

/**
 *
 * Created by peter on 2017/2/16.
 *
 */

public class MyService extends Service {

    private RemoteCallbackList<MyCallBack> myCallBackRemoteCallbackList;

    @Override
    public void onCreate() {
        super.onCreate();
        myCallBackRemoteCallbackList = new RemoteCallbackList<>();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub(){

        @Override
        public void function1() throws RemoteException {
            method1();
        }

        @Override
        public void function2() throws RemoteException {
            method2();
        }

        @Override
        public void registerCallBack(MyCallBack callback) throws RemoteException {
            if(callback != null) {
                myCallBackRemoteCallbackList.register(callback);
                onCallback("registerCallBack");
            }
        }

        @Override
        public void unRegisterCallBack(MyCallBack callback) throws RemoteException {
            if(callback != null) {
                onCallback("unRegisterCallBack");
                myCallBackRemoteCallbackList.unregister(callback);
            }
        }

    };

//    public void toast(final String toast) {
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(MyService.this, toast, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    public void method1() {
        onCallback("method1");
    }

    public void method2() {
        onCallback("method2");
    }

    private void onCallback(String message) {
        int len = myCallBackRemoteCallbackList.beginBroadcast();
        for (int i = 0; i < len; i++) {
            try {
                myCallBackRemoteCallbackList.getBroadcastItem(i).onResult(message + "\n");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        myCallBackRemoteCallbackList.finishBroadcast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myCallBackRemoteCallbackList.kill();
    }

}
