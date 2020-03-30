package com.rice.adapter

import android.widget.ImageView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.rice.model.MineFragmnetModel
import com.rice.riceframe.R

/**
 * Created by wry on 2018-05-04 17:10
 */
class MineFragmentAdapter(data: List<MineFragmnetModel>, itemId: Int = R.layout.item_minefragment) :
    BaseQuickAdapter<MineFragmnetModel, BaseViewHolder>(itemId, data) {

    override fun convert(helper: BaseViewHolder, bean: MineFragmnetModel) {
        helper.setText(R.id.mTitle, bean.title)
        //        ((ImageView) helper.getView(R.id.mImage)).setImageResource(bean.getResId());
        when (bean.location) {
            MineFragmnetModel.FIRST -> {
                helper.setGone(R.id.space, true)
                helper.setVisible(R.id.line, true)
            }
            MineFragmnetModel.NORMAL -> {
                helper.setGone(R.id.space, false)
                helper.setVisible(R.id.line, true)
            }
            MineFragmnetModel.END -> {
                helper.setGone(R.id.space, false)
                helper.setVisible(R.id.line, false)
            }
        }
    }
}
