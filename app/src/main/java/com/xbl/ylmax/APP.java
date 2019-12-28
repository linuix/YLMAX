package com.xbl.ylmax;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.widget.Toast;


/**
 * Author: link
 * Create: 2019-2019/12/28 0028-11:53
 * Changes (from 2019/12/28 0028)
 * 2019/12/28 0028 : Create APP.java (link);
 **/
public class APP extends Application {

    private static APP S_CONTENT;

    public static final int MSG_TOAST = 0X1001;

    public Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MSG_TOAST:{
                    Toast.makeText(APP.this.getApplicationContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            return false;
        }
    });

    public static Handler workHandler = null;
    public static HandlerThread handlerThread = null;



    public static APP getInstance(){
        return S_CONTENT;
    }

    @Override
    protected void attachBaseContext(Context base) {
        S_CONTENT = this;
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handlerThread = new HandlerThread("work");
        handlerThread.start();
        workHandler = new Handler(handlerThread.getLooper());
    }


    public static void runWorkThread(Runnable runnable,int delayed){
        workHandler.postDelayed(runnable,delayed);
    }
}
