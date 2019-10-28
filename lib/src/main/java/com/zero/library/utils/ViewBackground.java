package com.zero.library.utils;

import com.ubzx.library.R;

/**
 * create by szl on 2017/5/3
 */

public class ViewBackground {

    private static int version = android.os.Build.VERSION.SDK_INT;
    //white
    public static int getWhiteSelector(){
        return version >= 21 ? R.drawable.ripple_white_bg : R.drawable.white_selector;
    }
    //accent
    public static int getThemeSelecter(){
        return version >= 21 ? R.drawable.ripple_theme_bg : R.drawable.theme_selector;
    }
}
