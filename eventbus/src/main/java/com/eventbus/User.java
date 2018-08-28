package com.eventbus;

import java.io.Serializable;

/**
 *
 * @author 王兵兵
 * @date 2018/6/21
 */
public class User implements Serializable {

    //版本号
    private static final long serialVersionUID = 2016L;

    private int id;

    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
