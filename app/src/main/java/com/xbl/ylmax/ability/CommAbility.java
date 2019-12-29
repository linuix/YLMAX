package com.xbl.ylmax.ability;

import android.view.accessibility.AccessibilityNodeInfo;

import com.xbl.ylmax.utils.NodeUtil;
import com.xbl.ylmax.utils.ToastUtils;

import java.util.List;

/**
 * Author: link
 * Create: 2019-2019/12/28 0028-21:55
 * Changes (from 2019/12/28 0028)
 * 2019/12/28 0028 : Create CommAbility.java (link);
 **/
public class CommAbility extends Ability {

    private static CommAbility commAbility = new CommAbility();

    public static CommAbility getInstance(){
        return commAbility;
    }


    public void ignoreUpdate(){
        AccessibilityNodeInfo accessibilityNodeInfo = mService.getRootInActiveWindow();
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = NodeUtil.findNodeForText(accessibilityNodeInfo,"以后再说");
        if (accessibilityNodeInfoList.size() > 0){
            accessibilityNodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            ToastUtils.showToast("忽略升级成功！");
        }
    }

}
