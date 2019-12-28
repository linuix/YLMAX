package com.xbl.ylmax.ability;

import android.accessibilityservice.AccessibilityService;

/**
 * Author: link
 * Create: 2019-2019/12/28 0028-12:02
 * Changes (from 2019/12/28 0028)
 * 2019/12/28 0028 : Create Ability.java (link);
 **/
public abstract class Ability {


    protected AccessibilityService mService;


    public void init(AccessibilityService accessibilityService){
        this.mService = accessibilityService;
    }
}
