package com.yyy.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.yyy.domain.model.MovieListItem
import com.yyy.domain.model.MovieSearchResultItem

@JsonClass(generateAdapter = true)
data class MovieSearchResponse(
    @Json(name = "Search")
    val search: List<MovieSearchResultItemResponse>?,
    @Json(name = "totalResults")
    val totalResults: String?,
    @Json(name = "Response")
    val response: String?
)

@JsonClass(generateAdapter = true)
data class MovieSearchResultItemResponse(
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

fun MovieSearchResponse.toModel() = MovieListItem(
    search = search?.map { it.toModel() }.orEmpty()

)

fun MovieSearchResultItemResponse.toModel() = MovieSearchResultItem(
    title = title.orEmpty(),
    year = year.orEmpty(),
    imdbID = imdbID.orEmpty(),
    type = type.orEmpty(),
    poster = poster.orEmpty(),
    listTitle = "",
    sortYear = if (year?.contains("–") == true) {
        year.split("–").first().toInt()
    } else {
        year?.toInt() ?: 0
    }
)
