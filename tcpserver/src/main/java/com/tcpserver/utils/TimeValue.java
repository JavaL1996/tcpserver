package com.tcpserver.utils;

import java.util.Date;

/*
* 时间矫正
*   这是给服务器用的 尽量用byte
* */
public class TimeValue {

    private byte[] date;//获取日期
    private byte[] time;//获取时间

    public byte[] getDate() {
        return date;
    }

    public void setDate(byte[] date) {
        this.date = date;
    }

    public byte[] getTime() {
        return time;
    }

    public void setTime(byte[] time) {
        this.time = time;
    }
}
