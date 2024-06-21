package com.alienspace.moviedb.movielist.data.repo

import coil.network.HttpException
import com.alienspace.moviedb.movielist.data.local.MovieDatabase
import com.alienspace.moviedb.movielist.data.local.MovieEntity
import com.alienspace.moviedb.movielist.data.mappers.toMovie
import com.alienspace.moviedb.movielist.data.mappers.toMovieEntity
import com.alienspace.moviedb.movielist.data.remote.MovieAPI
import com.alienspace.moviedb.movielist.domain.model.Movie
import com.alienspace.moviedb.movielist.domain.repo.MovieListRepo
import com.alienspace.moviedb.movielist.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class MovieListRepoImpl @Inject constructor(
    private val movieAPI: MovieAPI,
    private val movieDatabase: MovieDatabase,
) : MovieListRepo {

    override suspend fun getMovieList(
        page: Int,
        category: String,
        forceFetchFomRemote: Boolean
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            val localMovieList = movieDatabase.movieDao.getMoviesByCategory(category)

            val shouldLoadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFomRemote
            if (shouldLoadLocalMovie) {
                emit(Resource.Success(
                    data = localMovieList.map { movieEntity ->
                        movieEntity.toMovie(category)
                    }
                ))

                emit(Resource.Loading(false))
                return@flow
            }

            val movieListFromAPI = try {
                movieAPI.getMovieList(category = category, page = page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error Loading Movies"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error Loading Movies"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error Loading Movies"))
                return@flow
            }

            val movieEntities = movieListFromAPI.results.let {
                it!!.map { movieDto ->
                    movieDto!!.toMovieEntity(category)

                }
            }

            movieDatabase.movieDao.upsertMovies(movieEntities)

            emit(Resource.Success(movieEntities.map {
                it.toMovie(category)
            }))
            emit(Resource.Loading(false))

        }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {

            emit(Resource.Loading(true))

            val movieEntity = movieDatabase.movieDao.getMoviesById(id)
            if (movieEntity!=null){
                emit(Resource.Success(movieEntity.toMovie(movieEntity.category)))
                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Error(message = "Error No Such Movie!"))
            emit(Resource.Loading(false))
            return@flow

        }
    }
}