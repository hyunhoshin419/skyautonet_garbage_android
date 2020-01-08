package com.skyautonet.garbage.api.core

import okhttp3.Interceptor
import okhttp3.Response

class SessionRequestInterceptor : Interceptor{

    private var token = ""

    fun setToken(key : String?){
        this.token = key ?: ""
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .header("x-access-token", token)
            .build()

        return chain.proceed(request)
    }
}