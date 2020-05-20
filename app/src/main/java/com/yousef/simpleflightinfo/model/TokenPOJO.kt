package com.yousef.simpleflightinfo.model

import com.google.gson.annotations.SerializedName

class TokenPOJO {
    @JvmField
    @SerializedName("access_token")
    var access_token: String? = null
}