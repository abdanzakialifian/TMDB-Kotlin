package com.application.zaki.movies.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.application.zaki.movies.data.source.remote.RemoteDataSource
import com.application.zaki.movies.domain.interfaces.IMoviesRepository
import com.application.zaki.movies.domain.model.other.Genres
import com.application.zaki.movies.domain.model.movies.DetailMovies
import com.application.zaki.movies.domain.model.movies.ListMovies
import com.application.zaki.movies.utils.DataMapperMovies.toDetailMovie
import com.application.zaki.movies.utils.DataMapperMovies.toListMovies
import com.application.zaki.movies.utils.DataMapperOther.toGenres
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    IMoviesRepository {
    override fun getDetailMovies(movieId: String): Flowable<DetailMovies> =
        remoteDataSource.getDetailMovies(movieId).map { data ->
            data.toDetailMovie()
        }

    override fun getGenres(category: Category): Flowable<Genres> =
        remoteDataSource.getGenre(category).map { data ->
            data.toGenres()
        }

    override fun getMovies(movie: Movie, page: Page): Flowable<PagingData<ListMovies>> =
        remoteDataSource.getMovies(movie, page).map { pagingData ->
            pagingData.map { data ->
                data.toListMovies()
            }
        }
}