package com.neu.mobileapplicationdevelopment202430.model

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MyRetrofitBuilder {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://team1groceryapp.pythonanywhere.com/"

    fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .build()
        }
        return retrofit!!
    }

    fun getApiService(): FoodApiService {
        return getRetrofit().create(FoodApiService::class.java)
    }
}
