package com.yyy.domain.usecase

import app.cash.turbine.test
import com.yyy.domain.repository.MoviesRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class GetSearchHistoryUseCaseTest {

    @MockK
    private lateinit var mockMoviesRepository: MoviesRepository

    private lateinit var getSearchHistoryUseCase: GetSearchHistoryUseCase

    @BeforeEach
    fun setUp() {
        getSearchHistoryUseCase = GetSearchHistoryUseCase(mockMoviesRepository)
    }

    @Test
    fun `invoke - when repository returns search history - should return search history from repository`() =
        runTest {
            val expectedHistory = listOf("batman", "superman", "avengers")
            every { mockMoviesRepository.getSearchHistory() } returns flowOf(expectedHistory)

            val resultFlow = getSearchHistoryUseCase()

            resultFlow.test {
                val emittedHistory = awaitItem()
                assertEquals(expectedHistory, emittedHistory)
                awaitComplete()
            }
            verify(exactly = 1) { mockMoviesRepository.getSearchHistory() }
        }

    @Test
    fun `invoke - when repository returns empty history - should return empty list`() = runTest {
        val emptyHistory = emptyList<String>()
        every { mockMoviesRepository.getSearchHistory() } returns flowOf(emptyHistory)

        val resultFlow = getSearchHistoryUseCase()

        resultFlow.test {
            val emittedHistory = awaitItem()
            assertEquals(emptyHistory, emittedHistory)
            assertTrue(emittedHistory.isEmpty())
            awaitComplete()
        }
        verify(exactly = 1) { mockMoviesRepository.getSearchHistory() }
    }

    @Test
    fun `invoke - when repository flow emits error - use case flow should also emit error`() =
        runTest {
            val expectedException = RuntimeException("Database error fetching history")
            every { mockMoviesRepository.getSearchHistory() } returns flow {
                throw expectedException
            }

            val resultFlow = getSearchHistoryUseCase()

            resultFlow.test {
                val error = awaitError()
                assertEquals(expectedException, error)
            }
            verify(exactly = 1) { mockMoviesRepository.getSearchHistory() }
        }
}