// IMyAidlInterface.aidl
package com.javalive09.server_aidl;
import com.javalive09.server_aidl.MyCallBack;

interface IMyAidlInterface {

    void function1();

    void function2();

    void registerCallBack(MyCallBack callback);

    void unRegisterCallBack(MyCallBack callback);

}
