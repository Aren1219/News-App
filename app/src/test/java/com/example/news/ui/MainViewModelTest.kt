package com.example.news.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.model.Data
import com.example.domain.use_case.NewsUseCases
import com.example.domain.util.Resource
import com.example.news.testutil.TestUtil.previewNewsData
import com.example.news.testutil.TestUtil.previewNewsDataList
import com.example.news.testutil.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

//TODO: need to update ViewModel tests for the use case change
class MainViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantTaskExecutionRule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var useCases: NewsUseCases
    private lateinit var viewModel: MainViewModel

    private val savedNewsLiveData: LiveData<List<Data>> = MutableLiveData()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        runBlocking {
            viewModel = MainViewModel(useCases)
            Mockito.`when`(useCases.getApiNewsUseCase(1, null, listOf()))
                .thenReturn(Resource.Success(previewNewsDataList()))
            Mockito.`when`(useCases.getDatabaseNewsUseCase())
                .thenReturn(savedNewsLiveData)
            viewModel.newsList.getOrAwaitValue()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial news list`() {
        assertEquals(previewNewsDataList(), viewModel.newsList.value!!.data)
    }
//
    @Test
    fun `load two pages from api`() {
        viewModel.getNewsList()
        Thread.sleep(1)
        assertEquals(
            previewNewsDataList() + previewNewsDataList(),
            viewModel.newsList.getOrAwaitValue().data
        )
    }

    @Test
    fun `save news`() {
        viewModel.saveNews(previewNewsData())
        viewModel.savedList.getOrAwaitValue()
        Thread.sleep(1)
        assertEquals(listOf(previewNewsData()), viewModel.savedList.value)
    }

    @Test
    fun `delete news`() {
        viewModel.saveNews(previewNewsData())
        viewModel.savedList.getOrAwaitValue()
        viewModel.deleteNews(previewNewsData())
        Thread.sleep(1)
        assertEquals(listOf<Data>(), viewModel.savedList.getOrAwaitValue())
    }
}