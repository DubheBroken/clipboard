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
    var uri: Uri? =null,
    var phone: String = ""
) : Serializable