package com.tcpserver;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * Created by Jone on 2018-05-18.
 */
public class Test1 {
    @Test
    public void test2(){

        byte [] bytes={(byte) 0xBB,
                (byte) 0x88,0x01,0x22,0x33,
                0x41,0x4A,0x38,0x30,0x33,0x38,0x38,
                0x01,0x01,0x01,0x01,0x38,0x38,0x38,0x38,0x38,0x38,0x38,0x38,0x38,
                0x2B,(byte) 0xEE, (byte) 0xFF};//车闸时间

        byte[] times={(byte) 0xBB, (byte) 0x88,0x04,
                0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
                0x00,0x00,0x00,0x00,0x00,0x00,
                0x37, (byte) 0xEE, (byte) 0xFF};

        byte first=times[0];
        for (int i=1;i<times.length-3;i++){
            first= (byte) (first^times[i]);
        }
        System.out.println(Integer.toHexString(first&0xff));
    }

    @Test
    public void test3(){
        byte myByte=0x01;
        System.out.println( (int)myByte);
    }
}
