package com.dubhe.clipboardDemo.activity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.LinearLayoutManager
import com.dubhe.clipboardDemo.Constant
import com.dubhe.clipboardDemo.R
import com.dubhe.clipboardDemo.adapter.IMMessageAdapter
import com.dubhe.clipboardDemo.model.IMMessageModel
import com.rice.base.RiceBaseActivity
import com.rice.dialog.OkCancelDialog
import kotlinx.android.synthetic.main.activitiy_im.*
import kotlin.math.asin


/**
 * 聊天界面
 */
@SuppressLint("Registered")
class ImActivity : RiceBaseActivity() {

    //聊天消息
    var messageList: MutableList<IMMessageModel> = ArrayList()
    lateinit var iMMessageAdapter: IMMessageAdapter

    lateinit var pasteAsImgDialog: OkCancelDialog

    var mode = MODE_SINGLE

    companion object {
        const val MODE_SINGLE = -1//私聊
    }

    override fun getLayoutId(): Int {
        return R.layout.activitiy_im
    }

    override fun initView() {
        pasteAsImgDialog = OkCancelDialog(mContext)
        pasteAsImgDialog.setTitle("温馨提示")
        pasteAsImgDialog.setInfo("检测到图片链接，是否粘贴为图片？")
        pasteAsImgDialog.setOkText("粘贴为图片")
        pasteAsImgDialog.setCancleText("粘贴为文本")
        recyclerIM.layoutManager = LinearLayoutManager(mContext)
        iMMessageAdapter = IMMessageAdapter(mContext, messageList)
        recyclerIM.adapter = iMMessageAdapter
        textPaste.setOnClickListener {
            paste()
        }
        initData()
    }

    /**
     * 粘贴
     */
    @SuppressLint("DefaultLocale")
    private fun paste() {
        val clipboardmanager: ClipboardManager? = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clipData: ClipData? = clipboardmanager?.primaryClip
        val item = clipData?.getItemAt(0)
        //获取uri
        val str: String? = item?.text.toString()
        val uri: Uri? = item?.uri
        val intent: Intent? = item?.intent
        if (str != null && str != "null") {
            //文本内容
            if (str.startsWith("http")) {
                if (str.toUpperCase().endsWith("GIF") ||
                        str.toUpperCase().endsWith("PNG") ||
                        str.toUpperCase().endsWith("JPG") ||
                        str.toUpperCase().endsWith("JPEG") ||
                        str.toUpperCase().endsWith("WEBP")) {
                    //网络图片
                    pasteAsImgDialog.onOkClickListener = object : OkCancelDialog.OnOkClickListener {
                        override fun onOkClick() {
                            //粘贴为图片
                            pasteStr(str, true)
                        }
                    }
                    pasteAsImgDialog.onCancelClickListener = object : OkCancelDialog.OnCancelClickListener {
                        override fun onCancelClick() {
                            //粘贴为文本
                            pasteStr(str, false)
                        }
                    }
                    pasteAsImgDialog.show()
                }
            } else {
                //纯文本内容
                //粘贴为文本
                pasteStr(str, false)
            }
        } else if (uri != null) {
            //URI内容
            pasteAsURI(uri)
        } else if (intent != null) {
            //Intent内容

        }
    }

    /**
     * 粘贴
     * @param asImg 是否作为图片粘贴，否将作为文本粘贴
     */
    private fun pasteStr(str: String, asImg: Boolean = false) {
        if (asImg) {
            messageList.add(IMMessageModel(messageList.size, Constant.DEFAULT_HEADER, "用户${messageList.size}", str, IMMessageModel.MODE_IMG, "01234567890"))
        } else {
            messageList.add(IMMessageModel(messageList.size, Constant.DEFAULT_HEADER, "用户${messageList.size}", str, IMMessageModel.MODE_TEXT, "01234567890"))
        }
        iMMessageAdapter.notifyDataSetChanged()
        recyclerIM.smoothScrollToPosition(messageList.size)
    }

    /**
     * 作为Uri粘贴
     */
    private fun pasteAsURI(uri: Uri?) {
        messageList.add(IMMessageModel(messageList.size, Constant.DEFAULT_HEADER, "用户${messageList.size}", uri.toString(), IMMessageModel.MODE_FILE, "01234567890"))
        iMMessageAdapter.notifyDataSetChanged()
        recyclerIM.smoothScrollToPosition(messageList.size)
    }

    /**
     * 加载点假数据
     */
    private fun initData() {
        for (i in 0..3) {
            messageList.add(IMMessageModel(i, Constant.DEFAULT_HEADER, "用户$i", "内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容"))
        }
        messageList[messageList.lastIndex].phone = "01234567890"
        iMMessageAdapter.notifyDataSetChanged()
    }

    override fun getIntentData() {

    }

    override fun clear() {

    }

}