package com.rice.tool

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import java.io.File

/**
 * @author SmartSean
 * @date 17/12/11 16:04
 */
object OpenFileUtils {
    /**
     * 声明各种类型文件的dataType
     */
    private const val DATA_TYPE_APK = "application/vnd.android.package-archive"
    private const val DATA_TYPE_VIDEO = "video/*"
    private const val DATA_TYPE_AUDIO = "audio/*"
    private const val DATA_TYPE_HTML = "text/html"
    private const val DATA_TYPE_IMAGE = "image/*"
    private const val DATA_TYPE_PPT = "application/vnd.ms-powerpoint"
    private const val DATA_TYPE_EXCEL = "application/vnd.ms-excel"
    private const val DATA_TYPE_WORD = "application/msword"
    private const val DATA_TYPE_CHM = "application/x-chm"
    private const val DATA_TYPE_TXT = "text/plain"
    private const val DATA_TYPE_PDF = "application/pdf"

    /**
     * 未指定明确的文件类型，不能使用精确类型的工具打开，需要用户选择
     */
    private const val DATA_TYPE_ALL = "*/*"

    /**
     * 打开文件
     * @param mContext
     * @param file
     */
    @SuppressLint("DefaultLocale")
    fun openFile(mContext: Context, file: File) {
        if (!file.exists()) {
            return
        }
        // 取得文件扩展名
        val end: String = file.name.substring(file.name.lastIndexOf(".") + 1, file.name.length).toLowerCase()
        when (end) {
            "3gp", "mp4" -> openVideoFileIntent(mContext, file)
            "m4a", "mp3", "mid", "xmf", "ogg", "wav" -> openAudioFileIntent(mContext, file)
            "doc", "docx" -> commonOpenFileWithType(mContext, file, DATA_TYPE_WORD)
            "xls", "xlsx" -> commonOpenFileWithType(mContext, file, DATA_TYPE_EXCEL)
            "jpg", "gif", "png", "jpeg", "bmp" -> commonOpenFileWithType(mContext, file, DATA_TYPE_IMAGE)
            "txt" -> commonOpenFileWithType(mContext, file, DATA_TYPE_TXT)
            "htm", "html" -> commonOpenFileWithType(mContext, file, DATA_TYPE_HTML)
            "apk" -> commonOpenFileWithType(mContext, file, DATA_TYPE_APK)
            "ppt" -> commonOpenFileWithType(mContext, file, DATA_TYPE_PPT)
            "pdf" -> commonOpenFileWithType(mContext, file, DATA_TYPE_PDF)
            "chm" -> commonOpenFileWithType(mContext, file, DATA_TYPE_CHM)
            else -> commonOpenFileWithType(mContext, file, DATA_TYPE_ALL)
        }
    }

    /**
     * Android传入type打开文件
     * @param mContext
     * @param file
     * @param type
     */
    fun commonOpenFileWithType(mContext: Context, file: File, type: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        FileProviderUtils.setIntentDataAndType(mContext, intent, type, file, true)
        mContext.startActivity(intent)
    }

    /**
     * Android打开Video文件
     * @param mContext
     * @param file
     */
    fun openVideoFileIntent(mContext: Context, file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        FileProviderUtils.setIntentDataAndType(mContext, intent, DATA_TYPE_VIDEO, file, false)
        mContext.startActivity(intent)
    }

    /**
     * Android打开Audio文件
     * @param mContext
     * @param file
     */
    private fun openAudioFileIntent(mContext: Context, file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        FileProviderUtils.setIntentDataAndType(mContext, intent, DATA_TYPE_AUDIO, file, false)
        mContext.startActivity(intent)
    }
}
