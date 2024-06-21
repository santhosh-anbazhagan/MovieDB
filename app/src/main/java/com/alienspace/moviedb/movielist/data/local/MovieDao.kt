package com.alienspace.moviedb.movielist.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert


@Dao
interface MovieDao {

    @Upsert
    suspend fun upsertMovies(movieList : List<MovieEntity>)

    @Query("select * from  movie_table where id = :id")
    suspend fun getMoviesById(id:Int):MovieEntity

    @Query("select * from  movie_table where category = :category")
    suspend fun getMoviesByCategory(category:String):List<MovieEntity>

}