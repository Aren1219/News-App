package com.example.news.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Data
import com.example.data.repo.Repository
import com.example.news.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _newsList: MutableLiveData<Resource<List<Data>>> = MutableLiveData()
    val newsList: LiveData<Resource<List<Data>>> = _newsList

    val savedList: LiveData<List<Data>> = repository.getSavedNews()

    private var loadedPage = 0

    private var currentDateTime: String? = null

    var webViewURL: String? = null

    init {
        getNewsList()
    }

    fun getNewsList() = viewModelScope.launch(Dispatchers.IO) {
        if (_newsList.value is Resource.Loading<*>) return@launch
        _newsList.postValue(Resource.Loading(_newsList.value?.data))
        try {
            val response = repository.getNews(page = loadedPage + 1, currentDateTime)
            if (response.isSuccessful) {
                _newsList.postValue(
                    Resource.Success(
                        _newsList.value?.data?.plus(response.body()!!.data)
                            ?: response.body()!!.data
                    )
                )
                if (loadedPage == 0) currentDateTime =
                    response.body()!!.data[0].publishedAt.take(19)
                loadedPage++
            } else {
                _newsList.postValue(Resource.Error(response.message()))
            }
        } catch (e: Exception) {
            _newsList.postValue(Resource.Error("ERROR: could not load online news"))
        }
    }

//    fun getNewsUUID(uuid: String): Data? = _newsList.value?.data?.find { it.uuid == uuid }
//        ?: savedList.value?.find { it.uuid == uuid }


    fun saveNews(data: Data) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveNews(data)
    }

    fun deleteNews(data: Data) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNews(data)
    }
}