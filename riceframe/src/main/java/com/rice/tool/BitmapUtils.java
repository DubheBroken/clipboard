package com.rice.tool;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import androidx.exifinterface.media.ExifInterface;

import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Bitmap相关工具类
 */
public class BitmapUtils {

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static final String USERHEAD = "temp.jpg";

    private static final float RADIUS_FACTOR = 8.0f;
    private static final int TRIANGLE_WIDTH = 120;
    private static final int TRIANGLE_HEIGHT = 100;
    private static final int TRIANGLE_OFFSET = 300;

    public Bitmap processImage(Bitmap bitmap) {
        Bitmap bmp;

        bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);

        float radius = Math.min(bitmap.getWidth(), bitmap.getHeight()) / RADIUS_FACTOR;
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        RectF rect = new RectF(TRIANGLE_WIDTH, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawRoundRect(rect, radius, radius, paint);

        Path triangle = new Path();
        triangle.moveTo(0, TRIANGLE_OFFSET);
        triangle.lineTo(TRIANGLE_WIDTH, TRIANGLE_OFFSET - (TRIANGLE_HEIGHT / 2));
        triangle.lineTo(TRIANGLE_WIDTH, TRIANGLE_OFFSET + (TRIANGLE_HEIGHT / 2));
        triangle.close();
        canvas.drawPath(triangle, paint);

        return bmp;
    }

//    @TargetApi(19)
//    public static String getPath(final Context context, final Uri uri) {
//
//        final boolean isKitKat = Build.VERSION.SDK_INT >= 19;
//
//        // DocumentProvider
//        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//            // ExternalStorageProvider
//            if (isExternalStorageDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                if ("primary".equalsIgnoreCase(type)) {
//                    return Environment.getExternalStorageDirectory() + "/" + split[1];
//                }
//
//                // TODO handle non-primary volumes
//            }
//            // DownloadsProvider
//            else if (isDownloadsDocument(uri)) {
//
//                final String id = DocumentsContract.getDocumentId(uri);
//                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
//                        Long.valueOf(id));
//
//                return getDataColumn(context, contentUri, null, null);
//            }
//            // MediaProvider
//            else if (isMediaDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                Uri contentUri = null;
//                if ("image".equals(type)) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                } else if ("video".equals(type)) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                } else if ("audio".equals(type)) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                }
//
//                final String selection = "_id=?";
//                final String[] selectionArgs = new String[]{split[1]};
//
//                return getDataColumn(context, contentUri, selection, selectionArgs);
//            }
//        }
//        // MediaStore (and general)
//        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            // Return the remote address
//            if (isGooglePhotosUri(uri))
//                return uri.getLastPathSegment();
//
//            return getDataColumn(context, uri, null, null);
//        }
//        // File
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        } else {
//            return selectImage(context, uri);
//        }
//        return null;
//    }

    public static String selectImage(Context context, Uri selectedImage) {
        if (selectedImage != null) {
            String uriStr = selectedImage.toString();
            String path = uriStr.substring(10, uriStr.length());
            if (path.startsWith("com.sec.android.gallery3d")) {
                return null;
            }
        }
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }
//
//    /**
//     * Get the value of the data column for this Uri. This is useful for
//     * MediaStore Uris, and other file-based ContentProviders.
//     *
//     * @param context       The context.
//     * @param uri           The Uri to query.
//     * @param selection     (Optional) Filter used in the query.
//     * @param selectionArgs (Optional) Selection arguments used in the query.
//     * @return The value of the _data column, which is typically a file path.
//     */
//    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
//        if (EasyPermissions.hasPermissions(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            Cursor cursor = null;
//            final String column = "_data";
//            final String[] projection = {column};
//            try {
//                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
//                if (cursor != null && cursor.moveToFirst()) {
//                    final int index = cursor.getColumnIndexOrThrow(column);
//                    return cursor.getString(index);
//                }
//            } finally {
//                if (cursor != null)
//                    cursor.close();
//            }
//        } else {
//            ToastUtil.showShort("使用此功能需要您开启存储权限");
//            return null;
//        }
//        return null;
//    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    // //

    /**
     * 创建一个缩放的图片
     *
     * @param path 图片地址
     * @param w    图片宽度
     * @param h    图片高度
     * @return 缩放后的图片
     */
    public static Bitmap createBitmap(String path, int w, int h) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            // 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
            BitmapFactory.decodeFile(path, opts);
            int srcWidth = opts.outWidth;// 获取图片的原始宽度
            int srcHeight = opts.outHeight;// 获取图片原始高度
            int destWidth = 0;
            int destHeight = 0;
            // 缩放的比例
            double ratio = 0.0;
            if (srcWidth < w || srcHeight < h) {
                ratio = 0.0;
                destWidth = srcWidth;
                destHeight = srcHeight;
            } else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
                ratio = (double) srcWidth / w;
                destWidth = w;
                destHeight = (int) (srcHeight / ratio);
            } else {
                ratio = (double) srcHeight / h;
                destHeight = h;
                destWidth = (int) (srcWidth / ratio);
            }
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
            newOpts.inSampleSize = (int) ratio + 1;
            // inJustDecodeBounds设为false表示把图片读进内存中
            newOpts.inJustDecodeBounds = false;
            // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;
            // 获取缩放后图片
            return BitmapFactory.decodeFile(path, newOpts);
        } catch (Exception e) {
            return null;
        }
    }
//
//    public static boolean delUserHead() {
//        File delFile = new File(DIR, USERHEAD);
//        if (delFile.exists()) {
//            delFile.delete();
//            return true;
//        } else {
//            return false;
//        }
//
//    }

    /**
     * 读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap ReadBitmapById(Context context, int resId) {
        return ReadBitmapById(context, resId, -1, -1);
    }

    public static Bitmap ReadBitmapById(Context context, int resId, int outwidth, int outheight) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        if (-1 != outwidth) {
            opt.outWidth = outwidth;
            opt.outHeight = outheight;
        }
        opt.inJustDecodeBounds = false;
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static Bitmap ReadBitmapById1(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        InputStream is = context.getResources().openRawResource(resId);
        BitmapFactory.decodeStream(is, null, opt);
        float scale = context.getResources().getDisplayMetrics().density;
        if (scale >= 1.5) {
            opt.inSampleSize = 1;
        } else {
            opt.inSampleSize = 2;
        }
        opt.inJustDecodeBounds = false;
        // 获取资源图片
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /***
     * 根据资源文件获取Bitmap
     *
     * @param context
     * @param drawableId
     * @return
     */
    public static Bitmap ReadBitmapById2(Context context, int drawableId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inInputShareable = true;
        options.inPurgeable = true;
        InputStream stream = context.getResources().openRawResource(drawableId);
        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
        return getBitmap(bitmap, context);
    }

    public static Bitmap DrawShadowImg(Bitmap bitmap, float radius) {
        BlurMaskFilter blurFilter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.OUTER);
        Paint shadowPaint = new Paint();
        shadowPaint.setColor(0xff81c340);
        shadowPaint.setMaskFilter(blurFilter);
        int[] offsetXY = new int[2];
        Bitmap shadowImage = bitmap.extractAlpha(shadowPaint, offsetXY);
        Bitmap shadow = shadowImage.copy(Bitmap.Config.ARGB_8888, true);
        Canvas c = new Canvas(shadow);
        c.drawBitmap(bitmap, -offsetXY[0], -offsetXY[1], null);
        return shadow;
    }

    // 阴影
    public static Bitmap DrawShadowImg(Bitmap bitmap) {
        return DrawShadowImg(bitmap, 3.2f);

    }

    /***
     * 等比例压缩图片
     *
     * @param bitmap
     * @param context
     * @return
     */
    public static Bitmap getBitmap(Bitmap bitmap, Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHight = dm.heightPixels;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scale = (float) screenWidth / w;
        float scale2 = (float) screenHight / h;
        scale = scale < scale2 ? scale : scale2;
        // 保证图片不变形.
        matrix.postScale(scale, scale);
        // w,h是原图的属性.
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }


    /**
     * 根据路径获得图片并压缩返回bitmap用于显示
     *
     * @param filePath
     * @return
     */
    public static Bitmap getCompileBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = 2;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

//    /***
//     * 保存图片至SD卡(JPG)
//     *
//     * @param bm
//     * @param url
//     * @param quantity
//     */
//    private static int FREE_SD_SPACE_NEEDED_TO_CACHE = 1;
//    private static int MB = 1024 * 1024;
//    public final static String DIR = Constant.STORE_PATH;
//
//    public static String saveBmpToSd(Bitmap bm, int quantity) {
//        String savepath = Common.getOneImagePathName();
//        File file = new File(savepath);
//        try {
//            file.createNewFile();
//            OutputStream outStream = new FileOutputStream(file);
//            bm.compress(Bitmap.CompressFormat.JPEG, quantity, outStream);
//            outStream.flush();
//            outStream.close();
//
//            if (file != null) {
//                // 通知图库更新
//                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                Uri uri = Uri.fromFile(file);
//                intent.setData(uri);
//                MyApplication.mContext.sendBroadcast(intent);
//            }
//
//            return savepath;
//
//        } catch (FileNotFoundException e) {
//            return null;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }


//    public static Map<String, Object> saveBmpToSd(Bitmap bm) {//保存压缩图片至sd卡
//        String savepath = "";
//        File file1 = new File(DIR);
//        if (!file1.exists()) {
//            file1.mkdirs();
//        }
//        savepath = DIR + System.currentTimeMillis() + "_cut.jpg";
//        File file = new File(savepath);
//        try {
//            file.createNewFile();
//            OutputStream outStream = new FileOutputStream(file);
//            outStream.flush();
//            outStream.close();
//            return compressImage(bm, savepath, false);
//
//        } catch (FileNotFoundException e) {
//            return null;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }


//    /**
//     * 保存图片至SD卡(PNG)
//     *
//     * @param bm
//     * @param url
//     * @param quantity
//     */
//    public static String saveBmpToSdPNG(Bitmap bm, String url, int quantity) {
//        // 判断sdcard上的空间
//        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
//            return null;
//        }
//        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
////            Log.i("SDcard", "SDcard Not Found");
//            return null;
//        }
//        String filename = url;
////        // 目录不存在就创建
////        File dirPath = new File(DIR);
////        if (!dirPath.exists()) {
////            dirPath.mkdirs();
////        }
//
//        File file = new File(filename);
//        if (!file.exists()) {//如果文件（名）不存在，新建文件(视频截图保存)
//            file = new File(DIR + "video" + filename);
//        }
//        try {
//            file.createNewFile();
//            OutputStream outStream = new FileOutputStream(file);
//            bm.compress(Bitmap.CompressFormat.JPEG, quantity, outStream);
//            outStream.flush();
//            outStream.close();
//
//            if (file != null) {
//                // 通知图库更新
//                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                Uri uri = Uri.fromFile(file);
//                intent.setData(uri);
//                MyApplication.mContext.sendBroadcast(intent);
//            }
//
//            return file.getAbsolutePath();
//
//        } catch (FileNotFoundException e) {
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//
//    }

    /**
     * LOMO特效
     *
     * @param bitmap 原图片
     * @return LOMO特效图片
     */
    public static Bitmap lomoFilter(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int dst[] = new int[width * height];
        bitmap.getPixels(dst, 0, width, 0, 0, width, height);

        int ratio = width > height ? height * 32768 / width : width * 32768 / height;
        int cx = width >> 1;
        int cy = height >> 1;
        int max = cx * cx + cy * cy;
        int min = (int) (max * (1 - 0.8f));
        int diff = max - min;

        int ri, gi, bi;
        int dx, dy, distSq, v;

        int R, G, B;

        int value;
        int pos, pixColor;
        int newR, newG, newB;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pos = y * width + x;
                pixColor = dst[pos];
                R = Color.red(pixColor);
                G = Color.green(pixColor);
                B = Color.blue(pixColor);

                value = R < 128 ? R : 256 - R;
                newR = (value * value * value) / 64 / 256;
                newR = (R < 128 ? newR : 255 - newR);

                value = G < 128 ? G : 256 - G;
                newG = (value * value) / 128;
                newG = (G < 128 ? newG : 255 - newG);

                newB = B / 2 + 0x25;

                // ==========边缘黑暗==============//
                dx = cx - x;
                dy = cy - y;
                if (width > height)
                    dx = (dx * ratio) >> 15;
                else
                    dy = (dy * ratio) >> 15;

                distSq = dx * dx + dy * dy;
                if (distSq > min) {
                    v = ((max - distSq) << 8) / diff;
                    v *= v;

                    ri = (int) (newR * v) >> 16;
                    gi = (int) (newG * v) >> 16;
                    bi = (int) (newB * v) >> 16;

                    newR = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
                    newG = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
                    newB = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
                }
                // ==========边缘黑暗end==============//

                dst[pos] = Color.rgb(newR, newG, newB);
            }
        }

        Bitmap acrossFlushBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        acrossFlushBitmap.setPixels(dst, 0, width, 0, 0, width, height);
        return acrossFlushBitmap;
    }

    /**
     * 旧时光特效
     *
     * @param bmp 原图片
     * @return 旧时光特效图片
     */
    public static Bitmap oldTimeFilter(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
                newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
                newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
                int newColor = Color.argb(255, newR > 255 ? 255 : newR, newG > 255 ? 255 : newG,
                        newB > 255 ? 255 : newB);
                pixels[width * i + k] = newColor;
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 暖意特效
     *
     * @param bmp     原图片
     * @param centerX 光源横坐标
     * @param centerY 光源纵坐标
     * @return 暖意特效图片
     */
    public static Bitmap warmthFilter(Bitmap bmp, int centerX, int centerY) {
        final int width = bmp.getWidth();
        final int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;
        int radius = Math.min(centerX, centerY);

        final float strength = 150F; // 光照强度 100~150
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                pos = i * width + k;
                pixColor = pixels[pos];

                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);

                newR = pixR;
                newG = pixG;
                newB = pixB;

                // 计算当前点到光照中心的距离，平面座标系中求两点之间的距离
                int distance = (int) (Math.pow((centerY - i), 2) + Math.pow(centerX - k, 2));
                if (distance < radius * radius) {
                    // 按照距离大小计算增加的光照值
                    int result = (int) (strength * (1.0 - Math.sqrt(distance) / radius));
                    newR = pixR + result;
                    newG = pixG + result;
                    newB = pixB + result;
                }

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

//    /***
//     * 判断图片是存在
//     *
//     * @param url
//     * @return
//     */
//    public static boolean Exist(String url) {
//        File file = new File(DIR + url);
//        return file.exists();
//    }


//    /**
//     * 计算sdcard上的剩余空间 * @return
//     */
//    private static int freeSpaceOnSd() {
//        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
//        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
//
//        return (int) sdFreeMB;
//    }

    public static void removeOneCache(String path) {
        File fileame = new File(path);
        if (fileame.exists()) {
            fileame.delete();
        }
    }

    public static Bitmap getimagebmpCompress(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        float w = newOpts.outWidth;
        float h = newOpts.outHeight;
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) Math.round((double) (w / ww));
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) Math.round((double) (h / hh));
        } else if (w == h) {
            be = (int) Math.round((double) (w / ww));
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

//    public static Map<String, Object> getimageCompress(String srcPath) {//压缩图片  4.5M压缩至90kb左右
//        if (FileUtils.getFileSize(srcPath) <= 1024 * 1024) {//小于0.5M不压缩
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("path", srcPath);
//            return map;
//        } else {
//            String path1 = srcPath.substring(0, srcPath.lastIndexOf("."));
//            if (path1.contains("/")) {
//                String path2 = path1.split("/")[path1.split("/").length - 1];
//                String savepath = Constant.STORE_PATH + path2 + "_compress.jpg";
//                if (new File(savepath).exists()) {
//                    Map<String, Object> map = new HashMap<String, Object>();
//                    map.put("path", savepath);
//                    return map;
//                }
//            }
//
//
//            BitmapFactory.Options newOpts = new BitmapFactory.Options();
//            // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
//            newOpts.inJustDecodeBounds = true;
//            Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
//
//            newOpts.inJustDecodeBounds = false;
//            float w = newOpts.outWidth;
//            float h = newOpts.outHeight;
//            float hh = 1280f;// 这里设置高度为800f
//            float ww = 720f;// 这里设置宽度为480f
//            // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
//            int be = 1;// be=1表示不缩放
//            if (w / h < 9 / (float) 16) {//长图，一般宽度都小于720，压缩根据宽度来
//                if (h > 13000) {
//                    if (w <= ww) {
//                        be = (int) Math.round((double) (h / 13000));
//                    } else {
//                        be = (int) Math.round((double) (ww / w));
//                    }
//                } else {
//                    if (w <= ww) {
//                        be = (int) Math.round((double) (w / ww));
//                    } else {
//                        be = (int) Math.round((double) (ww / w));
//                    }
//                }
//            } else {
//                if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
//                    be = (int) Math.round((double) (w / ww));
//                } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
//                    be = (int) Math.round((double) (h / hh));
//                } else if (w == h) {
//                    be = (int) Math.round((double) (w / ww));
//                }
//            }
//            if (be <= 0)
//                be = 1;
//            newOpts.inSampleSize = be;// 设置缩放比例
//            // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
//            bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//            if (bitmap != null) {
//                return compressImage(bitmap, srcPath, false);// 压缩好比例大小后再进行质量压缩
//            } else {
//                return null;
//            }
//        }
//    }

//    public static Map<String, Object> compressImage(Bitmap image, String path, boolean isDelete) {
//        String savepath = "";
//        int Maxsize = 0;
//        Map<String, Object> map = null;
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//
//        //如果以前压缩过该图片并且存在图片，直接引用压缩的图片
//        if (path != null) {
//            String fileName = new File(path).getName();
//            savepath = Constant.STORE_PATH + fileName.substring(0, fileName.lastIndexOf(".")) + "_compress.jpg";
//            try {
//                new File(savepath).createNewFile();
//                if (isDelete) {
//                    File deletefile = new File(path);
//                    deletefile.delete();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        int options = 100;
//        int baosbyte = baos.toByteArray().length / 1024;
//        if (baosbyte > 1024) {//压缩后还大于1M则进行再次压缩，否则直接返回
//            Maxsize = 1024;
//            while (baos.toByteArray().length / 1024 > Maxsize) { // 循环判断如果压缩后图片是否大于Maxsize,大于继续压缩
//                baos.reset();// 重置baos即清空ba9os
//                options -= 20;// 每次都减少10
//                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//            }
//        }
//        if (image != null && !image.isRecycled()) {
//            image.recycle();
//            image = null;
//        }
//        try {
//            File file = new File(savepath);
//            FileOutputStream fos = new FileOutputStream(savepath);
//            fos.write(baos.toByteArray());
//            fos.flush();
//            fos.close();
//            map = new HashMap<String, Object>();
//            map.put("path", savepath);
//            baos.reset();
//            baos.close();
//            if (file != null) {
//                // 通知图库更新
//                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                Uri uri = Uri.fromFile(file);
//                intent.setData(uri);
//                MyApplication.mContext.sendBroadcast(intent);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return map;
//    }

//    public static Map<String, Object> compressImage(Bitmap image, String path, boolean isDelete, int compressbyte) {
//        String savepath = "";
//        int Maxsize = 0;
//        Map<String, Object> map = null;
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//
//        //如果以前压缩过该图片并且存在图片，直接引用压缩的图片
//        if (path != null) {
//            String fileName = new File(path).getName();
//            savepath = Constant.STORE_PATH + fileName.substring(0, fileName.lastIndexOf(".")) + "_compress.jpg";
//            try {
//                new File(savepath).createNewFile();
//                if (isDelete) {
//                    File deletefile = new File(path);
//                    deletefile.delete();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        int options = 100;
//        int baosbyte = baos.toByteArray().length / 1024;
//        if (baosbyte > compressbyte) {//压缩后还大于1M则进行再次压缩，否则直接返回
//            Maxsize = compressbyte;
//            while (baos.toByteArray().length / 1024 > Maxsize) { // 循环判断如果压缩后图片是否大于Maxsize,大于继续压缩
//                baos.reset();// 重置baos即清空ba9os
//                options -= 5;// 每次都减少5
//                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//            }
//        }
//        if (image != null && !image.isRecycled()) {
//            image.recycle();
//            image = null;
//        }
//        try {
//            File file = new File(savepath);
//            FileOutputStream fos = new FileOutputStream(savepath);
//            fos.write(baos.toByteArray());
//            fos.flush();
//            fos.close();
//            map = new HashMap<String, Object>();
//            map.put("path", savepath);
//            baos.reset();
//            baos.close();
//            if (file != null) {
//                // 通知图库更新
//                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                Uri uri = Uri.fromFile(file);
//                intent.setData(uri);
//                MyApplication.mContext.sendBroadcast(intent);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return map;
//    }


    /**
     * 缩放图片
     *
     * @param bitmap
     * @param zf
     * @return
     */

    public static Bitmap zoom(Bitmap bitmap, float zf) {
        Matrix matrix = new Matrix();
        matrix.postScale(zf, zf);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 缩放图片
     *
     * @param bitmap
     * @param wf
     * @return
     */
    public static Bitmap zoom(Bitmap bitmap, float wf, float hf) {
        Matrix matrix = new Matrix();
        matrix.postScale(wf, hf);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 图片圆角处理
     *
     * @param bitmap
     * @param roundPX
     * @return
     */
    public static Bitmap getRCB(Bitmap bitmap, float roundPX) {
        // RCB means
        // Rounded
        // Corner Bitmap
        Bitmap dstbmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dstbmp);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return dstbmp;
    }

    /**
     * 获取原始图片的角度（解决三星手机拍照后图片是横着的问题）
     *
     * @param path 图片的绝对路径
     * @return 原始图片的角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

//    public static String createVideoThumbnail(String filePath, int viewId) {
//        if (new File(Constant.STORE_PATH + viewId + ".jpg").exists()) {
//            return Constant.STORE_PATH + viewId + ".jpg";
//        } else {
//            Bitmap bitmap = null;
//            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//            try {
//                if (filePath.startsWith("http://")
//                        || filePath.startsWith("https://")
//                        || filePath.startsWith("widevine://")) {
//                    retriever.setDataSource(filePath, new Hashtable<String, String>());
//                } else {
//                    retriever.setDataSource(filePath);
//                }
//                bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC); //retriever.getFrameAtTime(-1);
//            } catch (IllegalArgumentException ex) {
//                // Assume this is a corrupt video file
//                ex.printStackTrace();
//            } catch (RuntimeException ex) {
//                // Assume this is a corrupt video file.
//                ex.printStackTrace();
//            } finally {
//                try {
//                    retriever.release();
//                } catch (RuntimeException ex) {
//                    // Ignore failures while cleaning up.
//                    ex.printStackTrace();
//                }
//            }
//
//            if (bitmap == null) {
//                return null;
//            }
//
//            return (String) compressImage(bitmap, Constant.STORE_PATH + viewId + ".jpg", false, 20).get("path");
//        }
//    }

}
