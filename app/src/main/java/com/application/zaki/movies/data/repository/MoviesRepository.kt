package com.application.zaki.movies.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.application.zaki.movies.data.source.remote.RemoteDataSource
import com.application.zaki.movies.domain.interfaces.IMoviesRepository
import com.application.zaki.movies.domain.model.DetailModel
import com.application.zaki.movies.domain.model.MovieTvShowModel
import com.application.zaki.movies.utils.DataMapper.toDetailMovie
import com.application.zaki.movies.utils.DataMapper.toMovie
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
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