package com.yyy.ui.viewmodel

import app.cash.turbine.test
import com.yyy.domain.model.MovieSearchResultItem
import com.yyy.domain.usecase.GetFavoriteMoviesUseCase
import com.yyy.ui.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class FavoritesViewModelTest {

    @JvmField
    @RegisterExtension
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    lateinit var getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase

    private lateinit var viewModel: FavoritesViewModel

    @BeforeEach
    fun setUp() {
        coEvery { getFavoriteMoviesUseCase() } returns flowOf(emptyList())
        viewModel = FavoritesViewModel(getFavoriteMoviesUseCase)
    }

    @Test
    fun `init - when GetFavoriteMoviesUseCase returns a list of movies - uiState is updated with movies`() =
        runTest {
            val favoriteMovies = listOf(
                MovieSearchResultItem(
                    "id1",
                    "Favorite Movie 1",
                    "2020",
                    "movie",
                    "poster1",
                    "Favorite Movie 1",
                    2020
                ),
                MovieSearchResultItem(
                    "id2",
                    "Favorite Movie 2",
                    "2021",
                    "series",
                    "poster2",
                    "Favorite Movie 2",
                    2021
                )
            )
            every { getFavoriteMoviesUseCase() } returns flowOf(favoriteMovies)

            viewModel = FavoritesViewModel(getFavoriteMoviesUseCase)
            advanceUntilIdle()
            viewModel.uiState.test {
                val emittedState = awaitItem()
                assertEquals(favoriteMovies, emittedState.movies)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `init - when GetFavoriteMoviesUseCase flow emits multiple lists - uiState reflects the latest list`() =
        runTest {
            val initialFavorites = listOf(
                MovieSearchResultItem(
                    "id1",
                    "Old Fav Movie",
                    "2019",
                    "movie",
                    "p_old",
                    "Old Fav Movie",
                    2019
                )
            )
            val updatedFavorites = listOf(
                MovieSearchResultItem(
                    "id1",
                    "Old Fav Movie",
                    "2019",
                    "movie",
                    "p_old",
                    "Old Fav Movie",
                    2019
                ),
                MovieSearchResultItem(
                    "id2",
                    "New Fav Movie",
                    "2023",
                    "movie",
                    "p_new",
                    "New Fav Movie",
                    2023
                )
            )

            every { getFavoriteMoviesUseCase() } returns flowOf(initialFavorites, updatedFavorites)

            viewModel = FavoritesViewModel(getFavoriteMoviesUseCase)
            advanceUntilIdle()
            assertEquals(updatedFavorites, viewModel.uiState.value.movies)
            viewModel.uiState.test {
                assertEquals(updatedFavorites, awaitItem().movies)
                cancelAndConsumeRemainingEvents()
            }
        }
}