package com.moduleprocesscommunication;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
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

import com.moduleprocesscommunication.bean.User;
import com.moduleprocesscommunication.db.UserDb;
import com.moduleprocesscommunication.server.AIDLServer;
import com.moduleprocesscommunication.server.MessengerService;
import com.moduleprocesscommunication.utils.ParcelableUtils;
import com.moduleprocesscommunication.utils.Utils;

import org.xutils.x;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import xiaofei.library.hermeseventbus.HermesEventBus;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mAidl;
    private Button mSerialize;
    private Button mBroadcast;
    private Button mSocket;
    private Button mContentProvider;
    private Button mIntent;
    private Button mMessenger;
    private Button mSharedPreferences;
    private Button mEventbus;
    private EditText mEt;

    private AIDLServer.MyBinder binder;

    private Messenger messenger;

    private ChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEt = (EditText) findViewById(R.id.et);

        mAidl = (Button) findViewById(R.id.aidl);
        mMessenger = (Button) findViewById(R.id.messenger);
        mSerialize = (Button) findViewById(R.id.serialize);
        mBroadcast = (Button) findViewById(R.id.broadcast);
        mSocket = (Button) findViewById(R.id.socket);
        mContentProvider = (Button) findViewById(R.id.content_provider);
        mIntent = (Button) findViewById(R.id.intent);
        mSharedPreferences = (Button) findViewById(R.id.sharedPreferences);

        mEventbus = (Button) findViewById(R.id.eventbus);

        mAidl.setOnClickListener(this);
        mSerialize.setOnClickListener(this);
        mBroadcast.setOnClickListener(this);
        mSocket.setOnClickListener(this);
        mContentProvider.setOnClickListener(this);
        mIntent.setOnClickListener(this);
        mMessenger.setOnClickListener(this);
        mSharedPreferences.setOnClickListener(this);
        mEventbus.setOnClickListener(this);

        Intent bindService = new Intent(this, MessengerService.class);
        //最后一个参数代表绑定的时候 服务会自动创建
        bindService(bindService, mConnectionMess, BIND_AUTO_CREATE);

        //动态接受网络变化的广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.moduleprocesscommunication.ChangeReceiver");

        networkChangeReceiver = new ChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);

        Utils.verifyStoragePermissions(this);

        x.Ext.init(getApplication());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aidl:
                String string = mEt.getText().toString();
                if (binder == null) {
                    Intent bindService = new Intent(this, AIDLServer.class);
                    bindService.putExtra("aidl", string);
                    //最后一个参数代表绑定的时候 服务会自动创建
                    bindService(bindService, mConnection, BIND_AUTO_CREATE);
                } else {
                    binder.setName(string);
                }

                Intent intent = new Intent();
                intent.setClassName("com.aidldemo", "com.aidldemo.MainActivity");
                startActivity(intent);
                break;
            case R.id.messenger:
//                if (messenger == null){
//
//                }else {
//                    Message message = new Message();
//                    Bundle build = new Bundle();
//                    build.putString("msg",mEt.getText().toString());
//                    message.setData(build);
//                    try {
//                        messenger.send(message);
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                }
                Intent intentm = new Intent();
                intentm.setClassName("com.messengerdemo", "com.messengerdemo.MainActivity");
                startActivity(intentm);
                break;
            case R.id.serialize:
                User user = new User();
                user.setName(mEt.getText().toString());
                ParcelableUtils.writeUser(user);
                Intent intents = new Intent();
                intents.setClassName("com.parcelable", "com.parcelable.MainActivity");
                startActivity(intents);
                break;
            case R.id.broadcast:
                Intent intentb = new Intent("com.moduleprocesscommunication.ChangeReceiver");
                intentb.putExtra("Broadcast",mEt.getText().toString());
                sendBroadcast(intentb);
                break;
            case R.id.socket:
                startService();
                break;
            case R.id.content_provider:
                User userx = new User();
                userx.setName(mEt.getText().toString());
                UserDb.get().addData(userx);
                Intent intentx = new Intent();
                intentx.setClassName("com.photo3d.dbdeno", "com.photo3d.dbdeno.MainActivity");
                startActivity(intentx);
                break;
            case R.id.intent:
                Intent intenti = new Intent();
                intenti.putExtra("intent","传给你的，接好了");
                intenti.setClassName("com.parcelable", "com.parcelable.MainActivity");
                startActivity(intenti);
                break;
            case R.id.sharedPreferences:
                save(mEt.getText().toString(),22);
                startActivity(new Intent().setClassName("com.eventbus", "com.eventbus.MainActivity"));
                break;
            case R.id.eventbus:
                mEt.setText(getData());
//                startActivity(new Intent().setClassName("com.eventbus", "com.eventbus.MainActivity"));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (AIDLServer.MyBinder) service;
            Log.e("wbb", "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private ServiceConnection mConnectionMess = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private class ChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getStringExtra("Broadcast");
            mEt.setText(str);
        }
    }


    /**
     * 启动服务监听，等待客户端连接
     */
    private void startService() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建ServerSocket
                    ServerSocket serverSocket = new ServerSocket(8888);
                    System.out.println("--开启服务器，监听端口 8888--");
//            mSocket.setText("--开启服务器，监听端口 9999--");

                    // 监听端口，等待客户端连接
                    while (true) {
                        System.out.println("--等待客户端连接--");
//                mSocket.setText("--等待客户端连接--");
                        Socket socket = serverSocket.accept(); //等待客户端连接
                        System.out.println("得到客户端连接：" + socket);
//                mSocket.setText("得到客户端连接：");

                        startReader(socket);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("IOException：" + e.getMessage());
                }

            }
        }).start();
    }

    /**
     * 从参数的Socket里获取最新的消息
     */
    private void startReader(final Socket socket) {
        new Thread(){
            @Override
            public void run() {
                DataInputStream reader;
                try {
                    // 获取读取流
                    reader = new DataInputStream( socket.getInputStream());
                    while (true) {
                        System.out.println("*等待客户端输入*");
                        Log.e("wbb", "*等待客户端输入*" );
                        // 读取数据
                        final String msg = reader.readUTF();

                        System.out.println("获取到客户端的信息：" + msg);
                        mSocket.post(new Runnable() {
                            @Override
                            public void run() {
                                mSocket.setText("获取到客户端的信息：" + msg);
                            }
                        });
                        Log.e("wbb", "*等待客户端输入*" );
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }



    /**
     * 保存用户偏好设置
     */

    public void save(String name, int age) {

        //第一个参数  是最终保存的文件名，不用指定文件后缀，因为SharedPreferences这个API默认就是xml格式保存
        //  Activity.MODE_WORLD_READABLE：表示当前文件可以被其他应用读取，
        // Activity.MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入；
        //第二个参数是文件操作模式，这里是只能本软件自己访问的私有操作模式
        SharedPreferences preferences = getSharedPreferences("text",MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", name);
        editor.putInt("age", age);
        editor.apply();//把数据提交到文件里，在这之前数据都是存放在内存中
    }

    public String getData(){
        SharedPreferences share = getSharedPreferences("text",
                Context.MODE_MULTI_PROCESS);
        return share.getString("name", "没有得到数据");
    }
}
