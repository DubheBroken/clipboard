package com.rice.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.rice.riceframe.R;
import com.rice.tool.TextUtils;

/**
 * 自定义标题栏
 */
public class RiceToolbar extends ConstraintLayout {

    private TextView textBack;//左侧文本
    private TextView textTitle;//中间文本
    private TextView textOk;//右侧文本
    private ImageView imgBack;//左侧图标
    private ImageView imgOk;//右侧图标
    private FrameLayout frameBack;//左侧Frame，用于点击监听
    private FrameLayout frameOk;//右侧Frame，用于点击监听
    private ConstraintLayout rootConstraint;//根布局
    private int textColor;//字体颜色，3处字体相同
    private int backId;//整体背景资源ID
    private int backImg;//左侧图标
    private int okImg;//右侧图标
    private String title = "";//中间文本内容
    private String okText = "";//右侧文本内容

    public static final int MODE_TEXT = 0;//显示文字
    public static final int MODE_IMG = 1;//显示图标
    public static final int MODE_HIDE = 2;//不显示

    private int backMode = MODE_IMG;//默认返回为图标模式
    private int okMode = MODE_HIDE;//默认右侧为更多模式
    private int titleMode = MODE_TEXT;//默认标题为文本模式

    public TextView getTextBack() {
        return textBack;
    }

    public TextView getTextTitle() {
        return textTitle;
    }

    public TextView getTextOk() {
        return textOk;
    }

    public ImageView getImgBack() {
        return imgBack;
    }

    public ImageView getImgOk() {
        return imgOk;
    }

    public FrameLayout getFrameBack() {
        return frameBack;
    }

    public FrameLayout getFrameOk() {
        return frameOk;
    }

    public RiceToolbar(Context context) {
        super(context);
        initView(context);
    }

    public RiceToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RiceToolbar);
        backMode = a.getInt(R.styleable.RiceToolbar_backMode, MODE_IMG);
        okMode = a.getInt(R.styleable.RiceToolbar_okMode, MODE_HIDE);
        titleMode = a.getInt(R.styleable.RiceToolbar_titleMode, MODE_TEXT);
        title = a.getString(R.styleable.RiceToolbar_title);
        okText = a.getString(R.styleable.RiceToolbar_okText);
        textColor = a.getColor(R.styleable.RiceToolbar_textColor, Color.BLACK);
        backId = a.getResourceId(R.styleable.RiceToolbar_background, R.color.white);
        backImg = a.getResourceId(R.styleable.RiceToolbar_backImg, R.drawable.vector_icon_left_arrow);
        okImg = a.getResourceId(R.styleable.RiceToolbar_okImg, R.drawable.vector_drawable_ok);
        a.recycle();
        initView(context);
    }

    public RiceToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RiceToolbar);
        backMode = a.getInt(R.styleable.RiceToolbar_backMode, MODE_IMG);
        okMode = a.getInt(R.styleable.RiceToolbar_okMode, MODE_HIDE);
        titleMode = a.getInt(R.styleable.RiceToolbar_titleMode, MODE_TEXT);
        title = a.getString(R.styleable.RiceToolbar_title);
        okText = a.getString(R.styleable.RiceToolbar_okText);
        textColor = a.getColor(R.styleable.RiceToolbar_textColor, Color.BLACK);
        backId = a.getResourceId(R.styleable.RiceToolbar_background, Color.WHITE);
        backImg = a.getResourceId(R.styleable.RiceToolbar_backImg, R.drawable.vector_icon_left_arrow);
        okImg = a.getResourceId(R.styleable.RiceToolbar_okImg, R.drawable.vector_drawable_ok);
        a.recycle();
        initView(context);
    }

    public void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.toolbar, this, true);
        textBack = findViewById(R.id.textBack);
        textTitle = findViewById(R.id.textTitle);
        textOk = findViewById(R.id.textOk);
        imgBack = findViewById(R.id.imgBack);
        imgOk = findViewById(R.id.imgOk);
        frameBack = findViewById(R.id.frameBack);
        frameOk = findViewById(R.id.frameOk);
        rootConstraint = findViewById(R.id.rootConstraint);

        rootConstraint.setBackgroundResource(backId);//设置背景

        imgBack.setImageResource(backImg);
        imgOk.setImageResource(okImg);
        //返回键监听
        frameBack.setOnClickListener(v -> {
            if (textBack.getVisibility() == View.VISIBLE ||
                    imgBack.getVisibility() == View.VISIBLE) {
                if (context instanceof AppCompatActivity) {
                    ((AppCompatActivity) context).onBackPressed();
                } else if (context instanceof Activity) {
                    ((Activity) context).onBackPressed();
                }
            }
        });
        setTitle(title);
//        if (TextUtils.isNotEmpty(okText)) {
        setOkText(okText);
//        }
        setTextColor(textColor);
        initMode();
    }

    /**
     * 设置文字颜色
     */
    public void setTextColor(int color) {
        textBack.setTextColor(color);
        textTitle.setTextColor(color);
        textOk.setTextColor(color);
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        textTitle.setText(title);
    }

    /**
     * 设置右侧文本
     */
    public void setOkText(String okText) {
        textOk.setText(okText);
    }


    /**
     * 设置左侧点击事件
     * 不设置默认点击事件为onBackPress
     */
    public void setOnBackClickListener(OnClickListener onBackClickListener) {
        frameBack.setOnClickListener(onBackClickListener);
    }

    /**
     * 设置右侧点击事件
     */
    public void setOnOkClickListener(OnClickListener onOkClickListener) {
        frameOk.setOnClickListener(onOkClickListener);
    }

    /**
     * 设置左侧显示模式
     */
    public void setBackMode(int backMode) {
        this.backMode = backMode;
        initMode();
    }

    /**
     * 设置右侧显示模式
     */
    public void setOkMode(int okMode) {
        this.okMode = okMode;
        initMode();
    }

    /**
     * 设置中间显示模式
     */
    public void setTitleMode(int titleMode) {
        this.titleMode = titleMode;
        initMode();
    }

    /**
     * 初始化显示模式
     */
    public void initMode() {
        switch (backMode) {
            case MODE_TEXT:
                textBack.setVisibility(VISIBLE);
                imgBack.setVisibility(INVISIBLE);
                break;
            case MODE_IMG:
                textBack.setVisibility(INVISIBLE);
                imgBack.setVisibility(VISIBLE);
                break;
            case MODE_HIDE:
                textBack.setVisibility(INVISIBLE);
                imgBack.setVisibility(INVISIBLE);
                break;
        }
        switch (titleMode) {
            case MODE_TEXT:
            case MODE_IMG:
                textTitle.setVisibility(VISIBLE);
                break;
            case MODE_HIDE:
                textTitle.setVisibility(INVISIBLE);
                break;
        }
        switch (okMode) {
            case MODE_TEXT:
                textOk.setVisibility(VISIBLE);
                imgOk.setVisibility(INVISIBLE);
                break;
            case MODE_IMG:
                textOk.setVisibility(INVISIBLE);
                imgOk.setVisibility(VISIBLE);
                break;
            case MODE_HIDE:
                textOk.setVisibility(INVISIBLE);
                imgOk.setVisibility(INVISIBLE);
                break;
        }
    }

}
