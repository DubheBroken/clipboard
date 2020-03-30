package com.dubhe.clipboardDemo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.dubhe.clipboardDemo.MyApplication
import com.dubhe.clipboardDemo.R
import com.dubhe.clipboardDemo.model.IMMessageModel
import com.rice.imageloader.GlideLoadUtils
import com.rice.tool.DirUtils
import com.rice.tool.FileUtil
import com.rice.tool.ScreenUtils
import com.rice.tool.TextUtils
import me.jessyan.autosize.utils.AutoSizeUtils
import java.io.File

/**
 * 聊天消息
 */
class IMMessageAdapter(var context: Context, data: MutableList<IMMessageModel>) :
        BaseQuickAdapter<IMMessageModel, BaseViewHolder>(R.layout.item_message_text, data) {

    @SuppressLint("SimpleDateFormat")
    override fun convert(helper: BaseViewHolder, bean: IMMessageModel) {
//        var img = helper.getView<ImageView>(R.id.img)
        helper.setText(R.id.text_content_left, bean.content)
        helper.setText(R.id.textNick, bean.name)
        var headerLeft = helper.getView<ImageView>(R.id.image_header_left)
        var headerRight = helper.getView<ImageView>(R.id.image_header_right)
        GlideLoadUtils.getInstance().glideLoad(context, bean.header, headerLeft)
        GlideLoadUtils.getInstance().glideLoad(context, bean.header, headerRight)
        var constraint = helper.getView<ConstraintLayout>(R.id.constraint)
        val text_content_left = helper.getView<TextView>(R.id.text_content_left)
        val imgSize = mContext.resources.getDimensionPixelOffset(R.dimen.im_img_size) * 2
        val dp16 = mContext.resources.getDimensionPixelOffset(R.dimen.dp_16)
        text_content_left.maxWidth = ScreenUtils.getRealScreenWidth(MyApplication.instant) - imgSize - dp16
        val set = ConstraintSet()
        set.clone(constraint)
        if (bean.phone == "01234567890") {
            //我发出的
            set.setHorizontalBias(R.id.text_content_left, 1f)
            set.setHorizontalBias(R.id.img, 1f)
            set.setHorizontalBias(R.id.textNick, 1f)
            set.applyTo(constraint)
            headerRight.visibility = View.VISIBLE
            headerLeft.visibility = View.INVISIBLE
            helper.setBackgroundRes(R.id.text_content_left, R.drawable.bg_message_right)
        } else {
            //我收到的
            set.setHorizontalBias(R.id.text_content_left, 0f)
            set.setHorizontalBias(R.id.img, 0f)
            set.setHorizontalBias(R.id.textNick, 0f)
            set.applyTo(constraint)
            headerRight.visibility = View.INVISIBLE
            headerLeft.visibility = View.VISIBLE
            helper.setBackgroundRes(R.id.text_content_left, R.drawable.bg_message_left)
        }
        if (bean.content == "uri") {
            text_content_left.text = TextUtils.getFileName(bean.uri.toString())
            text_content_left.setCompoundDrawables(null,null,context.resources.getDrawable(getIcon(File(bean.uri.toString()))),null)
        }
    }

    /**
     * 获取文件对应的图标
     * @param file 要匹配图标的文件
     * @return 图标对应的ID
     */
    fun getIcon(file: File): Int {
        if (file.isDirectory) {
            return R.drawable.vec_dir
        }
        var nameBig = file.name.toUpperCase()
        var nameTemp = nameBig.split(".")
        when (nameTemp[nameTemp.lastIndex]) {
            "MP4" -> return R.drawable.vec_mp4
            "PNG" -> return R.drawable.vec_png
            "JPG" -> return R.drawable.vec_jpg
            "GIF" -> return R.drawable.vec_gif
            else -> return R.drawable.vec_file
        }
    }

}
