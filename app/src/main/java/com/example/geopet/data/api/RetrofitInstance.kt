package com.example.geopet.data.api

import com.example.geopet.data.model.ApiConstants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.geopet.data.model.ApiConstants.BASE_URL

object RetrofitInstance {
    val api: PetApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PetApiService::class.java)
    }
}
