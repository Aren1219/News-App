package com.example.news.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Data
import com.example.domain.use_case.NewsUseCases
import com.example.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases
) : ViewModel() {

    private val _newsList = MutableStateFlow<Resource<List<Data>>>(Resource.Success(listOf()))
    val newsList: StateFlow<Resource<List<Data>>> = _newsList

    val savedList: LiveData<List<Data>> = newsUseCases.getDatabaseNewsUseCase()

    private var loadedPage = 0
    private var currentDateTime: String? = null

    var webViewURL: String? = null

    init {
        getNewsList()
    }

    fun getNewsList() {
        if (_newsList is Resource.Loading<*>) return
        newsUseCases.getApiNewsUseCase(loadedPage + 1, currentDateTime, _newsList.value.data!!).onEach {
            _newsList.value = it
        }.launchIn(viewModelScope)
        loadedPage++
    }

    fun saveNews(data: Data) = viewModelScope.launch(Dispatchers.IO) {
        newsUseCases.insertNewsUseCase(data)
    }

    fun deleteNews(data: Data) = viewModelScope.launch(Dispatchers.IO) {
        newsUseCases.deleteNewsUseCase(data)
    }
}