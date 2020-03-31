package com.rice.tool

import android.content.Context
import android.content.Intent
import android.net.Uri

import android.os.Build
import androidx.core.content.FileProvider
import java.io.File


/**
 * @author SmartSean
 * @date 17/12/11 14:23
 */
object FileProviderUtils {
    fun getUriForFile(mContext: Context, file: File): Uri? {
        var fileUri: Uri? = null
        fileUri = if (Build.VERSION.SDK_INT >= 24) {
            getUriForFile24(mContext, file)
        } else {
            Uri.fromFile(file)
        }
        return fileUri
    }

    fun getUriForFile24(mContext: Context, file: File): Uri {
        return FileProvider.getUriForFile(mContext, mContext.packageName + ".fileProvider", file)
    }

    fun setIntentDataAndType(mContext: Context,
                             intent: Intent,
                             type: String,
                             file: File,
                             writeAble: Boolean) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(getUriForFile(mContext, file), type)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type)
        }
    }
}

