package com.moduleprocesscommunication.server;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * 使用Messenger，一种轻量级的跨进程通讯方案
 *
 * @author 王兵兵
 * @date 2018/6/21
 */

public class MessengerService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    private static final int MSG_SUM = 0x110;

    private Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Message msgToClient = Message.obtain(msg);//返回给客户端的消息
            switch (msg.what) {
                //msg 客户端传来的消息
                case MSG_SUM:
                    Bundle data = msg.getData();
                    String msgString = data.getString("msg");
                    Log.e("wbb", "handleMessage: " + msgString);
                    try {
                        //模拟耗时
                        Thread.sleep(2000);
                        Bundle build = new Bundle();
                        build.putString("msg","收到你的信息"+msgString+"，稍后回复");
                        msgToClient.setData(build);
                        msg.replyTo.send(msgToClient);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    }));

    //最好换成HandlerThread的形式
//    private Messenger mMessenger = new Messenger(new Handler() {
//        @Override
//        public void handleMessage(Message msgfromClient) {
//            Message msgToClient = Message.obtain(msgfromClient);//返回给客户端的消息
//            switch (msgfromClient.what) {
//                //msg 客户端传来的消息
//                case MSG_SUM:
//                    msgToClient.what = MSG_SUM;
//                    try {
//                        //模拟耗时
//                        Thread.sleep(2000);
//                        msgToClient.arg2 = msgfromClient.arg1 + msgfromClient.arg2;
//                        msgfromClient.replyTo.send(msgToClient);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//            }
//
//            super.handleMessage(msgfromClient);
//        }
//    });
}
