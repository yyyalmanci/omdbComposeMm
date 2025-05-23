package com.yyy.ui.commonview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.yyy.domain.model.MovieSearchResultItem
import com.yyy.theme.OmdbComposeMmTheme
import com.yyy.ui.R

@Composable
fun MoviePoster(
    movie: MovieSearchResultItem,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    useLocalImage: Boolean = false,
    onMoveClick: () -> Unit = {},
    shouldShowMove: Boolean = false,
    onPosterClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .width(160.dp)
            .then(
                if (shouldShowMove) {
                    Modifier.height(370.dp)
                } else {
                    Modifier.height(350.dp)
                }
            )
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
                    .height(180.dp)
                    .clickable {
                        onPosterClick(movie.imdbID)
                    },
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

        if (shouldShowMove) {
            Button(
                onClick = onMoveClick,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = stringResource(R.string.move_to_list),
                    maxLines = 1,
                    style = MaterialTheme.typography.labelMedium
                )
            }
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
                poster = "",
                listTitle = "Favorites",
                sortYear = 2011
            ),
            isFavorite = true,
            onFavoriteClick = {},
            useLocalImage = true,
            onPosterClick = {}
        )
    }
}