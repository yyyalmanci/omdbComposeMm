package com.yyy.domain.usecase

import app.cash.turbine.test
import com.yyy.domain.model.MovieDetail
import com.yyy.domain.repository.MoviesRepository
import com.yyy.domain.repository.result.MovieDetailRepositoryResult
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
class GetMovieDetailUseCaseTest {


    @MockK
    private lateinit var mockMoviesRepository: MoviesRepository

    private lateinit var getMovieDetailUseCase: GetMovieDetailUseCase

    private val testImdbId = "tt0123456"
    private val dummyMovieDetail = MovieDetail(
        title = "",
        year = "",
        rated = "",
        released = "",
        runtime = "",
        genre = "",
        director = "",
        writer = "",
        actors = "",
        plot = "",
        language = "",
        country = "",
        awards = "",
        poster = "",
        ratings = emptyList(),
        metascore = "",
        imdbRating = "",
        imdbVotes = "",
        imdbID = "tt0123456",
        type = "",
        dvd = "",
        boxOffice = "",
        production = "",
        website = "",
        response = ""
    )

    @BeforeEach
    fun setUp() {
        getMovieDetailUseCase = GetMovieDetailUseCase(mockMoviesRepository)
    }

    @Test
    fun `invoke - when repository returns Success - should emit Loading then Success`() = runTest {
        val successResult = MovieDetailRepositoryResult.Success(dummyMovieDetail)
        coEvery { mockMoviesRepository.getMovieDetail(testImdbId) } returns successResult

        val resultFlow = getMovieDetailUseCase(testImdbId)

        resultFlow.test {
            assertEquals(MovieDetailRepositoryResult.Loading, awaitItem())
            assertEquals(successResult, awaitItem())
            awaitComplete()
        }

        coVerify(exactly = 1) { mockMoviesRepository.getMovieDetail(testImdbId) }
    }

    @Test
    fun `invoke - when repository returns Failure - should emit Loading then Failure`() = runTest {
        val errorMessage = "Network error"
        val failureResult = MovieDetailRepositoryResult.Failed(errorMessage)
        coEvery { mockMoviesRepository.getMovieDetail(testImdbId) } returns failureResult

        val resultFlow = getMovieDetailUseCase(testImdbId)

        resultFlow.test {
            assertEquals(MovieDetailRepositoryResult.Loading, awaitItem()) // Loading state
            val actualFailure = awaitItem()
            assertEquals(failureResult, actualFailure)
            awaitComplete()
        }
        coVerify(exactly = 1) { mockMoviesRepository.getMovieDetail(testImdbId) }
    }

    @Test
    fun `invoke - when repository throws exception - should emit Loading then propagate error in flow`() =
        runTest {
            val expectedException = RuntimeException("Database connection failed")
            coEvery { mockMoviesRepository.getMovieDetail(testImdbId) } throws expectedException

            val resultFlow = getMovieDetailUseCase(testImdbId)

            resultFlow.test {
                assertEquals(MovieDetailRepositoryResult.Loading, awaitItem()) // Loading state
                val error = awaitError()
                assertEquals(expectedException, error)
            }
            coVerify(exactly = 1) { mockMoviesRepository.getMovieDetail(testImdbId) }
        }
}