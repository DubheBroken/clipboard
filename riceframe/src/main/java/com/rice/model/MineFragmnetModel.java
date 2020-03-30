package com.rice.model;


/**
 * Created by Administrator on 2016/3/8 0008.
 */
public class MineFragmnetModel extends BaseModel {
    private int resId = 0;//图标
    private String title = "";//名称
    public static final int FIRST = 0;//第一项,前部加空白区域
    public static final int NORMAL = 1;//中间项,只有下划线
    public static final int END = 2;//末尾项,取消下划线
    private int location = NORMAL;//当前项所在的位置

    public int getResId() {
        return resId;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
