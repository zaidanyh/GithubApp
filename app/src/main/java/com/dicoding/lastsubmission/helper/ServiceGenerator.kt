package com.dicoding.lastsubmission.helper

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {
    private const val BASE_URL = "https://api.github.com"

    private val builder:Retrofit.Builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())

    private lateinit var retrofit: Retrofit
    private val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val reqClient: OkHttpClient.Builder = OkHttpClient.Builder()

    fun <S> createContent(serviceClass:Class<S>): S {
        if (!reqClient.interceptors().contains(logging)) {
            reqClient.addInterceptor(Authentication())
            reqClient.addInterceptor(logging)
            builder.client(reqClient.build())
            retrofit = builder.build()
        }
        return retrofit.create(serviceClass)
    }
}