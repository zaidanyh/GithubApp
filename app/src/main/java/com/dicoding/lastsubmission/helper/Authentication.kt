package com.dicoding.lastsubmission.helper

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class Authentication: Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
            .addHeader("Authorization", "token 1ab30c2ba8091ddd7aca0951af519d0142d1cc63")
            .addHeader("User-Agent", "request")
        val request = builder.build()
        return chain.proceed(request)
    }
}