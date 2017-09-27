package com.safeneck.safeneck.Utils

import com.safeneck.safeneck.Models.Alarm
import com.safeneck.safeneck.Models.Setting
import com.safeneck.safeneck.Models.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by eka on 2017. 9. 21..
 */
interface RetrofitInterface {

    //Auth
    @POST("/auth/login")
    @FormUrlEncoded
    fun userLogin(@Field("id") id: String, @Field("password") password: String): Call<User>

    @POST("/auth/register")
    @FormUrlEncoded
    fun userRegister(@Field("id") id: String, @Field("password") password: String, @Field("name") name: String): Call<User>

    @POST("/auth/init")
    @FormUrlEncoded
    fun userInit(@Field("token") token: String, @Field("age") age: String, @Field("sex") sex: String, @Field("work") work: String): Call<ResponseBody>

    //Alarm
    @GET("/alarm/getData")
    fun getData(@Query("token") token: String): Call<Alarm>

    @GET("/alarm/circleGraph")
    fun getCircleGraph(@Query("token") token: String): Call<ResponseBody>

    @GET("/alarm/bafGraph")
    fun getBarGraph(@Query("token") token: String): Call<ResponseBody>

    @GET("/alarm/saveUserNeck")
    fun saveUserNeck(@Query("token") token: String, @Query("middleSlope") middleSlope: String, @Query("leftSlope") leftSlope: String, @Query("rightSlope") rightSlope: String): Call<ResponseBody>

    //Setting
    @POST("/setting/dailyAward")
    @FormUrlEncoded
    fun setDailyAward(@Field("token") token: String, @Field("dailyAward") dailyAward: String): Call<Setting>

    @POST("/setting/weeklyAward")
    @FormUrlEncoded
    fun setWeeklyAward(@Field("token") token: String, @Field("weeklyAward") weeklyAward: String): Call<Setting>

    @POST("/setting/reportTime")
    @FormUrlEncoded
    fun setReportTime(@Field("token") token: String, @Field("time") time: String): Call<Setting>

    @GET("/setting/list")
    fun getSettingList(@Query("token") token: String): Call<ResponseBody>

    //Neck
    @POST("/neck/checkToday")
    @FormUrlEncoded
    fun checkToday(@Field("token") token: String): Call<ResponseBody>

    @GET("/neck/checkDay")
    fun checkDay(@Query("token") token: String): Call<ResponseBody>
}