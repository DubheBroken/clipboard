package com.rice.tool;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 屏幕相关操作
 */
public class ScreenUtils {

    /**
     * 获取屏幕宽度原始尺寸，包括状态栏和导航栏
     *
     * @param mApplication Application对象
     */
    public static Integer getRealScreenWidth(Application mApplication) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) (mApplication.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getMetrics(dm);
        int screenwidth = dm.widthPixels;
        return screenwidth;
    }

    /**
     * 获取屏幕高度原始尺寸，包括状态栏和导航栏
     *
     * @param mApplication Application对象
     */
    public static Integer getRealScreenHeight(Application mApplication) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) (mApplication.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getMetrics(dm);
        int screenheight = dm.heightPixels;
        return screenheight;
    }

}
