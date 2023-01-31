package com.application.zaki.movies.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.paging.rxjava2.flowable
import com.application.zaki.movies.data.source.remote.RemoteDataSource
import com.application.zaki.movies.data.source.remote.paging.tvshows.OnTheAirTvShowsRxPagingSource
import com.application.zaki.movies.data.source.remote.paging.tvshows.PopularTvShowsRxPagingSource
import com.application.zaki.movies.data.source.remote.paging.tvshows.TopRatedTvShowsRxPagingSource
import com.application.zaki.movies.domain.interfaces.ITvShowsRepository
import com.application.zaki.movies.domain.model.tvshows.*
import com.application.zaki.movies.utils.DataMapperTvShows
import com.application.zaki.movies.utils.UiState
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
class TvShowsRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val onTheAirTvShowsRxPagingSource: OnTheAirTvShowsRxPagingSource,
    private val popularTvShowsRxPagingSource: PopularTvShowsRxPagingSource,
    private val topRatedTvShowsRxPagingSource: TopRatedTvShowsRxPagingSource,
) :
    ITvShowsRepository {

    override fun getAiringTodayTvShows(rxDisposer: RxDisposer): Flowable<UiState<AiringTodayTvShows>> {
        val result = ReplaySubject.create<UiState<AiringTodayTvShows>>()

        result.onNext(UiState.Loading(null))
        remoteDataSource.getAiringTodayTvShows()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { value ->
                DataMapperTvShows.mapAiringTodayTvShowsResponseToAiringTodayTvShows(value)
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

    override fun getTopRatedTvShows(rxDisposer: RxDisposer): Flowable<UiState<TopRatedTvShows>> {
        val result = ReplaySubject.create<UiState<TopRatedTvShows>>()

        result.onNext(UiState.Loading(null))
        remoteDataSource.getTopRatedTvShows()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { value ->
                DataMapperTvShows.mapTopRatedTvShowsResponseToTopRatedTvShows(value)
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

    override fun getPopularTvShows(rxDisposer: RxDisposer): Flowable<UiState<PopularTvShows>> {
        val result = ReplaySubject.create<UiState<PopularTvShows>>()

        result.onNext(UiState.Loading(null))
        remoteDataSource.getPopularTvShows()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { value ->
                DataMapperTvShows.mapPopularTvShowsResponseToPopularTvShows(value)
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

    override fun getOnTheAirTvShows(rxDisposer: RxDisposer): Flowable<UiState<OnTheAirTvShows>> {
        val result = ReplaySubject.create<UiState<OnTheAirTvShows>>()

        result.onNext(UiState.Loading(null))
        remoteDataSource.getOnTheAirTvShows()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { value ->
                DataMapperTvShows.mapOnTheAirTvShowsResponseToOnTheAirTvShows(value)
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

    override fun getDetailTvShows(
        rxDisposer: RxDisposer,
        tvId: String,
    ): Flowable<UiState<DetailTvShows>> {
        val result = ReplaySubject.create<UiState<DetailTvShows>>()

        result.onNext(UiState.Loading(null))

        remoteDataSource.getDetailTvShows(tvId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { value ->
                DataMapperTvShows.mapDetailTvShowsResponseToDetailTvShows(value)
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

    override fun getOnTheAirTvShowsPaging(rxDisposer: RxDisposer): Flowable<UiState<PagingData<ListOnTheAirTvShows>>> {
        val result = ReplaySubject.create<UiState<PagingData<ListOnTheAirTvShows>>>()

        val listOnTheAirTvShowsPaging = Pager(
            config = PagingConfig(
                /*
                pageSize : Mandatory, if your API has query string to show how many data will be shown use this
                to pass it to API. In this tutorial TMDB doesn’t have the feature to load list of
                 movies using custom pageSize, so we use their default value.
                 */
                pageSize = 20,
                /*
                enablePlaceholders : Defines whether PagingData may display null placeholders if
                PagingSource provides them
                 */
                enablePlaceholders = true,
                /*
                maxSize : Default MAX_SIZE_UNBOUNDED. Defines the maximum number of items that may be loaded
                into PagingData before pages should be dropped. Value must be at least
                pageSize + (2 * prefetchDistance)
                */
                maxSize = 24,
                /*
                Prefetch distance which defines how far from the edge of loaded content an access
                must be to trigger further loading. Typically should be set several times the number
                of visible items onscreen
                 */
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                onTheAirTvShowsRxPagingSource
            }
        ).flowable
        val listGenreTvShows = remoteDataSource.getGenreTvShows()

        result.onNext(UiState.Loading(null))
        Flowable.zip(
            listOnTheAirTvShowsPaging,
            listGenreTvShows
        ) { onTheAirTvShowsPaging, genreTvShows ->
            return@zip onTheAirTvShowsPaging.map {
                DataMapperTvShows.mapListOnTheAirTvShowsResponseToListOnTheAirTvShows(
                    it,
                    genreTvShows
                )
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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

    override fun getPopularTvShowsPaging(rxDisposer: RxDisposer): Flowable<UiState<PagingData<ListPopularTvShows>>> {
        val result = ReplaySubject.create<UiState<PagingData<ListPopularTvShows>>>()

        val listPopularTvShowsPaging = Pager(
            config = PagingConfig(
                /*
                pageSize : Mandatory, if your API has query string to show how many data will be shown use this
                to pass it to API. In this tutorial TMDB doesn’t have the feature to load list of
                 movies using custom pageSize, so we use their default value.
                 */
                pageSize = 20,
                /*
                enablePlaceholders : Defines whether PagingData may display null placeholders if
                PagingSource provides them
                 */
                enablePlaceholders = true,
                /*
                maxSize : Default MAX_SIZE_UNBOUNDED. Defines the maximum number of items that may be loaded
                into PagingData before pages should be dropped. Value must be at least
                pageSize + (2 * prefetchDistance)
                */
                maxSize = 24,
                /*
                Prefetch distance which defines how far from the edge of loaded content an access
                must be to trigger further loading. Typically should be set several times the number
                of visible items onscreen
                 */
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                popularTvShowsRxPagingSource
            }
        ).flowable
        val listGenreTvShows = remoteDataSource.getGenreTvShows()

        result.onNext(UiState.Loading(null))
        Flowable.zip(
            listPopularTvShowsPaging,
            listGenreTvShows
        ) { popularTvShowsPaging, genreTvShows ->
            return@zip popularTvShowsPaging.map {
                DataMapperTvShows.mapListPopularTvShowsResponseToListPopularTvShows(
                    it,
                    genreTvShows
                )
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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

    override fun getTopRatedTvShowsPaging(rxDisposer: RxDisposer): Flowable<UiState<PagingData<ListTopRatedTvShows>>> {
        val result = ReplaySubject.create<UiState<PagingData<ListTopRatedTvShows>>>()

        val listTopRatedTvShowsPaging = Pager(
            config = PagingConfig(
                /*
                pageSize : Mandatory, if your API has query string to show how many data will be shown use this
                to pass it to API. In this tutorial TMDB doesn’t have the feature to load list of
                 movies using custom pageSize, so we use their default value.
                 */
                pageSize = 20,
                /*
                enablePlaceholders : Defines whether PagingData may display null placeholders if
                PagingSource provides them
                 */
                enablePlaceholders = true,
                /*
                maxSize : Default MAX_SIZE_UNBOUNDED. Defines the maximum number of items that may be loaded
                into PagingData before pages should be dropped. Value must be at least
                pageSize + (2 * prefetchDistance)
                */
                maxSize = 24,
                /*
                Prefetch distance which defines how far from the edge of loaded content an access
                must be to trigger further loading. Typically should be set several times the number
                of visible items onscreen
                 */
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                topRatedTvShowsRxPagingSource
            }
        ).flowable
        val listGenreTvShows = remoteDataSource.getGenreTvShows()

        result.onNext(UiState.Loading(null))
        Flowable.zip(
            listTopRatedTvShowsPaging,
            listGenreTvShows
        ) { topRatedTvShowsPaging, genreTvShows ->
            return@zip topRatedTvShowsPaging.map {
                DataMapperTvShows.mapListTopRatedTvShowsResponseToListTopRatedTvShows(
                    it,
                    genreTvShows
                )
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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