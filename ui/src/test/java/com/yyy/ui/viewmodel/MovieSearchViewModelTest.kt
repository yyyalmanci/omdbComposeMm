package com.yyy.ui.viewmodel

import app.cash.turbine.test
import com.yyy.domain.model.MovieListItem
import com.yyy.domain.model.MovieSearchResultItem
import com.yyy.domain.repository.result.MoviesRepositoryResult
import com.yyy.domain.usecase.GetFavoriteMoviesUseCase
import com.yyy.domain.usecase.GetLanguageUseCase
import com.yyy.domain.usecase.GetSearchHistoryUseCase
import com.yyy.domain.usecase.GetThemeUseCase
import com.yyy.domain.usecase.SearchMoviesUseCase
import com.yyy.domain.usecase.ToggleFavoriteMovieUseCase
import com.yyy.theme.ThemeOption
import com.yyy.ui.MainCoroutineRule
import com.yyy.ui.model.FavoriteMovieId
import com.yyy.ui.model.SortOption
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class MovieSearchViewModelTest {

    @JvmField
    @RegisterExtension
    val mainCoroutineRule = MainCoroutineRule()

    // Mock UseCase'ler
    @MockK
    lateinit var searchMoviesUseCase: SearchMoviesUseCase
    @MockK
    lateinit var getSearchHistoryUseCase: GetSearchHistoryUseCase
    @MockK
    lateinit var getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase
    @MockK
    lateinit var toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase
    @MockK
    lateinit var getThemeUseCase: GetThemeUseCase
    @MockK
    lateinit var getLanguageUseCase: GetLanguageUseCase

    private lateinit var viewModel: MovieSearchViewModel

    @BeforeEach
    fun setUp() {
        every { getThemeUseCase() } returns flowOf(ThemeOption.SYSTEM)
        every { getLanguageUseCase() } returns flowOf("en")
        every { getSearchHistoryUseCase() } returns flowOf(emptyList())
        every { getFavoriteMoviesUseCase() } returns flowOf(emptyList())

        viewModel = MovieSearchViewModel(
            searchMoviesUseCase,
            getSearchHistoryUseCase,
            getFavoriteMoviesUseCase,
            toggleFavoriteMovieUseCase,
            getThemeUseCase,
            getLanguageUseCase
        )
    }

    // --- Initialization Testleri ---
    @Test
    fun `init should collect theme and update themeState`() = runTest {
        val expectedTheme = ThemeOption.DARK
        every { getThemeUseCase() } returns flowOf(expectedTheme)

        // ViewModel init behaviour
        viewModel = MovieSearchViewModel(
            searchMoviesUseCase,
            getSearchHistoryUseCase,
            getFavoriteMoviesUseCase,
            toggleFavoriteMovieUseCase,
            getThemeUseCase,
            getLanguageUseCase
        )
        advanceUntilIdle()

        assertEquals(expectedTheme, viewModel.themeState.value)
    }

    @Test
    fun `init  should collect language and update langState`() = runTest {
        val expectedLang = "tr"
        every { getLanguageUseCase() } returns flowOf(expectedLang)
        viewModel = MovieSearchViewModel(
            searchMoviesUseCase,
            getSearchHistoryUseCase,
            getFavoriteMoviesUseCase,
            toggleFavoriteMovieUseCase,
            getThemeUseCase,
            getLanguageUseCase
        )
        advanceUntilIdle()
        assertEquals(expectedLang, viewModel.langState.value)
    }

    @Test
    fun `init - should collect search history and update uiState searchHistory`() = runTest {
        val history = listOf("batman", "superman")
        every { getSearchHistoryUseCase() } returns flowOf(history)
        viewModel = MovieSearchViewModel(
            searchMoviesUseCase,
            getSearchHistoryUseCase,
            getFavoriteMoviesUseCase,
            toggleFavoriteMovieUseCase,
            getThemeUseCase,
            getLanguageUseCase
        )
        advanceUntilIdle()
        assertEquals(history, viewModel.uiState.value.searchHistory)
    }

    @Test
    fun `init  should collect favorite movies and update uiState favoriteMovieIds`() = runTest {
        val favorites = listOf(
            MovieSearchResultItem(
                "id1",
                "Fav Movie 1",
                "2022",
                "movie",
                "poster_url1",
                "Fav Movie 1",
                2022
            )
        )
        val expectedFavoriteIds = favorites.map { FavoriteMovieId(it.imdbID, it.listTitle) }.toSet()
        every { getFavoriteMoviesUseCase() } returns flowOf(favorites)

        viewModel = MovieSearchViewModel(
            searchMoviesUseCase,
            getSearchHistoryUseCase,
            getFavoriteMoviesUseCase,
            toggleFavoriteMovieUseCase,
            getThemeUseCase,
            getLanguageUseCase
        )
        advanceUntilIdle()

        assertEquals(expectedFavoriteIds, viewModel.uiState.value.favoriteMovieIds)
    }

    // --- Search Fonksiyonu Testleri ---
    @Test
    fun `search - when query is valid - updates uiState to Loading then Success`() = runTest {
        val query = "batman"
        val movieItem = MovieSearchResultItem(
            query,
            "Batman Begins",
            "2005",
            "movie",
            "poster_url",
            "Batman Begins",
            2005
        )
        val successResult =
            MoviesRepositoryResult.Success(MovieListItem(search = listOf(movieItem)))

        every { searchMoviesUseCase(query, 1) } returns flowOf(
            MoviesRepositoryResult.Loading,
            successResult
        )

        viewModel.uiState.test {
            awaitItem()

            viewModel.search(query)

            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)
            assertTrue(loadingState.movies.search.isEmpty())

            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertEquals(listOf(movieItem), successState.movies.search)
            assertEquals(1, viewModel.currentPage)
            assertFalse(viewModel.endReached)

            cancelAndConsumeRemainingEvents()
        }
        verify { searchMoviesUseCase(query, 1) }
    }

    @Test
    fun `search - when query is different - resets previous results and pagination`() = runTest {
        val initialQuery = "superman"
        val newQuery = "batman"
        val initialMovie = MovieSearchResultItem(
            initialQuery,
            "Man of Steel",
            "2013",
            "movie",
            "p",
            "Man of Steel",
            2013
        )
        val newMovie =
            MovieSearchResultItem(newQuery, "The Batman", "2022", "movie", "p", "The Batman", 2022)

        every {
            searchMoviesUseCase(
                initialQuery,
                1
            )
        } returns flowOf(MoviesRepositoryResult.Success(MovieListItem(search = listOf(initialMovie))))
        every { searchMoviesUseCase(newQuery, 1) } returns flowOf(
            MoviesRepositoryResult.Success(
                MovieListItem(search = listOf(newMovie))
            )
        )

        viewModel.search(initialQuery)
        advanceUntilIdle()
        assertEquals(listOf(initialMovie), viewModel.uiState.value.movies.search)

        viewModel.search(newQuery)
        advanceUntilIdle()

        viewModel.uiState.test {
            val finalState = awaitItem()
            assertEquals(listOf(newMovie), finalState.movies.search)
            assertEquals(1, viewModel.currentPage)
            assertFalse(viewModel.endReached)
            cancelAndConsumeRemainingEvents()
        }
        assertEquals(newQuery, viewModel.currentQuery)
    }

    @Test
    fun `search when API fails  updates uiState with isLoading false`() = runTest {
        val query = "error_query"
        val failureResult = MoviesRepositoryResult.Failed("Network Error")
        every { searchMoviesUseCase(query, 1) } returns flowOf(failureResult)

        viewModel.uiState.test {
            searchMoviesUseCase(query, 1)
            assertFalse(awaitItem().isLoading)
        }
    }

    // --- Load Next Page Testleri ---
    @Test
    fun `loadNextPage - when conditions met - fetches and appends new items`() = runTest {
        val query = "batman"
        val initialItem = MovieSearchResultItem(
            "id1",
            "Batman Begins",
            "2005",
            "movie",
            "p",
            "Batman Begins",
            2005
        )
        val nextItem = MovieSearchResultItem(
            "id2",
            "The Dark Knight",
            "2008",
            "movie",
            "p",
            "The Dark Knight",
            2008
        )

        every { searchMoviesUseCase(query, 1) } returns flowOf(
            MoviesRepositoryResult.Success(
                MovieListItem(search = listOf(initialItem))
            )
        )
        every { searchMoviesUseCase(query, 2) } returns flowOf(
            MoviesRepositoryResult.Success(
                MovieListItem(search = listOf(nextItem))
            )
        )

        viewModel.search(query)
        advanceUntilIdle()

        viewModel.loadNextPage()
        advanceUntilIdle()

        assertEquals(2, viewModel.currentPage)
        assertEquals(listOf(initialItem, nextItem), viewModel.uiState.value.movies.search)
        assertFalse(viewModel.isLoadingMore)
    }

    @Test
    fun `loadNextPage - when endReached is true - does nothing`() = runTest {
        viewModel.currentQuery = "batman"
        viewModel.currentPage = 1
        viewModel.endReached = true

        viewModel.loadNextPage()
        advanceUntilIdle()

        coVerify(exactly = 0) { searchMoviesUseCase(any(), any()) }
        assertEquals(1, viewModel.currentPage)
    }

    @Test
    fun `loadNextPage - when isLoadingMore is true - does nothing`() = runTest {
        viewModel.currentQuery = "batman"
        viewModel.currentPage = 1
        viewModel.endReached = false
        viewModel.isLoadingMore = true // Test edilecek durum

        viewModel.loadNextPage()
        advanceUntilIdle()

        coVerify(exactly = 0) { searchMoviesUseCase(any(), any()) }
        assertEquals(1, viewModel.currentPage)
    }

    // --- Toggle Favorite Testi ---
    @Test
    fun `toggleFavorite - should call toggleFavoriteMovieUseCase`() = runTest {
        val movie =
            MovieSearchResultItem("id1", "Test Movie", "2023", "movie", "p", "Test Movie", 2023)
        val listTitle = "My Favorites"
        coEvery { toggleFavoriteMovieUseCase(movie, listTitle) } returns Unit

        viewModel.toggleFavorite(movie, listTitle)
        advanceUntilIdle()

        coVerify { toggleFavoriteMovieUseCase(movie, listTitle) }
    }

    // --- Filtreleme ve Sıralama Testleri ---
    @Test
    fun `setSelectedType - updates typeFilter and filters operatedList`() = runTest {
        val initialMovies = listOf(
            MovieSearchResultItem("id1", "Avengers", "2012", "movie", "p1", "Avengers", 2012),
            MovieSearchResultItem("id2", "Batman", "2022", "movie", "p2", "Batman", 2022),
            MovieSearchResultItem("id3", "Superman", "2013", "series", "p3", "Superman", 2013)
        )
        val successResult = MoviesRepositoryResult.Success(MovieListItem(search = initialMovies))
        every { searchMoviesUseCase("query", 1) } returns flowOf(
            MoviesRepositoryResult.Loading,
            successResult
        )

        viewModel.operatedList.test {
            viewModel.search("query")
            awaitItem()
            viewModel.updateOperatedList()
            assertEquals(initialMovies, awaitItem())
            viewModel.setSelectedType("movie")

            val filteredByType = awaitItem()
            assertEquals(2, filteredByType.size)
            assertTrue(filteredByType.all { it.type == "movie" })

            viewModel.setSelectedType(null)
            assertEquals(initialMovies.size, awaitItem().size)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `setSortOption - updates sortOption and sorts operatedList by A-Z`() = runTest {
        val initialMovies = listOf(
            MovieSearchResultItem("Batman", "Batman", "2022", "movie", "p2", "Batman", 2022),
            MovieSearchResultItem("Avengers", "Avengers", "2012", "movie", "p1", "Avengers", 2012),
            MovieSearchResultItem("Superman", "Superman", "2013", "series", "p3", "Superman", 2013)
        )
        val successResult = MoviesRepositoryResult.Success(MovieListItem(search = initialMovies))
        every { searchMoviesUseCase("query", 1) } returns flowOf(successResult)

        viewModel.operatedList.test {
            viewModel.search("query")
            awaitItem()
            viewModel.updateOperatedList()
            assertEquals(initialMovies.size, awaitItem().size)

            viewModel.setSortOption(SortOption.A_TO_Z)
            val sortedListAZ = awaitItem()
            assertEquals("Avengers", sortedListAZ[0].title)
            assertEquals("Batman", sortedListAZ[1].title)
            assertEquals("Superman", sortedListAZ[2].title)

            viewModel.setSortOption(SortOption.YEAR)
            val sortedListYear = awaitItem()
            assertEquals("Batman", sortedListYear[0].title)
            assertEquals("Superman", sortedListYear[1].title)
            assertEquals("Avengers", sortedListYear[2].title)

            cancelAndConsumeRemainingEvents()
        }
    }
}