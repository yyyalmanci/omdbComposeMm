package com.yyy.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.yyy.ui.model.FavoriteMovieId
import com.yyy.ui.model.MovieSearchUiState
import com.yyy.ui.model.SortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieSearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val getLanguageUseCase: GetLanguageUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MovieSearchUiState())
    val uiState: StateFlow<MovieSearchUiState> = _uiState.asStateFlow()

    private val _themeState = MutableStateFlow(ThemeOption.SYSTEM)
    val themeState: StateFlow<ThemeOption> = _themeState.asStateFlow()

    private val _langState = MutableStateFlow("")
    val langState: StateFlow<String> = _langState.asStateFlow()

    private val _operatedList = MutableStateFlow(emptyList<MovieSearchResultItem>())
    val operatedList: StateFlow<List<MovieSearchResultItem>> = _operatedList

    internal var currentQuery: String = ""
    var currentPage: Int = 1
    internal var isLoadingMore = false
    internal var endReached = false

    init {
        viewModelScope.launch {
            getLanguageUseCase().collect { lang ->
                _langState.update {
                    lang
                }
            }
        }
        viewModelScope.launch {
            getThemeUseCase().collect { theme ->
                _themeState.update {
                    theme
                }
            }
        }

        viewModelScope.launch {
            getSearchHistoryUseCase().collect { history ->
                _uiState.update { it.copy(searchHistory = history) }
            }
        }

        viewModelScope.launch {
            getFavoriteMoviesUseCase().collect { favoriteMovies ->
                _uiState.update {
                    it.copy(favoriteMovieIds = favoriteMovies.map { movie ->
                        FavoriteMovieId(movie.imdbID, movie.listTitle)
                    }.toSet())
                }
            }
        }
    }

    fun search(query: String) {
        if (query != currentQuery) {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    movies = MovieListItem(
                        search = emptyList()
                    ),
                    showFilmNotFound = false
                )
            }
            currentPage = 1
            endReached = false
        }
        currentQuery = query

        viewModelScope.launch {
            searchMoviesUseCase(query, currentPage).collect { result ->
                when (result) {
                    is MoviesRepositoryResult.Failed -> {
                        Log.d("MovieSearchViewModel", "Failed: ${result.message}")
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is MoviesRepositoryResult.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is MoviesRepositoryResult.Success -> {
                        val newList = result.movie.search
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                movies = result.movie.copy(
                                    search = newList
                                )
                            )
                        }
                        endReached = newList.isEmpty()
                    }
                }
            }
        }
    }

    fun loadNextPage() {
        if (isLoadingMore || endReached || currentQuery.isBlank() || currentPage >= 100) return
        isLoadingMore = true
        currentPage++
        viewModelScope.launch {
            searchMoviesUseCase(currentQuery, currentPage).collect { result ->
                when (result) {
                    is MoviesRepositoryResult.Success -> {
                        val currentList = _uiState.value.movies.search
                        val newList = result.movie.search
                        if (newList.isEmpty() || currentPage >= 100) {
                            endReached = true
                        }
                        _uiState.update {
                            it.copy(
                                movies = it.movies.copy(
                                    search = currentList + newList
                                )
                            )
                        }
                    }

                    is MoviesRepositoryResult.Failed -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        endReached = true
                    }

                    else -> {}
                }
                isLoadingMore = false
            }
        }
    }

    fun toggleFavorite(movie: MovieSearchResultItem, listTitle: String) {
        viewModelScope.launch {
            toggleFavoriteMovieUseCase(movie, listTitle)
        }
    }

    fun setSelectedType(type: String?) {
        _uiState.update { it.copy(typeFilter = type) }
        updateOperatedList()
    }

    fun setYearFilter(year: String?) {
        _uiState.update { it.copy(yearFilter = year) }
        updateOperatedList()
    }

    fun setSortOption(sortOption: SortOption) {
        _uiState.update { it.copy(sortOption = sortOption) }
        updateOperatedList()
    }

    internal fun updateOperatedList() {
        val list = _uiState.value.movies.search.filter {
            val typeMatch = if (_uiState.value.typeFilter != null) {
                it.type == _uiState.value.typeFilter
            } else {
                true
            }
            val yearMatch = if (_uiState.value.yearFilter != null) {
                it.year.contains(_uiState.value.yearFilter.orEmpty())
            } else {
                true
            }
            typeMatch && yearMatch
        }
        _operatedList.update {
            when (_uiState.value.sortOption) {
                SortOption.YEAR -> list.sortedByDescending { it.sortYear }
                SortOption.A_TO_Z -> list.sortedBy { it.title }
                SortOption.Z_TO_A -> list.sortedByDescending { it.title }
                else -> list
            }
        }

    }
}