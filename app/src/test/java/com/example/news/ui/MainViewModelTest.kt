package com.example.news.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.data.model.Data
import com.example.news.testutil.FakeRepository
import com.example.news.testutil.TestUtil
import com.example.news.testutil.getOrAwaitValue
import com.example.news.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.MockitoAnnotations

class MainViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantTaskExecutionRule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var fakeRepository: FakeRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeRepository()
        runBlocking {
            viewModel = MainViewModel(fakeRepository)
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
        assertEquals(TestUtil.previewNewsDataList(), viewModel.newsList.value!!.data)
    }

    @Test
    fun `load two pages from api`() {
        viewModel.getNewsList()
        Thread.sleep(1)
        assertEquals(
            TestUtil.previewNewsDataList() + TestUtil.previewNewsDataList(),
            viewModel.newsList.getOrAwaitValue().data
        )
    }

    @Test
    fun `error response`() {
        fakeRepository.errorResponse = true
        viewModel.getNewsList()
        Thread.sleep(1)
        assertTrue(viewModel.newsList.getOrAwaitValue() is Resource.Error)
    }

    @Test
    fun `save news`() {
        viewModel.saveNews(TestUtil.previewNewsData())
        viewModel.savedList.getOrAwaitValue()
        Thread.sleep(1)
        assertEquals(listOf(TestUtil.previewNewsData()), viewModel.savedList.value)
    }

    @Test
    fun `delete news`() {
        viewModel.saveNews(TestUtil.previewNewsData())
        viewModel.savedList.getOrAwaitValue()
        viewModel.deleteNews(TestUtil.previewNewsData())
        Thread.sleep(1)
        assertEquals(listOf<Data>(), viewModel.savedList.getOrAwaitValue())
    }
}