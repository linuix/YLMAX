package com.xbl.ylmax.ability;

import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.xbl.ylmax.APP;
import com.xbl.ylmax.utils.NetUtil;
import com.xbl.ylmax.utils.NodeUtil;
import com.xbl.ylmax.utils.SPUtil;
import com.xbl.ylmax.utils.ToastUtils;

import java.util.List;

/**
 * Author: link
 * Create: 2019-2019/12/28 0028-22:06
 * Changes (from 2019/12/28 0028)
 * 2019/12/28 0028 : Create UserAbility.java (link);
 **/
public class UserAbility extends Ability {

    private static final String TAG = "UserAbility";
    public static UserAbility userAbility = new UserAbility();

    public boolean isWorked = false;
    public boolean hasImgUpdaled = false;

    private String workStatus = "workStatus";
    private String key_imgUpdate = "imgupdate";

    public static UserAbility getInstance(){

        return userAbility;
    }



    @Override
    public void init(AccessibilityService accessibilityService) {
        super.init(accessibilityService);
        isWorked = SPUtil.readDataFromKey(workStatus,Boolean.class);
        Log.d(TAG, "init: isWorked = "+isWorked);

    }

    public void updateNickName(){
        AccessibilityNodeInfo accessibilityNodeInfo = mService.getRootInActiveWindow();
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = NodeUtil.findNodeForText(accessibilityNodeInfo, "请输入名字");
        if (accessibilityNodeInfoList.size() > 0){
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, NetUtil.nickName);
            accessibilityNodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,arguments);
            isWorked = true;
            SPUtil.writeData(workStatus,isWorked);
            ToastUtils.showToast("修改名称成功！");
        }
    }


    public void gotoEdit() {

        AccessibilityNodeInfo accessibilityNodeInfo = mService.getRootInActiveWindow();
        NodeUtil.clickNodeForTxt(mService,accessibilityNodeInfo,"编辑资料");
        ToastUtils.showToast("编辑资料点击成功！");
    }

    public void showUpdateImg() {
        if (hasImgUpdaled){
            return;
        }
        AccessibilityNodeInfo accessibilityNodeInfo = mService.getRootInActiveWindow();
        NodeUtil.clickNodeForTxt(mService,accessibilityNodeInfo,"点击更换头像","android.widget.ImageView");
        ToastUtils.showToast("点击更换头像成功！");
    }

    public void editImg() {
        AccessibilityNodeInfo accessibilityNodeInfo = mService.getRootInActiveWindow();
        NodeUtil.clickNodeForTxt(mService,accessibilityNodeInfo,"相册选择");
        ToastUtils.showToast("相册选择成功！");
    }

    public void selectImg() {
        AccessibilityNodeInfo accessibilityNodeInfo = mService.getRootInActiveWindow();
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = NodeUtil.findeNodeForClassName(accessibilityNodeInfo,"android.view.View");
        List<AccessibilityNodeInfo> okNodeInfoList = NodeUtil.findNodeForText(accessibilityNodeInfo,"确定");
        if (accessibilityNodeInfoList!=null && accessibilityNodeInfoList.size() > 0){
            accessibilityNodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            if (okNodeInfoList != null && okNodeInfoList.size()>0){
                okNodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                ToastUtils.showToast("选择成功！");
            }
        }


    }

    public void cropImg() {
        AccessibilityNodeInfo accessibilityNodeInfo = mService.getRootInActiveWindow();
        NodeUtil.clickNodeForTxt(mService,accessibilityNodeInfo,"完成");
        ToastUtils.showToast("裁剪成功！");
        hasImgUpdaled = true;
        SPUtil.writeData(key_imgUpdate,hasImgUpdaled);
        APP.runWorkThread(new Runnable() {
            @Override
            public void run() {
                NodeUtil.goHome(mService);
            }
        },5000);
    }
}
