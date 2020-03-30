package com.rice.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gyf.barlibrary.ImmersionBar
import com.orhanobut.logger.Logger
import com.rice.riceframe.R

/**
 * 包含沉浸式的Fragment
 */

abstract class BaseImmersionFragment : ImmersionFragment() {

    protected var statusBarColorId = R.color.white//状态栏(顶部)背景颜色ID
    protected var navigationBarColorId = R.color.white//导航栏(底部)背景颜色ID
    protected var isWhiteStatusBarIcon = false//状态栏(顶部)图标颜色,true为亮色图标,false为暗色图标
    protected var isWhiteNavigationBarIcon = false//导航栏(顶部)图标颜色,true为亮色图标,false为暗色图标
    protected var isContentInvade = false//是否开启内容入侵模式(顶部元素的背景会覆盖状态栏背景)
    protected var viewTopId = 0//顶部元素ID，内容入侵沉浸式状态栏使用
    protected var enableImmersionBar = true//是否开启沉浸式状态栏，默认开启

    protected var rootView: View? = null
    /**
     * 获取布局ID
     */
    protected abstract val contentViewLayoutID: Int

    /**
     * 界面初始化
     */
    protected abstract fun initView()

    /**
     * 重写这个方法进行沉浸操作
     * ImmersionBar.with(this)
     * .statusBarColor(R.color.white)     //状态栏颜色，不写默认透明色
     * .navigationBarColor(R.color.white) //导航栏颜色，不写默认黑色
     * .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
     * .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
     */
    override fun initImmersionBar() {
        if (isContentInvade) {
            ImmersionBar.with(this)
                    .statusBarView(viewTopId)
                    .navigationBarColor(navigationBarColorId) //导航栏颜色，不写默认黑色
                    .statusBarDarkFont(!isWhiteStatusBarIcon)   //状态栏字体是深色，不写默认为亮色
                    .navigationBarDarkIcon(!isWhiteNavigationBarIcon) //导航栏图标是深色，不写默认为亮色
                    .flymeOSStatusBarFontColor(if (isWhiteStatusBarIcon) R.color.white else R.color.theme_black)  //修改flyme OS状态栏字体颜色
                    .fitsSystemWindows(true)    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
//                    .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
//                    .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)  //单独指定软键盘模式
                    //                    .setOnKeyboardListener(new OnKeyboardListener() {    //软键盘监听回调
                    //                        @Override
                    //                        public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
                    ////                        Logger.e(isPopup);  //isPopup为true，软键盘弹出，为false，软键盘关闭
                    //                        }
                    //                    })
                    .init()
        } else {
            ImmersionBar.with(this)
                    .statusBarColor(statusBarColorId)     //状态栏颜色，不写默认透明色
                    .navigationBarColor(navigationBarColorId) //导航栏颜色，不写默认黑色
                    .statusBarDarkFont(!isWhiteStatusBarIcon)   //状态栏字体是深色，不写默认为亮色
                    .navigationBarDarkIcon(!isWhiteNavigationBarIcon) //导航栏图标是深色，不写默认为亮色
                    .flymeOSStatusBarFontColor(if (isWhiteStatusBarIcon) R.color.white else R.color.theme_black)  //修改flyme OS状态栏字体颜色
                    .fitsSystemWindows(true)    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
//                    .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
//                    .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)  //单独指定软键盘模式
                    //                    .setOnKeyboardListener(new OnKeyboardListener() {    //软键盘监听回调
                    //                        @Override
                    //                        public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
                    ////                        Logger.e(isPopup);  //isPopup为true，软键盘弹出，为false，软键盘关闭
                    //                        }
                    //                    })
                    .init()
        }
    }

    override fun onDestroy() {
        Logger.e("$this onDestroy")
        super.onDestroy()
        if (ImmersionBar.with(this) != null)
            ImmersionBar.with(this).destroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Logger.e("$this onCreateView")
        if (rootView != null) {
            var parent = rootView!!.parent as ViewGroup?
            if (parent != null) {
                parent.removeView(rootView)
            }
            return rootView
        }
        rootView = inflater.inflate(contentViewLayoutID, null)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Logger.e("$this onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        initView()
    }



    fun isTransparentStatusColor(hidden: Boolean) {
        if (hidden) {
            ImmersionBar.with(this).statusBarColor(R.color.transparent)
                    //                    .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                    //                    .statusBarDarkFont(true) //状态栏字体是深色，不写默认为亮色
                    .flymeOSStatusBarFontColor(R.color.white)
                    //                    .fitsSystemWindows(true)
                    .init()
        } else {
            ImmersionBar.with(this).statusBarColor(R.color.colorPrimaryDark)
                    //                    .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                    //                    .statusBarDarkFont(true) //状态栏字体是深色，不写默认为亮色
                    .flymeOSStatusBarFontColor(R.color.white)
                    //                    .fitsSystemWindows(true)
                    .init()
        }
    }

    fun isTransparentStatusAndTextColor(dark: Boolean) {
        ImmersionBar.with(this).statusBarColor(R.color.transparent)
                //                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .statusBarDarkFont(dark) //true :状态栏字体是深色，不写默认为 false 亮色
                .flymeOSStatusBarFontColor(R.color.white)
                .init()
    }
}
