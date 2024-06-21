package com.alienspace.moviedb.di

import com.alienspace.moviedb.movielist.data.repo.MovieListRepoImpl
import com.alienspace.moviedb.movielist.domain.repo.MovieListRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    @Singleton
    abstract fun bindMovieListRepo(movieListRepoImpl: MovieListRepoImpl):MovieListRepo
}