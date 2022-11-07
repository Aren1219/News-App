package com.example.data.repo

import androidx.lifecycle.LiveData
import com.example.data.api.Api
import com.example.data.db.NewsDao
import com.example.domain.Repository
import com.example.domain.model.AllNewsList
import com.example.domain.model.Data
import retrofit2.Response

class RepositoryImp(
    private val api: Api,
    private val newsDao: NewsDao
) : Repository {


    override suspend fun getNews(
        page: Int,
        publishedBefore: String?
    ): Response<AllNewsList> =
        api.getAllNews(page = page, publishedBefore = publishedBefore)

    override fun getSavedNews(): LiveData<List<Data>> = newsDao.getAllNews()

    override suspend fun saveNews(data: Data) {
        newsDao.insertNews(data)
    }

    override suspend fun deleteNews(data: Data) {
        newsDao.deleteNews(data)
    }
}