package com.moduleprocesscommunication.utils;

import android.os.Environment;

import com.moduleprocesscommunication.bean.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化
 * Created by 王兵兵 on 2018/6/21.
 */

public class ParcelableUtils {

    private static String PATH = Environment.getExternalStorageDirectory().getPath() + "/oos.txt";

    public static User readUser() {
        User obj = null;
        try {
            // 创建反序列化对象
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PATH));
            // 还原对象
            obj = (User) ois.readObject();
            // 释放资源
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;

    }

    public static void writeUser(User user) {
        try {
            File file = new File(PATH);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            // 创建序列化流对象
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
                    file));
            // public final void writeObject(Object obj)
            oos.writeObject(user);
            // 释放资源
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
