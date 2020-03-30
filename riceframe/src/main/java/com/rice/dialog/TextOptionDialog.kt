package com.rice.dialog


import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.rice.adapter.TextOptionAdapter
import com.rice.model.SelectOption
import com.rice.riceframe.R
import kotlinx.android.synthetic.main.dialog_text_option.*

/**
 * 文本选择Dialog
 */
class TextOptionDialog<T : SelectOption>(var mContext: Context, var listText: MutableList<T>) :
        Dialog(mContext, R.style.centerDialog) {

    lateinit var textOptionAdapter: TextOptionAdapter<T>

    var onOkClickListener: OnOkClickListener? = null

    interface OnOkClickListener {
        fun onOkClick(str: String)
    }

    var view: View

    init {
        val window = this.window
        window!!.setBackgroundDrawable(ColorDrawable(0))
        window.setGravity(Gravity.BOTTOM)
        val lp = window.attributes
        //设置宽
        window.decorView.setPadding(0, 0, 0, 0)
        window.setWindowAnimations(R.style.BottomDialog_Animation)
        lp.width = FrameLayout.LayoutParams.MATCH_PARENT
        //设置高
        lp.height = FrameLayout.LayoutParams.WRAP_CONTENT
        window.attributes = lp
        window.setBackgroundDrawable(ColorDrawable(0))
        view = View.inflate(context, R.layout.dialog_text_option, null)
        setContentView(view)
        initView()
        setCanceledOnTouchOutside(true)
    }

    private fun initView() {
        recycler.layoutManager = LinearLayoutManager(mContext)
        textOptionAdapter = TextOptionAdapter(mContext, listText)
        textOptionAdapter.setOnItemClickListener { adapter, view, position ->
            dismiss()
            onOkClickListener?.onOkClick(listText[position].getOptionString())
        }
        recycler.adapter = textOptionAdapter
    }

}
