package com.rice.tool;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class ProcessUtils {

    //判断是否在主进程,这个方法判断进程名或者pid都可以,如果进程名一样那pid肯定也一样
//true:当前进程是主进程 false:当前进程不是主进程
    public static boolean isUIProcess(Context context) {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = null;
        if (am != null) {
            processInfos = am.getRunningAppProcesses();
        }
        String mainProcessName = context.getPackageName();
        int myPid = android.os.Process.myPid();
        if (processInfos != null) {
            for (ActivityManager.RunningAppProcessInfo info : processInfos) {
                if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断service是否已经运行
     * 必须判断uid,因为可能有重名的Service,所以要找自己程序的Service,不同进程只要是同一个程序就是同一个uid,个人理解android系统中一个程序就是一个用户
     * 用pid替换uid进行判断强烈不建议,因为如果是远程Service的话,主进程的pid和远程Service的pid不是一个值,在主进程调用该方法会导致Service即使已经运行也会认为没有运行
     * 如果Service和主进程是一个进程的话,用pid不会出错,但是这种方法强烈不建议,如果你后来把Service改成了远程Service,这时候判断就出错了
     *
     * @param className Service的全名,例如PushService.class.getName()
     * @return true:Service已运行 false:Service未运行
     */

    public static boolean isServiceExisted(Context context, String className) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList =
                am.getRunningServices(Integer.MAX_VALUE);
        int myUid = android.os.Process.myUid();
        for (ActivityManager.RunningServiceInfo runningServiceInfo : serviceList) {
            if (runningServiceInfo.uid == myUid &&
                    runningServiceInfo.service.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    // 是否在前台
    public static boolean isAppForeground(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String cn = null;
        if (am != null) {
            cn = am.getRunningTasks(1).get(0).topActivity.getPackageName();
        }
//        String currentPackageName = null;
//        if (cn != null) {
//            currentPackageName = cn.getPackageName();
//        }
        return TextUtils.isNotEmpty(cn)
                && cn.equals(packageName);
    }

    //检测某ActivityUpdate是否在当前Task的栈顶

//    把应用置为前台
//    moveTaskToFront()
//    moveTaskToBack()

//    private void bring2Front(Context context) {
//        ActivityManager activtyManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> runningTaskInfos =
//                activtyManager.getRunningTasks(3);
//        for (ActivityManager.RunningTaskInfo runningTaskInfo : runningTaskInfos) {
//            if (context.getPackageName().equals(runningTaskInfo.topActivity.getPackageName())) {
//                activtyManager.moveTaskToFront(
//                        runningTaskInfo.id, ActivityManager.MOVE_TASK_WITH_HOME);
//                return;
//            }
//        }
//    }

//    回到系统桌面

//    private void back2Home() {
//        Intent home = new Intent(Intent.ACTION_MAIN);
//        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        home.addCategory(Intent.CATEGORY_HOME);
//        startActivity(home);
//    }

}
