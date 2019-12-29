package com.xbl.ylmax.utils;

import android.util.Log;

import java.util.Random;

/**
 * Author: link
 * Create: 2019-2019/12/29 0029-16:07
 * Changes (from 2019/12/29 0029)
 * 2019/12/29 0029 : Create DelayUtil.java (link);
 **/
public class DelayUtil {


    private static final String TAG = "DelayUtil";
    public static int viewVideoTime;
    public static int commTime = 5;

    public final static int maxVideoTime = 60;
    public final static int minVideoTime = 10;


    public static int getViewVideoTime(){

        Random random = new Random();
        int f = random.nextInt(maxVideoTime - minVideoTime);
        viewVideoTime = minVideoTime + f;
        viewVideoTime = viewVideoTime*1000;
        Log.d(TAG, "getViewVideoTime: viewVideoTime = "+viewVideoTime);
        return 15000;
    }


    public static int getCommTime(){
        Random random = new Random();
        int r = random.nextInt(10);
        return (r+commTime)*1000;
    }


    public static int getLickTime() {
        return 5000;
    }
}
