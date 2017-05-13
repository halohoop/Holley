package com.halohoop.holley.utils;

import android.util.Log;

/**
 * Created by Pooholah on 2017/5/13.
 */

public class Utils {
    private final static boolean DEBUG = true;
    private final static String TAG = "HolleyLog--";

    public static void logI(String s) {
        if (DEBUG) {
            Log.i(TAG, s);
        }
    }
}
