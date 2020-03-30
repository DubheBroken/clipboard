package com.rice.tool

import android.app.Activity
import android.graphics.*
import android.view.View

/**
 * 截取屏幕中指定内容的工具类
 */
class CaptureUtils {

    companion object {
        /**
         * 截取当前窗体的截图
         * @param isShowStatusBar 是否包含状态栏
         * 原理是获取当前窗体decorView的缓存生成图片
         */
        fun captureWindow(activity: Activity, isShowStatusBar: Boolean): Bitmap? {
            // 获取当前窗体的View对象
            val view = activity.window.decorView
            view.isDrawingCacheEnabled = true
            // 生成缓存
            view.buildDrawingCache()

            val bitmap = if (isShowStatusBar) {
                // 绘制整个窗体，包括状态栏
                Bitmap.createBitmap(view.drawingCache, 0, 0, view.measuredWidth, view.measuredHeight)
            } else {
                // 获取状态栏高度
                val rect = Rect()
                view.getWindowVisibleDisplayFrame(rect)
                val display = activity.windowManager.defaultDisplay

                // 减去状态栏高度
                Bitmap.createBitmap(view.drawingCache, 0,
                        rect.top, display.width, display.height - rect.top)
            }

            view.isDrawingCacheEnabled = false
            view.destroyDrawingCache()

            return bitmap
        }

        /**
         * 截取常用的View
         *
         * View已经在界面上展示了，可以直接获取View的缓存
         * 对View进行量测，布局后生成View的缓存
         * View为固定大小的View，包括TextView,ImageView,LinearLayout,FrameLayout,RelativeLayout等
         * @param view 截取的View,View必须有固定的大小，不然drawingCache返回null
         * @return 生成的Bitmap
         */
        fun captureView(view: View): Bitmap? {
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache()
            // 重新测量一遍View的宽高
            view.measure(View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(view.height, View.MeasureSpec.EXACTLY))
            // 确定View的位置
            view.layout(view.x.toInt(), view.y.toInt(), view.x.toInt() + view.measuredWidth,
                    view.y.toInt() + view.measuredHeight)
            // 生成View宽高一样的Bitmap
            val bitmap = Bitmap.createBitmap(view.drawingCache, 0, 0, view.measuredWidth,
                    view.measuredHeight)
            view.isDrawingCacheEnabled = false
            view.destroyDrawingCache()
            return bitmap
        }
//
//    /**
//     * 截取ListView
//     * 原理：获取到每一个子View，将子View生成的bitmap存入集合，并且累积ListView高度
//     * 遍历完成后，创建一个ListView大小的画布，将集合的Bitmap绘制到画布上
//     * @param listView 截图控件对象
//     * @return 生成的截图对象
//     */
//    fun captureListView(listView: ListView): Bitmap? {
//        val adapter = listView.adapter
//        val itemCount = adapter.count
//        var allitemsheight = 0
//        val bitmaps = ArrayList<Bitmap>()
//
//        for (i in 0 until itemCount) {
//            // 获取每一个子View
//            val childView = adapter.getView(i, null, listView)
//            // 测量宽高
//            childView.measure(
//                    View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.EXACTLY),
//                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
//
//            // 布局位置
//            childView.layout(0, 0, childView.measuredWidth, childView.measuredHeight)
//            // 设置背景颜色，避免是黑色的
//            childView.setBackgroundColor(Color.parseColor("#FFFFFF"))
//            childView.isDrawingCacheEnabled = true
//            // 生成缓存
//            childView.buildDrawingCache()
//            // 将每一个View的截图加入集合
//            bitmaps.add(childView.drawingCache)
//            // 叠加截图高度
//            allitemsheight += childView.measuredHeight
//        }
//
//        // 创建和ListView宽高一样的画布
//        val bitmap = createBitmap(listView.measuredWidth, allitemsheight)
//        val canvas = Canvas(bitmap)
//
//        val paint = Paint()
//        var iHeight = 0f
//
//        for (i in bitmaps.indices) {
//            val bmp: Bitmap = bitmaps[i]
//            // 将每一个生成的bitmap绘制在画布上
//            canvas.drawBitmap(bmp, 0f, iHeight, paint)
//            iHeight += bmp.height
//
//            bmp.recycle()
//        }
//        return bitmap
//    }


//    /**
//     * 截取ScrollerView
//     * 原理是获取scrollView的子View的高度，然后创建一个子View宽高的画布，将ScrollView绘制在画布上
//     * @param scrollView 控件
//     * @return 返回截图后的Bitmap
//     */
//    fun captureScrollView(scrollView: ScrollView): Bitmap? {
//        var h = 0
//        for (i in 0 until scrollView.childCount) {
//            val childView = scrollView.getChildAt(i)
//            // 获取子View的高度
//            h += childView.height
//            // 设置背景颜色，避免布局里未设置背景颜色，截的图背景黑色
//            childView.setBackgroundColor(Color.parseColor("#FFFFFF"))
//        }
//
//        val bitmap = createBitmap(scrollView.width, h)//不知道导哪个包
//        val canvas = Canvas(bitmap)
//        // 将ScrollView绘制在画布上
//        scrollView.draw(canvas)
//        return bitmap
//    }

    }


}