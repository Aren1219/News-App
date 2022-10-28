package com.example.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.data.model.Data
import com.example.domain.test_util.FakeRepository
import com.example.domain.test_util.TestUtil.previewNewsData
import com.example.domain.test_util.TestUtil.previewNewsDataList
import com.example.domain.util.Resource
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class NewsUseCasesTest {
    @get:Rule
    val instantTaskExecutionRule: TestRule = InstantTaskExecutorRule()

    private lateinit var newsUseCases: NewsUseCases
    private lateinit var fakeRepository: FakeRepository

    @Before
    fun setup() {
        fakeRepository = FakeRepository()
        newsUseCases = NewsUseCases(fakeRepository)
    }

    @Test
    fun `api data use case`() = runBlocking {
        assertEquals(
            previewNewsDataList(),
            newsUseCases.getApiNewsUseCase(1, null, listOf()).data
        )
    }

    @Test
    fun `error response`() = runBlocking {
        fakeRepository.errorResponse = true
        assertTrue(newsUseCases.getApiNewsUseCase(1, null, listOf()) is Resource.Error<*>)
    }

    @Test
    fun `save database data use case`() = runBlocking {
        newsUseCases.insertNewsUseCase(previewNewsData())
        assertEquals(previewNewsData(), newsUseCases.getDatabaseNewsUseCase().value?.get(0)!!)
    }

    @Test
    fun `delete database data use case`() = runBlocking {
        newsUseCases.insertNewsUseCase(previewNewsData())
        newsUseCases.deleteNewsUseCase(previewNewsData())
        assertEquals(listOf<Data>(), newsUseCases.getDatabaseNewsUseCase().value)
    }
}