package com.rice.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.rice.imageloader.GlideLoadUtils;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 二维码工具类
 */
public class QRCodeUtils {

    /**
     * 异步生成二维码图片
     *
     * @param url 二维码内容
     * @param img 要放置图片的ImageView
     */
    public static void createQRCode(Context mContext, String url, ImageView img) {

        Observable.create((ObservableOnSubscribe<Bitmap>) emitter -> {
            Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(url, UnitUtils.dp2px(mContext, 362f));
            emitter.onNext(bitmap);
        }).subscribe(new Observer<Bitmap>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Bitmap bitmap) {
                if (GlideLoadUtils.isValidContextForGlide(mContext)) {
                    Glide.with(mContext).load(new BitmapDrawable(bitmap)).into(img);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
//                runOnUiThread {
                Toast.makeText(mContext, "生成二维码失败，请重试", Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onComplete() {

            }
        });

    }

}
