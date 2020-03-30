package com.rice.imageloader;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
//import com.bumptech.glide.load.resource.bitmap.CircleCrop;
//import com.bumptech.glide.request.RequestOptions;

/**
 * Glide 加载 简单判空封装 防止异步加载数据时调用Glide 抛出异常
 * Created by Li_Xavier on 2017/6/20 0020.
 */
public class GlideLoadUtils {
    private String TAG = "ImageLoader";

    /**
     * 借助内部类 实现线程安全的单例模式
     * 属于懒汉式单例，因为Java机制规定，内部类SingletonHolder只有在getInstance()
     * 方法第一次调用的时候才会被加载（实现了lazy），而且其加载过程是线程安全的。
     * 内部类加载的时候实例化一次instance。
     */
    public GlideLoadUtils() {
    }

    private static class GlideLoadUtilsHolder {
        private final static GlideLoadUtils INSTANCE = new GlideLoadUtils();
    }

    public static GlideLoadUtils getInstance() {
        return GlideLoadUtilsHolder.INSTANCE;
    }

    public static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity.isDestroyed()) {
                    return false;
                } else return !activity.isFinishing();
            } else {
                return !activity.isFinishing();
            }
        }
        return true;
    }

    /**
     * Glide 加载 简单判空封装 防止异步加载数据时调用Glide 抛出异常
     *
     * @param context
     * @param url       加载图片的url地址  String
     * @param imageView 加载图片的ImageView 控件
     */
    public void glideLoad(Context context, String url, ImageView imageView) {
        if (isValidContextForGlide(context)) {
            try {
                Glide.with(context).load(url.replace("\\", "/"))
                        //.crossFade()
                        .into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Picture loading failed,context is null");
        }
    }

    /**
     * Glide 加载 简单判空封装 防止异步加载数据时调用Glide 抛出异常
     *
     * @param context
     * @param url         加载图片的url地址  String
     * @param imageView   加载图片的ImageView 控件
     * @param roundRadius 圆角半径
     */
    public void glideLoad(Context context, String url, int roundRadius, ImageView imageView) {
        if (isValidContextForGlide(context)) {
            try {
                Glide.with(context).load(url.replace("\\", "/"))
                        .apply(new RequestOptions().transform(new CenterCrop(),new RoundedCorners(roundRadius)))//圆角半径
                        //.crossFade()
                        .into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Picture loading failed,context is null");
        }
    }

    /**
     * Glide 加载 简单判空封装 防止异步加载数据时调用Glide 抛出异常
     *
     * @param context
     * @param url       加载图片的url地址  String
     * @param imageView 加载图片的ImageView 控件
     * @param isCir     加载为圆形
     */
    public void glideLoad(Context context, String url, ImageView imageView, boolean isCir) {
        if (!isCir) {
            glideLoad(context, url, imageView);
        } else {
            if (context != null) {
                try {
                    Glide.with(context).load(url.replace("\\", "/"))
                            //.crossFade()
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.i(TAG, "Picture loading failed,context is null");
            }
        }
    }

    /**
     * Glide 加载 简单判空封装 防止异步加载数据时调用Glide 抛出异常
     *
     * @param context
     * @param url           加载图片的url地址  String
     * @param imageView     加载图片的ImageView 控件
     * @param default_image 图片展示错误的本地图片 id
     */
    public void glideLoad(Context context, String url, ImageView imageView, int default_image) {
        if (context != null) {
            try {
                Glide.with(context).load(url.replace("\\", "/"))
                        .error(default_image)
                        //.crossFade()
                        .into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Picture loading failed,context is null");
        }
    }

    public void glideLoad(Activity activity, String url, ImageView imageView, int default_image) {
        if (isValidContextForGlide(activity)) {
            try {
                Glide.with(activity).load(url.replace("\\", "/"))
                        .error(default_image)
                        //.crossFade()
                        .into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Picture loading failed,activity is Destroyed");
        }
    }

    public void glideLoad(Fragment fragment, String url, ImageView imageView, int default_image) {
        if (fragment != null && fragment.getActivity() != null) {
            try {
                Glide.with(fragment).load(url.replace("\\", "/"))
                        .error(default_image)
                        //.crossFade()
                        .into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Picture loading failed,fragment is null");
        }
    }

    public void glideLoad(android.app.Fragment fragment, String url, ImageView imageView, int default_image) {
        if (fragment != null && fragment.getActivity() != null) {
            try {
                Glide.with(fragment).load(url.replace("\\", "/"))
                        .error(default_image)
                        //.crossFade()
                        .into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Picture loading failed,android.app.Fragment is null");
        }
    }

    /**
     * Glide 加载 简单判空封装 防止异步加载数据时调用Glide 抛出异常
     *
     * @param context
     * @param url       加载图片的url地址  String
     * @param imageView 加载图片的ImageView 控件
     */
    public void glideGifLoad(Context context, String url, ImageView imageView) {
        if (isValidContextForGlide(context)) {
            Glide.with(context).load(url.replace("\\", "/"))
//                    .asGif()
//                    .error(default_image)
                    //.crossFade()
                    .into(imageView);
        } else {
            Log.i(TAG, "Picture loading failed,context is null");
        }
    }

    public void glideGifLoad(Activity activity, String url, ImageView imageView) {
        if (isValidContextForGlide(activity)) {
            Glide.with(activity).load(url.replace("\\", "/"))
//                    .asGif()
//                    .error(default_image)
                    //.crossFade()
                    .into(imageView);
        } else {
            Log.i(TAG, "Picture loading failed,activity is Destroyed");
        }
    }

    public void glideGifLoad(Fragment fragment, String url, ImageView imageView) {
        if (fragment != null && fragment.getActivity() != null && isValidContextForGlide(fragment.getActivity())) {
            Glide.with(fragment).load(url.replace("\\", "/"))
//                    .asGif()
//                    .error(default_image)
                    //.crossFade()
                    .into(imageView);
        } else {
            Log.i(TAG, "Picture loading failed,fragment is null");
        }
    }

    public void glideGifLoad(android.app.Fragment fragment, String url, ImageView imageView) {
        if (fragment != null && fragment.getActivity() != null && isValidContextForGlide(fragment.getActivity())) {
            Glide.with(fragment).load(url.replace("\\", "/"))
//                    .asGif()
//                    .error(default_image)
                    //.crossFade()
                    .into(imageView);
        } else {
            Log.i(TAG, "Picture loading failed,android.app.Fragment is null");
        }
    }
}