package com.yyy.domain.usecase

import com.yyy.domain.model.MovieSearchResultItem
import com.yyy.domain.repository.FavoritesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class ToggleFavoriteMovieUseCaseTest {

    @MockK
    private lateinit var mockFavoritesRepository: FavoritesRepository

    private lateinit var toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase

    private val testMovie =
        MovieSearchResultItem("tt123", "Test Movie", "2023", "movie", "", "Test Movie", 2023)
    private val anotherMovie =
        MovieSearchResultItem("tt456", "Another Movie", "2022", "movie", "", "Another Movie", 2022)
    private val testListTitle = "MyList"

    @BeforeEach
    fun setUp() {
        toggleFavoriteMovieUseCase = ToggleFavoriteMovieUseCase(mockFavoritesRepository)
    }

    @Test
    fun `invoke - when movie is NOT in favorites - should call addToFavorites`() = runTest {
        every { mockFavoritesRepository.getAllFavoriteMovies() } returns flowOf(listOf(anotherMovie))
        coEvery { mockFavoritesRepository.addToFavorites(testMovie, testListTitle) } coAnswers {}

        toggleFavoriteMovieUseCase(testMovie, testListTitle)

        coVerify(exactly = 1) { mockFavoritesRepository.getAllFavoriteMovies() }
        coVerify(exactly = 1) { mockFavoritesRepository.addToFavorites(testMovie, testListTitle) }
        coVerify(exactly = 0) { mockFavoritesRepository.removeFromFavorites(any()) }
    }

    @Test
    fun `invoke - when movie IS in favorites - should call removeFromFavorites`() = runTest {
        every { mockFavoritesRepository.getAllFavoriteMovies() } returns flowOf(
            listOf(
                testMovie,
                anotherMovie
            )
        )
        coEvery { mockFavoritesRepository.removeFromFavorites(testMovie.imdbID) } coAnswers {}

        toggleFavoriteMovieUseCase(testMovie, testListTitle)

        coVerify(exactly = 1) { mockFavoritesRepository.getAllFavoriteMovies() }
        coVerify(exactly = 1) { mockFavoritesRepository.removeFromFavorites(testMovie.imdbID) }
        coVerify(exactly = 0) { mockFavoritesRepository.addToFavorites(any(), any()) }
    }

    @Test
    fun `invoke - when favorite list is empty - should call addToFavorites`() = runTest {
        every { mockFavoritesRepository.getAllFavoriteMovies() } returns flowOf(emptyList())
        coEvery { mockFavoritesRepository.addToFavorites(testMovie, testListTitle) } coAnswers {}

        toggleFavoriteMovieUseCase(testMovie, testListTitle)

        coVerify(exactly = 1) { mockFavoritesRepository.getAllFavoriteMovies() }
        coVerify(exactly = 1) { mockFavoritesRepository.addToFavorites(testMovie, testListTitle) }
        coVerify(exactly = 0) { mockFavoritesRepository.removeFromFavorites(any()) }
    }

    @Test
    fun `invoke - when getAllFavoriteMovies throws exception - should propagate exception`() =
        runTest {
            val expectedException = RuntimeException("Database error")
            every { mockFavoritesRepository.getAllFavoriteMovies() } returns kotlinx.coroutines.flow.flow {
                throw expectedException
            }

            var thrownException: Throwable? = null

            try {
                toggleFavoriteMovieUseCase(testMovie, testListTitle)
            } catch (e: Throwable) {
                thrownException = e
            }

            assertEquals(expectedException, thrownException)
            coVerify(exactly = 1) { mockFavoritesRepository.getAllFavoriteMovies() }
            coVerify(exactly = 0) { mockFavoritesRepository.addToFavorites(any(), any()) }
            coVerify(exactly = 0) { mockFavoritesRepository.removeFromFavorites(any()) }
        }
}