package com.yyy.domain.model

data class MovieListItem(
    val search: List<MovieSearchResultItem>,
    val totalResults: String,
    val response: String
)

data class MovieSearchResultItem(
    val title: String,
    val year: String,
    val imdbID: String,
    val type: String,
    val poster: String,
    val listTitle: String
)
