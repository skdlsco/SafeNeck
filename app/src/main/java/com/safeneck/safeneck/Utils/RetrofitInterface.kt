package com.safeneck.safeneck.Utils

import com.safeneck.safeneck.Models.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by eka on 2017. 9. 21..
 */
interface RetrofitInterface {

    @POST("/auth/login")
    @FormUrlEncoded
    fun userLogin(@Field("id") id: String, @Field("password") password: String): Call<User>

    @POST("/auth/register")
    @FormUrlEncoded
    fun userRegister(@Field("id") id: String, @Field("password") password: String, @Field("name") name: String): Call<User>

    @POST("/auth/init")
    @FormUrlEncoded
    fun userInit(@Field("token") token: String, @Field("age") age: String, @Field("sex") sex: String, @Field("work") work: String): Call<ResponseBody>
}