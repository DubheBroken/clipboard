package com.rice.tool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * 作者：DubheBroken
 * 时间：2018/11/19
 * 地址：github.com/DubheBroken/FragmentHelper
 * 说明：Fragment管理工具类
 */

public class FragmentHelper {

    private static FragmentManager fragmentManager;
    private static FragmentTransaction fragmentTransaction;

    private static FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    private static void setFragmentManager(FragmentManager fragmentManager) {
        FragmentHelper.fragmentManager = fragmentManager;
    }

    public static FragmentTransaction getFragmentTransaction() {
        return fragmentTransaction;
    }

    public static void setFragmentTransaction(FragmentTransaction fragmentTransaction) {
        FragmentHelper.fragmentTransaction = fragmentTransaction;
    }

    private static void initFragmentTransaction() {
        fragmentTransaction = fragmentManager.beginTransaction();
    }

    /**
     * 此方法在onBackPressed被重写时使用
     * 回退到上一层fragment
     * 如果已经是最后一层，隐藏界面
     */
    public static void back(AppCompatActivity activity) {
        if (getFragmentManager().getBackStackEntryCount() <= 1) {
            activity.moveTaskToBack(true);
        } else {
            fragmentManager.popBackStack();
        }
    }

    /**
     * 显示Fragment
     *
     * @param activity       当前activity，仅支持AppCompatActivity
     *                       在fragment中请使用(AppCompatActivity)getActivity()作为参数传入
     * @param targetFragment 要显示的Fragment
     */
    public static void showFragment(AppCompatActivity activity, Fragment targetFragment, int targetFrameId) {
        FragmentHelper.setFragmentManager(activity.getSupportFragmentManager());
        FragmentHelper.initFragmentTransaction();
        //frame容器id
        fragmentManager.findFragmentById(targetFrameId);
        fragmentManager.beginTransaction()
                .show(targetFragment)
                .commit();
    }


    /**
     * 隐藏Fragment
     *
     * @param activity       当前activity，仅支持AppCompatActivity
     *                       在fragment中请使用(AppCompatActivity)getActivity()作为参数传入
     * @param targetFragment 要隐藏的Fragment
     */
    public static void hideFragment(AppCompatActivity activity, Fragment targetFragment, int targetFrameId) {
        FragmentHelper.setFragmentManager(activity.getSupportFragmentManager());
        FragmentHelper.initFragmentTransaction();
        //frame容器id
        fragmentManager.findFragmentById(targetFrameId);
        fragmentManager.beginTransaction()
                .hide(targetFragment)
                .commit();
    }

    public static void removeAllFragments(FragmentManager fragmentManager) {
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
    }

    /**
     * 显示隐藏Fragment
     *
     * @param activity       当前activity，仅支持AppCompatActivity
     *                       在fragment中请使用(AppCompatActivity)getActivity()作为参数传入
     * @param targetFragment 要显示的Fragment
     * @param hideFragment   要隐藏的Fragment
     */
    public static void showHideFragment(AppCompatActivity activity, Fragment targetFragment, Fragment hideFragment, int targetFrameId) {
        FragmentHelper.setFragmentManager(activity.getSupportFragmentManager());
        FragmentHelper.initFragmentTransaction();
        //frame容器id
        fragmentManager.findFragmentById(targetFrameId);
        fragmentManager.beginTransaction()
                .show(targetFragment)
                .hide(hideFragment)
                .commit();
    }

    /**
     * 切换Fragment为传入参数
     *
     * @param activity      当前activity，仅支持AppCompatActivity
     *                      在fragment中请使用(AppCompatActivity)getActivity()作为参数传入
     * @param fragment      目标fragment对象
     * @param targetFrameId 目标FrameLayout容器
     */
    public static void switchFragment(Fragment fragment, AppCompatActivity activity, int targetFrameId) {
        FragmentHelper.setFragmentManager(activity.getSupportFragmentManager());
        FragmentHelper.initFragmentTransaction();
        //frame容器id
        fragmentManager.findFragmentById(targetFrameId);
        fragmentTransaction
                .replace(targetFrameId, fragment)
                .addToBackStack(null)
                .commit();//替换成下面那句可以在frameLayout容器被遮挡的情况下替换fragment
//                .commitAllowingStateLoss();
    }

}
