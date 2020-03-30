package com.dubhe.clipboardDemo

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.lky.toucheffectsmodule.TouchEffectsManager
import com.lky.toucheffectsmodule.factory.TouchEffectsFactory
import com.lky.toucheffectsmodule.types.TouchEffectsViewType
import com.lky.toucheffectsmodule.types.TouchEffectsWholeType
import com.rice.tool.ToastUtil

class MyApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        ToastUtil.init(this)

    }

    companion object{
        init {
            TouchEffectsManager.build(TouchEffectsWholeType.RIPPLE)//设置全局使用哪种效果
                .addViewType(TouchEffectsViewType.LinearLayout)//添加哪些View支持这个效果
                .addViewType(TouchEffectsViewType.ConstraintLayout)//添加哪些View支持这个效果
//                .setListWholeType(TouchEffectsWholeType.RIPPLE)//为父控件为列表的情况下，设置特定效果
//                .setAspectRatioType(4f, TouchEffectsWholeType.RIPPLE)//宽高比大于4时启动水波纹
        }
    }

}