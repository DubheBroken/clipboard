package com.dubhe.clipboardDemo.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.getSystemService
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.dubhe.clipboardDemo.R
import com.orhanobut.logger.Logger
import com.rice.dialog.OkCancelDialog
import com.rice.tool.FileMamagerHelper
import com.rice.tool.TextUtils
import com.rice.tool.ToastUtil
import java.io.File


/**
 * Created by wry on 2018-05-04 17:10
 */
class FileAdapter(data: MutableList<File?>, var dirPath: String) :
    BaseQuickAdapter<File, BaseViewHolder>(R.layout.item_file, data) {

    private var mOnEditClick: OnEditClick? = null
    private var mOnDeleteClick: OnDeleteClick? = null
    private var mOnDefaultClick: OnDefaultClick? = null
    private var onDataUpdateListener: OnDataUpdateListener? = null
    lateinit var okCancelDialog: OkCancelDialog

//    val CONTACTS = "content://com.example.contacts"
//    val COPY_PATH = "/copy"// Declares the Uri to paste to the clipboard

    override fun convert(helper: BaseViewHolder, item: File?) {
        if (item == null) {
            helper.setText(R.id.text, "...")
            helper.setImageDrawable(R.id.img, mContext.resources.getDrawable(R.drawable.vec_dir))
            var linearRoot = helper.getView<LinearLayout>(R.id.linearRoot)
            linearRoot.setOnClickListener { backDir() }
//            if (isSdRoot()) {
//                linearRoot.visibility = View.GONE
//            } else {
//                linearRoot.visibility = View.VISIBLE
//            }
        } else {
            helper.setText(R.id.text, item.name)
            helper.setImageDrawable(R.id.img, mContext.resources.getDrawable(getIcon(item)))
            var linearRoot = helper.getView<LinearLayout>(R.id.linearRoot)
            linearRoot.setOnClickListener {
                if (item.isDirectory) {
                    enterDir(item)
                } else {
                    copyFile(item)
                }
            }
//        var img = helper.getView<ImageView>(R.id.img)
//        if (GlideLoadUtils.isValidContextForGlide(mContext)) {
//            GlideLoadUtils.getInstance().glideLoad(mContext, , img)
//        }
        }
    }

    /**
     * 复制文件
     */
    private fun copyFile(file: File) {
        val clipboardmanager: ClipboardManager? = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val copyUri = Uri.parse("$file")
        val clip = ClipData.newUri(mContext.contentResolver, "URI", copyUri)
        clipboardmanager?.setPrimaryClip(clip)
        ToastUtil.showShort("文件${file.name}已复制到剪贴板")
    }

//    /**
//     * 打开文件
//     */
//    @SuppressLint("SimpleDateFormat")
//    private fun openFile(file: File) {
//        okCancelDialog = OkCancelDialog(mContext)
//        okCancelDialog.isEnableCancal = false
//        okCancelDialog.setInfo("暂不支持播放该类型文件")
//        okCancelDialog.setTitle("提示")
//        var nameBig = file.name.toUpperCase()
//        var nameTemp = nameBig.split(".")
//        when (nameTemp[nameTemp.lastIndex]) {
//            "MP4" -> okCancelDialog.show()
//            "PNG" -> okCancelDialog.show()
//            "JPG" -> okCancelDialog.show()
//            "GIF" -> {
//                var model = PlayListModel()
//                model.length = "15"
//                model.id = "1"
//                model.url = file.path
//                model.number = 2
//                model.time = TimeUtils.timestamp2Time(
//                    file.lastModified().toString(),
//                    SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
//                )
//                var playDialog = PlayDialog(mContext, model)
//                playDialog.onDeleteListener = object : PlayDialog.OnDeleteListener {
//                    override fun onDelete() {
//                        file.delete()
//                        data.remove(file)
//                        notifyDataSetChanged()
//                    }
//                }
//                playDialog.show()
//            }
//            else -> okCancelDialog.show()
//        }
//    }

    /**
     * 进入子目录
     */
    fun enterDir(item: File) {
        val files = FileMamagerHelper.getFiles(item)
        data.clear()
        data.add(null)
        if (files != null && files.size > 0) {
            data.addAll(files)
        }
        updatePath(item.path)
        notifyDataSetChanged()
    }

    /**
     * @return 是否已在最外层
     */
    fun isSdRoot(): Boolean {
//        return dirPath == Environment.getExternalStorageDirectory().toString() + File.separator || dirPath == Environment.getExternalStorageDirectory().toString()
        return dirPath == "/storage/emulated/0" || dirPath == "/" || dirPath == "/storage/emulated/0/"
    }

    /**
     * 返回上级目录，如果已在最外层不会做任何操作
     * @return 是否已在最外层
     */
    fun backDir(): Boolean {
        if (isSdRoot()) {
            //已经在最外层
            return true
        }
        var newPath = dirPath.substring(0, dirPath.lastIndexOf("/"))
        if (TextUtils.isEmpty(newPath)) {
            return true
        }
        updatePath(newPath)
        val files = FileMamagerHelper.getFiles(dirPath)
        data.clear()
        if (!isSdRoot()) {
            data.add(null)
        }
        if (files != null && files.size > 0) {
            data.addAll(files)
        }
        notifyDataSetChanged()
        return false
    }

    /**
     * 更新当前目录，通过回调传给外部
     * @param path 目标目录
     */
    fun updatePath(path: String) {
        dirPath = path
        Logger.d("目录更新:$dirPath")
        if (onDataUpdateListener != null) {
            onDataUpdateListener!!.onDataUpdate(dirPath)
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

    interface OnEditClick {
        fun onEditClick(view: View, position: Int)
    }

    fun setOnEditClick(onEditClick: OnEditClick) {
        this.mOnEditClick = onEditClick
    }


    interface OnDeleteClick {
        fun onDeleteClick(view: View, position: Int)
    }

    fun setOnDeleteClick(onDeleteClick: OnDeleteClick) {
        this.mOnDeleteClick = onDeleteClick
    }


    interface OnDefaultClick {
        fun onDefaultClick(view: View, position: Int)
    }

    fun setOnDefaultClick(onDefaultClick: OnDefaultClick) {
        this.mOnDefaultClick = onDefaultClick
    }

    interface OnDataUpdateListener {
        fun onDataUpdate(path: String)
    }

    fun setOnDataUpdateListener(listener: OnDataUpdateListener) {
        this.onDataUpdateListener = listener
    }

}
