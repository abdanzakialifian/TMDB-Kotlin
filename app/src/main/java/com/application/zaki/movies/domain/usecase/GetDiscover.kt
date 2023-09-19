package com.application.zaki.movies.domain.usecase

import androidx.paging.PagingData
import com.application.zaki.movies.domain.interfaces.IOtherRepository
import com.application.zaki.movies.domain.model.other.DiscoverItem
import com.application.zaki.movies.utils.Category
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDiscover @Inject constructor(private val iOtherRepository: IOtherRepository) {
    operator fun invoke(
        genreId: String, category: Category
    ): Flowable<PagingData<DiscoverItem>> = iOtherRepository.getDiscoverPaging(genreId, category)
}