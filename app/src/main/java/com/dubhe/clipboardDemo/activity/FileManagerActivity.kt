package com.dubhe.clipboardDemo.activity

import android.annotation.SuppressLint
import androidx.recyclerview.widget.GridLayoutManager
import com.dubhe.clipboardDemo.R
import com.dubhe.clipboardDemo.adapter.FileAdapter
import com.rice.base.RiceBaseActivity
import com.rice.tool.FileMamagerHelper
import kotlinx.android.synthetic.main.activity_file_manager.*
import java.io.File

/**
 * 文件管理器
 */
@SuppressLint("Registered")
class FileManagerActivity : RiceBaseActivity() {

    val sdDir = "/storage/emulated/0"

    var list: MutableList<File?> = ArrayList()
    lateinit var fileAdapter: FileAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_file_manager
    }

    override fun initView() {
        recycler.layoutManager = GridLayoutManager(mContext, 4)
        fileAdapter = FileAdapter(list, sdDir)
        fileAdapter.setOnDataUpdateListener(object : FileAdapter.OnDataUpdateListener {
            override fun onDataUpdate(path: String) {
                textDir.text = path
            }
        })
        fileAdapter.bindToRecyclerView(recycler)
        recycler.adapter = fileAdapter
        initData()
    }

    override fun onBackPressed() {
        if (fileAdapter.backDir()) {
            super.onBackPressed()
        }
    }

    private fun initData() {
        val files = FileMamagerHelper.getFiles(sdDir)
        textDir.text = sdDir
        if (files != null && files.size > 0) {
            list.addAll(files)
        }
        fileAdapter.notifyDataSetChanged()
    }

    override fun getIntentData() {

    }

    override fun clear() {

    }

}