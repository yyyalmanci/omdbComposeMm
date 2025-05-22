package com.yyy.domain.model

data class MovieListItemDomainModel(
    val search: List<MovieSearchResultItemDomainModel>?,
    val totalResults: String?,
    val response: String?
)

data class MovieSearchResultItemDomainModel(
    val title: String?,
    val year: String?,
    val imdbID: String?,
    val type: String?,
    val poster: String?
)
