package com.xbl.ylmax.utils;

import android.app.ActivityManager;
import android.content.Context;
import java.util.List;

/**
 * Created by xbl
 */
public class SystemInfoUtils {
    /**
     * 获取正在运行的进程的数量
     *
     * @param context 上下文
     * @return the running process count in devices
     */
    public static int getRunningProcessCount(Context context) {
        /*PackageManager 包管理器，相当于程序管理器，静态的内容*/
        /*ActivityManager 进程管理器。管理手机活动的信息*/
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();

        return appProcessInfos.size();
    }

    /**
     * 已用内存占比
     * @param context
     * @return
     */
    public static int getUsedMemoryRatio(Context context){
        double usedRatio = 0.0;
        long AvailableRemainsMemory = getAvailableRemainsMemoryCount(context);
        long TotalRemainsMemory = getTotalRemainsMemoryCount(context);
        long usedRemainsMemory = TotalRemainsMemory - AvailableRemainsMemory;
        if(TotalRemainsMemory != 0){
            usedRatio = (double) usedRemainsMemory / TotalRemainsMemory;
        }
        return (int)(usedRatio * 100);
    }

    /**
     * 获取手机可用可用内存数量RemainsMemory
     *
     * @param context
     * @return
     */
    public static long getAvailableRemainsMemoryCount(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    /**
     * 获取手机可用总内存数量
     *
     * @param context
     * @return
     */
    public static long getTotalRemainsMemoryCount(Context context) {
        /*只能在4.0以上运行*/
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem;
        //使用下面代码可兼容低版本
//        try{
//            File file = new File("/proc/meminfo");
//            FileInputStream fis = new FileInputStream(file);
//            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
//            String line = br.readLine();
//            StringBuffer sb = new StringBuffer();
//            for (char c : line.toCharArray()) {
//                if(c >= '0' && c <= '9'){
//                    sb.append(c);
//                }
//            }
//            return Integer.parseInt(sb.toString())*1024l;
//        }catch(Exception e){
//            e.printStackTrace();
//            return 0;
//        }
    }
}
