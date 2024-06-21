package com.alienspace.moviedb.details.presentation

import com.alienspace.moviedb.movielist.domain.model.Movie

data class DetailsState(
    val movie: Movie? = null,
    val isLoading: Boolean = false
)