package com.rice.model

import com.contrarywind.interfaces.IPickerViewData
import java.io.Serializable

data class CityModel(
        var data: MutableList<Data> = ArrayList()
) : Serializable {
    data class Data(//省
            var city: MutableList<City> = ArrayList(),
            var cityname: String = "",
            var id: String = "",
            var pid: String = "",
            var selectNum: Int = 0,//本地变量，被选中的市数量
            var isChecked: Boolean = false//本地变量，该省是否被选中
    ) : Serializable, IPickerViewData {
        override fun getPickerViewText(): String {
            return cityname
        }

        data class City(//市
                var area: MutableList<Area> = ArrayList(),
                var cityname: String = "",
                var id: String = "",
                var pid: String = "",
                var isChecked: Boolean = false//本地变量，该城市是否被选中
        ) : Serializable, IPickerViewData {
            override fun getPickerViewText(): String {
                return cityname
            }

            data class Area(//区
                    var cityname: String = "",
                    var id: String = "",
                    var pid: String = ""
            ) : Serializable, IPickerViewData {
                override fun getPickerViewText(): String {
                    return cityname
                }
            }
        }
    }
}