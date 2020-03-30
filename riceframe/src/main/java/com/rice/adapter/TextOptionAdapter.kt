package com.rice.adapter

import android.content.Context
import android.widget.ImageView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.rice.model.SelectOption
import com.rice.model.TextOptionModel
import com.rice.riceframe.R

/**
 * 文本选项
 */
class TextOptionAdapter<T:SelectOption>(context: Context, data: MutableList<T>) :
    BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_text_select, data) {

    override fun convert(helper: BaseViewHolder, bean: T) {
        helper.setText(R.id.text, bean.getOptionString())
        helper.setVisible(R.id.line, data.indexOf(bean) != data.lastIndex)
    }

}
