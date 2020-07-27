package com.dicoding.lastsubmission.helper

import com.dicoding.lastsubmission.model.ListFollow
import com.dicoding.lastsubmission.model.ResponseDataUser
import com.dicoding.lastsubmission.model.ResponseDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RequestInterface {

    @GET("/search/users")
    fun getUsers(@Query("q") q: String): Call<ResponseDataUser>

    @GET("/users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<ResponseDetail>

    @GET("/users/{username}/followers")
    fun getFollowersUser(@Path("username") username: String): Call<ArrayList<ListFollow>>

    @GET("users/{username}/following")
    fun getFollowingUser(@Path("username") username: String): Call<ArrayList<ListFollow>>
}