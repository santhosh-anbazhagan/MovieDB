package com.alienspace.moviedb.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alienspace.moviedb.movielist.data.repo.MovieListRepoImpl
import com.alienspace.moviedb.movielist.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val movieListRepoImpl: MovieListRepoImpl,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _detailsState = MutableStateFlow(DetailsState())
    val detailsState = _detailsState.asStateFlow()
    private val movieId = savedStateHandle.get<Int>("movieId")

    init {
        getMovie(movieId)
    }

    private fun getMovie(movieId: Int?) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(isLoading = true)
            }
            movieListRepoImpl.getMovie(movieId!!).collectLatest { result ->

                when (result) {
                    is Resource.Error -> {
                        _detailsState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        _detailsState.update {
                            it.copy(isLoading = result.isLoading)
                        }

                    }

                    is Resource.Success -> {
                        result.data?.let {movie->
                            _detailsState.update {
                                it.copy(movie = movie)
                            }
                        }

                    }
                }

            }
        }

    }
}