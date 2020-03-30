/*
 * Copyright (C) 2013, 2014 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rice.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.rice.riceframe.R;
import com.rice.tool.UnitUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 带滑动切换动画的TabLayout
 */
public class PagerSlidingTabStrip extends HorizontalScrollView {

    public interface IconTabProvider {
        public int getPageIconResId(int position);
    }

    // @formatter:off
    private static final int[] ATTRS = new int[]{android.R.attr.textSize, android.R.attr.textColor};
    // @formatter:on

    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;

    private final PageListener pageListener = new PageListener();
    public OnPageChangeListener delegatePageListener;

    private LinearLayout tabsContainer;
    private ViewPager pager;

    private int tabCount;

    private int currentPosition = 0;
    private float currentPositionOffset = 0f;

    private Paint rectPaint;
    private Paint dividerPaint;

    private int indicatorColor = 0xFF48bc1b;//选中后的下划线
    private int underlineColor = 0x1A000000;//下划线
    private int dividerColor = 0x1A000000;//分割线

    private boolean shouldExpand = false;//自动填充满屏
    private boolean textAllCaps = true;
    private boolean colorType = true;// 是否需要改变颜色

    private int scrollOffset = 52;
    private int indicatorHeight = 8;//下划线高度
    private int indicatorPadding = 12;//下划线边距
    private int indicatorRcRadio = 12;//下划线圆角半径
    private int underlineHeight = 2;
    private int dividerPadding = 12;
    private int tabPadding = 15;
    private int dividerWidth = 1;
    private int position = 0;// 要改变颜色的位置
    // private int tabNum = 0;// title的各个标题后的数字

    private int tabTextSize = 18;//未选中的字号
    private int newTabTextSize = 18;//选中后的字号
    private int tabTextColor = 0xFFFFFFFF;//未选中的字体颜色
    private int newTextColor = 0xFFFFFFFF;//选中后的字体颜色
    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.NORMAL;

    private int lastScrollX = 0;

    private int tabBackgroundResId;

    private Locale locale;

    private int[] imageRes;

    private Context mContext;

    private int LineType;

    private int type = 0;//默认不加数据条数显示

    private boolean isContainImage = false;//默认不包含图片
    private List<Drawable> drawable_unchecked = new ArrayList<>();//图片数组
    private List<Drawable> drawable_checked = new ArrayList<>();

    private boolean isFill = false;//是否充满


    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setFillViewport(true);
        setWillNotDraw(false);

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
        indicatorPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorPadding, dm);
        indicatorRcRadio = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorRcRadio, dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);
        newTabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, newTabTextSize, dm);
        tabTextColor = (int) TypedValue.applyDimension(TypedValue.TYPE_INT_COLOR_RGB8, tabTextColor, dm);
        newTextColor = (int) TypedValue.applyDimension(TypedValue.TYPE_INT_COLOR_RGB8, newTextColor, dm);

        // get system attrs (android:textSize and android:textColor)

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        tabTextColor = a.getColor(1, tabTextColor);

        a.recycle();

        // get custom attrs

        a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);

        indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, indicatorColor);
        underlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, dividerColor);
        indicatorHeight = a.getDimensionPixelOffset(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight,
                indicatorHeight);
        underlineHeight = a.getDimensionPixelOffset(R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight,
                underlineHeight);
        dividerPadding = a.getDimensionPixelOffset(R.styleable.PagerSlidingTabStrip_pstsDividerPadding, dividerPadding);
        indicatorPadding = a.getDimensionPixelOffset(R.styleable.PagerSlidingTabStrip_pstsIndicatorPadding, indicatorPadding);
        indicatorRcRadio = a.getDimensionPixelOffset(R.styleable.PagerSlidingTabStrip_pstsIndicatorRcRadio, indicatorRcRadio);
        tabPadding = a.getDimensionPixelOffset(R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight, tabPadding);
        tabBackgroundResId = a.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabBackground, tabBackgroundResId);
        shouldExpand = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand, shouldExpand);
        scrollOffset = a.getDimensionPixelOffset(R.styleable.PagerSlidingTabStrip_pstsScrollOffset, scrollOffset);
        textAllCaps = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);
        tabTextSize = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabTextSize, tabTextSize);
        newTabTextSize = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsNewTabTextSize, newTabTextSize);
        tabTextColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsTabTextColor, tabTextColor);
        newTextColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsNewTextColor, newTextColor);

        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);//抗锯齿
        rectPaint.setStyle(Style.FILL);
//        if (indicatorRcRadio > 0) {
//            rectPaint.setStrokeWidth(indicatorRcRadio);
//            rectPaint.setStrokeCap(Paint.Cap.ROUND);
//            rectPaint.setStrokeJoin(Paint.Join.ROUND);
//        }

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }

    public void setViewPager(ViewPager pager, int type) {
        this.pager = pager;

        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        pager.setOnPageChangeListener(pageListener);
        this.type = type;
        notifyDataSetChanged(type, null);

    }

    public void setNumList(List<String> textlist) {
        notifyDataSetChanged(type, textlist);
    }


    public void setImagesRes(int[] images) {
        this.imageRes = images;
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void notifyDataSetChanged(int type, List<String> textlist) {

        tabsContainer.removeAllViews();

        tabCount = pager.getAdapter().getCount();

        for (int i = 0; i < tabCount; i++) {

            if (pager.getAdapter() instanceof IconTabProvider) {
                addIconTab(i, ((IconTabProvider) pager.getAdapter()).getPageIconResId(i));
            } else {
                if (textlist != null && textlist.size() > 0) {
                    if (textlist.get(i).equals("0")) {
                        addTextTab(i, pager.getAdapter().getPageTitle(i).toString(), "(0)", type);
                    } else {
                        addTextTab(i, pager.getAdapter().getPageTitle(i).toString(), "(" + textlist.get(i) + ")", type);
                    }
                } else {
                    addTextTab(i, pager.getAdapter().getPageTitle(i).toString(), "", type);
                }
            }

        }

        updateTabStyles(position, tabTextColor, newTextColor, tabTextSize, newTabTextSize, colorType);

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT < 16) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                currentPosition = pager.getCurrentItem();
                scrollToChild(currentPosition, 0);
            }
        });

    }

    private void addTextTab(int position, String title, String content, int type) {
        LinearLayout tab = new LinearLayout(getContext());
        tab.setOrientation(LinearLayout.HORIZONTAL);
        tab.setGravity(Gravity.CENTER);
        LineType = type;
        if (imageRes != null && imageRes.length > 0) {
//            ImageView imageView = new ImageView(getContext());
//            imageView.setImageResource(imageRes[position]);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
//                    LayoutParams.WRAP_CONTENT);
//            lp.rightMargin = Common.dip2px(getContext(), 5);
//            imageView.setLayoutParams(lp);
//            tab.addView(imageView);
            drawable_checked.add(this.getResources().getDrawable(imageRes[2 * position]));
            drawable_unchecked.add(this.getResources().getDrawable(imageRes[2 * position + 1]));
            isContainImage = true;
        }

        if (type == 0) {
            TextView text = new TextView(getContext());
            text.setText(title);
            text.setGravity(Gravity.CENTER);
            text.setSingleLine();
            tab.addView(text);
        } else {
            LinearLayout tab1 = new LinearLayout(getContext());
            tab1.setOrientation(LinearLayout.VERTICAL);
            tab1.setGravity(Gravity.CENTER);
            TextView text = new TextView(getContext());
            text.setText(title);
            text.setGravity(Gravity.CENTER);
            text.setSingleLine();
            tab1.addView(text);
            TextView text2 = new TextView(getContext());
            text2.setText(content);
            text2.setGravity(Gravity.CENTER);
            text2.setSingleLine();
            tab1.addView(text2);
            tab.addView(tab1);
        }
        addTab(position, tab);
    }

    private void addIconTab(final int position, int resId) {

        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(resId);

        addTab(position, tab);

    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });

        tab.setPadding(tabPadding, 0, tabPadding, 0);
        tabsContainer.addView(tab, position, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
    }

    private void updateTabStyles(int position, int tabTextColor, int newcolorres, int tabTextSize, int newTabTextSize, boolean colorType) {

        if (colorType) {
            for (int i = 0; i < tabCount; i++) {

                View v = tabsContainer.getChildAt(i);

                v.setBackgroundResource(tabBackgroundResId);

                if (LineType == 0) {
                    TextView tab = null;
                    ImageView imageTab = null;
                    if (v instanceof LinearLayout) {
                        LinearLayout container = (LinearLayout) v;
                        for (int j = 0; j < container.getChildCount(); j++) {
                            View childview = container.getChildAt(j);
                            if (childview instanceof TextView) {
                                tab = (TextView) childview;
                                break;
                            }
                        }
                        if (tab == null)
                            return;
                        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                        tab.setTypeface(tabTypeface, tabTypefaceStyle);
                        ViewGroup.LayoutParams lp = tab.getLayoutParams();
                        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                        tab.setLayoutParams(lp);
                        if (position == i) {
                            tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTabTextSize);
                            tab.setTextColor(newcolorres);
                            if (isContainImage) {//如果需要图片则添加图片
                                tab.setCompoundDrawablePadding((int) mContext.getResources().getDimension(R.dimen.dp_3));
                                /// 这一步必须要做,否则不会显示.
                                drawable_checked.get(i).setBounds(0, 0, drawable_checked.get(i).getMinimumWidth(), drawable_checked.get(i).getMinimumHeight());
                                tab.setCompoundDrawables(drawable_checked.get(i), null, null, null);
                            }
                        } else {
                            tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                            tab.setTextColor(tabTextColor);
                            if (isContainImage) {//如果需要图片则添加图片
                                tab.setCompoundDrawablePadding((int) mContext.getResources().getDimension(R.dimen.dp_3));
                                /// 这一步必须要做,否则不会显示.
                                drawable_unchecked.get(i).setBounds(0, 0, drawable_unchecked.get(i).getMinimumWidth(), drawable_unchecked.get(i).getMinimumHeight());
                                tab.setCompoundDrawables(drawable_unchecked.get(i), null, null, null);
                            }
                        }

                        // setAllCaps() is only available from API 14, so the
                        // upper
                        // case is made manually if we are on a
                        // pre-ICS-build
                        if (textAllCaps) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                                tab.setAllCaps(true);
                            } else {
                                tab.setText(tab.getText().toString().toUpperCase(locale));
                            }
                        }
                    }
                } else {
                    TextView tab = null;
                    TextView tab1 = null;
                    if (v instanceof LinearLayout) {
                        LinearLayout container = (LinearLayout) v;
                        LinearLayout v1 = (LinearLayout) container.getChildAt(0);
                        tab = (TextView) v1.getChildAt(0);
                        tab1 = (TextView) v1.getChildAt(1);
                    }
                    if (tab == null)
                        return;
                    if (tab1 == null)
                        return;
                    tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                    tab.setTypeface(tabTypeface, tabTypefaceStyle);
                    tab1.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                    tab1.setTypeface(tabTypeface, tabTypefaceStyle);
                    ViewGroup.LayoutParams lp = tab.getLayoutParams();
                    lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    tab.setLayoutParams(lp);
                    ViewGroup.LayoutParams lp2 = tab.getLayoutParams();
                    lp2.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    lp2.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    tab1.setLayoutParams(lp2);
                    if (position == i) {
                        tab.setTextColor(newcolorres);
                        tab1.setTextColor(newcolorres);
                        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTabTextSize);
                        tab1.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTabTextSize);
                    } else {
                        tab.setTextColor(tabTextColor);
                        tab1.setTextColor(tabTextColor);
                        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                        tab1.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                    }

                    // setAllCaps() is only available from API 14, so the
                    // upper
                    // case is made manually if we are on a
                    // pre-ICS-build
                    if (textAllCaps) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                            tab.setAllCaps(true);
                        } else {
                            tab.setText(tab.getText().toString().toUpperCase(locale));
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < tabCount; i++) {

                View v = tabsContainer.getChildAt(i);

                v.setBackgroundResource(tabBackgroundResId);
                TextView tab = null;
                if (v instanceof LinearLayout) {
                    LinearLayout container = (LinearLayout) v;
                    for (int j = 0; j < container.getChildCount(); j++) {
                        View childview = container.getChildAt(j);
                        if (childview instanceof TextView) {
                            tab = (TextView) childview;
                            break;
                        }
                    }
                    if (tab == null)
                        return;
                    tab.setTextSize(TypedValue.COMPLEX_UNIT_SP, tabTextSize);
                    tab.setTypeface(tabTypeface, tabTypefaceStyle);
                    tab.setTextColor(tabTextColor);

                    // setAllCaps() is only available from API 14, so the upper
                    // case is made manually if we are on a
                    // pre-ICS-build
                    if (textAllCaps) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                            tab.setAllCaps(true);
                        } else {
                            tab.setText(tab.getText().toString().toUpperCase(locale));
                        }
                    }
                }
            }
        }

    }

    private void scrollToChild(int position, int offset) {

        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || tabCount == 0) {
            return;
        }

        final int height = getHeight();

        // draw indicator line

        rectPaint.setColor(indicatorColor);

        // default: line below current tab
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        // if there is an offset, start interpolating left and right coordinates
        // between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
        }
        RectF rect = new RectF();
        if (isFill) {
            rect.left = lineLeft + indicatorPadding;
            rect.top = height - indicatorHeight;
            rect.right = lineRight - indicatorPadding;
            rect.bottom = height;
            canvas.drawRoundRect(rect, indicatorHeight, indicatorHeight, rectPaint);
//            canvas.drawLine( lineLeft + indicatorPadding,height,lineRight - indicatorPadding, height, rectPaint);
        } else {
            rect.left = lineLeft + indicatorPadding;
            rect.top = height - indicatorHeight;
            rect.right = lineRight - indicatorPadding;
            rect.bottom = height;
//            canvas.drawRoundRect( rect, (lineRight - lineLeft) / 2, indicatorRcRadio, rectPaint);
            canvas.drawRoundRect(rect, indicatorHeight, indicatorHeight, rectPaint);
//            canvas.drawLine( lineLeft + indicatorPadding,height,lineRight - indicatorPadding, height, rectPaint);
//            canvas.drawRect(lineLeft + getResources().getDimensionPixelSize(R.dimen.dp_10), height - indicatorHeight, lineRight - getResources().getDimensionPixelSize(R.dimen.dp_10), height, rectPaint);
//            canvas.drawRect(lineLeft + tabsContainer.getChildAt(0).getWidth() / 2 - getResources().getDimensionPixelSize(R.dimen.dp_20), height - indicatorHeight, lineLeft + tabsContainer.getChildAt(0).getWidth() / 2 + getResources().getDimensionPixelSize(R.dimen.dp_20), height, rectPaint);
        }

        // draw underline

        rectPaint.setColor(underlineColor);
//        canvas.drawRect(tabsContainer.getChildAt(0).getWidth() / 5, height * 2 / 3 - underlineHeight, tabsContainer.getWidth() - tabsContainer.getChildAt(0).getWidth() / 5, height * 2 / 3 , rectPaint);

//         draw divider
        dividerPaint.setColor(dividerColor);
        for (int i = 0; i < tabCount - 1; i++) {
            View tab = tabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
        }
    }

    private class PageListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            currentPosition = position;
            currentPositionOffset = positionOffset;

            scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));
            updateTabStyles(position, tabTextColor, newTextColor, tabTextSize, newTabTextSize, colorType);
            invalidate();

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(pager.getCurrentItem(), 0);
            }

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
        }

    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.dividerColor = getResources().getColor(resId);
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }

    public int getDividerPadding() {
        return dividerPadding;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles(position, tabTextColor, newTextColor, tabTextSize, newTabTextSize, colorType);
    }

    public int getTextSize() {
        return tabTextSize;
    }

    public void setTextColor(int textColor, int position, int newTextColor, boolean colorType) {
        this.colorType = colorType;
        this.tabTextColor = textColor;
        this.newTextColor = newTextColor;
        this.position = position;
        updateTabStyles(position, tabTextColor, newTextColor, tabTextSize, newTabTextSize, colorType);
    }

    public void setTextColorResource(int resId) {
        this.tabTextColor = getResources().getColor(resId);
        updateTabStyles(position, tabTextColor, newTextColor, tabTextSize, newTabTextSize, colorType);
    }

    public int getTextColor() {
        return tabTextColor;
    }

    public void setTypeface(Typeface typeface, int style) {
        this.tabTypeface = typeface;
        this.tabTypefaceStyle = style;
        updateTabStyles(position, tabTextColor, newTextColor, tabTextSize, newTabTextSize, colorType);
    }

    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
    }

    public int getTabBackground() {
        return tabBackgroundResId;
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        this.tabPadding = paddingPx;
        updateTabStyles(position, tabTextColor, newTextColor, tabTextSize, newTabTextSize, colorType);
    }

    public int getTabPaddingLeftRight() {
        return tabPadding;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }


    public void setFillUnderline(boolean isFill) {
        this.isFill = isFill;
        invalidate();
    }

}
