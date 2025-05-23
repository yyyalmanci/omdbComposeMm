package com.yyy.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.yyy.domain.model.MovieDetail
import com.yyy.domain.model.Rating

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

fun RatingResponse.toModel(): Rating {
    return Rating(
        source = source.orEmpty(),
        value = value.orEmpty()
    )
}


fun MovieDetailResponse.toModel(): MovieDetail {
    return MovieDetail(
        title = title.orEmpty(),
        year = year.orEmpty(),
        rated = rated.orEmpty(),
        released = released.orEmpty(),
        runtime = runtime.orEmpty(),
        genre = genre.orEmpty(),
        director = director.orEmpty(),
        writer = writer.orEmpty(),
        actors = actors.orEmpty(),
        plot = plot.orEmpty(),
        language = language.orEmpty(),
        country = country.orEmpty(),
        awards = awards.orEmpty(),
        poster = poster.orEmpty(),
        ratings = ratings?.map { it.toModel() } ?: emptyList(),
        metascore = metascore.orEmpty(),
        imdbRating = imdbRating.orEmpty(),
        imdbVotes = imdbVotes.orEmpty(),
        imdbID = imdbID.orEmpty(),
        type = type.orEmpty(),
        dvd = dvd.orEmpty(),
        boxOffice = boxOffice.orEmpty(),
        production = production.orEmpty(),
        website = website.orEmpty(),
        response = response.orEmpty()
    )
}