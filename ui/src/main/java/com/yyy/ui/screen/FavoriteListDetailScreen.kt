package com.yyy.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yyy.domain.model.MovieSearchResultItem
import com.yyy.ui.R
import com.yyy.ui.commonview.MoviePoster
import com.yyy.ui.commonview.RemoveConfirmationDialog
import com.yyy.ui.model.SortOption
import com.yyy.ui.viewmodel.FavoriteListDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteListDetailScreen(
    viewModel: FavoriteListDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    goDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showRemoveConfirmationDialog by remember { mutableStateOf<MovieSearchResultItem?>(null) }
    var showMoveDialog by remember { mutableStateOf<MovieSearchResultItem?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.favorite_list_detail, uiState.listTitle)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            if (uiState.goBack) {
                onNavigateBack()
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(10.dp, 10.dp, 10.dp, 60.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = if (uiState.sortOption != SortOption.NONE) {
                        uiState.operatedList
                    } else {
                        uiState.movies
                    }
                ) { movie ->
                    MoviePoster(
                        movie = movie,
                        isFavorite = true,
                        onFavoriteClick = { showRemoveConfirmationDialog = movie },
                        onMoveClick = { showMoveDialog = movie },
                        shouldShowMove = true,
                        onPosterClick = {
                            goDetail(it)
                        }
                    )
                }
            }
        }
    }

    if (showRemoveConfirmationDialog != null) {
        RemoveConfirmationDialog(
            onDismiss = { showRemoveConfirmationDialog = null },
            onConfirm = {
                showRemoveConfirmationDialog?.let { movie ->
                    viewModel.toggleFavorite(movie)
                }
                showRemoveConfirmationDialog = null
            }
        )
    }

    if (showMoveDialog != null) {
        MoveToOtherListDialog(
            availableLists = uiState.availableLists,
            onDismiss = { showMoveDialog = null },
            onListSelected = { targetList ->
                showMoveDialog?.let { movie ->
                    viewModel.moveMovieToOtherList(movie, targetList)
                }
                showMoveDialog = null
            }
        )
    }
}

@Composable
private fun MoveToOtherListDialog(
    availableLists: List<String>,
    onDismiss: () -> Unit,
    onListSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.select_target_list)) },
        text = {
            if (availableLists.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_other_lists),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    items(availableLists) { listTitle ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onListSelected(listTitle) }
                                .padding(vertical = 4.dp),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Text(
                                text = listTitle,
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        dismissButton = null
    )
}
