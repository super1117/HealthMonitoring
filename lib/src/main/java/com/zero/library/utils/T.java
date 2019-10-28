package com.zero.library.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * create by szl on 2017/8/18
 */

public class T {

    public static void s(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void l(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
