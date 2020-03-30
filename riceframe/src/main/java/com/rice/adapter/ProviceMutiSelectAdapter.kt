package com.rice.adapter

import android.annotation.SuppressLint
import android.content.Context

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.rice.model.CityModel
import com.rice.riceframe.R

/**
 * 省多选适配器
 */
class ProviceMutiSelectAdapter(var context: Context, data: MutableList<CityModel.Data>) :
        BaseQuickAdapter<CityModel.Data, BaseViewHolder>(R.layout.item_provice_mutiselect, data) {

    @SuppressLint("SimpleDateFormat")
    override fun convert(helper: BaseViewHolder, bean: CityModel.Data) {
        helper.setText(R.id.textName, bean.cityname)
        helper.setText(R.id.textNumber, bean.selectNum.toString())
        helper.setVisible(R.id.frameNumber, bean.selectNum > 0)
        helper.setTextColor(R.id.textName, if (bean.isChecked)
            context.resources.getColor(R.color.colorPrimary) else
            context.resources.getColor(R.color.black3))
    }

//    /**
//     * 刷新当前项数量
//     */
//    fun refreshNum() {
//        var item = getCheckedItem()
//        if (getCheckedItem() != null) {
//            item.
//        }
//    }

    /**
     * 获取被选中项下标
     */
    fun getCheckedIndex(): Int {
        var index = -1
        for (item in data) {
            if (item.isChecked) {
                index = data.indexOf(item)
            }
        }
        return index
    }

    /**
     * 获取被选中项对象
     */
    fun getCheckedItem(): CityModel.Data? {
        for (item in data) {
            if (item.isChecked) {
                return item
            }
        }
        return null
    }

}
