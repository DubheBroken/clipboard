package com.dubhe.clipboardDemo.activity

import android.annotation.SuppressLint
import androidx.recyclerview.widget.GridLayoutManager
import com.dubhe.clipboardDemo.R
import com.rice.base.RiceBaseActivity
import kotlinx.android.synthetic.main.activity_file_manager.*
import java.io.File

/**
 * 文件管理器
 */
@SuppressLint("Registered")
class FileManagerActivity : RiceBaseActivity() {

    val sdDir = "/storage"

    var list: MutableList<File?> = ArrayList()
    lateinit var fileAdapter: FileAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_file_manager
    }

    override fun initView() {
        recycler.layoutManager = GridLayoutManager(mContext, 4)

    }

    override fun getIntentData() {

    }

    override fun clear() {

    }

}