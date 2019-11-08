package com.zero.healthmonitoring.api

import android.util.Log
import com.zero.healthmonitoring.provide.Config
import com.zero.library.network.JsonConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

class SystemApi {

    companion object{

        private const val DEFAULT_TIMEOUT = 10

        private fun provideRetrofit(host: String): Retrofit {
            val client = OkHttpClient.Builder()
                .addInterceptor {chain ->
                    val request = chain.request().newBuilder().build()
                    Log.e("aiya", "*********************************************************************************")
                    Log.e("aiya", "* ${request.url()}")
                    Log.e("aiya", "*********************************************************************************")
                    Log.e("aiya", "\n")
                    chain.proceed(request)
                }
                .connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .build()
            return Retrofit.Builder()
                .baseUrl(host)
                .addConverterFactory(JsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
        }
        @JvmStatic
        fun provideService(): Api {
            return provideRetrofit(Config.HOST).create(Api::class.java!!)
        }

    }

}