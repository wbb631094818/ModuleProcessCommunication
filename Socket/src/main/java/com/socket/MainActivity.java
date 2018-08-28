package com.socket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private EditText mEt;
    private Button mBt;
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEt = (EditText) findViewById(R.id.et);
        mBt = (Button) findViewById(R.id.bt);
        mTv = (TextView) findViewById(R.id.tv);
        // 链接
        conn(mTv);
        mBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send(v,mEt.getText().toString());
            }
        });
    }

    private Socket socket;

    /**
     * 建立服务端连接
     */
    public void conn(View v) {
        Log.e("JAVA", "开始连接：" );
        new Thread() {
            @Override
            public void run() {
                try {
                    socket = new Socket("192.168.1.184", 8888);
                    Log.e("JAVA", "建立连接：" + socket.isConnected());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("JAVA", "Exception：" + e.getMessage());
                }
            }
        }.start();
    }

    /**
     * 发送消息
     */
    public void send(View v, final String massage) {
        new Thread() {
            @Override
            public void run() {

                try {
                    // socket.getInputStream()
                    DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
                    writer.writeUTF(massage); // 写一个UTF-8的信息
                    System.out.println("发送消息");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
