package com.skyautonet.garbage.api.core

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

internal object GarbageApiClient {

    private const val BASE_URL ="https://api.sky-net.co.kr"

    internal val service : GarbageApiInterface
        get() = LazyHolder.INSTANCE



    private object LazyHolder{
        internal val INSTANCE = create()

        private fun create() : GarbageApiInterface{
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE

            val client = OkHttpClient.Builder()
                .enableTls12OnPreLollipop()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .build()

            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()

            return retrofit.create(GarbageApiInterface::class.java)
        }
    }

}
