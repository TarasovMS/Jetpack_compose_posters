package com.tarasovms.application.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.material.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tarasovms.application.ui.details.DetailScreen
import com.tarasovms.application.ui.posters.Posters
import com.tarasovms.application.ui.theme.ApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            ApplicationTheme {
                Scaffold() {
                    NavigationComponent(navController)
                }
            }
        }
    }
}

@Composable
fun NavigationComponent(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavScreen.Home.route
    ) {
        composable(
            NavScreen.Home.route,
        ) {
            Posters(
                viewModel = hiltViewModel(),
                selectPoster = {
                    navController.navigate("${NavScreen.PosterDetails.route}/$it")
                }
            )
        }
        composable(NavScreen.Favorites.route) {
            Posters(
                viewModel = hiltViewModel(),
                selectPoster = {
                    navController.navigate("${NavScreen.PosterDetails.route}/$it")
                }
            )
        }
        composable(
            route = NavScreen.PosterDetails.routeWithArgument,
            arguments = listOf(
                navArgument(NavScreen.PosterDetails.argument) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val posterId = backStackEntry.arguments?.getLong(NavScreen.PosterDetails.argument)
                    ?: return@composable

            DetailScreen(posterId = posterId, viewModel = hiltViewModel()) {
                navController.navigateUp()
            }
        }
    }
}

sealed class NavScreen(val route: String) {

    object Home : NavScreen("Home")
    object Favorites : NavScreen("Favorites")

    object PosterDetails : NavScreen("PosterDetails") {
        const val routeWithArgument: String = "PosterDetails/{posterId}"
        const val argument: String = "posterId"
    }
}
