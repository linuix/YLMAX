package com.xbl.ylmax.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.xbl.ylmax.APP;
import com.xbl.ylmax.MainActivity;
import com.xbl.ylmax.ability.CommAbility;
import com.xbl.ylmax.ability.FollowAbility;
import com.xbl.ylmax.ability.KeepAliveAbility;
import com.xbl.ylmax.ability.LoginAbility;
import com.xbl.ylmax.ability.UserAbility;
import com.xbl.ylmax.constString.ConstString;
import com.xbl.ylmax.utils.DelayUtil;
import com.xbl.ylmax.utils.NetUtil;
import com.xbl.ylmax.utils.NodeUtil;
import com.xbl.ylmax.utils.RandomUtil;
import com.xbl.ylmax.utils.ScreenUtils;
import com.xbl.ylmax.utils.SystemInfoUtils;
import com.xbl.ylmax.utils.ToastUtils;

import java.util.List;
import java.util.Random;

import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_SELECTED;

public class DYService extends AccessibilityService {
    private static final String TAG = "DYService";
    public static Context context;

    public static final String LAUNCH_PACKAGE = "com.miui.home";
    public static final String DY_PACKAGE = "com.ss.android.ugc.aweme";

    public static final String luanchActivity = "com.miui.home.launcher.Launcher";



    public static final String LOGIN_ACTIVITY = "com.ss.android.ugc.aweme.account.login.ui.LoginOrRegisterActivity";
    public static final String DY_MAINACTIVITY = "com.ss.android.ugc.aweme.main.MainActivity";
    public static final String INFO_EDIT_ACTIVITY = "com.ss.android.ugc.aweme.profile.ui.ProfileEditActivity";
    public static final String SELECT_IMG_ACTIVITY = "com.zhihu.matisse.ui.MatisseActivity";
    public static final String CROP_IMG_ACTIVITY = "com.ss.android.ugc.aweme.profile.ui.CropActivity";


    public static final String cd = "com.ss.android.ugc.aweme.main.cd";
    //com.ss.android.ugc.aweme.update.m --升级提醒
    //com.ss.android.ugc.aweme.profile.ui.widget.aa

    public static final String updataLevelClass = "com.ss.android.ugc.aweme.update.m";
    public static final String frendClass = "com.ss.android.ugc.aweme.main.cd";

    public static final String FRAMElAYOUT = "android.widget.FrameLayout";


    //com.ss.android.ugc.aweme.profile.ui.widget.aa--修改名称的弹框
    public static final String updateNameClass = "com.ss.android.ugc.aweme.profile.ui.widget.aa";

    public static final String updateUserImgClass = "com.bytedance.ies.uikit.dialog.AlertDialog";

    private AccessibilityEvent event;

    private int viewViedoTime = 15000;



    private int delayTime = 3000;

    public Handler monitorHandler;
    public HandlerThread handlerThread;

    public final long monitorTime = 180000;


    @Override
    public void onCreate() {
        super.onCreate();
        //TODO
        //获取配置 1.第三方码平台账号 2.后台全局设置参数相关
        context = getApplicationContext();
        ToastUtils.showToast(context, "启动服务");
        Log.d(TAG, "onCreate: ");

        //新开线程启动APP，防止主线程阻塞
//        Thread newThread  = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                startApplication();
//
//            }
//        });
//        newThread.start();

    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        //监听音量键关闭服务
//        Log.d(TAG, "onKeyEvent: type = "+event.getKeyCode());
//        Log.d(TAG, "action = "+event.getAction());
//        if (KeyEvent.ACTION_UP == event.getAction()){
//            switch (event.getKeyCode()){
//                case KeyEvent.KEYCODE_HOME:{
//                    APP.runWorkThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            KeepAliveAbility.getInstance().checkMemory();
//                        }
//                    },500);
//                    break;
//                }
//            }
//        }
        return super.onKeyEvent(event);
    }

    @Override
    public void onAccessibilityEvent(final AccessibilityEvent event) {
        try {
            this.event = event;
            int type = event.getEventType();
            if (type == 0x800){
                return;
            }
            if (TextUtils.isEmpty(NetUtil.deviceId)){
                return;
            }
            if (type == TYPE_VIEW_SELECTED){
                return;
            }
            String typeStr = event.eventTypeToString(type);
            Log.d(TAG, "onAccessibilityEvent: typeStr = " + typeStr);
            // 判断我们的辅助功能是否在约定好的应用界面执行，以设置界面为例
            CharSequence packageName = event.getPackageName();
            CharSequence className = event.getClassName();

            Log.d(TAG, " -------------------------package = " + packageName+"-----------------");
            Log.d(TAG, " -------------------------className = " + className+"-----------------");
            if (packageName == null || className == null){
                return;
            }
            switch (type) {
                case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED: {
                    if (LAUNCH_PACKAGE.equals(packageName)&& luanchActivity.equals(className)) {
                        KeepAliveAbility.getInstance().checkMemory();
                        APP.runWorkThread(new Runnable() {
                            @Override
                            public void run() {
                                LoginAbility.getInstance().openDY(event);
                            }
                        }, DelayUtil.getCommTime());
                    } else if (DY_PACKAGE.equals(packageName)) {
                        switch (className.toString()) {
                            case LOGIN_ACTIVITY: //登陆界面
                                APP.runWorkThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        LoginAbility.getInstance().inputPhoneNumber();
                                    }
                                }, delayTime);

                                break;
                            case DY_MAINACTIVITY: //主界面
                                APP.runWorkThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!LoginAbility.getInstance().isFirst) {
                                            KeepAliveAbility.getInstance().startAlive();
                                            return;
                                        }
                                        LoginAbility.getInstance().gotoUserCenter();
                                        APP.runWorkThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (!UserAbility.getInstance().hasImgUpdaled){
                                                    UserAbility.getInstance().gotoEdit();
                                                }else if (KeepAliveAbility.getInstance().fans == -1){
                                                    KeepAliveAbility.getInstance().obtainUserFlow();
                                                }else{
                                                    KeepAliveAbility.getInstance().startAlive();
                                                }

                                            }
                                        }, delayTime);
//                                KeepAliveAbility.getInstance().obtainUserFlow();
                                    }
                                }, DelayUtil.getViewVideoTime());
                                break;
                            case updataLevelClass:
                                APP.runWorkThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        CommAbility.getInstance().ignoreUpdate();
                                    }
                                }, delayTime);
                                break;
                            case updateNameClass:
                                APP.runWorkThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        UserAbility.getInstance().updateNickName();
                                    }
                                }, delayTime);

                                break;
                            case INFO_EDIT_ACTIVITY:
                                APP.runWorkThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        UserAbility.getInstance().showUpdateImg();
                                    }
                                }, delayTime);
                                break;
                            case updateUserImgClass:
                                APP.runWorkThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        UserAbility.getInstance().editImg();
                                    }
                                }, delayTime);

                                break;
                            case SELECT_IMG_ACTIVITY:

                                APP.runWorkThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        UserAbility.getInstance().selectImg();
                                    }
                                }, delayTime);
                                break;
                            case CROP_IMG_ACTIVITY:

                                APP.runWorkThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        UserAbility.getInstance().cropImg();
                                    }
                                }, delayTime);
                                break;
                            case frendClass:{
                                APP.runWorkThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        CommAbility.getInstance().ignoreFrend();
                                    }
                                },DelayUtil.getCommTime());
                                break;
                            }
                        }
                    }
                }
                case AccessibilityEvent.TYPE_VIEW_FOCUSED:{
                    if (KeepAliveAbility.getInstance().isStart && FRAMElAYOUT.equals(className)){
                        APP.runWorkThread(new Runnable() {
                            @Override
                            public void run() {
                                if (RandomUtil.isLike()){
                                    KeepAliveAbility.getInstance().doubleClick();
                                }
                                APP.runWorkThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        KeepAliveAbility.getInstance().MainActivityToUp();
                                    }
                                },DelayUtil.getViewVideoTime()-DelayUtil.getLickTime());
                            }
                        },DelayUtil.getLickTime());
                    }
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

//        CharSequence className = event.getClassName();
//        if(className != null)
//        {
//            Log.e("你好-》", className.toString());
//            //显示更新提示框
//            if (event.getClassName().toString().equals("com.ss.android.ugc.aweme.update.j")) {
//                perforGlobalClick("com.ss.android.ugc.aweme:id/bqi");//点击"以后再说"
//            }
//            //跳过广告
//            if (event.getClassName().toString().equals("com.ss.android.ugc.aweme.splash.SplashActivity")) {
//                perforGlobalClick("com.ss.android.ugc.aweme:id/ce2");//点击"跳过广告"
//            }
//            //点赞
//            if ("android.widget.ImageView".equals(event.getClassName().toString())) {
//                perforGlobalClick("com.ss.android.ugc.aweme:id/a4x");
//                Log.i("点赞", "用户名:" + getTextById(" id:com.ss.android.ugc.aweme:id/title"));
//                Log.i("点赞", "文本信息:" + getTextById(" id:com.ss.android.ugc.aweme:id/a3m"));
//            }
//        }


    }

    @Override
    public void onInterrupt() {

    }


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected: ");
        LoginAbility.getInstance().init(this);
        CommAbility.getInstance().init(this);
        UserAbility.getInstance().init(this);
        KeepAliveAbility.getInstance().init(this);
        FollowAbility.getInstance().init(this);

        handlerThread = new HandlerThread("monitor");
        handlerThread.start();
        monitorHandler = new Handler(handlerThread.getLooper());
        monitorHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (TextUtils.isEmpty(APP.getInstance().oldTxt)){
                    APP.getInstance().oldTxt = APP.getInstance().msgTxt;
                }else if (APP.getInstance().oldTxt.equals(APP.getInstance().msgTxt)){
                    NodeUtil.goHome(DYService.this);
                }
                APP.getInstance().oldTxt = APP.getInstance().msgTxt;
                monitorHandler.postDelayed(this,monitorTime);
            }
        },monitorTime);
    }

    public void mySleep(int m) {
        try {
            Thread.sleep(m * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过模拟点击的方式启动app
     */
    public boolean startApplication() {
        String appName = "抖音短视频";
        mySleep(3);
        ToastUtils.showToast(context, "返回桌面");
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
        mySleep(5);
        ToastUtils.showToast(context, "再次返回桌面");
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
        mySleep(5);
        AccessibilityNodeInfo deskNode = getRootInActiveWindow();
        if (deskNode != null) {
            List<AccessibilityNodeInfo> applist = deskNode.findAccessibilityNodeInfosByText(appName);
            if (applist.size() < 1) {
                Log.i("TIAOSHI###", "桌面环境下没有applist，直接返回了...");
                return false;
            }
            ToastUtils.showToast(context, "启动抖音");
            AccessibilityNodeInfo appNode = applist.get(0);
            Rect rect = new Rect();
            appNode.getBoundsInScreen(rect);
            int x = (rect.left + rect.right) / 2;
            int y = (rect.top + rect.bottom) / 2;
            clickPoint(x, y, 300);
            mySleep(10);
            //perforGlobalClick("com.ss.android.ugc.aweme:id/a4x");
            ToastUtils.showToast(context, "视频随机浏览");
            MainActivityToUp();
            mySleep(10);
            getNodeByClassName("android.support.v4.view.ViewPager", "视频");
            mySleep(10);
            MainActivityToUp();
            mySleep(10);
            //getNodeByClassName("android.widget.LinearLayout", "喜欢");
            //mySleep(10);
            //perforGlobalClick("com.ss.android.ugc.aweme:id/dp2");
            mySleep(5);
//            String huozhan = getTextById("com.ss.android.ugc.aweme:id/a4d");
//            String guanzhu = getTextById("com.ss.android.ugc.aweme:id/ahf");
//            String fensi = getTextById("com.ss.android.ugc.aweme:id/ahb");
            //Log.e("TIAOSHI###","数据：（"+huozhan+","+guanzhu+","+fensi+")");
            //mySleep(6);
            ToastUtils.showToast(context, "返回桌面");
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
            mySleep(10);
            checkMemory();
            mySleep(30);
            startApplication();
            //perforGlobalClick("com.ss.android.ugc.aweme:id/ahb");
            return true;
        } else {
            Log.e("TIAOSHI###", "没有拿到miui的桌面list哦");
        }
        return false;
    }


    /**
     * 点击
     *
     * @param x1       X轴
     * @param y1       Y轴
     * @param duration 延时
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void clickPoint(float x1, float y1, long duration) {
        Path path = new Path();
        path.moveTo(x1, y1);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription gestureDescription = builder
                .addStroke(new GestureDescription.StrokeDescription(path, 0, duration))
                .build();
        boolean b = dispatchGesture(gestureDescription, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.e("TIAOSHI###", "点击结束..." + gestureDescription.getStrokeCount());
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.e("TIAOSHI###", "点击取消");
            }
        }, null);
    }

    /**
     * 左滑
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void MainActivityToLeft() {
        final int x = ScreenUtils.getScreenWidth(this) / 2;
        final int y = ScreenUtils.getScreenHeight(this) / 2;
        Log.e("TIAOSHI###", "MyAccessibilityService中滑动左滑动点,reset：（" + x + "," + y + ")");
        Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(0, y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(path, 100L, 50L));
        GestureDescription gesture = builder.build();
        dispatchGesture(gesture, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.e("滑动", "onCompleted: 主页左滑.");
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
            }
        }, null);
    }

    /**
     * 滑动视频
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void MainActivityToUp() {
        final int x = ScreenUtils.getScreenWidth(this) / 2;
        final int y = ScreenUtils.getScreenHeight(this) / 2;
        Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(x, 0);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(path, 100L, 50L));
        GestureDescription gesture = builder.build();
        dispatchGesture(gesture, new GestureResultCallback() {
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
     * 判断页面是否有此文本的view
     */
    private boolean viewByText(String str) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(str);
            nodeInfo.recycle();
            for (AccessibilityNodeInfo item : list) {
                if (str.equals(item.getText().toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param className 类名
     * @param text      描述
     * @return
     */
    private AccessibilityNodeInfo getNodeByClassName(String className, String text) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo shiPinNode = null;
        if (nodeInfo != null) {
            shiPinNode = recycle(nodeInfo.getChild(0), className, text);
        }

        //获取个人图标位置
        if (shiPinNode != null) {
            int count = shiPinNode.getChildCount();
            for (int i = 0; i < count; i++) {
                AccessibilityNodeInfo guanZhuNode = nodeInfo.getChild(i);
                if (guanZhuNode.getContentDescription() != null && guanZhuNode.getContentDescription() == "关注" && guanZhuNode.getClassName().equals("android.widget.Button")) {
                    ConstString.guanZhuId = nodeInfo.getViewIdResourceName();
                    ConstString.personId = nodeInfo.getChild(i + 1).getViewIdResourceName();
                    break;
                }
            }
        }
        return nodeInfo;
    }

    /**
     * 从根节点节点开始向下查找指定类名的组件（深度遍历），在找到一个符合之后就会结束
     *
     * @param classNames 类名（可多个），每进行一次节点的深度遍历，都会遍历一遍这里传入来的类名，找到了就立即返回
     * @return 最后找到的节点
     */
    @Nullable
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected AccessibilityNodeInfo getNodeByClassName(@NonNull String... classNames) {
        AccessibilityNodeInfo rootNodeInfo = getRootInActiveWindow();
        if (rootNodeInfo == null) {
            return null;
        }
        AccessibilityNodeInfo result = getNodeByClassName(rootNodeInfo, classNames);
        rootNodeInfo.recycle();
        return result;
    }

    /**
     * 从指定的节点开始向下查找指定类名的组件（深度遍历），在找到一个符合之后就会结束
     *
     * @param nodeInfo   起始节点
     * @param classNames 类名（可多个），每进行一次节点的深度遍历，都会遍历一遍这里传入来的类名，找到了就立即返回
     * @return 最后找到的节点
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected AccessibilityNodeInfo getNodeByClassName(@NonNull AccessibilityNodeInfo nodeInfo, @NonNull String... classNames) {
        if (nodeInfo.getChildCount() == 0) {
            return null;
        }
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo childNodeInfo = nodeInfo.getChild(i);
//            if (DLog.isDebug()) {
//                StringBuilder sb = new StringBuilder(classNames.length);
//                for (String className : classNames) {
//                    sb.append(className).append(" ");
//                }
//            }
            for (String className : classNames) {
                if (childNodeInfo.getClassName().toString().equals(className)) {
                    return childNodeInfo;
                }
            }
            AccessibilityNodeInfo switchOrCheckBoxNodeInfo = getNodeByClassName(childNodeInfo, classNames);
            if (switchOrCheckBoxNodeInfo != null) {
                return switchOrCheckBoxNodeInfo;
            }
        }
        return null;
    }

    public AccessibilityNodeInfo recycle(AccessibilityNodeInfo info, String className, String text) {
        for (int i = 0; i < info.getChildCount(); i++) {
            if (info.getContentDescription() == null) continue;
            if (info.getContentDescription() != null && info.getClassName().equals(className) && info.getContentDescription().toString().contains(text)) {
                ToastUtils.showToast(context, "" + info.getContentDescription() + "");
                return info;
            } else {
                return recycle(info.getChild(i), className, text);
            }
        }
        return null;
    }
//        AccessibilityNodeInfo dectInfo = null;
//        if (info.getChildCount() == 0) {
//            Log.i("", "child widget----------------------------" + info.getClassName() + "   " + info.getContentDescription());
//            return info;
//        } else {
//
//        }

    /**
     * 获取指定ID的文本
     */
    private String getTextById(String id) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(id);
            nodeInfo.recycle();
            for (AccessibilityNodeInfo item : list) {
                return item.getText() + "";
            }
        }
        return "";
    }

    /**
     * 根据ID点击某个视图
     */
    public void perforGlobalClick(String id) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(id);
            nodeInfo.recycle();
            if (list.size() < 1) {
                Log.e("TIAOSHI###", "无法获取清理内存软件..." + list.size() + ", " + id + "");
            }
            for (AccessibilityNodeInfo item : list) {
                if (id == "com.miui.home:id/progressBar") {
                    item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                break;
            }
        }
    }

    /**
     * 检测内存的使用情况,退出桌面的时候调用
     */
    public void checkMemory() {
        double usedRatio = SystemInfoUtils.getUsedMemoryRatio(context);
        //ToastUtils.showToast(context, "内存使用情况:"+usedRatio+"");
        if (usedRatio > 40) {
            ToastUtils.showToast(context, "清理内存");
            //执行清理内存的操作
            perforGlobalClick("com.miui.home:id/progressBar");
        }
    }
}
