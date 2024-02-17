package com.application.tmdb.core.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.application.tmdb.core.domain.interfaces.IMoviesRepository
import com.application.tmdb.core.domain.model.DetailModel
import com.application.tmdb.core.domain.model.MovieTvShowModel
import com.application.tmdb.common.DataMapper.toDetailMovie
import com.application.tmdb.common.DataMapper.toMovie
import com.application.tmdb.common.Movie
import com.application.tmdb.common.Page
import com.application.tmdb.core.source.remote.RemoteDataSource
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
        movie: com.application.tmdb.common.Movie?,
        page: com.application.tmdb.common.Page?,
        query: String?,
        movieId: Int?
    ): Flowable<PagingData<MovieTvShowModel>> =
        remoteDataSource.getMovies(movie, page, query, movieId).map { pagingData ->
            pagingData.map { data ->
                data.toMovie()
            }
        }
}