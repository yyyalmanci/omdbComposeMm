package com.yyy.ui.route

import kotlinx.serialization.Serializable

@Serializable
sealed class RouteClass() {

    @Serializable
    data class MovieSearch(
        val name: String = "MovieSearch"
    ) : RouteClass()

    @Serializable
    data class FavoritesList(
        val name: String = "FavoritesList"
    ) : RouteClass()

    @Serializable
    data class FavoritesListDetail(
        val name: String = "FavoritesListDetail",
        val listTitle: String
    ) : RouteClass()

    @Serializable
    data class Settings(
        val name: String = "Settings"
    ) : RouteClass()
}