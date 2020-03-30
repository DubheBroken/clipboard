package com.rice.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.rice.model.CityModel
import com.rice.riceframe.R

/**
 * 城市多选适配器
 */
class CityMutiSelectAdapter(var context: Context, data: MutableList<CityModel.Data.City>) :
        BaseQuickAdapter<CityModel.Data.City, BaseViewHolder>(R.layout.item_city_mutiselect, data) {

    @SuppressLint("SimpleDateFormat")
    override fun convert(helper: BaseViewHolder, bean: CityModel.Data.City) {
        var textName = helper.getView<TextView>(R.id.textName)
        textName.text = bean.cityname
        if (bean.isChecked) {
            textName.setTextColor(context.resources.getColor(R.color.colorPrimary))
            textName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, context.resources.getDrawable(R.drawable.icon_check), null)
        } else {
            textName.setTextColor(context.resources.getColor(R.color.black3))
            textName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
        }
    }

    /**
     * 获得选中项的ID列表
     */
    fun getCheckedIds(): MutableList<String> {
        var ids: MutableList<String> = ArrayList()
        for (item in data) {
            if (item.isChecked) {
                ids.add(item.id)
            }
        }
        return ids
    }

    /**
     * 获得选中项的名称列表
     */
    fun getCheckedNames(): MutableList<String> {
        var ids: MutableList<String> = ArrayList()
        for (item in data) {
            if (item.isChecked) {
                ids.add(item.cityname)
            }
        }
        return ids
    }

    /**
     * 获得选中项的对象列表
     */
    fun getCheckedItems(): MutableList<CityModel.Data.City> {
        var ids: MutableList<CityModel.Data.City> = ArrayList()
        for (item in data) {
            if (item.isChecked) {
                ids.add(item)
            }
        }
        return ids
    }

}
