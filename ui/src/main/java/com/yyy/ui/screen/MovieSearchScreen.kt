package com.yyy.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.yyy.domain.model.MovieSearchResultItem
import com.yyy.theme.OmdbComposeMmTheme
import com.yyy.ui.R
import com.yyy.ui.viewmodel.MovieSearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun MovieSearchScreen(viewModel: MovieSearchViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var isSearchFocused by remember { mutableStateOf(false) }
    val gridState = rememberLazyGridState()

    LaunchedEffect(gridState, uiState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo }
            .distinctUntilChanged()
            .collectLatest { visibleItems ->
                val lastVisible = visibleItems.lastOrNull()?.index ?: 0
                val total = uiState.movies.search.size
                if (lastVisible >= getNextLoadPoint(total) && total > 0 && !uiState.isLoading) {
                    viewModel.loadNextPage()
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isSearchFocused = it.isFocused },
                label = {
                    Text(
                        stringResource(R.string.search_film),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )

            if (isSearchFocused && searchText.text.isEmpty() && uiState.searchHistory.isNotEmpty()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 56.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 3.dp
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        uiState.searchHistory.forEach { query ->
                            Text(
                                text = query,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        searchText = TextFieldValue(
                                            text = query,
                                            selection = TextRange(query.length)
                                        )
                                        viewModel.search(query)
                                    }
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                if (searchText.text.isNotBlank()) {
                    viewModel.search(searchText.text)
                }
            },
            enabled = searchText.text.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Text(stringResource(R.string.search))
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                uiState.movies.search.isEmpty() -> {
                    Text(
                        text = if (uiState.showFilmNotFound) {
                            stringResource(R.string.film_not_found)
                        } else {
                            stringResource(R.string.need_search_film)
                        },
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(10.dp, 10.dp, 10.dp, 60.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        state = gridState
                    ) {
                        items(items = uiState.movies.search, key = { it.imdbID }) { movie ->
                            MoviePoster(
                                movie = movie,
                                isFavorite = movie.imdbID in uiState.favoriteMovieIds,
                                onFavoriteClick = { viewModel.toggleFavorite(movie) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MoviePoster(
    movie: MovieSearchResultItem,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    useLocalImage: Boolean = false
) {
    Column(
        modifier = modifier
            .width(160.dp)
            .height(350.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            val painter = if (useLocalImage) {
                painterResource(id = android.R.drawable.star_on)
            } else {
                rememberAsyncImagePainter(model = movie.poster)
            }
            Image(
                painter = painter,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie.title,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = movie.year,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Button(
            onClick = onFavoriteClick,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = if (isFavorite) {
                    stringResource(R.string.remove_from_favorites)
                } else {
                    stringResource(R.string.add_to_favorites)
                },
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoviePosterPreview() {
    OmdbComposeMmTheme {
        MoviePoster(
            movie = MovieSearchResultItem(
                title = "Batman: The Dark Knight Returns - Part 1",
                year = "2022",
                imdbID = "tt1234567",
                type = "movie",
                poster = ""
            ),
            isFavorite = true,
            onFavoriteClick = {},
            useLocalImage = true
        )
    }
}

const val PAGE_ITEM_SIZE = 10

fun getNextLoadPoint(itemSize: Int) = if (PAGE_ITEM_SIZE * 4 >= itemSize) {
    itemSize - 4
} else {
    itemSize - 8
}