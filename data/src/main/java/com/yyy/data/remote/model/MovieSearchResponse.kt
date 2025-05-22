package com.yyy.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.yyy.domain.model.MovieListItemDomainModel
import com.yyy.domain.model.MovieSearchResultItemDomainModel

@JsonClass(generateAdapter = true)
data class MovieSearchResponse(
    @Json(name = "Search")
    val search: List<MovieSearchResultItem>?,
    @Json(name = "totalResults")
    val totalResults: String?,
    @Json(name = "Response")
    val response: String?
)

@JsonClass(generateAdapter = true)
data class MovieSearchResultItem(
    @Json(name = "Title")
    val title: String?,
    @Json(name = "Year")
    val year: String?,
    @Json(name = "imdbID")
    val imdbID: String?,
    @Json(name = "Type")
    val type: String?,
    @Json(name = "Poster")
    val poster: String?
)

fun MovieSearchResponse.toDomainModel() = MovieListItemDomainModel(
    search = search?.map { it.toDomainModel() },
    totalResults = totalResults,
    response = response

)

fun MovieSearchResultItem.toDomainModel() = MovieSearchResultItemDomainModel(
    title = title,
    year = year,
    imdbID = imdbID,
    type = type,
    poster = poster
)
