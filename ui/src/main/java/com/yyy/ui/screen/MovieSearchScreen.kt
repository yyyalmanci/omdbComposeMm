package com.yyy.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yyy.domain.model.MovieSearchResultItem
import com.yyy.ui.R
import com.yyy.ui.commonview.AddToFavoritesDialog
import com.yyy.ui.commonview.MoviePoster
import com.yyy.ui.commonview.RemoveConfirmationDialog
import com.yyy.ui.model.MovieType
import com.yyy.ui.model.SortOption
import com.yyy.ui.viewmodel.MovieSearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun MovieSearchScreen(viewModel: MovieSearchViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val operatedList by viewModel.operatedList.collectAsStateWithLifecycle()
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var isSearchFocused by remember { mutableStateOf(false) }
    val gridState = rememberLazyGridState()
    var showAddToFavoritesDialog by remember { mutableStateOf<MovieSearchResultItem?>(null) }
    var showRemoveConfirmationDialog by remember { mutableStateOf<MovieSearchResultItem?>(null) }
    var showYearDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.filters),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            TypeFilterChip(
                text = stringResource(R.string.movie),
                selected = uiState.typeFilter == MovieType.MOVIE.type,
                onClick = { viewModel.setSelectedType(if (uiState.typeFilter == MovieType.MOVIE.type) null else MovieType.MOVIE.type) }
            )
            TypeFilterChip(
                text = stringResource(R.string.series),
                selected = uiState.typeFilter == MovieType.SERIES.type,
                onClick = { viewModel.setSelectedType(if (uiState.typeFilter == MovieType.SERIES.type) null else MovieType.SERIES.type) }
            )
            TypeFilterChip(
                text = stringResource(R.string.episode),
                selected = uiState.typeFilter == MovieType.EPISODE.type,
                onClick = { viewModel.setSelectedType(if (uiState.typeFilter == MovieType.EPISODE.type) null else MovieType.EPISODE.type) }
            )
            TypeFilterChip(
                text = if (uiState.yearFilter != null) {
                    uiState.yearFilter.orEmpty()
                } else {
                    stringResource(R.string.year)
                },
                selected = uiState.yearFilter != null,
                onClick = { showYearDialog = true }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.sort),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            TypeFilterChip(
                text = stringResource(R.string.sort_by_year),
                selected = uiState.sortOption == SortOption.YEAR,
                onClick = {
                    viewModel.setSortOption(
                        if (uiState.sortOption == SortOption.YEAR) {
                            SortOption.NONE
                        } else {
                            SortOption.YEAR
                        }
                    )
                }
            )
            TypeFilterChip(
                text = stringResource(R.string.sort_a_to_z),
                selected = uiState.sortOption == SortOption.A_TO_Z,
                onClick = {
                    viewModel.setSortOption(
                        if (uiState.sortOption == SortOption.A_TO_Z) {
                            SortOption.NONE
                        } else {
                            SortOption.A_TO_Z
                        }
                    )
                }
            )
            TypeFilterChip(
                text = stringResource(R.string.sort_z_to_a),
                selected = uiState.sortOption == SortOption.Z_TO_A,
                onClick = {
                    viewModel.setSortOption(
                        if (uiState.sortOption == SortOption.Z_TO_A) {
                            SortOption.NONE
                        } else {
                            SortOption.Z_TO_A
                        }
                    )
                }
            )
        }

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
                        items(
                            items = if (uiState.sortOption != SortOption.NONE ||
                                uiState.yearFilter.isNullOrEmpty().not() ||
                                uiState.typeFilter.isNullOrEmpty().not()
                            ) {
                                operatedList
                            } else {
                                uiState.movies.search
                            }
                        ) { movie ->
                            val isFavorite =
                                movie.imdbID in uiState.favoriteMovieIds.map { it.imdbId }
                            MoviePoster(
                                movie = movie,
                                isFavorite = isFavorite,
                                onFavoriteClick = {
                                    if (isFavorite) {
                                        showRemoveConfirmationDialog = movie
                                    } else {
                                        showAddToFavoritesDialog = movie
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddToFavoritesDialog != null) {
        AddToFavoritesDialog(
            onDismiss = { showAddToFavoritesDialog = null },
            onConfirm = { listTitle ->
                showAddToFavoritesDialog?.let { movie ->
                    viewModel.toggleFavorite(movie, listTitle)
                }
                showAddToFavoritesDialog = null
            },
            existingLists = uiState.favoriteMovieIds.map { it.listTitle }.distinct(),
            onNewList = { listTitle ->
                showAddToFavoritesDialog?.let { movie ->
                    viewModel.toggleFavorite(movie, listTitle)
                }
                showAddToFavoritesDialog = null
            }
        )
    }

    if (showRemoveConfirmationDialog != null) {
        RemoveConfirmationDialog(
            onDismiss = { showRemoveConfirmationDialog = null },
            onConfirm = {
                showRemoveConfirmationDialog?.let { movie ->
                    val listTitle =
                        uiState.favoriteMovieIds.find { it.imdbId == movie.imdbID }?.listTitle
                            ?: context.getString(R.string.unnamed_list)
                    viewModel.toggleFavorite(movie, listTitle)
                }
                showRemoveConfirmationDialog = null
            }
        )
    }

    if (showYearDialog) {
        YearFilterDialog(
            currentYear = uiState.yearFilter,
            onDismiss = { showYearDialog = false },
            onYearSelected = { year ->
                viewModel.setYearFilter(year)
                showYearDialog = false
            }
        )
    }
}

const val PAGE_ITEM_SIZE = 10

fun getNextLoadPoint(itemSize: Int) = if (PAGE_ITEM_SIZE * 4 >= itemSize) {
    itemSize - 4
} else {
    itemSize - 8
}

@Composable
private fun TypeFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text) },
        modifier = modifier,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
private fun YearFilterDialog(
    currentYear: String?,
    onDismiss: () -> Unit,
    onYearSelected: (String?) -> Unit
) {
    var selectedYear by remember { mutableStateOf(currentYear) }
    val years = remember { (1900..2100).map { it.toString() } }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.select_year)) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onYearSelected(null) }
                        .padding(vertical = 4.dp),
                    color = if (selectedYear == null)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surface
                ) {
                    Text(
                        text = stringResource(R.string.delete_filter),
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (selectedYear == null)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    items(years) { year ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedYear = year },
                            color = if (selectedYear == year)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surface
                        ) {
                            Text(
                                text = year,
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (selectedYear == year)
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onYearSelected(selectedYear) }) {
                Text(stringResource(R.string.apply))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}