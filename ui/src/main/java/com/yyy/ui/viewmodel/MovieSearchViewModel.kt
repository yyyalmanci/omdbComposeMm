package com.yyy.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yyy.domain.model.MovieListItem
import com.yyy.domain.model.MovieSearchResultItem
import com.yyy.domain.repository.result.MoviesRepositoryResult
import com.yyy.domain.usecase.GetFavoriteMoviesUseCase
import com.yyy.domain.usecase.GetSearchHistoryUseCase
import com.yyy.domain.usecase.SearchMoviesUseCase
import com.yyy.domain.usecase.ToggleFavoriteMovieUseCase
import com.yyy.ui.model.FavoriteMovieId
import com.yyy.ui.model.MovieSearchUiState
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
    private val toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MovieSearchUiState())
    val uiState: StateFlow<MovieSearchUiState> = _uiState.asStateFlow()

    private var currentQuery: String = ""
    var currentPage: Int = 1
    private var isLoadingMore = false
    private var endReached = false

    init {
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
                        search = emptyList(),
                        totalResults = "",
                        response = ""
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
}