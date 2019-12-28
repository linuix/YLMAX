package com.xbl.ylmax.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: link
 * Create: 2019-2019/12/28 0028-16:08
 * Changes (from 2019/12/28 0028)
 * 2019/12/28 0028 : Create NetUtil.java (link);
 **/
public class NetUtil {

    private static final String TAG = "NetUtil";
    public static String key;
    public static String valeu;

    public static final String keyValueUrl = "http://13.228.16.161:33023/api/getconfig";

    public static void obtainKeyValue(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(keyValueUrl).build();
        final Call call = okHttpClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    String resStr = response.body().toString();
                    Log.d(TAG, "run: response = "+resStr);
                    JSONObject jsonObject = new JSONObject(resStr);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
