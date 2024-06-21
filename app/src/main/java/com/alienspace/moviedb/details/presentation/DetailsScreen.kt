package com.alienspace.moviedb.details.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.alienspace.moviedb.movielist.data.remote.MovieAPI
import com.alienspace.moviedb.movielist.utils.RatingBar

@Composable
fun DetailsScreen(backStackEntry: NavBackStackEntry) {

    val detailsViewModel = hiltViewModel<DetailsViewModel>()
    val detailsState = detailsViewModel.detailsState.collectAsState().value

    val backDropImageState = rememberAsyncImagePainter(
        model =
        ImageRequest.Builder(LocalContext.current)
            .data(MovieAPI.IMAGE_BASE_URL + detailsState.movie?.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    )
    val posterImageState = rememberAsyncImagePainter(
        model =
        ImageRequest.Builder(LocalContext.current)
            .data(MovieAPI.IMAGE_BASE_URL + detailsState.movie?.poster_path)
            .size(Size.ORIGINAL)
            .build()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (backDropImageState.state is AsyncImagePainter.State.Error) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .height(250.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(70.dp),
                    imageVector = Icons.Rounded.ImageNotSupported,
                    contentDescription = detailsState.movie?.title
                )
            }
        }
        if (backDropImageState.state is AsyncImagePainter.State.Success) {

            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .height(250.dp),
                painter = backDropImageState,
                contentDescription = detailsState.movie?.title,
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(240.dp)
            ) {

                if (posterImageState.state is AsyncImagePainter.State.Error) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(250.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(70.dp),
                            imageVector = Icons.Rounded.ImageNotSupported,
                            contentDescription = detailsState.movie?.title
                        )
                    }
                }

                if (posterImageState.state is AsyncImagePainter.State.Success) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp)),
                        painter = posterImageState,
                        contentDescription = detailsState.movie?.title,
                        contentScale = ContentScale.Crop
                    )
                }


            }

            detailsState.movie?.let { movie ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp)
                ) {

                    Text(
                        text = movie.title,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Row() {
                        RatingBar(
                            starsModifier = Modifier.size(20.dp),
                            rating = movie.vote_average / 2,
                        )
                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = movie.vote_average.toString().take(3),
                            color = Color.LightGray,
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Language : ${movie.original_language}",)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Release Date : ${movie.release_date}")
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "${movie.vote_count} Votes")

                }
            }


        }

        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.padding(start = 14.dp)) {

            Text(
                text = "OVERVIEW :",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = detailsState.movie?.overview ?: "",
                fontSize = 16.sp,
            )
        }

    }

}