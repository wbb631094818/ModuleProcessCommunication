package com.messengerdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "messenger";

    private static final int MSG_SUM = 0x110;

    private EditText mEt;
    private Button mBt;
    private TextView mTv;

    private Messenger mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEt = (EditText) findViewById(R.id.et);
        mBt = (Button) findViewById(R.id.bt);
        mTv = (TextView) findViewById(R.id.tv);

        mBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 发送消息到服务端
                Message msg = Message.obtain(null, MSG_SUM);
                msg.replyTo = mGetReplyMessenger; // 传给服务端一个客户端的信使
                Bundle data = new Bundle();
                data.putString("msg", mEt.getText().toString());
                msg.setData(data);
                try {
                    mService.send(msg); // 发送消息
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        bindServiceInvoked(); // 链接服务端
    }

    // 与服务端链接
    private void bindServiceInvoked() {
        Intent intent = new Intent();
        intent.setClassName("com.moduleprocesscommunication","com.moduleprocesscommunication.server.MessengerService");
        bindService(intent, mConn, Context.BIND_AUTO_CREATE);
        Log.e(TAG, "bindService invoked !");
    }

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            mTv.setText("服务端连接成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mTv.setText("服务端连接失败!");
        }
    };

    //通过这个处理信件的Handler大叔来创建信使
    private Messenger mGetReplyMessenger = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (MSG_SUM == msg.what) {
                Bundle data = msg.getData();
                String msgString = data.getString("msg");
                mTv.setText(msgString);
            }
            return false;
        }
    }));

}
