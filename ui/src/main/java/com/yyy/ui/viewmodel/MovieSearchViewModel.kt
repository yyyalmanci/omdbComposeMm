package com.yyy.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yyy.domain.usecase.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieSearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {
    init {
        viewModelScope.launch {
            delay(4000)
            searchMoviesUseCase("Batman").collect { result ->
                Log.d("MovieSearchViewModel", "Result: $result")

            }
        }
    }
}