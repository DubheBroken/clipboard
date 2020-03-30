package com.rice.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import com.rice.riceframe.R
import kotlinx.android.synthetic.main.ok_cancel_dialog.*

/**
 * 确定取消Dialog
 */
class OkCancelDialog(var mContext: Context) : Dialog(mContext, R.style.centerDialog) {

    var onOkClickListener: OnOkClickListener? = null
    var onCancelClickListener: OnCancelClickListener? = null

    interface OnOkClickListener {
        fun onOkClick()
    }

    interface OnCancelClickListener {
        fun onCancelClick()
    }

    var view: View

    init {
        view = View.inflate(context, R.layout.ok_cancel_dialog, null)
        setContentView(view)
        val window = this.window
        window!!.setBackgroundDrawable(ColorDrawable(0))
        window.setGravity(Gravity.CENTER)
        val lp = window.attributes
        //设置宽
        lp.width = context.resources.getDimensionPixelOffset(R.dimen.width_ok_cancel_dialog)
        //设置高
        lp.height = context.resources.getDimensionPixelOffset(R.dimen.height_ok_cancel_dialog)
        window.attributes = lp
        window.setBackgroundDrawable(ColorDrawable(0))
        initView()
        setCanceledOnTouchOutside(true)
        setCancelable(true)
    }

    fun setCancleText(text: String) {
        textbtn_cancel.text = text
    }

    fun setTitle(title: String) {
        text_tag.text = title
    }

    fun setInfo(info: String) {
        text_info.text = info
    }

    fun setOkText(text: String) {
        textbtn_ok.text = text
    }

    private fun initView() {
        textbtn_cancel.setOnClickListener {
            if (onCancelClickListener != null) {
                onCancelClickListener!!.onCancelClick()
            }
            dismiss()
        }
        textbtn_ok.setOnClickListener {
            dismiss()
            if (onOkClickListener != null) {
                onOkClickListener!!.onOkClick()
            }
        }
    }


}
