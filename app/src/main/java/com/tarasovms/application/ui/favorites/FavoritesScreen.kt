package com.tarasovms.application.ui.favorites

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tarasovms.application.data.db.PosterLocal
import com.tarasovms.application.ui.posters.FavoriteButton
import com.tarasovms.application.ui.posters.PostersEmpty
import com.tarasovms.application.ui.theme.ApplicationTheme
import com.tarasovms.application.utils.ImageNetworkCoil


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoritesPosters(
    modifier: Modifier = Modifier,
    posters: List<PosterLocal?>,
    selectPoster: (Long) -> Unit,
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(minSize = 128.dp),
        modifier = modifier
    ) {
        items(posters) { poster ->
            if (poster != null) {
                FavoritePosterScreen(
                    poster = poster,
                    selectPoster = selectPoster
                )
            }
        }
    }

    if (posters.isEmpty()) PostersEmpty()
}

@Composable
private fun FavoritePosterScreen(
    modifier: Modifier = Modifier,
    poster: PosterLocal,
    selectPoster: (Long) -> Unit = {},
) {
    Surface(
        modifier = modifier
            .padding(4.dp)
            .clickable(
                onClick = { selectPoster(poster.id) }
            ),
        color = MaterialTheme.colors.background,
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        ConstraintLayout {
            val (image, title, content) = createRefs()
            ImageNetworkCoil(
                url = poster.poster,
                modifier = Modifier
                    .aspectRatio(0.8f)
                    .constrainAs(image) {
                        centerHorizontallyTo(parent)
                        top.linkTo(parent.top)
                    }
            )

            FavoriteButton(poster = poster)

            Text(
                modifier = Modifier
                    .constrainAs(title) {
                        centerHorizontallyTo(parent)
                        top.linkTo(image.bottom)
                    }
                    .padding(8.dp),
                text = poster.name,
                style = MaterialTheme.typography.subtitle2,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onBackground
            )

            Text(
                modifier = Modifier
                    .constrainAs(content) {
                        centerHorizontallyTo(parent)
                        top.linkTo(title.bottom)
                    }
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 12.dp),
                text = poster.playtime,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}

@Preview("News screen")
@Preview("News screen (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("News screen (big font)", fontScale = 1.5f)
@Preview("News screen (large screen)", device = Devices.PIXEL_C)
@Composable
fun PreviewNewsScreen() {
    ApplicationTheme {
        FavoritePosterScreen(
            poster = PosterLocal.instance()
        )
    }
}
