package com.tarasovms.application.ui.posters

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tarasovms.application.utils.ImageNetworkCoil
import com.tarasovms.application.data.db.PosterLocal
import com.tarasovms.application.ui.theme.*

@Composable
fun HomePosters(
    modifier: Modifier = Modifier,
    posters: List<PosterLocal>,
    selectPoster: (Long) -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colors.primaryVariant)

    ) {
        posters.forEach { poster ->
            HomePoster(
                poster = poster,
                selectPoster = selectPoster
            )
        }
    }
}

@Composable
private fun HomePoster(
    modifier: Modifier = Modifier,
    poster: PosterLocal,
    selectPoster: (Long) -> Unit = {},
) {
    Surface(
        modifier = modifier
            .padding(20.dp)
            .clickable(onClick = { selectPoster(poster.id) }),
        color = MaterialTheme.colors.background,
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
        ) {
            val (image, title, content) = createRefs()

            ImageNetworkCoil(
                modifier = Modifier
                    .padding(8.dp)
                    .aspectRatio(0.8f)
                    .constrainAs(image) {
                        centerHorizontallyTo(parent)
                        top.linkTo(parent.top)
                    },
                url = poster.poster,
            )

            FavoriteButton(
                modifier = Modifier.padding(12.dp),
                poster = poster
            )

            Text(
                modifier = Modifier
                    .constrainAs(title) {
                        centerHorizontallyTo(parent)
                        top.linkTo(image.bottom)
                    }
                    .padding(8.dp),
                text = poster.name,
                style = MaterialTheme.typography.h2,
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

@Preview("Home screen")
@Preview("Home screen (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("Home screen (big font)", fontScale = 1.5f)
@Preview("Home screen (large screen)", device = Devices.PIXEL_C)
@Composable
private fun HomePosterPreviewDark() {
    ApplicationTheme(darkTheme = true) {
        HomePoster(
            poster = PosterLocal.instance()
        )
    }
}
