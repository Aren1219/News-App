package com.example.domain.use_case

import com.example.data.model.Data
import com.example.data.repo.Repository
import com.example.domain.util.Resource

class NewsUseCases(
    private val repository: Repository
) {

    suspend fun getApiNewsUseCase(
        page: Int,
        publishedBefore: String? = null,
        loadedList: List<Data>
    ): Resource<List<Data>> = try {
        val response = repository.getNews(page, publishedBefore)
        if (response.isSuccessful) {
            Resource.Success(loadedList.plus(response.body()!!.data))
        } else {
            Resource.Error(response.message())
        }
    } catch (e: Exception) {
        Resource.Error("could not load online news")
    }

    fun getDatabaseNewsUseCase() = repository.getSavedNews()

    suspend fun insertNewsUseCase(news: Data) {repository.saveNews(news)}

    suspend fun deleteNewsUseCase(news: Data) {repository.deleteNews(news)}

}