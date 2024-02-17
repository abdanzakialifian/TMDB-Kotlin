package com.application.tmdb.core.domain.usecase

import androidx.paging.PagingData
import com.application.tmdb.core.domain.interfaces.IOtherRepository
import com.application.tmdb.core.domain.model.DiscoverItem
import com.application.tmdb.core.utils.Category
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDiscover @Inject constructor(private val iOtherRepository: IOtherRepository) {
    operator fun invoke(
        genreId: String, category: Category
    ): Flowable<PagingData<DiscoverItem>> = iOtherRepository.getDiscoverPaging(genreId, category)
}