package com.moduleprocesscommunication.bean;

import java.io.Serializable;

/**
 *
 *
 * @author 王兵兵
 * @date 2018/6/21
 */

public class User implements Serializable {

    //版本号
    private static final long serialVersionUID = 2016L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
