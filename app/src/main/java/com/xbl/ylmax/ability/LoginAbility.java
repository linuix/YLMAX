package com.xbl.ylmax.ability;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.xbl.ylmax.APP;
import com.xbl.ylmax.utils.NetUtil;
import com.xbl.ylmax.utils.NodeUtil;
import com.xbl.ylmax.utils.ToastUtils;

import java.util.List;

import androidx.annotation.RequiresApi;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.xbl.ylmax.utils.NodeUtil.findNodeForText;
import static com.xbl.ylmax.utils.NodeUtil.targetNodeInfo;


/**
 * Author: link
 * Create: 2019-2019/12/28 0028-11:51
 * Changes (from 2019/12/28 0028)
 * 2019/12/28 0028 : Create LoginAbility.java (link);
 **/
public class LoginAbility extends Ability {

    private static final String TAG = "LoginAbility";
    private static LoginAbility loginAbility = new LoginAbility();

    public static LoginAbility getInstance(){
        return loginAbility;
    }

    public void openDY(AccessibilityEvent event){
        AccessibilityNodeInfo accessibilityNodeInfo = mService.getRootInActiveWindow();
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = NodeUtil.findNodeForText(accessibilityNodeInfo,"抖音短视频");
        Log.d(TAG, "openDY: accessibilityNodeInfoList.size() = "+accessibilityNodeInfoList.size());
        if (accessibilityNodeInfoList.size() > 0){
            accessibilityNodeInfoList.get(2).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            ToastUtils.showToast("抖音打开成功！");
        }
    }


    public void register() {

    }


    public void inputPhoneNumber(){

        AccessibilityNodeInfo accessibilityNodeInfo = mService.getRootInActiveWindow();
        targetNodeInfo.clear();
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = NodeUtil.findeNodeForClassName(accessibilityNodeInfo,"android.widget.EditText");
        Log.d(TAG, "inputPhoneNumber: accessibilityNodeInfoList.size() = " +accessibilityNodeInfoList.size());
        if (accessibilityNodeInfoList.size()>1){
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, NetUtil.phoneNumber);
            accessibilityNodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,arguments);

            final AccessibilityNodeInfo msgCodeNodeInfo = accessibilityNodeInfoList.get(1);
            List<AccessibilityNodeInfo> infoList = findNodeForText(accessibilityNodeInfo,"获取验证码");
            if (infoList.size()>0){
                infoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                APP.runWorkThread(new Runnable() {
                    @Override
                    public void run() {
                        NetUtil.obtainMsgCode();
                    }
                },5000);
            }

            APP.workHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Bundle arguments = new Bundle();
                    arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, NetUtil.msgCode);
                    msgCodeNodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,arguments);
                }
            },6000);
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void gotoUserCenter(){
        AccessibilityNodeInfo accessibilityNodeInfo = mService.getRootInActiveWindow();
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = NodeUtil.findNodeForText(accessibilityNodeInfo,"我");
        for (int i = 0; i<accessibilityNodeInfoList.size() ;i++){
//            if (accessibilityNodeInfoList.get(i).getParent().isClickable()){
//                boolean res = accessibilityNodeInfoList.get(i).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                ToastUtils.showToast("进入我的界面"+res);
//                return;
//            }else {
                Rect rectangle = new Rect();
                accessibilityNodeInfoList.get(i).getBoundsInScreen(rectangle);
                int x = rectangle.centerX();
                int y = rectangle.centerY();
                NodeUtil.clickPoint(mService,x,y,100);
                Log.d(TAG, "gotoUserCenter: ");
//            }
        }
    }



    public void skipUpdate(){
        AccessibilityNodeInfo accessibilityNodeInfo = mService.getRootInActiveWindow();
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = NodeUtil.findNodeForText(accessibilityNodeInfo,"检测到新版本");
        if (accessibilityNodeInfoList.size()>0) {
            List<AccessibilityNodeInfo> skipNodeList = NodeUtil.findNodeForText(accessibilityNodeInfo,"以后在说");
            if (skipNodeList.size()>0){
                Log.d(TAG, "skipUpdate: skipNodeList.size() = "+skipNodeList.size());
                skipNodeList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                ToastUtils.showToast("跳过升级！");
            }
        }
    }



}
