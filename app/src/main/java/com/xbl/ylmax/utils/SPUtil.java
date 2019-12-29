package com.xbl.ylmax.utils;

import android.content.SharedPreferences;

import com.xbl.ylmax.APP;

import java.util.ArrayList;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Author: link
 * Create: 2019-2019/12/29 0029-13:24
 * Changes (from 2019/12/29 0029)
 * 2019/12/29 0029 : Create SPUtil.java (link);
 **/
public class SPUtil {

    public static String spName = "data_sp";

    public static void wirteData(String key,Object value){
        SharedPreferences sharedPreferences = APP.getInstance().getSharedPreferences(spName,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof String){
            editor.putString(key, (String) value);
        }else if (value instanceof Integer){
            editor.putInt(key, (Integer) value);
        }else if (value instanceof Boolean){
            return;
        }
        editor.commit();
    }

    public static  <T> T readDataFromKey(String key,Class<T> cls){
        T value = null;
        SharedPreferences sharedPreferences = APP.getInstance().getSharedPreferences(spName,MODE_PRIVATE);
        if (cls == String.class){
            value = (T) sharedPreferences.getString(key,"");
        }else if (cls == Integer.class){
            value = (T) Integer.valueOf(sharedPreferences.getInt(key,0));
        }else if (cls == Boolean.class){
            value = (T) Boolean.valueOf(sharedPreferences.getBoolean(key,false));
        }
        return value;

    }


}
