package com.moduleprocesscommunication.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.moduleprocesscommunication.IMyAidlInterface;

/**
 * @author 王兵兵
 * @date 2018/6/20
 */

public class AIDLServer extends Service {

    private String TAG = getClass().getSimpleName();
    private MyBinder myBinder;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.e(TAG, "onRebind: ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
       String string = intent.getStringExtra("aidl");
       if (myBinder == null){
           myBinder = new MyBinder();
       }
        myBinder.setName(string);
        Log.e(TAG, "onBind: "+string);
        return myBinder;
    }

    public class MyBinder extends IMyAidlInterface.Stub {

        public void setName(String name) {
            this.name = name;
        }

        private String name;

        @Override
        public String getName(){
            return name;
        }
    }
}
