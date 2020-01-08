package com.skyautonet.garbage.api.core

import com.skyautonet.garbage.api.model.CheckUpdateResponse
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

internal interface GarbageApiInterface {

    @POST("api/checkAppVersion.do")
    @FormUrlEncoded
    fun checkUpdate(@Field("appName") appName : String = "GARBAGE-HYBRID"): Single<CheckUpdateResponse>
}