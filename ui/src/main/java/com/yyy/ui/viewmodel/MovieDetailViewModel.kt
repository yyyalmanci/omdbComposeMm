package com.yyy.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.yyy.domain.repository.result.MovieDetailRepositoryResult
import com.yyy.domain.usecase.GetMovieDetailUseCase
import com.yyy.ui.model.MovieDetailUiState
import com.yyy.ui.route.RouteClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val imdbId: String = savedStateHandle.toRoute<RouteClass.MovieDetail>().imdbId

    private val _uiState = MutableStateFlow(MovieDetailUiState(isLoading = true))
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    init {
        loadMovieDetail()
    }

    private fun loadMovieDetail() {
        viewModelScope.launch {
            getMovieDetailUseCase(imdbId).collect { result ->
                when (result) {
                    is MovieDetailRepositoryResult.Loading -> {
                        //state is already loading
                    }

                    is MovieDetailRepositoryResult.Failed -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "Film detayları yüklenemedi"
                            )
                        }
                    }

                    is MovieDetailRepositoryResult.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                movie = result.movie,
                                error = null
                            )
                        }
                    }
                }
            }
        }
    }
} 