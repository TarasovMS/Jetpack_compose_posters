package com.tarasovms.application.ui.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tarasovms.application.R
import com.tarasovms.application.utils.ImageNetworkCoil
import com.tarasovms.application.data.db.PosterLocal
import com.tarasovms.application.ui.posters.FavoriteButton
import com.tarasovms.application.ui.posters.PosterAppBar

@Composable
fun DetailScreen(
    posterId: Long,
    viewModel: DetailViewModel,
    pressOnBack: () -> Unit = {}
) {
    LaunchedEffect(key1 = posterId) {
        viewModel.loadPosterById(posterId)
    }

    val details: PosterLocal? by viewModel.posterDetailsByIdFlow.collectAsState(initial = null)

    details?.let { poster ->
        DetailsScreenBody(poster, pressOnBack)
    }
}

@Composable
fun DetailsScreenBody(
    poster: PosterLocal,
    pressOnBack: () -> Unit
) {
    var visibleText: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colors.background)
            .fillMaxHeight()
    ) {

        PosterAppBar(
            name = poster.name,
            visibleButton = true,
            pressOnBack = pressOnBack
        )

        ConstraintLayout(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
            val (image, title, description) = createRefs()

            ImageNetworkCoil(
                url = poster.poster,
                circularRevealEnabled = false,
                modifier = Modifier
                    .aspectRatio(0.8f)
                    .constrainAs(image) {
                        centerHorizontallyTo(parent)
                        top.linkTo(parent.top)
                    }
            )

            FavoriteButton(poster = poster)

            Text(
                text = poster.name,
                style = MaterialTheme.typography.subtitle2,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .padding(start = 16.dp, top = 12.dp)
                    .constrainAs(title) {
                        top.linkTo(image.bottom)
                    },
                color = MaterialTheme.colors.onBackground
            )

            Text(
                text = poster.description,
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .padding(16.dp)
                    .constrainAs(description) {
                        top.linkTo(title.bottom)
                    },
                color = MaterialTheme.colors.onBackground
            )
        }

        AnimatedVisibility(visible = visibleText) {
            Text(
                text = poster.plot,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .padding(16.dp),
                color = MaterialTheme.colors.onBackground
            )
        }

        OutlinedButton(
            onClick = { visibleText = !visibleText },
            modifier = Modifier
                .fillMaxWidth()
                .align(CenterHorizontally)
                .padding(16.dp)
        ) {
            Text(
                text = if (visibleText) stringResource(id = R.string.hide_plot)
                else stringResource(id = R.string.show_plot)
            )
        }
    }
}
