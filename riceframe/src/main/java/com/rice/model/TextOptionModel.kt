package com.rice.model

class TextOptionModel(
    var text: String = ""
) : SelectOption {

    override fun getOptionString(): String {
        return text
    }

}