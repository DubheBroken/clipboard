package com.rice.model

import com.contrarywind.interfaces.IPickerViewData
import java.io.Serializable

data class AreaModel(
    var name: String = "",
    var cityList: List<City> = listOf(),
    var id: Int = 0
) : Serializable, IPickerViewData {
    override fun getPickerViewText(): String {
        return name
    }

    data class City(
        var name: String = "",
        var districtList: List<District> = listOf(),
        var id: Int = 0
    ) : Serializable, IPickerViewData {
        override fun getPickerViewText(): String {
            return name
        }

        data class District(
            var name: String = "",
            var id: Int = 0
        ) : Serializable, IPickerViewData {
            override fun getPickerViewText(): String {
                return name
            }
        }
    }
}