package com.moduleprocesscommunication.db;


import android.os.Environment;
import com.moduleprocesscommunication.bean.User;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;
import java.io.File;
import java.util.ArrayList;


/**
 *
 */
public class UserDb {

    private static UserDb userDb = null;

    private DbManager dbManager;

    public static UserDb get() {
        if (userDb == null) {
            userDb = new UserDb();
        }
        return userDb;
    }

    private UserDb() {
        try {
            DbManager.DaoConfig daoconfig = new DbManager.DaoConfig();
            daoconfig.setDbName("FriendsDb")
                    .setDbDir(new File(Environment.getExternalStorageDirectory().getPath() + "/xutlsdb"))
                    .setDbVersion(1)//默认1
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                            if (newVersion != oldVersion) {
                                //按需求进行更新
                            }
                        }
                    });
            //通过manager进行增删改查
            dbManager = x.getDb(daoconfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据名字查找数据库是否有数据
     *
     * @param name
     * @return
     */
    public User queryData(String name) {
        try {
            return dbManager.selector(User.class).where("name", "=", name).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加所有
     *
     * @param list 数据集合
     */
    public void addData(ArrayList<User> list) {
        try {
            dbManager.save(list);
        } catch (DbException e) {
            e.printStackTrace();

        }
    }
    /**
     * 添加
     *
     * @param user 数据
     */
    public void addData(User user) {
        try {
            dbManager.save(user);
        } catch (DbException e) {
            e.printStackTrace();

        }
    }
}
