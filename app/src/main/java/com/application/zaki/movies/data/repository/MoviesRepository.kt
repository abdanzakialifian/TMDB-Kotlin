package com.application.zaki.movies.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.paging.rxjava2.flowable
import com.application.zaki.movies.data.source.remote.RemoteDataSource
import com.application.zaki.movies.data.source.remote.paging.movies.DiscoverMoviesRxPagingSource
import com.application.zaki.movies.data.source.remote.paging.movies.PopularMoviesRxPagingSource
import com.application.zaki.movies.data.source.remote.paging.movies.TopRatedMoviesRxPagingSource
import com.application.zaki.movies.data.source.remote.paging.movies.UpComingMoviesRxPagingSource
import com.application.zaki.movies.domain.interfaces.IMoviesRepository
import com.application.zaki.movies.domain.model.movies.*
import com.application.zaki.movies.utils.DataMapperMovies
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.UiState
import com.application.zaki.movies.utils.addToDisposer
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val popularMoviesRxPagingSource: PopularMoviesRxPagingSource,
    private val topRatedMoviesRxPagingSource: TopRatedMoviesRxPagingSource,
    private val upComingMoviesRxPagingSource: UpComingMoviesRxPagingSource,
    private val discoverMoviesRxPagingSource: DiscoverMoviesRxPagingSource
) : IMoviesRepository {

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

    override fun getDetailMovies(
        rxDisposer: RxDisposer,
        movieId: String,
    ): Flowable<UiState<DetailMovies>> {
        val result = ReplaySubject.create<UiState<DetailMovies>>()

        result.onNext(UiState.Loading(null))

        remoteDataSource.getDetailMovies(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { value ->
                DataMapperMovies.mapDetailMovieResponseToDetailMovie(value)
            }
            .subscribe(
                { value ->
                    if (value != null) {
                        result.onNext(UiState.Success(value))
                    } else {
                        result.onNext(UiState.Empty)
                    }
                },
                { throwable ->
                    result.onNext(UiState.Error(throwable.message.toString()))
                }
            )
            .addToDisposer(rxDisposer)

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun getPopularMoviesPaging(): Flowable<PagingData<ListPopularMovies>> {
        val listPopularMoviesPaging = Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                maxSize = 24,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                popularMoviesRxPagingSource
            }
        ).flowable
        val listGenreMovies = remoteDataSource.getGenreMovies()
        // combine two api into one result
        return Flowable.zip(
            listPopularMoviesPaging,
            listGenreMovies
        ) { popularMoviesPaging, genreMovies ->
            return@zip popularMoviesPaging.map { map ->
                DataMapperMovies.mapListPopularMoviesResponseToListPopularMovies(map, genreMovies)
            }
        }
    }

    override fun getTopRatedMoviesPaging(): Flowable<PagingData<ListTopRatedMovies>> {
        val listTopRatingMoviesPaging = Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                maxSize = 24,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                topRatedMoviesRxPagingSource
            }
        ).flowable
        val listGenreMovies = remoteDataSource.getGenreMovies()

        // combine two api into one result
        return Flowable.zip(
            listTopRatingMoviesPaging,
            listGenreMovies
        ) { topRatedMoviesPaging, genreMovies ->
            return@zip topRatedMoviesPaging.map { map ->
                DataMapperMovies.mapListTopRatedMoviesResponseToListTopRatedMovies(
                    map,
                    genreMovies
                )
            }
        }
    }

    override fun getUpComingMoviesPaging(): Flowable<PagingData<ListUpComingMovies>> {
        val listUpComingMoviesPaging = Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                maxSize = 24,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                upComingMoviesRxPagingSource
            }
        ).flowable
        val listGenreMovies = remoteDataSource.getGenreMovies()

        return Flowable.zip(
            listUpComingMoviesPaging,
            listGenreMovies
        ) { upComingMoviesPaging, genreMovies ->
            return@zip upComingMoviesPaging.map { map ->
                DataMapperMovies.mapListUpComingMoviesResponseToListUpComingMovies(map, genreMovies)
            }
        }
    }

    override fun getReviewsMoviePaging(
        rxDisposer: RxDisposer,
        movieId: String
    ): Flowable<UiState<ReviewsMovie>> {
        val result = ReplaySubject.create<UiState<ReviewsMovie>>()

        result.onNext(UiState.Loading(null))

        remoteDataSource.getReviews(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { data ->
                DataMapperMovies.mapReviewMovieResponseToReviewMovie(data)
            }
            .subscribe(
                { value ->
                    if (value != null) {
                        result.onNext(UiState.Success(value))
                    } else {
                        result.onNext(UiState.Empty)
                    }
                },
                { throwable ->
                    result.onNext(UiState.Error(throwable.message.toString()))
                }
            )
            .addToDisposer(rxDisposer)

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun getDiscoverMovies(
        rxDisposer: RxDisposer,
        genreId: String
    ): Flowable<UiState<PagingData<ResultsItemDiscover>>> {
        val result = ReplaySubject.create<UiState<PagingData<ResultsItemDiscover>>>()

        val listDiscoverMoviesPaging = Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                maxSize = 24,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                discoverMoviesRxPagingSource.apply {
                    getGenreId(genreId)
                }
            }
        ).flowable

        result.onNext(UiState.Loading(null))

        listDiscoverMoviesPaging
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.map { map ->
                    DataMapperMovies.mapResultItemDiscoverResponseToResultItemDiscover(map)
                }
            }
            .subscribe(
                { value ->
                    if (value != null) {
                        result.onNext(UiState.Success(value))
                    } else {
                        result.onNext(UiState.Empty)
                    }
                },
                { throwable ->
                    result.onNext(UiState.Error(throwable.message.toString()))
                }
            )
            .addToDisposer(rxDisposer)

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }
}