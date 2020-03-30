/*******************************************************************************
 * Copyright (c) 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package com.rice.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CcCc
 */
public class SimpeViewPaperAdaper extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;
    private List<String> tabs = new ArrayList<String>();
    private Activity mActivity;
    private FragmentManager fm;

    public SimpeViewPaperAdaper(FragmentActivity activity) {
        super(activity.getSupportFragmentManager());
        this.mActivity = activity;
    }

    public SimpeViewPaperAdaper(FragmentActivity activity, List<Fragment> list, List<String> tabs) {
        super(activity.getSupportFragmentManager());
        this.mActivity = activity;
        mFragments = list;
        this.tabs = tabs;
    }

    public SimpeViewPaperAdaper(FragmentActivity activity, List<Fragment> list) {
        super(activity.getSupportFragmentManager());
        this.mActivity = activity;
        mFragments = list;
    }

    public SimpeViewPaperAdaper(Fragment fragment, List<Fragment> list, List<String> tabs) {
        super(fragment.getChildFragmentManager());
        this.mActivity = fragment.getActivity();
        mFragments = list;
        this.tabs = tabs;
    }

    public SimpeViewPaperAdaper(Fragment fragment, List<Fragment> list, List<String> tabs, FragmentManager fm) {
        super(fragment.getChildFragmentManager());
        this.mActivity = fragment.getActivity();
        this.fm = fm;
        mFragments = list;
        this.tabs = tabs;
    }

    public void clear() {
        mFragments.clear();
        // tabs.clear();
    }

    public void addTab(Fragment fragment) {
        mFragments.add(fragment);
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 得到缓存的fragment
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mFragments.set(position, fragment);
        return fragment;
    }


}
