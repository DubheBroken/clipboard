package com.rice.tool;

import android.app.Application;
import android.widget.Toast;

/**
 * @author chuxinbuer.zhugeliang
 */
public class ToastUtil {

    private static Toast mToast;
    private static Application mApplication;

    public static void init(Application mApplication) {
        ToastUtil.mApplication = mApplication;//TODO：一定要在Application的onCreate中先初始化才能使用
    }

    public static void showShort(int resid) {
        if (mApplication == null) {
            throw new IllegalArgumentException("请先在Application中初始化");
        }
        show(mApplication.getString(resid), Toast.LENGTH_SHORT);
    }

    public static void showShort(String msg) {
        if (mApplication == null) {
            throw new IllegalArgumentException("请先在Application中初始化");
        }
        show(msg, Toast.LENGTH_SHORT);
    }

    public static void showLong(int resid) {
        if (mApplication == null) {
            throw new IllegalArgumentException("请先在Application中初始化");
        }
        show(mApplication.getString(resid), Toast.LENGTH_LONG);
    }

    public static void showLong(String msg) {
        if (mApplication == null) {
            throw new IllegalArgumentException("请先在Application中初始化");
        }
        show(msg, Toast.LENGTH_LONG);
    }

    public static void show(String msg, int duration) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
//        if (mToast == null) {
        mToast = Toast.makeText(mApplication, msg, duration);
        // 设置消息颜色
//            TextView textView = (TextView) mToast.getView().findViewById(android.R.id.message);
//            textView.setTextColor(Color.WHITE);
//            textView.setTextSize(Common.px2sp(IApp.mContext, IApp.mContext.getResources().getDimensionPixelSize(R.dimen.x13)));
//            textView.setPadding(IApp.mContext.getResources().getDimensionPixelSize(R.dimen.x20),
//                    0,
//                    IApp.mContext.getResources().getDimensionPixelSize(R.dimen.x20),
//                    0);
        // 设置背景颜色
//            mToast.getView().setBackgroundColor(ContextCompat.getColor(IApp.mContext, R.color.colorAccent));
//        } else {
        mToast.setText(msg);
        mToast.setDuration(duration);
//        }

        mToast.show();
    }

}
