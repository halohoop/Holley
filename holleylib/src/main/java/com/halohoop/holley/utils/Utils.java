package com.halohoop.holley.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;

/**
 * Created by Pooholah on 2017/5/13.
 */

public class Utils {
    private final static boolean DEBUG = true;
    private final static String TAG = "HolleyLog--";

    public static void logI(Object s) {
        if (DEBUG) {
            Log.i(TAG, s + "");
        }
    }

    public static <T> T parseObject(String content, Class<T> clazz) {
        return JSON.parseObject(content, clazz);
    }
    public static String toJSONString(Object object) {
        return JSON.toJSONString(object);
    }
}
