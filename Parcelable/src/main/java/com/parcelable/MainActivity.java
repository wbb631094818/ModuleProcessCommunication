package com.parcelable;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.moduleprocesscommunication.bean.User;
import com.parcelable.utils.ParcelableUtils;

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

        mBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bt = mBt.getText().toString();
                if ("获取本地信息".equals(bt)) {
                    User user = ParcelableUtils.readUser();
                    mTv.setText(user.getName());
                    mBt.setText("保存信息到本地");
                } else {
                    String et = mEt.getText().toString();
                    User user = new User();
                    user.setName(et);
                    ParcelableUtils.writeUser(user);
                    mBt.setText("获取本地信息");
                }
            }
        });

        verifyStoragePermissions(this);

       String str =  getIntent().getStringExtra("intent");
        mTv.setText(str);
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}
