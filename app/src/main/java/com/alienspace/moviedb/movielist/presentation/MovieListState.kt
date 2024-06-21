package com.alienspace.moviedb.movielist.presentation

import com.alienspace.moviedb.movielist.domain.model.Movie

data class MovieListState(

    val isLoading : Boolean = false,
    val popularMovieListPage:Int = 1,
    val upComingMovieListPage:Int = 1,

    val isCurrentPopularScreen : Boolean = true,

    val popularMovieList: List<Movie> = emptyList(),
    val upcomingMovieList: List<Movie> = emptyList()
)