package com.application.zaki.movies.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.application.zaki.movies.data.source.remote.RemoteDataSource
import com.application.zaki.movies.domain.interfaces.IMoviesRepository
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.domain.model.Detail
import com.application.zaki.movies.domain.model.Genres
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.DataMapper.toDetailMovie
import com.application.zaki.movies.utils.DataMapper.toGenres
import com.application.zaki.movies.utils.DataMapper.toMovie
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    IMoviesRepository {
    override fun getDetailMovies(movieId: String): Flowable<Detail> =
        remoteDataSource.getDetailMovies(movieId).map { data ->
            data.toDetailMovie()
        }

    override fun getGenres(category: Category): Flowable<Genres> =
        remoteDataSource.getGenre(category).map { data ->
            data.toGenres()
        }

    override fun getMovies(movie: Movie, page: Page): Flowable<PagingData<MovieTvShow>> =
        remoteDataSource.getMovies(movie, page).map { pagingData ->
            pagingData.map { data ->
                data.toMovie()
            }
        }
}