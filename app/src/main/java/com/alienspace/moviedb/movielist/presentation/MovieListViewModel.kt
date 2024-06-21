package com.alienspace.moviedb.movielist.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.isTraceInProgress
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alienspace.moviedb.movielist.domain.repo.MovieListRepo
import com.alienspace.moviedb.movielist.utils.Category
import com.alienspace.moviedb.movielist.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val movieListRepo: MovieListRepo) :
    ViewModel() {

        companion object{
            private const val TAG = "MovieListViewModel"
        }

    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()

    init {
        Log.d(TAG, "viewmodel init initialized....: ")
        getPopularMovieList(false)
        getUpcomingMovieList(false)
    }

    fun onEvent(event: MovieListUiEvents) {
        when (event) {
            is MovieListUiEvents.Paginate -> {
                if (event.category == Category.POPULAR) {
                    getPopularMovieList(true)
                } else if (event.category == Category.UPCOMING) {
                    getUpcomingMovieList(true)
                }
            }

            MovieListUiEvents.Navigate -> {
                _movieListState.update {
                    it.copy(
                        isCurrentPopularScreen = !movieListState.value.isCurrentPopularScreen
                    )
                }
            }
        }
    }

    private fun getUpcomingMovieList(forceFetchFomRemote: Boolean) {
        viewModelScope.launch {
            /*setting up the loading State*/
            _movieListState.update {
                it.copy(isLoading = true)
            }
            /*fetch data from local or api*/
            movieListRepo.getMovieList(
                page = movieListState.value.upComingMovieListPage,
                category = Category.UPCOMING, forceFetchFomRemote = forceFetchFomRemote
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieListState.update { it.copy(isLoading = false) }
                    }

                    is Resource.Loading -> {
                        _movieListState.update { it.copy(isLoading = result.isLoading) }

                    }

                    is Resource.Success -> {
                        result.data?.let { upcomingMovieList ->
                            _movieListState.update {
                                it.copy(
                                    upcomingMovieList = movieListState.value.upcomingMovieList + upcomingMovieList.shuffled(),
                                    upComingMovieListPage = movieListState.value.upComingMovieListPage + 1,
                                )
                            }
                        }
                        Log.d(TAG, "getUpcomingMovieList: ${movieListState.value.upcomingMovieList.size}")


                    }
                }
            }
        }

    }

    private fun getPopularMovieList(forceFetchFomRemote: Boolean) {
        viewModelScope.launch {
            /*setting up the loading State*/
            _movieListState.update {
                it.copy(isLoading = true)
            }

            /*fetch data from local or api*/
            movieListRepo.getMovieList(
                forceFetchFomRemote = forceFetchFomRemote,
                category = Category.POPULAR,
                page = movieListState.value.popularMovieListPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Success -> {

                        result.data?.let { popularMovieList ->
                            _movieListState.update {
                                it.copy(
                                    popularMovieList = movieListState.value.popularMovieList +
                                            popularMovieList.shuffled(),
                                    popularMovieListPage = movieListState.value.popularMovieListPage + 1
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}