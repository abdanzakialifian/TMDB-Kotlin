package com.application.zaki.movies.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.application.zaki.movies.data.source.remote.RemoteDataSource
import com.application.zaki.movies.domain.interfaces.IMoviesRepository
import com.application.zaki.movies.domain.model.movies.*
import com.application.zaki.movies.utils.DataMapperMovies
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    IMoviesRepository {
    override fun getNowPlayingMovies(): Flowable<NowPlayingMovies> =
        remoteDataSource.getNowPlayingMovies()
            .map { data ->
                DataMapperMovies.mapNowPlayingMoviesResponseToNowPlayingMovies(data)
            }

    override fun getDetailMovies(movieId: String): Flowable<DetailMovies> =
        remoteDataSource.getDetailMovies(movieId)
            .map { data ->
                DataMapperMovies.mapDetailMovieResponseToDetailMovie(data)
            }

    override fun getPopularMoviesPaging(type: String, totalPage: String): Flowable<PagingData<ListPopularMovies>> =
        Flowable.zip(
            remoteDataSource.getPopularMoviesPaging(totalPage),
            remoteDataSource.getGenre(type)
        ) { popularMoviesPaging, genreMovies ->
            return@zip popularMoviesPaging.map { data ->
                DataMapperMovies.mapListPopularMoviesResponseToListPopularMovies(data, genreMovies)
            }
        }

    override fun getTopRatedMoviesPaging(type: String, totalPage: String): Flowable<PagingData<ListTopRatedMovies>> =
        Flowable.zip(
            remoteDataSource.getTopRatedMoviesPaging(totalPage),
            remoteDataSource.getGenre(type)
        ) { topRatedMoviesPaging, genreMovies ->
            return@zip topRatedMoviesPaging.map { data ->
                DataMapperMovies.mapListTopRatedMoviesResponseToListTopRatedMovies(
                    data,
                    genreMovies
                )
            }
        }

    override fun getUpComingMoviesPaging(type: String, totalPage: String): Flowable<PagingData<ListUpComingMovies>> =
        Flowable.zip(
            remoteDataSource.getUpComingMoviesPaging(totalPage),
            remoteDataSource.getGenre(type)
        ) { upComingMoviesPaging, genreMovies ->
            return@zip upComingMoviesPaging.map { data ->
                DataMapperMovies.mapListUpComingMoviesResponseToListUpComingMovies(
                    data,
                    genreMovies
                )
            }
        }
}