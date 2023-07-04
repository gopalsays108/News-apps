package com.gopal.newsapps.api

import com.gopal.newsapps.BuildConfig
import com.gopal.newsapps.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("/v2/top-headlines")
    suspend fun getBreakingNew(
        @Query("country")
        countryCode: String = "in",
        @Query("page")
        pageNo: Int = 1,
        @Query("apiKey")
        apiKey: String = BuildConfig.API_KEY
    ): Response<NewsResponse>

    @GET("/v2/everything")
    suspend fun search(
        @Query("q")
        searchText: String,
        @Query("page")
        pageNo: Int = 1,
        @Query("apiKey")
        apiKey: String = BuildConfig.API_KEY,
        ): Response<NewsResponse>

}