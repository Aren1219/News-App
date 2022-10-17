package com.example.data.repo

import androidx.lifecycle.LiveData
import com.example.data.model.AllNewsList
import com.example.data.model.Data
import retrofit2.Response

interface Repository {

    suspend fun getNews(page: Int, publishedBefore: String?): Response<AllNewsList>

    fun getSavedNews(): LiveData<List<Data>>

    suspend fun saveNews(data: Data)

    suspend fun deleteNews(data: Data)
}