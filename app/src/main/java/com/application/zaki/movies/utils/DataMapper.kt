package com.application.zaki.movies.utils

import com.application.zaki.movies.data.source.remote.response.movies.DetailMoviesResponse
import com.application.zaki.movies.data.source.remote.response.movies.ListMoviesResponse
import com.application.zaki.movies.data.source.remote.response.other.ResultsItemDiscoverResponse
import com.application.zaki.movies.data.source.remote.response.other.ReviewItemResponse
import com.application.zaki.movies.data.source.remote.response.tvshows.DetailTvShowsResponse
import com.application.zaki.movies.data.source.remote.response.tvshows.ListTvShowsResponse
import com.application.zaki.movies.domain.model.AuthorDetails
import com.application.zaki.movies.domain.model.CastItem
import com.application.zaki.movies.domain.model.Detail
import com.application.zaki.movies.domain.model.DiscoverItem
import com.application.zaki.movies.domain.model.GenresItem
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.domain.model.ReviewItem
import com.application.zaki.movies.domain.model.Videos

object DataMapper {
    fun ListMoviesResponse.toMovie(): MovieTvShow = MovieTvShow(
        genreIds = genreIds,
        posterPath = posterPath,
        voteAverage = voteAverage,
        name = title,
        id = id,
        overview = overview,
        releaseDate = releaseDate,
        backdropPath = backdropPath
    )

    fun ListTvShowsResponse.toTvShow(): MovieTvShow = MovieTvShow(
        genreIds = genreIds,
        posterPath = posterPath,
        voteAverage = voteAverage,
        name = name,
        id = id,
        overview = overview,
        releaseDate = firstAirDate,
        backdropPath = backdropPath
    )

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

    fun DetailMoviesResponse.toDetailMovie(): Detail {
        val videos = mutableListOf<Videos>()
        this.videos?.results?.forEach { resultItemResponse ->
            resultItemResponse.let { data ->
                videos.add(
                    Videos(
                        site = data.site,
                        size = data.size,
                        iso31661 = data.iso31661,
                        name = data.name,
                        official = data.official,
                        id = data.id,
                        type = data.type,
                        publishedAt = data.publishedAt,
                        iso6391 = data.iso6391,
                        key = data.key,
                    ),
                )
            }
        }

        val cast = mutableListOf<CastItem>()
        credits?.cast?.forEach { castItemResponse ->
            castItemResponse.let { data ->
                cast.add(
                    CastItem(
                        castId = data.castId,
                        character = data.character,
                        gender = data.gender,
                        creditId = data.creditId,
                        knownForDepartment = data.knownForDepartment,
                        originalName = data.originalName,
                        popularity = data.popularity,
                        name = data.name,
                        profilePath = data.profilePath,
                        id = data.id,
                        adult = data.adult,
                        order = data.order,
                    ),
                )
            }
        }

        val genres = mutableListOf<GenresItem>()
        this.genres?.forEach { genreItemResponse ->
            genreItemResponse.let { data ->
                genres.add(GenresItem(name = data.name, id = data.id))
            }
        }

        val reviews = this.reviews?.results?.map { reviewItemResponse ->
            val authorDetail = AuthorDetails(
                avatarPath = reviewItemResponse.authorDetails?.avatarPath,
                name = reviewItemResponse.authorDetails?.name,
                rating = reviewItemResponse.authorDetails?.rating,
                username = reviewItemResponse.authorDetails?.username
            )

            ReviewItem(
                authorDetails = authorDetail,
                updatedAt = reviewItemResponse.updatedAt,
                author = reviewItemResponse.author,
                createdAt = reviewItemResponse.createdAt,
                content = reviewItemResponse.content,
                url = reviewItemResponse.url
            )
        }

        return Detail(
            originalLanguage = originalLanguage,
            title = title,
            backdropPath = backdropPath,
            cast = cast,
            genres = genres,
            popularity = popularity,
            id = id,
            voteCount = voteCount,
            overview = overview,
            posterPath = posterPath,
            releaseDate = releaseDate,
            voteAverage = voteAverage,
            tagline = tagline,
            adult = adult,
            homepage = homepage,
            status = status,
            reviews = reviews,
            videos = videos,
            runtime = runtime
        )
    }

    fun DetailTvShowsResponse.toDetailTvShow(): Detail {
        val videos = mutableListOf<Videos>()
        this.videos?.results?.forEach { resultItemResponse ->
            resultItemResponse.let { data ->
                videos.add(
                    Videos(
                        site = data.site,
                        size = data.size,
                        iso31661 = data.iso31661,
                        name = data.name,
                        official = data.official,
                        id = data.id,
                        type = data.type,
                        publishedAt = data.publishedAt,
                        iso6391 = data.iso6391,
                        key = data.key,
                    ),
                )
            }
        }

        val cast = mutableListOf<CastItem>()
        credits?.cast?.forEach { castItemResponse ->
            castItemResponse.let { data ->
                cast.add(
                    CastItem(
                        castId = data.id,
                        character = data.character,
                        gender = data.gender,
                        creditId = data.creditId,
                        knownForDepartment = data.knownForDepartment,
                        originalName = data.originalName,
                        popularity = data.popularity,
                        name = data.name,
                        profilePath = data.profilePath,
                        id = data.id,
                        adult = data.adult,
                        order = data.order,
                    ),
                )
            }
        }

        val genres = mutableListOf<GenresItem>()
        this.genres?.forEach { genreItemResponse ->
            genreItemResponse.let { data ->
                genres.add(GenresItem(name = data.name, id = data.id))
            }
        }

        val reviews = this.reviews?.results?.map { reviewItemResponse ->
            val authorDetail = AuthorDetails(
                avatarPath = reviewItemResponse.authorDetails?.avatarPath,
                name = reviewItemResponse.authorDetails?.name,
                rating = reviewItemResponse.authorDetails?.rating,
                username = reviewItemResponse.authorDetails?.username
            )

            ReviewItem(
                authorDetails = authorDetail,
                updatedAt = reviewItemResponse.updatedAt,
                author = reviewItemResponse.author,
                createdAt = reviewItemResponse.createdAt,
                content = reviewItemResponse.content,
                url = reviewItemResponse.url
            )
        }

        return Detail(
            originalLanguage = originalLanguage,
            title = name,
            backdropPath = backdropPath,
            cast = cast,
            genres = genres,
            popularity = popularity,
            id = id,
            voteCount = voteCount,
            overview = overview,
            posterPath = posterPath,
            releaseDate = firstAirDate,
            voteAverage = voteAverage,
            tagline = tagline,
            adult = adult,
            homepage = homepage,
            status = status,
            reviews = reviews,
            videos = videos,
            runtime = lastEpisodeToAir?.runtime
        )
    }
}