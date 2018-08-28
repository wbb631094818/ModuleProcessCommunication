package com.moduleprocesscommunication.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 *
 * @author 王兵兵
 * @date 2018/6/21
 */
@Table(name = "User")
public class User implements Serializable {

    //版本号
    private static final long serialVersionUID = 2016L;

    @Column(name = "id", isId = true, autoGen = true)
    private int id;

    @Column(name = "name")
    private String name;

    public User(String name) {
        this.name = name;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
