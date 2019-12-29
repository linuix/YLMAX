package com.xbl.ylmax.ability;

import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.xbl.ylmax.APP;
import com.xbl.ylmax.utils.NodeUtil;
import com.xbl.ylmax.utils.ScreenUtils;

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


    public void obtainUserFlow(){
        AccessibilityNodeInfo rootNodeInfo = mService.getRootInActiveWindow();
        if (rootNodeInfo == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        NodeUtil.getNodeByClassName(sb, rootNodeInfo, "android.widget.TextView");
        Log.d(TAG, "obtainUserFlow: sb =  "+sb.toString());
    }


    public void doubleClick(){
        int scx = ScreenUtils.getScreenWidth(APP.getInstance()) / 2;
        int scy = ScreenUtils.getScreenHeight(APP.getInstance()) / 2;
        Log.d(TAG, "doubleClick: scx = "+scx+"---scy = "+scy);
        NodeUtil.clickPoint(mService,scx,scy,3);
        SystemClock.sleep(200);
        NodeUtil.clickPoint(mService,scx,scy,3);

    }
}
