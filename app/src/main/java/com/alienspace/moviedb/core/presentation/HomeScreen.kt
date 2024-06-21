package com.alienspace.moviedb.core.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alienspace.moviedb.movielist.utils.Screen
import com.alienspace.moviedb.movielist.presentation.MovieListUiEvents
import com.alienspace.moviedb.movielist.presentation.MovieListViewModel
import com.alienspace.moviedb.movielist.presentation.PopularMoviesScreen
import com.alienspace.moviedb.movielist.presentation.UpcomingMoviesScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {


    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    val movieState = movieListViewModel.movieListState.collectAsState().value

    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation(
                bottomNavController = bottomNavController,
                onEvent = movieListViewModel::onEvent
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = if (movieState.isCurrentPopularScreen) "Popular Movies" else "Upcoming Movies")
                },
                modifier = Modifier.shadow(2.dp),
                colors = topAppBarColors(
                    MaterialTheme.colorScheme.inverseOnSurface,
                )
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            NavHost(
                navController = bottomNavController,
                startDestination = Screen.PopularMovieList.rout
            ) {

                composable(Screen.PopularMovieList.rout) {
                    PopularMoviesScreen(
                        navController,
                        movieState,
                        onEvent = movieListViewModel::onEvent
                    )
                }
                composable(Screen.UpcomingMovieList.rout) {
                    UpcomingMoviesScreen(
                        navController,
                        movieState,
                        onEvent = movieListViewModel::onEvent
                    )
                }
            }
        }

    }
}

@Composable
fun BottomNavigation(bottomNavController: NavHostController, onEvent: (MovieListUiEvents) -> Unit) {

    val items = listOf(
        BottomItem(
            title = "Popular",
            icon = Icons.Rounded.Movie
        ),
        BottomItem(
            title = "Upcoming",
            icon = Icons.Rounded.Upcoming
        )
    )

    val selected = rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar {
        Row(modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)) {

            items.forEachIndexed { index, botemItem ->
                NavigationBarItem(
                    selected = selected.intValue == index,
                    onClick = {
                        Log.d("NavigationBar", "BottomNavigation: selected Index : $index")
                        selected.intValue = index
                        try {
                            when (selected.intValue) {
                                0 -> {
                                    onEvent(MovieListUiEvents.Navigate)
                                    bottomNavController.popBackStack()
                                    bottomNavController.navigate(Screen.PopularMovieList.rout)
                                }

                                1 -> {
                                    onEvent(MovieListUiEvents.Navigate)
                                    bottomNavController.popBackStack()
                                    bottomNavController.navigate(Screen.UpcomingMovieList.rout)
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    },
                    icon = {
                        Icon(
                            imageVector = botemItem.icon,
                            contentDescription = botemItem.title,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = {
                        Text(
                            text = botemItem.title,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    })

            }
        }
    }
}

data class BottomItem(
    val title: String,
    val icon: ImageVector
)