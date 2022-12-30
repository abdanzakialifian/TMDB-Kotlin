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
import com.application.zaki.movies.utils.NetworkResult
import com.application.zaki.movies.utils.RxDisposer
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

    override fun getNowPlayingMovies(rxDisposer: RxDisposer): Flowable<NetworkResult<NowPlayingMovies>> {
        val result = ReplaySubject.create<NetworkResult<NowPlayingMovies>>()

        result.onNext(NetworkResult.Loading(null))
        remoteDataSource.getNowPlayingMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { value ->
                DataMapperMovies.mapNowPlayingMoviesResponseToNowPlayingMovies(value)
            }
            .subscribe(
                { value ->
                    if (value != null) {
                        result.onNext(NetworkResult.Success(value))
                    } else {
                        result.onNext(NetworkResult.Empty)
                    }
                }, { throwable ->
                    result.onNext(NetworkResult.Error(throwable.message.toString()))
                }
            )
            .addToDisposer(rxDisposer)
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun getTopRatedMovies(rxDisposer: RxDisposer): Flowable<NetworkResult<TopRatedMovies>> {
        val result = ReplaySubject.create<NetworkResult<TopRatedMovies>>()

        result.onNext(NetworkResult.Loading(null))
        remoteDataSource.getTopRatedMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { value ->
                DataMapperMovies.mapTopRatedMoviesResponseToTopRatedMovies(value)
            }
            .subscribe(
                { value ->
                    if (value != null) {
                        result.onNext(NetworkResult.Success(value))
                    } else {
                        result.onNext(NetworkResult.Empty)
                    }
                },
                { throwable ->
                    result.onNext(NetworkResult.Error(throwable.message.toString()))
                }
            )
            .addToDisposer(rxDisposer)
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun getPopularMovies(rxDisposer: RxDisposer): Flowable<NetworkResult<PopularMovies>> {
        // using replay subject so that loading is included emit
        val result = ReplaySubject.create<NetworkResult<PopularMovies>>()

        result.onNext(NetworkResult.Loading(null))
        remoteDataSource.getPopularMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { value ->
                DataMapperMovies.mapPopularMoviesResponseToPopularMovies(value)
            }
            .subscribe(
                { value ->
                    if (value != null) {
                        result.onNext(NetworkResult.Success(value))
                    } else {
                        result.onNext(NetworkResult.Empty)
                    }
                }, { throwable ->
                    result.onNext(NetworkResult.Error(throwable.message.toString()))
                }
            )
            .addToDisposer(rxDisposer)
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun getUpComingMovies(rxDisposer: RxDisposer): Flowable<NetworkResult<UpComingMovies>> {
        val result = ReplaySubject.create<NetworkResult<UpComingMovies>>()

        result.onNext(NetworkResult.Loading(null))

        remoteDataSource.getUpComingMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { value ->
                DataMapperMovies.mapUpComingMoviesResponseToUpComingMovies(value)
            }
            .subscribe(
                { value ->
                    if (value != null) {
                        result.onNext(NetworkResult.Success(value))
                    } else {
                        result.onNext(NetworkResult.Empty)
                    }
                },
                { throwable ->
                    result.onNext(NetworkResult.Error(throwable.message.toString()))
                }
            )
            .addToDisposer(rxDisposer)

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun getDetailMovies(
        rxDisposer: RxDisposer,
        movieId: String,
    ): Flowable<NetworkResult<DetailMovies>> {
        val result = ReplaySubject.create<NetworkResult<DetailMovies>>()

        result.onNext(NetworkResult.Loading(null))

        remoteDataSource.getDetailMovies(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { value ->
                DataMapperMovies.mapDetailMovieResponseToDetailMovie(value)
            }
            .subscribe(
                { value ->
                    if (value != null) {
                        result.onNext(NetworkResult.Success(value))
                    } else {
                        result.onNext(NetworkResult.Empty)
                    }
                },
                { throwable ->
                    result.onNext(NetworkResult.Error(throwable.message.toString()))
                }
            )
            .addToDisposer(rxDisposer)

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun getPopularMoviesPaging(rxDisposer: RxDisposer): Flowable<NetworkResult<PagingData<ListPopularMovies>>> {
        val result = ReplaySubject.create<NetworkResult<PagingData<ListPopularMovies>>>()

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

        result.onNext(NetworkResult.Loading(null))
        // combine two api into one result
        Flowable.zip(listPopularMoviesPaging, listGenreMovies) { popularMoviesPaging, genreMovies ->
            return@zip popularMoviesPaging.map { map ->
                DataMapperMovies.mapListPopularMoviesResponseToListPopularMovies(map, genreMovies)
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { value ->
                    if (value != null) {
                        result.onNext(NetworkResult.Success(value))
                    } else {
                        result.onNext(NetworkResult.Empty)
                    }
                },
                { throwable ->
                    result.onNext(NetworkResult.Error(throwable.message.toString()))
                }
            )
            .addToDisposer(rxDisposer)
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun getTopRatedMoviesPaging(rxDisposer: RxDisposer): Flowable<NetworkResult<PagingData<ListTopRatedMovies>>> {
        val result = ReplaySubject.create<NetworkResult<PagingData<ListTopRatedMovies>>>()

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

        result.onNext(NetworkResult.Loading(null))
        // combine two api into one result
        Flowable.zip(
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
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { value ->
                    if (value != null) {
                        result.onNext(NetworkResult.Success(value))
                    } else {
                        result.onNext(NetworkResult.Empty)
                    }
                },
                { throwable ->
                    result.onNext(NetworkResult.Error(throwable.message.toString()))
                }
            )
            .addToDisposer(rxDisposer)

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun getUpComingMoviesPaging(rxDisposer: RxDisposer): Flowable<NetworkResult<PagingData<ListUpComingMovies>>> {
        val result = ReplaySubject.create<NetworkResult<PagingData<ListUpComingMovies>>>()

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

        result.onNext(NetworkResult.Loading(null))
        Flowable.zip(
            listUpComingMoviesPaging,
            listGenreMovies
        ) { upComingMoviesPaging, genreMovies ->
            return@zip upComingMoviesPaging.map { map ->
                DataMapperMovies.mapListUpComingMoviesResponseToListUpComingMovies(map, genreMovies)
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { value ->
                    if (value != null) {
                        result.onNext(NetworkResult.Success(value))
                    } else {
                        result.onNext(NetworkResult.Empty)
                    }
                },
                { throwable ->
                    result.onNext(NetworkResult.Error(throwable.message.toString()))
                }
            )
            .addToDisposer(rxDisposer)

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun getReviewsMoviePaging(
        rxDisposer: RxDisposer,
        movieId: String
    ): Flowable<NetworkResult<ReviewsMovie>> {
        val result = ReplaySubject.create<NetworkResult<ReviewsMovie>>()

        result.onNext(NetworkResult.Loading(null))

        remoteDataSource.getReviews(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { data ->
                DataMapperMovies.mapReviewMovieResponseToReviewMovie(data)
            }
            .subscribe(
                { value ->
                    if (value != null) {
                        result.onNext(NetworkResult.Success(value))
                    } else {
                        result.onNext(NetworkResult.Empty)
                    }
                },
                { throwable ->
                    result.onNext(NetworkResult.Error(throwable.message.toString()))
                }
            )
            .addToDisposer(rxDisposer)

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun getDiscoverMovies(
        rxDisposer: RxDisposer,
        genreId: String
    ): Flowable<NetworkResult<PagingData<ResultsItemDiscover>>> {
        val result = ReplaySubject.create<NetworkResult<PagingData<ResultsItemDiscover>>>()

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

        result.onNext(NetworkResult.Loading(null))

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
                        result.onNext(NetworkResult.Success(value))
                    } else {
                        result.onNext(NetworkResult.Empty)
                    }
                },
                { throwable ->
                    result.onNext(NetworkResult.Error(throwable.message.toString()))
                }
            )
            .addToDisposer(rxDisposer)

        return result.toFlowable(BackpressureStrategy.BUFFER)
    }
}