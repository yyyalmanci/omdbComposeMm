package com.yyy.domain.usecase

import app.cash.turbine.test
import com.yyy.domain.model.MovieSearchResultItem
import com.yyy.domain.repository.FavoritesRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class GetFavoriteMoviesUseCaseTest {

    @MockK
    private lateinit var mockFavoritesRepository: FavoritesRepository

    private lateinit var getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase

    @BeforeEach
    fun setUp() {
        getFavoriteMoviesUseCase = GetFavoriteMoviesUseCase(mockFavoritesRepository)
    }

    @Test
    fun `invoke - when repository returns movies - should return movies from repository`() =
        runTest {
            val expectedMovies = listOf(
                MovieSearchResultItem(
                    "id1",
                    "Favorite Movie 1",
                    "2020",
                    "movie",
                    "p1",
                    "Favorite Movie 1",
                    2020
                ),
                MovieSearchResultItem(
                    "id2",
                    "Favorite Movie 2",
                    "2021",
                    "series",
                    "p2",
                    "Favorite Movie 2",
                    2021
                )
            )
            every { mockFavoritesRepository.getAllFavoriteMovies() } returns flowOf(expectedMovies)

            val resultFlow = getFavoriteMoviesUseCase()

            resultFlow.test {
                val emittedMovies = awaitItem()
                assertEquals(expectedMovies, emittedMovies)
                awaitComplete()
            }

            verify(exactly = 1) { mockFavoritesRepository.getAllFavoriteMovies() }
        }

    @Test
    fun `invoke - when repository returns empty list - should return empty list`() = runTest {
        val emptyMoviesList = emptyList<MovieSearchResultItem>()
        every { mockFavoritesRepository.getAllFavoriteMovies() } returns flowOf(emptyMoviesList)

        val resultFlow = getFavoriteMoviesUseCase()

        resultFlow.test {
            val emittedMovies = awaitItem()
            assertEquals(emptyMoviesList, emittedMovies)
            awaitComplete()
        }
        verify(exactly = 1) { mockFavoritesRepository.getAllFavoriteMovies() }
    }

    @Test
    fun `invoke - when repository flow emits error - use case flow should also emit error`() =
        runTest {
            val expectedException = RuntimeException("Database error")
            every { mockFavoritesRepository.getAllFavoriteMovies() } returns kotlinx.coroutines.flow.flow {
                throw expectedException
            }

            val resultFlow = getFavoriteMoviesUseCase()


            resultFlow.test {
                val error = awaitError()
                assertEquals(expectedException, error)
                // awaitComplete() there is no here because error
            }
            verify(exactly = 1) { mockFavoritesRepository.getAllFavoriteMovies() }
        }
}