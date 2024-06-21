package com.alienspace.moviedb.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alienspace.moviedb.details.presentation.DetailsScreen
import com.alienspace.moviedb.movielist.utils.Screen
import com.alienspace.moviedb.ui.theme.MovieDbTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            MovieDbTheme(darkTheme = isSystemInDarkTheme(), dynamicColor = true) {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetBarColor(color = MaterialTheme.colorScheme.inverseOnSurface)

                    /*Defining the Navigation Host and Controller*/
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController, startDestination =
                        Screen.Home.rout
                    ) {
                        /*Home Screen*/
                        composable(route = Screen.Home.rout) {
                            HomeScreen(navController)
                        }
                        /*Popular Movie Screen*/
                        composable(route = Screen.Details.rout + "/{movieId}",
                            arguments = listOf(
                                navArgument("movieId") {
                                    type = NavType.IntType
                                }
                            )
                        ) { backStackEntry ->
                            DetailsScreen(backStackEntry)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SetBarColor(color: Color) {
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(key1 = color) {
        systemUiController.setSystemBarsColor(color, darkIcons = false,
            isNavigationBarContrastEnforced = false)
        systemUiController.statusBarDarkContentEnabled = true
    }
}
