package com.xbl.ylmax.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.TargetApi;
import android.content.Context;
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
    public static final String TAKE_PHOTO_ACTIVITY="com.ss.android.ugc.aweme.shortvideo.ui.VideoRecordNewActivity";
    public static final String SELECT_VIDEO_ACTIVITY="com.ss.android.ugc.aweme.shortvideo.mvtemplate.choosemedia.MvChoosePhotoActivity";
    public static final String CROP_VIDEO_ACTIVITY = "com.ss.android.ugc.aweme.shortvideo.cut.VECutVideoActivity";
    public static final String BROWSE_VIDEO_ACTIVITY = "com.ss.android.ugc.aweme.shortvideo.edit.AIMusicVEVideoPublishEditActivity";
    public static final String EDIT_TXT_FOR_VIDEO_ACTIVITY = "com.ss.android.ugc.aweme.shortvideo.ui.VideoPublishActivity";



    public static final String cd = "com.ss.android.ugc.aweme.main.cd";
    //com.ss.android.ugc.aweme.update.m --升级提醒
    //com.ss.android.ugc.aweme.profile.ui.widget.aa

    public static final String updataLevelClass = "com.ss.android.ugc.aweme.update.m";
    public static final String frendClass = "com.ss.android.ugc.aweme.main.cd";

    public static final String FRAMElAYOUT = "android.widget.FrameLayout";



    //com.ss.android.ugc.aweme.profile.ui.widget.aa--修改名称的弹框
    public static final String updateNameClass = "com.ss.android.ugc.aweme.profile.ui.widget.aa";

    public static final String updateUserImgClass = "com.bytedance.ies.uikit.dialog.AlertDialog";

    public static final String childModelClass = "android.app.AlertDialog";

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
    public void onAccessibilityEvent(final AccessibilityEvent event) {
        try {
            this.event = event;
            int type = event.getEventType();
            if (type == 0x800){
                return;
            }
            if (TextUtils.isEmpty(NetUtil.deviceId)){
                Log.e(TAG, "deviceId is null");
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
                                        if (LoginAbility.getInstance().isFirst){
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
                                        }else if (KeepAliveAbility.getInstance().isUploadVideo)
                                        {
                                            KeepAliveAbility.getInstance().startUploadVideo();
                                            return;
                                        }
                                        KeepAliveAbility.getInstance().startAlive();


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
                            case childModelClass:{
                                APP.runWorkThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        CommAbility.getInstance().ignoreChildMode();
                                    }
                                },DelayUtil.getCommTime());
                                break;
                            }
                            case TAKE_PHOTO_ACTIVITY:{
                                if (KeepAliveAbility.getInstance().isUploadVideo){
                                    APP.runWorkThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            KeepAliveAbility.getInstance().startSelectVideo();
                                        }
                                    },DelayUtil.getCommTime());
                                }
                                break;
                            }
                            case SELECT_VIDEO_ACTIVITY:{
                                if (KeepAliveAbility.getInstance().isUploadVideo) {
                                    KeepAliveAbility.getInstance().selectVideo();
                                }
                                break;
                            }
                            case BROWSE_VIDEO_ACTIVITY:
                            case CROP_VIDEO_ACTIVITY:{
                                if (KeepAliveAbility.getInstance().isUploadVideo){
                                    APP.runWorkThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            KeepAliveAbility.getInstance().cropVideo();
                                        }
                                    },DelayUtil.getCommTime());
                                }
                                break;
                            }
                            case EDIT_TXT_FOR_VIDEO_ACTIVITY:{
                                if (KeepAliveAbility.getInstance().isUploadVideo){
                                    KeepAliveAbility.getInstance().addTitleAndPublish();
                                }
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

}
