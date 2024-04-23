package com.example.jsonparsing.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun instance(): Retrofit {
        if (retrofit == null) {
            synchronized(Retrofit::class.java) {
                retrofit = Retrofit.Builder()
                    .baseUrl("https://randomuser.me/api/")
                    .client(okHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
        }

        return retrofit!!
    }

    private fun okHttpClient(): OkHttpClient {
        val interceptor = Interceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .build()
            )
        }

        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
//            level = HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient().newBuilder().apply {
            addInterceptor(interceptor)
            addInterceptor(httpLoggingInterceptor)
            connectTimeout(1000 * 20, TimeUnit.SECONDS)
            writeTimeout(1000 * 20, TimeUnit.SECONDS)
            readTimeout(1000 * 20, TimeUnit.SECONDS)
        }.build()
    }
}