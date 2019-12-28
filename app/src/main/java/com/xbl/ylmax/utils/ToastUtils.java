package com.xbl.ylmax.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {
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

        Handler handler =new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(context, txt, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 100);
                toast.show();
            }
        });
    }
}
