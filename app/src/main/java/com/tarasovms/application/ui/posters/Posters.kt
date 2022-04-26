package com.tarasovms.application.ui.posters


import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.tarasovms.application.R
import com.tarasovms.application.data.db.PosterLocal
import com.tarasovms.application.ui.MainViewModel
import com.tarasovms.application.ui.details.DetailViewModel
import com.tarasovms.application.ui.favorites.FavoritesPosters
import com.tarasovms.application.utils.visible

@Composable
fun Posters(
    viewModel: MainViewModel,
    selectPoster: (Long) -> Unit
) {
    val posters: List<PosterLocal> by viewModel.postersLocal.collectAsState(initial = listOf())
    val postersLike: List<PosterLocal?> by viewModel.postersLocalLike.collectAsState(initial = listOf())
    val isLoading: Boolean by viewModel.isLoading.collectAsState(initial = true)
    val selectedTab = PostersTab.getTabFromResource(viewModel.selectedTab.value)
    val tabs = PostersTab.values()

    ConstraintLayout {
        val (body, progress) = createRefs()
        Scaffold(
            backgroundColor = MaterialTheme.colors.primarySurface,
            topBar = { PosterAppBar(stringResource(id = selectedTab.title)) },
            modifier = Modifier.constrainAs(body) {
                top.linkTo(parent.top)
            },
            bottomBar = {
                BottomNavigation(
                    backgroundColor = MaterialTheme.colors.background,
                    modifier = Modifier
                ) {
                    tabs.forEach { tab ->
                        BottomNavigationItem(
                            icon = { Icon(
                                imageVector = tab.icon,
                                contentDescription = null
                            ) },
                            label = { Text(
                                text = stringResource(tab.title),
                                color = MaterialTheme.colors.onBackground
                            ) },
                            selected = tab == selectedTab,
                            onClick = { viewModel.selectTab(tab.title) },
                            selectedContentColor = LocalContentColor.current,
                            unselectedContentColor = LocalContentColor.current
                        )
                    }
                }
            }
        ) { innerPadding ->
            val modifier = Modifier.padding(innerPadding)

            Crossfade(selectedTab) { destination ->
                when (destination) {
                    PostersTab.HOME -> HomePosters(modifier, posters, selectPoster)
                    PostersTab.FAVORITE -> FavoritesPosters(modifier, postersLike, selectPoster)
                }
            }
        }

        CircularProgressIndicator(
            modifier = Modifier
                .constrainAs(progress) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .visible(isLoading)
        )
    }
}

@Composable
fun PostersEmpty() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) { Text(text = stringResource(id = R.string.empty_favorites)) }
}


@Composable
fun FavoriteButton(
    poster: PosterLocal,
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
    viewModel: DetailViewModel = hiltViewModel()
) {
    var isFavorite by remember { mutableStateOf(poster.like) }

    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            isFavorite = !isFavorite
            viewModel.updatePosterLike(
                PosterLocal(
                    id = poster.id,
                    name = poster.name,
                    description = poster.description,
                    release = poster.release,
                    playtime = poster.playtime,
                    plot = poster.plot,
                    poster = poster.poster,
                    like = isFavorite
                )
            )
        }
    ) {
        Icon(
            tint = color,
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = null
        )
    }
}


@Preview
@Composable
fun PosterAppBar(
    name: String = stringResource(id = R.string.poster),
    visibleButton: Boolean = false,
    pressOnBack: () -> Unit = {}
) {
    TopAppBar(
        elevation = 6.dp,
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        if (visibleButton)
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            tint = MaterialTheme.colors.onBackground,
            contentDescription = null,
            modifier = Modifier
                .clickable(onClick = { pressOnBack.invoke() })
        )

        Text(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically),
            text = name,
            color = MaterialTheme.colors.onBackground,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

enum class PostersTab(
    @StringRes val title: Int,
    val icon: ImageVector
) {
    HOME(R.string.poster, Icons.Filled.List),
    FAVORITE(R.string.favorites, Icons.Filled.Favorite);

    companion object {
        fun getTabFromResource(@StringRes resource: Int): PostersTab {
            return when (resource) {
                R.string.favorites -> FAVORITE
                else -> HOME
            }
        }
    }
}
