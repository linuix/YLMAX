package com.xbl.ylmax.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.util.Log;

import com.xbl.ylmax.APP;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Author: link
 * Create: 2019-2019/12/28 0028-16:08
 * Changes (from 2019/12/28 0028)
 * 2019/12/28 0028 : Create NetUtil.java (link);
 **/
public class NetUtil {

    private static final String TAG = "NetUtil";
    public static String key = null;
    public static String value = null;
    public static String nickName = null;
    public static String imgUrl = null;
    public static String imgName = null;

    public static String phoneNumber;
    public static String token;
    public static String msgCode;
    public static String pid;



    public static final String keyValueUrl = "http://13.228.16.161:33023/api/getconfig";

    public static final String nickImgUrl = "http://13.228.16.161:33023/api/information";

    public static final String YIXINGUrl = "http://yixin.xx09.cn:88/yhapi.ashx";

    public static void obtainPhoneAndDownloadImg(){
        final OkHttpClient okHttpClient = new OkHttpClient();
        //获取账号密码
        APP.runWorkThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Request request = new Request.Builder().url(keyValueUrl).addHeader("content-type", "application/json").build();
                    final Call call = okHttpClient.newCall(request);
                    Response response = call.execute();
                    if (response.isSuccessful()){
                        String resStr = response.body().string();
                        Log.d(TAG, "run: response = "+resStr);
                        JSONObject jsonObject = new JSONObject(resStr);
                        String status = jsonObject.getString("status");
                        if (Integer.valueOf(status) != 0){
                            ToastUtils.showToast("服务器数据异常！");
                        }else {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            key = dataJson.getString("key");
                            value = dataJson.getString("value");
                            ToastUtils.showToast("获取服务器数据成功！");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },0);

        //获取头像 和 昵称。
        APP.runWorkThread(new Runnable() {
            @Override
            public void run() {
                if (key == null || value == null){
                    return;
                }
                final Request request = new Request.Builder().url(nickImgUrl).addHeader("content-type", "application/json").build();
                final Call call = okHttpClient.newCall(request);
                Response response = null;
                try {
                    response = call.execute();
                    if (response.isSuccessful()){
                        String resStr = response.body().string();
                        Log.d(TAG, "nickImgUrl: response = "+resStr);
                        JSONObject jsonObject = new JSONObject(resStr);
                        String status = jsonObject.getString("status");
                        if (Integer.valueOf(status) == 0){
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            imgUrl = dataJson.getString("img");
                            nickName = dataJson.getString("name");
                            imgName = dataJson.getString("img_name");
                        }else {
                            ToastUtils.showToast("服务器数据异常！");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },0);

        //登陆宜信
        APP.runWorkThread(new Runnable() {
            @Override
            public void run() {
                Request request = null;
                Request.Builder requestBuilder = new Request.Builder();
                HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(YIXINGUrl).newBuilder();
                httpUrlBuilder.addQueryParameter("act","login");
                httpUrlBuilder.addQueryParameter("ApiName",key);
                httpUrlBuilder.addQueryParameter("PassWord",value);
                requestBuilder.url(httpUrlBuilder.build());
                request = requestBuilder.build();

                final Call call = okHttpClient.newCall(request);
                Response response = null;
                try {
                    response = call.execute();
                    Log.d(TAG, "run: url = "+request.url());
                    if (response.isSuccessful()){
                        String resStr = response.body().string();
                        Log.d(TAG, "YIXINGUrl: response = "+resStr);
                        token = resStr.split("\\|")[1];
                        Log.d(TAG, "run: token = "+token);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },0);


        //获取手机号码
        APP.runWorkThread(new Runnable() {
            @Override
            public void run() {
                if (token == null){
                    ToastUtils.showToast("token 获取失败！");
                }
                Map <String, String> map = new HashMap<>();
                map.put("act","getPhone");
                map.put("token",token);
                map.put("iid","2499");
                Request request = addParameter(map,YIXINGUrl);
                final Call call = okHttpClient.newCall(request);
                Response response = null;
                try {
                    response = call.execute();
                    Log.d(TAG, "run: 获取手机号码 url = "+request.url());
                    if (response.isSuccessful()){
                        String resStr = response.body().string();
                        Log.d(TAG, "getPhone YIXINGUrl: response = "+resStr);
                        phoneNumber = resStr.split("\\|")[4];
                        pid = resStr.split("\\|")[1];
                        Log.d(TAG, "run: phoneNumber = "+phoneNumber);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },0);


        //保存图片
        APP.runWorkThread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                //获取请求对象
                Request request = new Request.Builder().url(imgUrl).build();
                //获取响应体
                ResponseBody body = null;
                try {
                    body = client.newCall(request).execute().body();
                    //获取流
                    InputStream in = body.byteStream();

                    FileOutputStream fileOutputStream = new FileOutputStream("/sdcard/ylmax"+ SystemClock.elapsedRealtime()+imgName);
                    byte data[] = new byte[4096];
                    int size = 0;
                    while (0<(size = in.read(data))){
                        fileOutputStream.write(data,0,size);
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    ToastUtils.showToast("图片下载完成");

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        },0);

    }


    //获取验证码
    public static void obtainMsgCode(){
        final OkHttpClient okHttpClient = new OkHttpClient();

        APP.runWorkThread(new Runnable() {
            @Override
            public void run() {

                Map <String, String> map = new HashMap<>();
                map.put("act","getPhoneCode");
                map.put("token",token);
                map.put("pid",pid);
                Request request = addParameter(map,YIXINGUrl);
                final Call call = okHttpClient.newCall(request);
                Response response = null;
                try {
                    response = call.execute();
                    Log.d(TAG, "run: 获取手机号码 url = "+request.url());
                    if (response.isSuccessful()){
                        String resStr = response.body().string();
                        Log.d(TAG, "getPhone YIXINGUrl: response = "+resStr);
                        if (Integer.valueOf(resStr.split("\\|")[0]) == 1){
                            msgCode = resStr.split("\\|")[1];
                            Log.d(TAG, "run: msgCode = "+msgCode);
                            ToastUtils.showToast("获取验证码 = "+msgCode);
                        }else {
                            Log.d(TAG, "run: 验证码获取失败 error = "+Integer.valueOf(resStr.split("\\|")[0]));
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },0);
    }

    static Request addParameter(Map<String,String> map,String url){
        Request.Builder requestBuilder = new Request.Builder();
        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(url).newBuilder();
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            httpUrlBuilder.addQueryParameter(key,map.get(key));
        }
        requestBuilder.url(httpUrlBuilder.build());
        return requestBuilder.build();
    }



    public static String toStr() {
        return "NetUtil{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", nickName='" + nickName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", token='" + token + '\'' +
                ", msgCode='" + msgCode + '\'' +
                '}';
    }



}
