package com.dubhe.clipboardDemo.activity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import androidx.recyclerview.widget.LinearLayoutManager
import com.dubhe.clipboardDemo.Constant
import com.dubhe.clipboardDemo.R
import com.dubhe.clipboardDemo.adapter.IMMessageAdapter
import com.dubhe.clipboardDemo.model.IMMessageModel
import com.rice.base.RiceBaseActivity
import kotlinx.android.synthetic.main.activitiy_im.*


/**
 * 聊天界面
 */
@SuppressLint("Registered")
class ImActivity : RiceBaseActivity() {

    //聊天消息
    var messageList: MutableList<IMMessageModel> = ArrayList()
    lateinit var iMMessageAdapter: IMMessageAdapter

    var mode = MODE_SINGLE

    companion object {
        const val MODE_SINGLE = -1//私聊
    }

    override fun getLayoutId(): Int {
        return R.layout.activitiy_im
    }

    override fun initView() {
        recyclerIM.layoutManager = LinearLayoutManager(mContext)
        iMMessageAdapter = IMMessageAdapter(mContext, messageList)
        recyclerIM.adapter = iMMessageAdapter
        textPaste.setOnClickListener {
            paste()
        }
        initData()
    }

    private fun paste() {
        val clipboardmanager: ClipboardManager? = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clipData: ClipData? = clipboardmanager?.primaryClip
        val item = clipData?.getItemAt(0)
        //获取uri
        val pasteUri: Uri? = item?.uri
        messageList.add(
                IMMessageModel(
                        messageList.size,
                        Constant.DEFAULT_HEADER,
                        "用户${messageList.size}",
                        "uri", pasteUri, "01234567890"
                )
        )
        iMMessageAdapter.notifyDataSetChanged()
    }

    /**
     * 加载点假数据
     */
    private fun initData() {
        for (i in 0..3) {
            messageList.add(
                    IMMessageModel(
                            i,
                            Constant.DEFAULT_HEADER,
                            "用户$i",
                            "内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容"
                    )
            )
        }
        messageList[messageList.lastIndex].phone = "01234567890"
        iMMessageAdapter.notifyDataSetChanged()
    }

    override fun getIntentData() {

    }

    override fun clear() {

    }

}