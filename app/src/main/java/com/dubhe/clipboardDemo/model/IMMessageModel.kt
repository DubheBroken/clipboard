package com.dubhe.clipboardDemo.model

import android.net.Uri
import java.io.Serializable

/**
 * 聊天内容
 */
data class IMMessageModel(
    var id: Int = 0,
    var header: String = "",
    var name: String = "",
    var content: String = "",
    var mode: Int = MODE_TEXT,
    var phone: String = ""
) : Serializable {
    companion object {
        const val MODE_TEXT = 0//文本模式
        const val MODE_IMG = 1//图片模式
        const val MODE_FILE = 2//文件模式
    }
}