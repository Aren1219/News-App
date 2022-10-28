package com.example.domain.test_util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.model.AllNewsList
import com.example.data.model.Data
import com.example.data.model.Meta
import com.example.data.repo.Repository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakeRepository : Repository {

    var errorResponse: Boolean = false

    private val dataList = TestUtil.previewNewsDataList()
    private val allNewsList = AllNewsList(dataList, Meta(dataList.size, 5, 1, 5))

    private val savedNewsList: MutableList<Data> = mutableListOf()
    private val _savedNews: MutableLiveData<List<Data>> = MutableLiveData(savedNewsList)

    override suspend fun getNews(page: Int, publishedBefore: String?): Response<AllNewsList> =
        if (errorResponse) {
            val errorResponse =
                "{\n" +
                        "  \"type\": \"error\",\n" +
                        "}"
            val errorResponseBody =
                errorResponse.toResponseBody("application/json".toMediaTypeOrNull())
            Response.error(400, errorResponseBody)
        } else Response.success(allNewsList)

    override fun getSavedNews(): LiveData<List<Data>> = _savedNews

    override suspend fun saveNews(data: Data) {
        savedNewsList.add(data)
    }

    override suspend fun deleteNews(data: Data) {
        savedNewsList.remove(data)
    }

}