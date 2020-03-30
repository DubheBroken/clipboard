package com.rice.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gyf.barlibrary.ImmersionOwner;
import com.gyf.barlibrary.ImmersionProxy;
import com.orhanobut.logger.Logger;

/**
 * 为了方便在Fragment使用沉浸式请继承ImmersionFragment，
 * 请在immersionBarEnabled方法中实现你的沉浸式代码，
 * ImmersionProxy已经做了ImmersionBar.with(mFragment).destroy()了，所以不需要在你的代码中做这个处理了
 * 如果不能继承，请拷贝代码到你的项目中
 *
 * @author geyifeng
 * @date 2017 /5/12
 */
public abstract class ImmersionFragment extends Fragment implements ImmersionOwner {

    protected boolean isDebug = false;//更改这个值打印生命周期日志
    protected Context mContext;
    protected Activity mActivity;

    /**
     * ImmersionBar代理类
     */
    private ImmersionProxy mImmersionProxy = new ImmersionProxy(this);

    @Override
    public void onAttach(@NonNull Context context) {
        if (isDebug) {
            Logger.e(this.toString() + " onAttach");
        }
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        if (isDebug) {
            Logger.e(this.toString() + " onAttach");
        }
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mImmersionProxy.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (isDebug) {
            Logger.e(this.toString() + " onCreate");
        }
        super.onCreate(savedInstanceState);
        mImmersionProxy.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (isDebug) {
            Logger.e(this.toString() + " onActivityCreated");
        }
        super.onActivityCreated(savedInstanceState);
        mImmersionProxy.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        if (isDebug) {
            Logger.e(this.toString() + " onResume");
        }
        super.onResume();
        mImmersionProxy.onResume();
    }

    @Override
    public void onPause() {
        if (isDebug) {
            Logger.e(this.toString() + " onPause");
        }
        super.onPause();
        mImmersionProxy.onPause();
    }

    @Override
    public void onDestroy() {
        if (isDebug) {
            Logger.e(this.toString() + " onDestroy");
        }
        super.onDestroy();
        mImmersionProxy.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mImmersionProxy.onHiddenChanged(hidden);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mImmersionProxy.onConfigurationChanged(newConfig);
    }

    /**
     * 懒加载，在view初始化完成之前执行
     * On lazy after view.
     */
    @Override
    public void onLazyBeforeView() {
    }

    /**
     * 懒加载，在view初始化完成之后执行
     * On lazy before view.
     */
    @Override
    public void onLazyAfterView() {
    }

    /**
     * Fragment用户可见时候调用
     * On visible.
     */
    @Override
    public void onVisible() {
    }

    /**
     * Fragment用户不可见时候调用
     * On invisible.
     */
    @Override
    public void onInvisible() {
    }

    /**
     * 是否可以实现沉浸式，当为true的时候才可以执行initImmersionBar方法
     * Immersion bar enabled boolean.
     *
     * @return the boolean
     */
    @Override
    public boolean immersionBarEnabled() {
        return true;
    }


}
