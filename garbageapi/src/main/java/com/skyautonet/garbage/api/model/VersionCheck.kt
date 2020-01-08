package com.skyautonet.garbage.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

enum class UpdatePolicy(val value : Int){
    NECESSARY(1),
    USER_DECISION(2),
    NEVER(3)
}

data class CheckUpdateResponse(

    @SerializedName("result")
    @Expose(deserialize = true)
    val result : UpdateInfo = UpdateInfo()
)

data class UpdateInfo(

    @SerializedName("version")
    @Expose(deserialize = true)
    val version : String = "",

    @SerializedName("appUpdateType")
    @Expose(deserialize = true)
    val policy : Int = UpdatePolicy.USER_DECISION.value,

    @SerializedName("isUpdateTime")
    @Expose(deserialize = true)
    val isActivated : Boolean = false
)
