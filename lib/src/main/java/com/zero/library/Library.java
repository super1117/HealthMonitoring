package com.zero.library;

import android.content.Context;

public class Library {

    public static Context CONTEXT;

    public static void init(Context context){
        Library.CONTEXT = context;
    }

}
