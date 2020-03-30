package com.dubhe.imageselector;

import android.net.Uri;

public class Path {

    public static String imgPath = "";
    public static final int REQUEST_SYSTEM_PIC = 1;//打开系统相册
    //请求码
    public static final int REQUEST_OPEN_CAMERA = 2;//打开相机
    public static final int REQUEST_PERMISSIONS = 3;//请求权限
    public static final int REQUEST_CROP_PHOTO = 4;//裁图
    //结果错误码
    public static final int CAMERA_OPEN_FAIL = 1;//打开相机失败
    public static final int FILE_SYSTEM_FAIL = 2;
    public static final int INTERNET_ERROR = 3;
    public static final int SERVER_ERROR = 4;
    public static String imgPathOri;
    public static Uri imgUriOri;

}
