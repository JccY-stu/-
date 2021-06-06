package com.southwind.mmall002.entity;

import java.util.List;

/**
 * Auto-generated: 2021-05-20 12:39:51
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class FanJson {

    private int code;
    private String msg;
    private int count;
    private List<User> data;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public int getCount() {
        return count;
    }

    public void setData(List<User> data) {
        this.data = data;
    }
    public List<User> getData() {
        return data;
    }

}