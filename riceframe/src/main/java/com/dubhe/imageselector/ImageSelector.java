package com.dubhe.imageselector;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Build.VERSION;
import android.view.Gravity;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.rice.riceframe.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kotlin.jvm.internal.Intrinsics;

import org.jetbrains.annotations.Nullable;

import static android.app.Activity.RESULT_OK;

public class ImageSelector {
    private boolean enableClip;//是否启动裁剪
    private OnProcessFinishListener onProcessFinishListener;//操作完成监听
    private int clipMode = ClipImageActivity.TYPE_CIRCLE;//裁剪模式，默认圆形模式
    private Dialog imageSelectDialog;//图片选择Dialog
    private AppCompatActivity mActivity;
    private Fragment mFragment;
    private static ImageSelector instance;

    public boolean getEnableClip() {
        return this.enableClip;
    }

    public ImageSelector.OnProcessFinishListener getOnProcessFinishListener() {
        return this.onProcessFinishListener;
    }

    public int getClipMode() {
        return this.clipMode;
    }

    public ImageSelector setEnableClip(boolean b) {
        this.enableClip = b;
        return this;
    }

    public ImageSelector setOnProcessFinishListener(ImageSelector.OnProcessFinishListener listener) {
        this.onProcessFinishListener = listener;
        return this;
    }

    public ImageSelector setClipMode(int mode) {
        this.clipMode = mode;
        return this;
    }

    /**
     * 回收数据，在调用的Activity的onDestroy中调用此方法
     */
    public final void clear() {
        if (instance != null) {
            instance.enableClip = false;
            instance.clipMode = 1;
            instance.onProcessFinishListener = null;
        }
        instance = null;
    }

    /**
     * 显示图片选择Dialog
     */
    public final void showImageSelectMenu() {
        imageSelectDialog.show();
        imageSelectDialog.setCanceledOnTouchOutside(true);//点击窗口外消失

        LinearLayout fromCamera = imageSelectDialog.getWindow().findViewById(R.id.linear_camera);
        LinearLayout fromAlbum = imageSelectDialog.getWindow().findViewById(R.id.linear_album);

        fromCamera.setOnClickListener(onClickListener);
        fromAlbum.setOnClickListener(onClickListener);
    }

    /**
     * 打开相机
     */
    @SuppressLint({"LongLogTag"})
    private void openCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File oriPhotoFile = null;
        try {
            oriPhotoFile = this.createOriImageFile();
        } catch (IOException e) {
            this.showErrorMessage(Path.FILE_SYSTEM_FAIL);
            e.printStackTrace();
        }
        if (oriPhotoFile != null) {
            if (VERSION.SDK_INT < 24) {
                Path.imgUriOri = Uri.fromFile(oriPhotoFile);
            } else {
                Path.imgUriOri = (FileProvider.getUriForFile(this.mActivity, this.mActivity.getPackageName() + ".fileProvider", oriPhotoFile));
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra("output", Path.imgUriOri);
            try {
                if (mFragment != null) {
                    this.mFragment.startActivityForResult(intent, Path.REQUEST_OPEN_CAMERA);
                } else {
                    this.mActivity.startActivityForResult(intent, Path.REQUEST_OPEN_CAMERA);
                }
            } catch (Exception e) {
                this.showErrorMessage(Path.CAMERA_OPEN_FAIL);
                e.printStackTrace();
            }
        }

    }

    /**
     * 显示错误信息Toast
     *
     * @param errorCode 错误码在Path文件中
     */
    private void showErrorMessage(int errorCode) {
        String message = null;
        switch (errorCode) {
            case Path.CAMERA_OPEN_FAIL:
                message = "打开相机失败，请允许相机权限";
                break;
            case Path.FILE_SYSTEM_FAIL:
                message = "保存照片失败，请允许文件存储权限";
                break;
            case Path.INTERNET_ERROR:
                message = "连接服务器失败，请检查网络";
                break;
            case Path.SERVER_ERROR:
                message = "服务器异常，请联系管理员";
                break;
        }
        if (message != null) {
            Toast.makeText(this.mActivity, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 新建图片文件
     *
     * @return 新建的图片文件
     * @throws IOException 可能发生的IO异常
     */
    @SuppressLint("SimpleDateFormat")
    private File createOriImageFile() throws IOException {
        String imgNameOri = "Pic" + (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
        File file = this.mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File pictureDirOri = new File(Intrinsics.stringPlus(file != null ? file.getAbsolutePath() : null, "/OriPicture"));
        if (!pictureDirOri.exists()) {
            pictureDirOri.mkdirs();
        }
        File image = File.createTempFile(imgNameOri, ".jpg", pictureDirOri);
        Intrinsics.checkExpressionValueIsNotNull(image, "image");
        Path.imgPathOri = image.getAbsolutePath();
        return image;
    }

    /**
     * 打开系统相册
     */
    private void openAlbum() {
//        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if (mFragment != null) {
            this.mFragment.startActivityForResult(intent, Path.REQUEST_SYSTEM_PIC);
        } else {
            this.mActivity.startActivityForResult(intent, Path.REQUEST_SYSTEM_PIC);
        }
    }

    /**
     * 在调用的activity的onActivityResult调用此方法
     * or
     * 在调用的Fragment的父Activity的onActivityResult调用此方法
     *
     * @params 此处原样传入onActivityResult的参数
     */
    public void onHeaderResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Path.REQUEST_OPEN_CAMERA: {
                    //相机拍照返回
                    if (this.enableClip) {
                        this.gotoClipActivity(Path.imgUriOri);
                    } else if (this.onProcessFinishListener != null) {
//                        onProcessFinishListener.onProcessFinish(ImageUtils.getRealPathFromUri(mActivity, Path.imgUriOri));
                        onProcessFinishListener.onProcessFinish(Path.imgPathOri);
                    }
                    break;
                }
                case Path.REQUEST_SYSTEM_PIC: {
                    //相册选图返回
                    if (data != null) {
                        Uri uri = data.getData();
                        Path.imgPathOri = (ImageUtils.getRealPathFromUri(this.mActivity, uri));
                        if (this.enableClip) {
                            this.gotoClipActivity(uri);
                        } else if (this.onProcessFinishListener != null) {
                            onProcessFinishListener.onProcessFinish(Path.imgPathOri);
                        }
                    } else {
                        Toast.makeText(mActivity, "获取图片失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case Path.REQUEST_CROP_PHOTO: {
                    //裁图返回
                    if (data != null) {
                        Path.imgPath = data.getStringExtra("path");
                        if (this.onProcessFinishListener != null) {
                            onProcessFinishListener.onProcessFinish(Path.imgPath);
                        }
                    } else {
                        Toast.makeText(mActivity, "获取图片失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    }

    /**
     * 启动裁图界面
     *
     * @param uri 相册or相机返回的Uri
     */
    public void gotoClipActivity(@Nullable Uri uri) {
        if (uri != null) {
            Intent intent = new Intent();
            intent.setClass(this.mActivity, ClipImageActivity.class);
            intent.putExtra("type", this.clipMode);
            intent.putExtra("path", Path.imgPathOri);
            intent.setData(uri);
            if (mFragment != null) {
                this.mFragment.startActivityForResult(intent, Path.REQUEST_CROP_PHOTO);
            } else {
                this.mActivity.startActivityForResult(intent, Path.REQUEST_CROP_PHOTO);
            }
        }
    }

    /**
     * 初始化方法
     *
     * @param mActivity 调用此选择器的Activity
     *                  or
     *                  调用此选择器的Fragment所在的Activity
     */
    private ImageSelector(AppCompatActivity mActivity) {
        imageSelectDialog = new Dialog(mActivity, R.style.BottomDialog);
        Window window = imageSelectDialog.getWindow();
        imageSelectDialog.setContentView(R.layout.image_select_bottom_menu);
        window.setGravity(Gravity.BOTTOM);
        //        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setWindowAnimations(R.style.BottomDialog_Animation);
        LayoutParams lp = window.getAttributes();
        //设置宽
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置高
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        this.mActivity = mActivity;
    }

    /**
     * 初始化方法
     *
     * @param mFragment 调用此选择器的Activity
     *                  or
     *                  调用此选择器的Fragment所在的Activity
     */
    private ImageSelector(Fragment mFragment) {
        imageSelectDialog = new Dialog(mFragment.getContext(), R.style.BottomDialog);
        Window window = imageSelectDialog.getWindow();
        imageSelectDialog.setContentView(R.layout.image_select_bottom_menu);
        window.setGravity(Gravity.BOTTOM);
        //        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setWindowAnimations(R.style.BottomDialog_Animation);
        LayoutParams lp = window.getAttributes();
        //设置宽
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置高
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        this.mFragment = mFragment;
    }


    /**
     * 弹出Dialog的点击监听
     */
    private OnClickListener onClickListener = v -> {
        int id = v.getId();
        if (id == R.id.linear_camera) {//点击弹出框中的相机按钮
            imageSelectDialog.dismiss();
            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, 1);
            } else {
//                    打开系统相机
                openCamera();
            }
        } else if (id == R.id.linear_album) {//点击弹出框中的相册
            imageSelectDialog.dismiss();
            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
//                    打开系统相册
                openAlbum();
            }
        }
    };

    /**
     * 单例模式取对象不解释
     *
     * @param mActivity 调用此选择器的Activity
     *                  or
     *                  调用此选择器的Fragment所在的Activity
     * @return 单例对象
     */
    public static ImageSelector getInstance(AppCompatActivity mActivity) {
        if (instance == null || instance.mActivity == null) {
            instance = new ImageSelector(mActivity);
        }
        return instance;
    }

    /**
     * 单例模式取对象不解释
     *
     * @param mFragment 调用此选择器的Activity
     *                  or
     *                  调用此选择器的Fragment所在的Activity
     * @return 单例对象
     */
    public static ImageSelector getInstance(Fragment mFragment) {
        if (instance == null || instance.mFragment == null) {
            instance = new ImageSelector(mFragment);
            instance.mActivity = (AppCompatActivity) mFragment.getActivity();
        }
        return instance;
    }

    public interface OnProcessFinishListener {
        void onProcessFinish(String path);
    }

}
