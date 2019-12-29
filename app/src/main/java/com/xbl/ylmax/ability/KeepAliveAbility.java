package com.xbl.ylmax.ability;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.xbl.ylmax.APP;
import com.xbl.ylmax.utils.NodeUtil;
import com.xbl.ylmax.utils.ScreenUtils;
import com.xbl.ylmax.utils.SystemInfoUtils;
import com.xbl.ylmax.utils.ToastUtils;

import java.util.List;

/**
 * Author: link
 * Create: 2019-2019/12/29 0029-10:23
 * Changes (from 2019/12/29 0029)
 * 2019/12/29 0029 : Create KeepAliveAbility.java (link);
 **/
public class KeepAliveAbility extends Ability {

    private static final String TAG = "KeepAliveAbility";
    public int fans;
    public int flow;
    public int like;








    private static KeepAliveAbility keepAliveAbility = new KeepAliveAbility();

    public static KeepAliveAbility getInstance(){
        return keepAliveAbility;
    }

    /**
     * 获取个人粉丝数量相关
     */
    public void obtainUserFlow(){
        AccessibilityNodeInfo rootNodeInfo = mService.getRootInActiveWindow();
        if (rootNodeInfo == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        NodeUtil.getNodeByClassName(sb, rootNodeInfo, "android.widget.TextView");
        Log.d(TAG, "obtainUserFlow: sb =  "+sb.toString());
    }

    /**
     * 双击点赞
     */
    public void doubleClick(){
        int scx = ScreenUtils.getScreenWidth(APP.getInstance()) / 2;
        int scy = ScreenUtils.getScreenHeight(APP.getInstance()) / 2;
        Log.d(TAG, "doubleClick: scx = "+scx+"---scy = "+scy);
        NodeUtil.clickPoint(mService,scx,scy,3);
        SystemClock.sleep(200);
        NodeUtil.clickPoint(mService,scx,scy,3);
    }

    /**
     * 右滑
     */
    private void MainActivityToRight() {
        final int x = ScreenUtils.getScreenWidth(mService);
        final int y = ScreenUtils.getScreenHeight(mService) / 2;
        Path path = new Path();
        path.moveTo(x - 800, y);
        path.lineTo(x, y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(path, 100L, 50L));
        GestureDescription gesture = builder.build();
        mService.dispatchGesture(gesture, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
            }
        }, null);
    }

    /**
     * 左滑
     */
    private void MainActivityToLeft() {
        final int x = ScreenUtils.getScreenWidth(mService);
        final int y = ScreenUtils.getScreenHeight(mService) / 2;
        Path path = new Path();
        path.moveTo(x - 100, y);
        path.lineTo(0, y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(path, 100L, 50L));
        GestureDescription gesture = builder.build();
        mService.dispatchGesture(gesture, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
            }
        }, null);
    }

    /**
     * 向上滑动视频
     */
    private void MainActivityToUp() {
        final int x = ScreenUtils.getScreenWidth(mService) / 2;
        final int y = ScreenUtils.getScreenHeight(mService) / 2;
        Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(x, 0);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(path, 100L, 50L));
        GestureDescription gesture = builder.build();
        mService.dispatchGesture(gesture, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
            }
        }, null);
    }

    /**
     * 检测内存的使用情况,退出桌面的时候调用
     */
    public void checkMemory(){
        double usedRatio = SystemInfoUtils.getUsedMemoryRatio(mService);
        if(usedRatio > 60){
            ToastUtils.showToast(mService, "清理内存");
            //执行清理内存的操作
            perforGlobalClick("com.miui.home:id/progressBar");
        }
    }

    /**
     * 根据ID点击某个视图
     */
    public void perforGlobalClick(String id) {
        AccessibilityNodeInfo nodeInfo = mService.getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(id);
            nodeInfo.recycle();
            if(list.size()<1){
                Log.e(TAG,"无法获取清理内存软件..."+list.size()+  ", "+id+"");
            }
            for (AccessibilityNodeInfo item : list) {
                if(id == "com.miui.home:id/progressBar") {
                    item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                break;
            }
        }
    }
}
