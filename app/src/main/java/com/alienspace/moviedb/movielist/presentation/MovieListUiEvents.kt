package com.alienspace.moviedb.movielist.presentation

import com.alienspace.moviedb.movielist.domain.model.Movie

sealed interface MovieListUiEvents {

    data class Paginate(val category: String) : MovieListUiEvents
    object Navigate : MovieListUiEvents


}