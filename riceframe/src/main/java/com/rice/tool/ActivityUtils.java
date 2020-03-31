package com.rice.tool;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

/**
 * Activity操作相关工具类
 */
public class ActivityUtils {

    /**
     * 工具类不实体化
     */
    private ActivityUtils() {
    }

    /**
     * 跳转到指定应用的首页
     */
    public static void showActivity(@NonNull String packageName, Context mContext) {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
        mContext.startActivity(intent);
    }

    /**
     * 跳转到指定应用的指定页面
     */
    public static void showActivity(@NonNull String packageName, @NonNull String activityDir, Context mContext) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityDir));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public static void openActivity(Context fromcontext, Class<?> toClass) {
        openActivity(fromcontext, toClass, null);
    }

    public static void openActivity(Context fromcontext, Class<?> toClass, int requestCode) {
        openActivity(fromcontext, toClass, null, requestCode, -1, -1);
    }

    public static void openActivity(Context fromcontext, Class<?> toClass, Bundle pBundle) {
        openActivity(fromcontext, toClass, pBundle, -1, -1, -1);
    }

    public static void openActivity(Context fromcontext, Class<?> toClass, Bundle pBundle, int requestCode) {
        openActivity(fromcontext, toClass, pBundle, requestCode, -1, -1);
    }

    public static void openActivity(Context fromcontext, Class<?> toClass, Bundle pBundle, int enterAnim, int exitAnim) {
        openActivity(fromcontext, toClass, pBundle, -1, enterAnim, exitAnim);
    }

    /**
     * 启动Activity
     *
     * @param fromcontext 上下文context对象
     * @param toClass     跳转目标Activity
     * @param pBundle     传递的参数
     * @param requestCode 请求码
     * @param enterAnim   跳转动画
     * @param exitAnim    退出动画
     */
    public static void openActivity(Context fromcontext, Class<?> toClass, Bundle pBundle, int requestCode, int enterAnim, int exitAnim) {

//        if (System.currentTimeMillis() - lastClick <= LASTCLICKTIME) {
//            return;
//        }
//        lastClick = System.currentTimeMillis();
        if (fromcontext != null) {
            Intent intent = new Intent(fromcontext, toClass);
            if (pBundle != null) {
                intent.putExtras(pBundle);
            }
            if (-1 == requestCode) {
                ((Activity) fromcontext).startActivity(intent);
            } else {
                ((Activity) fromcontext).startActivityForResult(intent, requestCode);
            }

            if (-1 != enterAnim && -1 != exitAnim) {
                ((Activity) fromcontext).overridePendingTransition(enterAnim, exitAnim);
            }
        }
    }

}
