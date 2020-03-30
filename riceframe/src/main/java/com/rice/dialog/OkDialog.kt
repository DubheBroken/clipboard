package com.rice.dialog


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager

import com.rice.riceframe.R
import kotlinx.android.synthetic.main.ok_dialog.*

/**
 * 只有确定的Dialog
 */
class OkDialog(var activity: Activity) : Dialog(activity, R.style.centerDialog) {
    companion object {
        private var tab = 0
    }

    var onOkClickListener: OnOkClickListener? = null

    private var colorText: String = ""

    interface OnOkClickListener {
        fun onOkClick()
    }

    var view: View

    init {
        view = View.inflate(context, R.layout.ok_dialog, null)
        setContentView(view)
        val window = this.window
        window!!.setBackgroundDrawable(ColorDrawable(0))
        window.setGravity(Gravity.CENTER)
        val lp = window.attributes
        //设置宽
        lp.width = context.resources.getDimensionPixelOffset(R.dimen.dp_320)
        //设置高
        lp.height = context.resources.getDimensionPixelOffset(R.dimen.dp_200)
        window.attributes = lp
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initView()
        setCanceledOnTouchOutside(false)
    }

    public fun setTitle(title: String) {
        text_tag.text = title
    }

    public fun setInfo(info: String) {
        text_info.text = info
    }

    public fun setOkText(text: String) {
        textbtn_ok.text = text
    }

    private fun initView() {
        textbtn_ok.setOnClickListener {
            if (onOkClickListener != null) {
                onOkClickListener!!.onOkClick()
            }
            dismiss()
        }
    }


}
