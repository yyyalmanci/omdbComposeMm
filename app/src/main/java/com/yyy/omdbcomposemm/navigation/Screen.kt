package com.yyy.omdbcomposemm.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class RouteClass() {

    @Serializable
    data class MovieSearch(
        val name: String = "MovieSearch"
    ) : RouteClass()

    @Serializable
    data class Favorites(
        val name: String = "Favorites"
    ) : RouteClass()

    @Serializable
    data class Settings(
        val name: String = "Settings"
    ) : RouteClass()
}