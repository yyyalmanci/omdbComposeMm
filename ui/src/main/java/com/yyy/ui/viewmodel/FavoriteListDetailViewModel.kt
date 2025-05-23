package com.yyy.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.yyy.domain.model.MovieSearchResultItem
import com.yyy.domain.usecase.GetFavoriteMoviesUseCase
import com.yyy.domain.usecase.ToggleFavoriteMovieUseCase
import com.yyy.ui.model.FavoriteListDetailUiState
import com.yyy.ui.route.RouteClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteListDetailViewModel @Inject constructor(
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val listTitle: String =
        savedStateHandle.toRoute<RouteClass.FavoritesListDetail>().listTitle

    private val _uiState = MutableStateFlow(FavoriteListDetailUiState(listTitle = listTitle))
    val uiState: StateFlow<FavoriteListDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getFavoriteMoviesUseCase().collect { allMovies ->
                val currentListMovies = allMovies.filter { it.listTitle == listTitle }
                val otherLists = allMovies
                    .map { it.listTitle }
                    .distinct()
                    .filter { it != listTitle }
                    .sorted()

                _uiState.update {
                    it.copy(
                        movies = currentListMovies,
                        goBack = currentListMovies.isEmpty(),
                        availableLists = otherLists
                    )
                }
            }
        }
    }

    fun toggleFavorite(movie: MovieSearchResultItem) {
        viewModelScope.launch {
            toggleFavoriteMovieUseCase(movie, listTitle)
        }
    }

    fun moveMovieToOtherList(movie: MovieSearchResultItem, targetList: String) {
        viewModelScope.launch {
            toggleFavoriteMovieUseCase(movie, listTitle)
            toggleFavoriteMovieUseCase(movie, targetList)
        }
    }
} 