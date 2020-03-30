package com.rice.dialog


import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.rice.riceframe.R
import com.rice.tool.KeyBoardUtils
import com.rice.tool.TextUtils
import kotlinx.android.synthetic.main.dialog_input.*
import kotlinx.android.synthetic.main.dialog_input.view.*

/**
 * 输入框Dialog
 */
class InputDialog(var mContext: Context, var hint: String) :
        Dialog(mContext, R.style.centerDialog) {

    companion object {
        private var tab = 0
    }

    var onOkClickListener: OnOkClickListener? = null

    var numberPlate: String = ""

    interface OnOkClickListener {
        fun onOkClick(str: String)
    }

    var view: View

    init {
        val window = this.window
        window!!.setBackgroundDrawable(ColorDrawable(0))
        window.setGravity(Gravity.CENTER)
        val lp = window.attributes
        //设置宽
        lp.width = context.resources.getDimensionPixelOffset(R.dimen.dp_320)
        //设置高
        lp.height = FrameLayout.LayoutParams.WRAP_CONTENT
        window.attributes = lp
        window.setBackgroundDrawable(ColorDrawable(0))
        view = View.inflate(context, R.layout.dialog_input, null)
        setContentView(view)
        initView()
        setCanceledOnTouchOutside(true)
    }

    fun setTitle(title: String) {
        text_tag.text = title
    }

    fun setContent(content: String) {
        if (TextUtils.isNotEmpty(content)) {
            view.et_custom_number_plate.setText(content)
        }
    }

    fun setInputHint(hint: String) {
        view.et_custom_number_plate.hint = hint
    }

    private fun initView() {
        view.textbtn_cancel.setOnClickListener { dismiss() }
        view.et_custom_number_plate.hint = hint
        view.textbtn_ok.setOnClickListener {
            numberPlate = et_custom_number_plate.text.toString()
            KeyBoardUtils.hindKeyboard(view)
            onOkClickListener!!.onOkClick(numberPlate)
            dismiss()
        }
    }

}
