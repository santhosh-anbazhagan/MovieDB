package com.alienspace.moviedb.movielist.domain.repo

import com.alienspace.moviedb.movielist.domain.model.Movie
import com.alienspace.moviedb.movielist.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MovieListRepo {

    suspend fun getMovieList(
        page:Int,
        category:String,
        forceFetchFomRemote:Boolean
    ):Flow<Resource<List<Movie>>>

    suspend fun getMovie(id:Int):Flow<Resource<Movie>>
}