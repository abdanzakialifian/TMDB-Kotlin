package com.application.tmdb.domain.usecase

import androidx.paging.PagingData
import com.application.tmdb.domain.interfaces.IOtherRepository
import com.application.tmdb.domain.model.DiscoverItem
import com.application.tmdb.utils.Category
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDiscover @Inject constructor(private val iOtherRepository: IOtherRepository) {
    operator fun invoke(
        genreId: String, category: Category
    ): Flowable<PagingData<DiscoverItem>> = iOtherRepository.getDiscoverPaging(genreId, category)
}