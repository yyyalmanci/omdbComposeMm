package com.yyy.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetailResponse(
    @Json(name = "Title")
    val title: String?,
    @Json(name = "Year")
    val year: String?,
    @Json(name = "Rated")
    val rated: String?,
    @Json(name = "Released")
    val released: String?,
    @Json(name = "Runtime")
    val runtime: String?,
    @Json(name = "Genre")
    val genre: String?,
    @Json(name = "Director")
    val director: String?,
    @Json(name = "Writer")
    val writer: String?,
    @Json(name = "Actors")
    val actors: String?,
    @Json(name = "Plot")
    val plot: String?,
    @Json(name = "Language")
    val language: String?,
    @Json(name = "Country")
    val country: String?,
    @Json(name = "Awards")
    val awards: String?,
    @Json(name = "Poster")
    val poster: String?,
    @Json(name = "Ratings")
    val ratings: List<RatingResponse>?,
    @Json(name = "Metascore")
    val metascore: String?,
    val imdbRating: String?,
    val imdbVotes: String?,
    val imdbID: String?,
    @Json(name = "Type")
    val type: String?,
    @Json(name = "DVD")
    val dvd: String?,
    @Json(name = "BoxOffice")
    val boxOffice: String?,
    @Json(name = "Production")
    val production: String?,
    @Json(name = "Website")
    val website: String?,
    @Json(name = "Response")
    val response: String?
)

@JsonClass(generateAdapter = true)
data class RatingResponse(
    @Json(name = "Source")
    val source: String?,
    @Json(name = "Value")
    val value: String?
)