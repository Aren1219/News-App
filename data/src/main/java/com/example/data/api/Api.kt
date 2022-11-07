package com.example.data.api

import com.example.data.api.ApiReferences.ALL_NEWS_END_POINT
import com.example.data.api.ApiReferences.TOKEN
import com.example.domain.model.AllNewsList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET(ALL_NEWS_END_POINT)
    suspend fun getAllNews(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en",
        @Query("published_before") publishedBefore: String?,
        @Query("api_token") token: String = TOKEN
    ): Response<AllNewsList>
}