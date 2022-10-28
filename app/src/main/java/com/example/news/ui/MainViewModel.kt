package com.example.news.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Data
import com.example.domain.use_case.NewsUseCases
import com.example.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases
) : ViewModel() {

    private val _newsList: MutableLiveData<Resource<List<Data>>> =
        MutableLiveData(Resource.Success(listOf()))
    val newsList: LiveData<Resource<List<Data>>> = _newsList

    val savedList: LiveData<List<Data>> = newsUseCases.getDatabaseNewsUseCase()

    private var loadedPage = 0
    private var currentDateTime: String? = null

    var webViewURL: String? = null

    init {
        getNewsList()
    }

    fun getNewsList() = viewModelScope.launch(Dispatchers.IO){
        if (_newsList.value is Resource.Loading<*>) return@launch
        viewModelScope.launch(Dispatchers.IO) {
            _newsList.postValue(Resource.Loading(_newsList.value?.data))
            _newsList.postValue(
                _newsList.value?.data?.let {
                    newsUseCases.getApiNewsUseCase(loadedPage + 1, currentDateTime, it)
                }
            )
        }.join()
        if (loadedPage == 0)
            currentDateTime = _newsList.value!!.data!![0].publishedAt.take(19)
        loadedPage++
    }

    fun saveNews(data: Data) = viewModelScope.launch(Dispatchers.IO) {
        newsUseCases.insertNewsUseCase(data)
    }

    fun deleteNews(data: Data) = viewModelScope.launch(Dispatchers.IO) {
        newsUseCases.deleteNewsUseCase(data)
    }
}