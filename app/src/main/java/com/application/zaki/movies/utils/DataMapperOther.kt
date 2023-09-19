package com.application.zaki.movies.utils

import com.application.zaki.movies.data.source.remote.response.other.GenreResponse
import com.application.zaki.movies.data.source.remote.response.other.ResultsItemDiscoverResponse
import com.application.zaki.movies.data.source.remote.response.other.ReviewItemResponse
import com.application.zaki.movies.domain.model.other.AuthorDetails
import com.application.zaki.movies.domain.model.other.DiscoverItem
import com.application.zaki.movies.domain.model.other.Genres
import com.application.zaki.movies.domain.model.other.GenresItem
import com.application.zaki.movies.domain.model.other.ReviewItem

object DataMapperOther {
    fun ReviewItemResponse.toReviewItem(): ReviewItem {
        val authorDetails = AuthorDetails(
            avatarPath = authorDetails?.avatarPath,
            name = authorDetails?.name,
            rating = authorDetails?.rating,
            username = authorDetails?.username
        )
        return ReviewItem(
            authorDetails = authorDetails,
            updatedAt = updatedAt,
            author = author,
            createdAt = createdAt,
            id = id,
            content = content,
            url = url
        )
    }

    fun ResultsItemDiscoverResponse.toResultItemDiscover(): DiscoverItem {
        return DiscoverItem(
            overview = overview,
            originalLanguage = originalLanguage,
            originalTitle = originalTitle,
            video = video,
            title = title,
            genreIds = genreIds,
            posterPath = posterPath,
            backdropPath = backdropPath,
            releaseDate = releaseDate,
            popularity = popularity,
            voteAverage = voteAverage,
            id = id,
            adult = adult,
            voteCount = voteCount
        )
    }

    fun GenreResponse.toGenres(): Genres {
        val listGenresItem = genres?.map {
            GenresItem(name = it.name, id = it.id)
        }
        return Genres(genres = listGenresItem)
    }
}