package com.dubhe.imageselector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rice.riceframe.R;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 头像裁剪Activity
 */
@SuppressLint("Registered")
public class ClipImageActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ClipImageActivity";
    private ClipViewLayout clipViewLayout1;
    private ClipViewLayout clipViewLayout2;
    private ImageView ivBack;
    private int type = TYPE_CIRCLE;//1圆形模式；2矩形模式
    private String path;//裁剪前的原图path
    private String imgPathOri;//裁剪后的path
    private TextView stockName;
    private ImageView ivOk;
    private Context context = this;
    public static final int TYPE_CIRCLE = 1;
    public static final int TYPE_RECTANGLE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_image);
        type = getIntent().getIntExtra("type", TYPE_CIRCLE);
        path = getIntent().getStringExtra("path");
        initView();
    }

    /**
     * 初始化组件
     */
    public void initView() {
        clipViewLayout1 = findViewById(R.id.clipViewLayout1);
        ivBack = findViewById(R.id.iv_back);
        ivOk = findViewById(R.id.iv_ok);
        //设置点击事件监听器
        ivBack.setOnClickListener(this);
        ivOk.setOnClickListener(this);
        stockName = findViewById(R.id.stock_name);
        clipViewLayout2 = findViewById(R.id.clipViewLayout2);

//        initStatusBar(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (type == 1) {
            clipViewLayout1.setVisibility(View.VISIBLE);
            clipViewLayout2.setVisibility(View.GONE);
            //设置图片资源
            clipViewLayout1.setImageSrc(getIntent().getData(), path);
        } else {
            clipViewLayout2.setVisibility(View.VISIBLE);
            clipViewLayout1.setVisibility(View.GONE);
            //设置图片资源
            clipViewLayout2.setImageSrc(getIntent().getData(), path);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.iv_ok) {
            generateUriAndReturn();
        }
    }


    /**
     * 生成Uri并且通过setResult返回给打开的activity
     */
    private void generateUriAndReturn() {
        //调用返回剪切图
        Bitmap zoomedCropBitmap;
        if (type == 1) {
            zoomedCropBitmap = clipViewLayout1.clip();
        } else {
            zoomedCropBitmap = clipViewLayout2.clip();
        }
        if (zoomedCropBitmap == null) {
            Log.e("android", "zoomedCropBitmap == null");
            return;
        }
        Uri mSaveUri = null;
        try {
            File pictureDirOri = createOriImageFile();
            mSaveUri = Uri.fromFile(pictureDirOri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(mSaveUri);
                if (outputStream != null) {
                    zoomedCropBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                }
            } catch (IOException ex) {
                Log.e("android", "Cannot open file: " + mSaveUri, ex);
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Intent intent = new Intent();
            intent.putExtra("path", imgPathOri);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    /**
     * 创建图片文件
     */
    private File createOriImageFile() throws IOException {
        String imgNameOri = "HomePic" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File pictureDirOri = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/OriPicture");
        if (!pictureDirOri.exists()) {
            pictureDirOri.mkdirs();
        }
        File image = File.createTempFile(
                imgNameOri,         /* prefix */
                ".jpg",             /* suffix */
                pictureDirOri       /* directory */
        );
        imgPathOri = image.getAbsolutePath();
        return image;
    }

//    /**
//     * 沉浸式状态栏
//     */
//    private void initStatusBar(boolean lightMode) {
//        //软键盘监听回调
//        ImmersionBar.with(this)
//                .statusBarColor(R.color.white)     //状态栏颜色，不写默认透明色
//                .navigationBarColor(lightMode ? R.color.white : R.color.black) //导航栏颜色，不写默认黑色
//                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
//                .navigationBarDarkIcon(lightMode) //导航栏图标是深色，不写默认为亮色
//                .flymeOSStatusBarFontColor(R.color.black)  //修改flyme OS状态栏字体颜色
//                .fitsSystemWindows(true)    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
//                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
//                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)  //单独指定软键盘模式
//                .setOnKeyboardListener((isPopup, keyboardHeight) -> {
////                        Logger.e(isPopup);  //isPopup为true，软键盘弹出，为false，软键盘关闭
//                })
//                .init();
//    }

}
