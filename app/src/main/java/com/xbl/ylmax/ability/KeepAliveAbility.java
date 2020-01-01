package com.xbl.ylmax.ability;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.xbl.ylmax.APP;
import com.xbl.ylmax.utils.DelayUtil;
import com.xbl.ylmax.utils.NetUtil;
import com.xbl.ylmax.utils.NodeUtil;
import com.xbl.ylmax.utils.ScreenUtils;
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
    public int fans = -1;
    public int flow = -1;
    public int like = -1;

    public boolean isStart = false;







    private static KeepAliveAbility keepAliveAbility = new KeepAliveAbility();
    public boolean isUploadVideo = true;

    public static KeepAliveAbility getInstance(){
        return keepAliveAbility;
    }

    /**
     * 获取个人粉丝数量相关
     */
    public void obtainUserFlow(){
        try {
            AccessibilityNodeInfo rootNodeInfo = mService.getRootInActiveWindow();
            if (rootNodeInfo == null) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            NodeUtil.getNodeByClassName(sb, rootNodeInfo, "android.widget.TextView");
            Log.d(TAG, "obtainUserFlow: sb =  "+sb.toString());
            like = Integer.valueOf(sb.toString().split(",")[0]);
            flow = Integer.valueOf(sb.toString().split(",")[1]);
            fans = Integer.valueOf(sb.toString().split(",")[2]);
            NetUtil.upLoadUserInfo(flow,fans);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        ToastUtils.showToast("点赞成功！");
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
    public void MainActivityToUp() {
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
        ToastUtils.showToast("下一个视频");

    }

    /**
     * 检测内存的使用情况,退出桌面的时候调用
     */
    public void checkMemory(){
//        double usedRatio = SystemInfoUtils.getUsedMemoryRatio(mService);
//        if(usedRatio > 60){
//
//        }
        ToastUtils.showToast(mService, "清理内存");
        //执行清理内存的操作
        perforGlobalClick("com.miui.home:id/progressBar");
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


    public void startAlive() {
        AccessibilityNodeInfo nodeInfo = mService.getRootInActiveWindow();
        NodeUtil.clickNodeForTxt(mService,nodeInfo,"首页");
        ToastUtils.showToast("返回首页");
        APP.runWorkThread(new Runnable() {
            @Override
            public void run() {
                MainActivityToUp();
                isStart = true;
                ToastUtils.showToast("浏览下一个视频");
            }
        }, DelayUtil.getViewVideoTime());
    }

    /**
     * 开始上传视频
     */
    public void startUploadVideo() {
        AccessibilityNodeInfo nodeInfo = mService.getRootInActiveWindow();
        NodeUtil.clickNodeForTxt(mService,nodeInfo,"拍摄，按钮");
        ToastUtils.showToast("点击拍摄按钮成功！");
    }

    /**
     * 开始选择视频
     */
    public void startSelectVideo() {
        AccessibilityNodeInfo nodeInfo = mService.getRootInActiveWindow();
        NodeUtil.clickNodeForTxt(mService,nodeInfo,"上传",0,-47);
        ToastUtils.showToast("点击上传成功！");
    }

    /**
     * 选择视频
     */
    public void selectVideo() {
        AccessibilityNodeInfo nodeInfo = mService.getRootInActiveWindow();
        NodeUtil.clickNodeForTxt(mService,nodeInfo,"视频",-77,115);
        ToastUtils.showToast("选择视频成功！");
    }

    public void cropVideo() {
        AccessibilityNodeInfo nodeInfo = mService.getRootInActiveWindow();
        NodeUtil.clickNodeForTxt(mService,nodeInfo,"下一步");
        ToastUtils.showToast("选择视频成功！");
    }

    public void addTitleAndPublish() {
        AccessibilityNodeInfo nodeInfo = mService.getRootInActiveWindow();

        NodeUtil.addTextToNode(mService,nodeInfo,"写标题",NetUtil.deviceInfo.getVideoTitle());
        ToastUtils.showToast("添加标题成功！");
        APP.runWorkThread(new Runnable() {
            @Override
            public void run() {
                NodeUtil.clickNedeForTxt(mService,mService.getRootInActiveWindow(),"发布",1);
                isUploadVideo = false;
                ToastUtils.showToast("发布成功！");
            }
        },DelayUtil.getCommTime());

    }
}
