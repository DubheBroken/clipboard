package com.rice.model

import com.contrarywind.interfaces.IPickerViewData
import java.io.Serializable

/**
 * 绑定银行卡省市信息
 */
data class BankCityModel(
    var lists: MutableList<Province> = ArrayList()
) : Serializable {
    data class Province(
        var id: String = "",
        var name: String = "",
        var pid: String = "",
        var list: MutableList<City> = ArrayList()
    ) : Serializable, IPickerViewData {
        override fun getPickerViewText(): String {
            return name
        }

        data class City(
            var id: String = "",
            var name: String = ""
        ) : Serializable, IPickerViewData {
            override fun getPickerViewText(): String {
                return name
            }
        }
    }

}
