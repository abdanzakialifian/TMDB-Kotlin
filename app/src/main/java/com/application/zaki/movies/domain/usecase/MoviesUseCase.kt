package com.application.zaki.movies.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.application.zaki.movies.domain.interfaces.IMoviesRepository
import com.application.zaki.movies.domain.interfaces.IMoviesUseCase
import com.application.zaki.movies.domain.model.movies.DetailMovies
import com.application.zaki.movies.domain.model.movies.ListMovies
import com.application.zaki.movies.utils.*
import com.application.zaki.movies.utils.DataMapperMovies.toMergeListMoviesGenres
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class MoviesUseCase @Inject constructor(private val moviesRepository: IMoviesRepository) :
    IMoviesUseCase {

    override fun getDetailMovies(movieId: String): Flowable<DetailMovies> =
        moviesRepository.getDetailMovies(movieId)

    override fun getMovies(
        movie: Movie, genre: Genre, page: Page, rxDisposer: RxDisposer
    ): Flowable<PagingData<ListMovies>> {
        val subject = PublishSubject.create<PagingData<ListMovies>>()

        val listMoviesMergeWithGenre = Flowable.zip(
            moviesRepository.getMovies(movie, page),
            moviesRepository.getGenres(genre)
        ) { movies, genres ->
            return@zip movies.map { map ->
                map.toMergeListMoviesGenres(genres)
            }
        }

        listMoviesMergeWithGenre
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                subject.onNext(data)
            }.addToDisposer(rxDisposer)
        return subject.toFlowable(BackpressureStrategy.BUFFER)
    }
}