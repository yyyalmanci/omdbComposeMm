package com.yyy.domain.usecase

import app.cash.turbine.test
import com.yyy.domain.model.MovieListItem
import com.yyy.domain.model.MovieSearchResultItem
import com.yyy.domain.repository.MoviesRepository
import com.yyy.domain.repository.result.MoviesRepositoryResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class SearchMoviesUseCaseTest {

    @MockK
    private lateinit var mockMoviesRepository: MoviesRepository

    private lateinit var searchMoviesUseCase: SearchMoviesUseCase

    private val testQuery = "batman"
    private val testPage = 1
    private val dummyMovieItem = MovieSearchResultItem(
        "tt123",
        "Batman Test",
        "2023",
        "movie",
        "poster.jpg",
        "Batman Test",
        2023
    )
    private val dummyMovieList = MovieListItem(search = listOf(dummyMovieItem))

    @BeforeEach
    fun setUp() {
        searchMoviesUseCase = SearchMoviesUseCase(mockMoviesRepository)
    }

    @Test
    fun `invoke - when repository returns Success - should emit Loading then Success`() = runTest {
        val successResult = MoviesRepositoryResult.Success(dummyMovieList)
        coEvery { mockMoviesRepository.getMovies(testQuery, testPage) } returns successResult

        val resultFlow = searchMoviesUseCase(testQuery, testPage)

        resultFlow.test {
            assertEquals(MoviesRepositoryResult.Loading, awaitItem(), "Should emit Loading first")
            assertEquals(successResult, awaitItem(), "Should emit Success after Loading")
            awaitComplete()
        }
        coVerify(exactly = 1) { mockMoviesRepository.getMovies(testQuery, testPage) }
    }

    @Test
    fun `invoke - when repository returns Failure - should emit Loading then Failure`() = runTest {
        val errorMessage = "Network error"
        val failureResult = MoviesRepositoryResult.Failed(errorMessage)
        coEvery { mockMoviesRepository.getMovies(testQuery, testPage) } returns failureResult

        val resultFlow = searchMoviesUseCase(testQuery, testPage)

        resultFlow.test {
            assertEquals(MoviesRepositoryResult.Loading, awaitItem(), "Should emit Loading first")
            val actualFailure = awaitItem()
            assertEquals(failureResult, actualFailure, "Should emit Failure after Loading")
            awaitComplete()
        }
        coVerify(exactly = 1) { mockMoviesRepository.getMovies(testQuery, testPage) }
    }

    @Test
    fun `invoke - when repository throws exception - should emit Loading then propagate error in flow`() =
        runTest {
            val expectedException = RuntimeException("API connection failed")
            coEvery { mockMoviesRepository.getMovies(testQuery, testPage) } throws expectedException

            val resultFlow = searchMoviesUseCase(testQuery, testPage)

            resultFlow.test {
                assertEquals(
                    MoviesRepositoryResult.Loading,
                    awaitItem(),
                    "Should emit Loading first"
                )
                val error = awaitError()
                assertEquals(
                    expectedException,
                    error,
                    "Should propagate the exception from repository"
                )
            }
            coVerify(exactly = 1) { mockMoviesRepository.getMovies(testQuery, testPage) }
        }
}