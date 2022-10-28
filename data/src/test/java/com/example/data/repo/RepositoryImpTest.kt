package com.example.data.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.data.api.Api
import com.example.data.db.NewsDao
import com.example.data.model.AllNewsList
import com.example.data.model.Meta
import com.example.data.testutil.TestUtil.previewNewsData
import com.example.data.testutil.TestUtil.previewNewsDataList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

class RepositoryImpTest {

    @get:Rule
    val instantTaskExecutionRule: TestRule = InstantTaskExecutorRule()

    private lateinit var repository: RepositoryImp

    @Mock
    private lateinit var api: Api

    @Mock
    private lateinit var dao: NewsDao

    private val savedNewsLiveData = MutableLiveData(previewNewsDataList())

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = RepositoryImp(api, dao)
    }

    @Test
    fun `repository api get news`() = runBlocking {
        val dummyList = previewNewsDataList()
        val dummy =
            Response.success(AllNewsList(previewNewsDataList(), Meta(dummyList.size, 5, 1, 5)))
        Mockito.`when`(api.getAllNews(publishedBefore = null))
            .thenReturn(dummy)
        val result = repository.getNews(1, null)
        assertEquals(dummy, result)
    }

    @Test
    fun `repository dao get saved news`() {
        Mockito.`when`(dao.getAllNews()).thenReturn(savedNewsLiveData)
        assertEquals(savedNewsLiveData.value, repository.getSavedNews().value)
    }

    @Test
    fun `repository dao insert news`() = runBlocking {
        Mockito.`when`(dao.insertNews(previewNewsData("6"))).then {
            savedNewsLiveData.postValue(previewNewsDataList(1, 6))
        }
        Mockito.`when`(dao.getAllNews()).thenReturn(savedNewsLiveData)
        repository.saveNews(previewNewsData("6"))
        assertEquals(savedNewsLiveData, repository.getSavedNews())
    }

    @Test
    fun `repository dao delete news`() = runBlocking{
        Mockito.`when`(dao.deleteNews(previewNewsData("5"))).then {
            savedNewsLiveData.postValue(previewNewsDataList(1, 4))
        }
        Mockito.`when`(dao.getAllNews()).thenReturn(savedNewsLiveData)
        repository.deleteNews(previewNewsData("5"))
        assertEquals(savedNewsLiveData, repository.getSavedNews())
    }
}