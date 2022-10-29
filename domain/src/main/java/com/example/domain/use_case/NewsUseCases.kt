package com.example.domain.use_case

import com.example.data.model.Data
import com.example.data.repo.Repository
import com.example.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsUseCases(
    private val repository: Repository
) {

    fun getApiNewsUseCase(
        page: Int,
        publishedBefore: String? = null,
        loadedList: List<Data>
    ) : Flow<Resource<List<Data>>> = flow {
        emit(Resource.Loading(loadedList))
        try {
            val response = repository.getNews(page, publishedBefore)
            if (response.isSuccessful) {
                emit(Resource.Success(loadedList.plus(response.body()!!.data)))
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: Exception) {
            emit(Resource.Error("could not load online news"))
        }
    }

    fun getDatabaseNewsUseCase() = repository.getSavedNews()

    suspend fun insertNewsUseCase(news: Data) {repository.saveNews(news)}

    suspend fun deleteNewsUseCase(news: Data) {repository.deleteNews(news)}

}