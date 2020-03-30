package com.rice.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.rice.riceframe.R
import com.rice.tool.TextUtils
import kotlinx.android.synthetic.main.loading_dialog_white.*

/**
 * Loading弹窗
 */
class RLoadingDialog : Dialog {

    private var str = ""

    constructor(context: Context, cancelable: Boolean) : super(context) {
        setCancelable(cancelable)
        setCanceledOnTouchOutside(false)
    }

    constructor(context: Context, cancelable: Boolean, str: String) : super(context) {
        setCancelable(cancelable)
        setCanceledOnTouchOutside(false)
        this.str = str
    }

    /**
     * 加载圈圈颜色
     */
    fun setColor(color: Int) {
        plv_loading.setColor(color)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //        LinearLayout linearLayout = new LinearLayout(getContext());
        //        ProgressBar progressBar = new ProgressBar(getContext());
        //
        //        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(this.getContext().getResources().getDimensionPixelSize(R.dimen.dp_30),
        //                this.getContext().getResources().getDimensionPixelSize(R.dimen.dp_30));
        //        params.setMargins(this.getContext().getResources().getDimensionPixelSize(R.dimen.dp_10),
        //                this.getContext().getResources().getDimensionPixelSize(R.dimen.dp_10),
        //                this.getContext().getResources().getDimensionPixelSize(R.dimen.dp_10),
        //                this.getContext().getResources().getDimensionPixelSize(R.dimen.dp_10));
        //        progressBar.setLayoutParams(params);
        //        linearLayout.addView(progressBar);
        //
        //        setContentView(linearLayout);
        setContentView(R.layout.loading_dialog_white)
        window!!.setBackgroundDrawable(ColorDrawable(0))
        if (TextUtils.isEmpty(str)) {
            text.visibility = View.GONE
        } else {
            text.text = str
            text.visibility = View.VISIBLE
        }
    }

    override fun show() {
        if (!isShowing) {
            super.show()
        }
    }
}
