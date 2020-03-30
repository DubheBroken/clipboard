package com.rice.model

import java.io.Serializable

data class AreaListModel(
    var lists: MutableList<AreaModel> = ArrayList()
) : Serializable