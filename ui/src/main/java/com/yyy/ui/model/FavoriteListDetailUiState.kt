package com.yyy.ui.model

import com.yyy.domain.model.MovieSearchResultItem

data class FavoriteListDetailUiState(
    val listTitle: String = "",
    val movies: List<MovieSearchResultItem> = emptyList(),
    val sortOption: SortOption = SortOption.NONE,
    val operatedList: List<MovieSearchResultItem> = emptyList(),
    val goBack: Boolean = false,
    val availableLists: List<String> = emptyList()
)