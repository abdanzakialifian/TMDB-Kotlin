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

    override fun getTopRatedMovies(): Flowable<TopRatedMovies> =
        remoteDataSource.getTopRatedMovies()
            .map { data ->
                DataMapperMovies.mapTopRatedMoviesResponseToTopRatedMovies(data)
            }

    override fun getPopularMovies(): Flowable<PopularMovies> =
        remoteDataSource.getPopularMovies()
            .map { data ->
                DataMapperMovies.mapPopularMoviesResponseToPopularMovies(data)
            }

    override fun getUpComingMovies(): Flowable<UpComingMovies> =
        remoteDataSource.getUpComingMovies()
            .map { data ->
                DataMapperMovies.mapUpComingMoviesResponseToUpComingMovies(data)
            }

    override fun getDetailMovies(movieId: String): Flowable<DetailMovies> =
        remoteDataSource.getDetailMovies(movieId)
            .map { data ->
                DataMapperMovies.mapDetailMovieResponseToDetailMovie(data)
            }

    override fun getPopularMoviesPaging(): Flowable<PagingData<ListPopularMovies>> = Flowable.zip(
        remoteDataSource.getPopularMoviesPaging(),
        remoteDataSource.getGenreMovies()
    ) { popularMoviesPaging, genreMovies ->
        return@zip popularMoviesPaging.map { data ->
            DataMapperMovies.mapListPopularMoviesResponseToListPopularMovies(data, genreMovies)
        }
    }

    override fun getTopRatedMoviesPaging(): Flowable<PagingData<ListTopRatedMovies>> = Flowable.zip(
        remoteDataSource.getTopRatedMoviesPaging(),
        remoteDataSource.getGenreMovies()
    ) { topRatedMoviesPaging, genreMovies ->
        return@zip topRatedMoviesPaging.map { data ->
            DataMapperMovies.mapListTopRatedMoviesResponseToListTopRatedMovies(data, genreMovies)
        }
    }

    override fun getUpComingMoviesPaging(): Flowable<PagingData<ListUpComingMovies>> = Flowable.zip(
        remoteDataSource.getUpComingMoviesPaging(),
        remoteDataSource.getGenreMovies()
    ) { upComingMoviesPaging, genreMovies ->
        return@zip upComingMoviesPaging.map { data ->
            DataMapperMovies.mapListUpComingMoviesResponseToListUpComingMovies(data, genreMovies)
        }
    }

    override fun getReviewsMoviePaging(movieId: String): Flowable<PagingData<ReviewItem>> =
        remoteDataSource.getReviewsPaging(movieId)
            .map { pagingData ->
                pagingData.map {
                    DataMapperMovies.mapReviewMovieResponseToReviewMovie(it)
                }
            }

    override fun getDiscoverMovies(genreId: String): Flowable<PagingData<ResultsItemDiscover>> =
        remoteDataSource.getDiscoverMoviesPaging(genreId)
            .map { pagingData ->
                pagingData.map { map ->
                    DataMapperMovies.mapResultItemDiscoverResponseToResultItemDiscover(map)
                }
            }
}