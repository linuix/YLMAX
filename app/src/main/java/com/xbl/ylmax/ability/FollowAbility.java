package com.xbl.ylmax.ability;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.xbl.ylmax.utils.NodeUtil;
import com.xbl.ylmax.utils.ScreenUtils;

import java.util.List;

public class FollowAbility extends Ability {


    private static FollowAbility followAbility = new FollowAbility();

    public static FollowAbility getInstance(){
        return followAbility;
    }

    /**
     * 点击关注
     */
    public void clickFollow(){
        AccessibilityNodeInfo nodeInfo = mService.getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("关注");
            nodeInfo.recycle();
            for (AccessibilityNodeInfo item : list) {
                if ("关注".equals(item.getText().toString())) {
                    item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    SystemClock.sleep(5000);
                }
            }
        }
    }

    /**
     * 向上滑动加关注
     */
    private void FollowToUp() {
        final int x = ScreenUtils.getScreenWidth(mService) / 2;
        final int y = ScreenUtils.getScreenHeight(mService) - 200;
        Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(x, y / 2);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(path, 100L, 2000L));
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
     * 点击粉丝按钮
     */
    public void clickFollowButton(){
        AccessibilityNodeInfo nodeInfo = mService.getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("粉丝");
            nodeInfo.recycle();
            for (AccessibilityNodeInfo item : list) {
                if ("粉丝".equals(item.getText().toString())) {
                    Rect outBounds = new Rect();
                    item.getBoundsInScreen(outBounds);
                    NodeUtil.clickPoint(mService,outBounds.centerX() + 10, outBounds.centerY() + 10, 100);
                    break;
                }
            }
        }
    }
}
