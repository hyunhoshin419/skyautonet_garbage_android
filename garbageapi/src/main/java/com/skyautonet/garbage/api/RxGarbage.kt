@file:JvmName("RxGarbage")

package com.skyautonet.garbage.api

import com.skyautonet.garbage.api.core.GarbageApiClient
import com.skyautonet.garbage.api.model.CheckUpdateResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun checkUpdate() : Single<CheckUpdateResponse>{

    return GarbageApiClient.service.checkUpdate()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}