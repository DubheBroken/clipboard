package com.rice.tool;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * 版本相关工具类
 */
public class VersionUtils {

    /**
     * 获取android系统版本
     */
    public static int getAndroidSDKVersion() {
        int version = Build.VERSION.SDK_INT;
        return version;
    }

    /**
     * 获取APK版本号
     */
    public static String getVersionCode(Context context) {
        String version = null;
        try {
            PackageManager packagemanager = context.getPackageManager();
            PackageInfo info = packagemanager.getPackageInfo(
                    context.getPackageName(), 0);
            version = Integer.toString(info.versionCode);
        } catch (Exception e) {
        }
        return version;
    }

    /**
     * 获取APK版本号
     */
    public static String getVersionName(Context context) {
        String version = null;
        try {
            PackageManager packagemanager = context.getPackageManager();
            PackageInfo info = packagemanager.getPackageInfo(
                    context.getPackageName(), 0);
            version = info.versionName;
        } catch (Exception e) {
        }
        return version;
    }

}
