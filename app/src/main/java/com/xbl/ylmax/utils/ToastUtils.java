package com.xbl.ylmax.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;

import com.xbl.ylmax.APP;

public class ToastUtils {
    private static final String TAG = "ToastUtils";

    /**
     * 在Service里面弹出Toast
     *
     * @param txt 需要显示的文字
     */
    public static void showToast(final Context context, final String txt) {
        if (txt == null) {
            Log.e("toast", "call method showToast, text is null.");
            return;
        }
        Message msg = Message.obtain();
        msg.what = APP.MSG_TOAST;
        msg.obj = txt+"";
        APP.getInstance().mHandler.sendMessage(msg);
    }

    public static void showToast(String txt){
        Log.d(TAG, "showToast: txt = "+txt);
        showToast(null,txt);
    }
}
