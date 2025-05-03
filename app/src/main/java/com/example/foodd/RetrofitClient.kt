package com.example.foodd

import com.example.foodd.Api.FooddApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2/foodd/"
    val api: FooddApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FooddApi::class.java)
    }
}