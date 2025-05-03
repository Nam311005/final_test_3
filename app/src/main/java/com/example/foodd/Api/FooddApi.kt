package com.example.foodd.Api

import retrofit2.Response
import com.example.foodd.DataClass.LoginData
import com.example.foodd.DataClass.UserRoot
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET;
import retrofit2.http.POST
import retrofit2.http.Query

interface FooddApi {
    @GET("display.php")
    suspend fun getDataByType(@Query("type") type: String): ResponseBody
    @POST("login.php")
    suspend fun loginUser(@Body loginData: LoginData): Response<UserRoot>
    }
