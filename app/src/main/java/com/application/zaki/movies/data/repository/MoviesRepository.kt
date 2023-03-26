package com.application.zaki.movies.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.application.zaki.movies.data.source.remote.RemoteDataSource
import com.application.zaki.movies.domain.interfaces.IMoviesRepository
import com.application.zaki.movies.domain.model.genre.Genres
import com.application.zaki.movies.domain.model.movies.DetailMovies
import com.application.zaki.movies.domain.model.movies.ListMovies
import com.application.zaki.movies.utils.DataMapperMovies
import com.application.zaki.movies.utils.DataMapperMovies.toGenreItemMovies
import com.application.zaki.movies.utils.DataMapperMovies.toListMovies
import com.application.zaki.movies.utils.Genre
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
            DataMapperMovies.mapDetailMovieResponseToDetailMovie(data)
        }

    override fun getGenres(genre: Genre): Flowable<Genres> = remoteDataSource.getGenre(genre)
        .map {
            it.toGenreItemMovies()
        }

    override fun getMovies(movie: Movie, page: Page): Flowable<PagingData<ListMovies>> =
        remoteDataSource.getMovies(movie, page)
            .map { pagingData ->
                pagingData.map {
                    it.toListMovies()
                }
            }
}