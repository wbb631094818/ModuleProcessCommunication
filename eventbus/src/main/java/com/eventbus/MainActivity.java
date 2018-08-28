package com.eventbus;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import xiaofei.library.hermeseventbus.HermesEventBus;

public class MainActivity extends AppCompatActivity {

    private EditText mEt;
    private Button mBt;
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        HermesEventBus.getDefault().connectApp(this, "com.moduleprocesscommunication");

        mEt = (EditText) findViewById(R.id.et);
        mBt = (Button) findViewById(R.id.bt);
        mTv = (TextView) findViewById(R.id.tv);

        mBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HermesEventBus.getDefault().post(new User("我这是客户端的消息"));
                try {
                    Context configContext = createPackageContext("com.moduleprocesscommunication",
                            Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
                    SharedPreferences share = configContext.getSharedPreferences("text",
                            Context.MODE_MULTI_PROCESS);
                    String currentChannel = share.getString("name", "没有得到数据");
                    Log.e("wbb", "onCreate: currentChannel: "+currentChannel );
                    mTv.setText(currentChannel);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

//        HermesEventBus.getDefault().register(this);

        try {
            Context configContext = createPackageContext("com.moduleprocesscommunication", Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
            SharedPreferences share = configContext.getSharedPreferences("text",
                    Context.MODE_WORLD_READABLE);
            String currentChannel = share.getString("name", "没有得到数据");
            Log.e("wbb", "onCreate: currentChannel: "+currentChannel );
            mTv.setText(currentChannel);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void eventUser(User messageEvent) {
//        mTv.setText(messageEvent.getName());
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (HermesEventBus.getDefault().isRegistered(this)) {
//            HermesEventBus.getDefault().unregister(this);
//        }
//    }


}
