package com.xbl.ylmax.utils;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: link
 * Create: 2019-2019/12/28 0028-13:10
 * Changes (from 2019/12/28 0028)
 * 2019/12/28 0028 : Create NodeUtil.java (link);
 **/
public class NodeUtil {

    public static List<AccessibilityNodeInfo> findNodeForText(AccessibilityNodeInfo accessibilityNodeInfo, String text){
        List<AccessibilityNodeInfo> targetNodeInfo = new ArrayList<>();
        if (accessibilityNodeInfo == null){
            return targetNodeInfo;
        }
        int size = accessibilityNodeInfo.getChildCount();
        for (int i = 0; i<size; i++){
           AccessibilityNodeInfo childNodeInfo = accessibilityNodeInfo.getChild(i);
           if (childNodeInfo == null){
               return null;
           }
           List<AccessibilityNodeInfo> textNodeInfo = childNodeInfo.findAccessibilityNodeInfosByText(text);
           if (textNodeInfo.size() > 0){
               targetNodeInfo.addAll(childNodeInfo.findAccessibilityNodeInfosByText(text));
           }else {
               findNodeForText(childNodeInfo,text);
           }
        }
        return targetNodeInfo;
    }


    public static List<AccessibilityNodeInfo> findeNodeForClassName(AccessibilityNodeInfo accessibilityNodeInfo,String className){
        List<AccessibilityNodeInfo> targetNodeInfo = new ArrayList<>();
        if (accessibilityNodeInfo == null){
            return targetNodeInfo;
        }
        int size = accessibilityNodeInfo.getChildCount();
        for (int i = 0; i<size; i++){
            AccessibilityNodeInfo childNodeInfo = accessibilityNodeInfo.getChild(i);
            if (childNodeInfo == null){
                return null;
            }
            if (childNodeInfo.getClassName().equals(className)){
                targetNodeInfo.add(childNodeInfo);
            }else {
                findeNodeForClassName(childNodeInfo,className);
            }
        }
        return targetNodeInfo;
    }



    public static void clickPoint(AccessibilityService service, float x1, float y1, long duration) {
        Path path = new Path();
        path.moveTo(x1, y1);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription gestureDescription = builder
                .addStroke(new GestureDescription.StrokeDescription(path, 0, duration))
                .build();
        boolean b = service.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
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

}