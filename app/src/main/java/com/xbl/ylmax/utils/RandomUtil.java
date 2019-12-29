package com.xbl.ylmax.utils;

import android.util.Log;

import java.util.Random;

/**
 * Author: link
 * Create: 2019-2019/12/29 0029-22:01
 * Changes (from 2019/12/29 0029)
 * 2019/12/29 0029 : Create RandomUtil.java (link);
 **/
public class RandomUtil {

    private static final String TAG = "RandomUtil";

    public static boolean isLike(){
        Random random = new Random();
        int r = random.nextInt(10);
        Log.d(TAG, "isLike: r = "+r);
        return r>5;
    }

}
