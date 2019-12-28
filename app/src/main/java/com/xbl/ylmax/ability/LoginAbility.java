package com.xbl.ylmax.ability;


import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.xbl.ylmax.utils.NodeUtil;
import com.xbl.ylmax.utils.ToastUtils;

import java.util.List;


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
        if (accessibilityNodeInfoList.size() > 0){
            accessibilityNodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            ToastUtils.showToast("抖音打开成功！");
        }
    }


    public void register() {

    }

    public void gotoUserCenter(){
        AccessibilityNodeInfo accessibilityNodeInfo = mService.getRootInActiveWindow();
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = NodeUtil.findNodeForText(accessibilityNodeInfo,"我");
        for (int i = 0; i<accessibilityNodeInfoList.size() ;i++){
            if (accessibilityNodeInfoList.get(i).getParent().isClickable()){
                boolean res = accessibilityNodeInfoList.get(i).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                ToastUtils.showToast("进入我的界面");
                return;
            }else {
                Rect rectangle = new Rect();
                accessibilityNodeInfoList.get(i).getBoundsInScreen(rectangle);
                int x = rectangle.centerX();
                int y = rectangle.centerY();
                NodeUtil.clickPoint(mService,x,y,100);
                Log.d(TAG, "gotoUserCenter: ");
            }
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
