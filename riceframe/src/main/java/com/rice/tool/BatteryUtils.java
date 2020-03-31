package com.rice.tool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * 电池管理、加白名单工具类
 */
public class BatteryUtils {

    /**
     * 依次判断手机品牌跳转指定页面
     */
    public static void run(Context mContext) {
        if (isHuawei()) {
            goHuaweiSetting(mContext);//华为
        } else if (isXiaomi()) {
            goXiaomiSetting(mContext);//小米
        } else if (isOPPO()) {
            goOPPOSetting(mContext);//OPPO
        } else if (isVIVO()) {
            goVIVOSetting(mContext);//VIVO
        } else if (isMeizu()) {
            goMeizuSetting(mContext);//魅族
        } else if (isSamsung()) {
            goSamsungSetting(mContext);//三星
        } else if (isLeTV()) {
            goLetvSetting(mContext);//乐视
        } else if (isSmartisan()) {
            goSmartisanSetting(mContext);//锤子
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!isIgnoringBatteryOptimizations(mContext)) {
                    requestIgnoreBatteryOptimizations(mContext);//原生
                }
            } else {
                //原生Android M以下
                ToastUtil.showShort("您的手机不需要进行此设置");
            }
        }
    }

    /**
     * 是否已忽略电池优化
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isIgnoringBatteryOptimizations(Context mContext) {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(mContext.getPackageName());
        }
        return isIgnoring;
    }

    /**
     * 申请加入白名单
     *
     * @param mContext
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestIgnoreBatteryOptimizations(Context mContext) {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + mContext.getPackageName()));
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    /**
     * 是否是华为手机
     */
    public static boolean isHuawei() {
        if (Build.BRAND == null) {
            return false;
        } else {
            return Build.BRAND.toLowerCase().equals("huawei") || Build.BRAND.toLowerCase().equals("honor");
        }
    }

    /**
     * 跳转华为手机管家
     * 操作步骤：应用启动管理 -> 关闭应用开关 -> 打开允许自启动
     */
    public static void goHuaweiSetting(Context mContext) {
        try {
            showActivity("com.huawei.systemmanager",
                    "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity", mContext);
        } catch (Exception e) {
            showActivity("com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.bootstart.BootStartActivity", mContext);
        }
        ToastUtil.showLong("操作步骤：应用启动管理 -> 关闭应用开关 -> 打开允许自启动");
    }

    /**
     * 是否是小米手机
     */
    public static boolean isXiaomi() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("xiaomi");
    }

    /**
     * 跳转小米安全中心的自启动管理页面
     * 操作步骤：授权管理 -> 自启动管理 -> 允许应用自启动
     *
     * @param mContext
     */
    public static void goXiaomiSetting(Context mContext) {
        showActivity("com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity", mContext);
        ToastUtil.showLong("操作步骤：授权管理 -> 自启动管理 -> 允许应用自启动");
    }

    /**
     * 是否是OPPO手机
     */
    public static boolean isOPPO() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("oppo");
    }

    /**
     * 跳转 OPPO 手机管家
     * 操作步骤：权限隐私 -> 自启动管理 -> 允许应用自启动
     *
     * @param mContext
     */
    public static void goOPPOSetting(Context mContext) {
        try {
            showActivity("com.coloros.phonemanager", mContext);
        } catch (Exception e1) {
            try {
                showActivity("com.oppo.safe", mContext);
            } catch (Exception e2) {
                try {
                    showActivity("com.coloros.oppoguardelf", mContext);
                } catch (Exception e3) {
                    showActivity("com.coloros.safecenter", mContext);
                }
            }
        }
        ToastUtil.showLong("操作步骤：权限隐私 -> 自启动管理 -> 允许应用自启动");
    }

    /**
     * 是否是VIVO手机
     */
    public static boolean isVIVO() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("vivo");
    }

    /**
     * 跳转 VIVO 手机管家
     * 操作步骤：权限管理 -> 自启动 -> 允许应用自启动
     */
    public static void goVIVOSetting(Context mContext) {
        showActivity("com.iqoo.secure", mContext);
        ToastUtil.showLong("操作步骤：权限管理 -> 自启动 -> 允许应用自启动");
    }

    /**
     * 是否是魅族手机
     */
    public static boolean isMeizu() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("meizu");
    }

    /**
     * 跳转魅族手机管家
     * 操作步骤：权限管理 -> 后台管理 -> 点击应用 -> 允许后台运行
     */
    public static void goMeizuSetting(Context mContext) {
        showActivity("com.meizu.safe", mContext);
        ToastUtil.showLong("操作步骤：权限管理 -> 后台管理 -> 点击应用 -> 允许后台运行");
    }

    /**
     * 是否是三星手机
     */
    public static boolean isSamsung() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("samsung");
    }

    /**
     * 跳转三星智能管理器
     * 操作步骤：自动运行应用程序 -> 打开应用开关 -> 电池管理 -> 未监视的应用程序 -> 添加应用
     */
    public static void goSamsungSetting(Context mContext) {
        try {
            showActivity("com.samsung.android.sm_cn", mContext);
        } catch (Exception e) {
            showActivity("com.samsung.android.sm", mContext);
        }
        ToastUtil.showLong("操作步骤：自动运行应用程序 -> 打开应用开关 -> 电池管理 -> 未监视的应用程序 -> 添加应用");
    }

    /**
     * 是否是乐视手机
     */
    public static boolean isLeTV() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("letv");
    }

    /**
     * 跳转乐视手机管家
     * 操作步骤：自启动管理 -> 允许应用自启动
     */
    public static void goLetvSetting(Context mContext) {
        showActivity("com.letv.android.letvsafe",
                "com.letv.android.letvsafe.AutobootManageActivity", mContext);
        ToastUtil.showLong("操作步骤：自启动管理 -> 允许应用自启动");
    }

    /**
     * 是否是锤子手机
     */
    public static boolean isSmartisan() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("smartisan");
    }

    /**
     * 跳转锤子手机管理
     * 操作步骤：权限管理 -> 自启动权限管理 -> 点击应用 -> 允许被系统启动
     */
    public static void goSmartisanSetting(Context mContext) {
        showActivity("com.smartisanos.security", mContext);
        ToastUtil.showLong("操作步骤：权限管理 -> 自启动权限管理 -> 点击应用 -> 允许被系统启动");
    }


}
