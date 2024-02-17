package com.application.tmdb.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.application.tmdb.data.source.remote.RemoteDataSource
import com.application.tmdb.domain.interfaces.IMoviesRepository
import com.application.tmdb.domain.model.DetailModel
import com.application.tmdb.domain.model.MovieTvShowModel
import com.application.tmdb.utils.DataMapper.toDetailMovie
import com.application.tmdb.utils.DataMapper.toMovie
import com.application.tmdb.utils.Movie
import com.application.tmdb.utils.Page
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    IMoviesRepository {
    override fun getDetailMovies(movieId: String): Flowable<DetailModel> =
        remoteDataSource.getDetailMovies(movieId).map { data ->
            data.toDetailMovie()
        }

    override fun getMovies(
        movie: Movie?,
        page: Page?,
        query: String?,
        movieId: Int?
    ): Flowable<PagingData<MovieTvShowModel>> =
        remoteDataSource.getMovies(movie, page, query, movieId).map { pagingData ->
            pagingData.map { data ->
                data.toMovie()
            }
        }
}