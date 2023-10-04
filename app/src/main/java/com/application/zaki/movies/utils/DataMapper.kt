package com.application.zaki.movies.utils

import com.application.zaki.movies.data.source.remote.response.movies.DetailMoviesResponse
import com.application.zaki.movies.data.source.remote.response.movies.ListMoviesResponse
import com.application.zaki.movies.data.source.remote.response.other.ResultsItemDiscoverResponse
import com.application.zaki.movies.data.source.remote.response.other.ReviewItemResponse
import com.application.zaki.movies.data.source.remote.response.tvshows.DetailTvShowsResponse
import com.application.zaki.movies.data.source.remote.response.tvshows.ListTvShowsResponse
import com.application.zaki.movies.domain.model.CastCrewItem
import com.application.zaki.movies.domain.model.Detail
import com.application.zaki.movies.domain.model.DiscoverItem
import com.application.zaki.movies.domain.model.GenresItem
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.domain.model.ReviewItem
import com.application.zaki.movies.domain.model.Videos

object DataMapper {
    fun ListMoviesResponse.toMovie(): MovieTvShow = MovieTvShow(
        posterPath = posterPath,
        voteAverage = voteAverage,
        name = title,
        id = id,
        overview = overview,
        releaseDate = releaseDate,
        backdropPath = backdropPath
    )

    fun ListTvShowsResponse.toTvShow(): MovieTvShow = MovieTvShow(
        posterPath = posterPath,
        voteAverage = voteAverage,
        name = name,
        id = id,
        overview = overview,
        releaseDate = firstAirDate,
        backdropPath = backdropPath
    )

    fun ReviewItemResponse.toReviewItem(): ReviewItem = ReviewItem(
        author = author,
        createdAt = createdAt,
        id = id,
        content = content,
    )

    fun DetailMoviesResponse.toDetailMovie(): Detail {
        val videos = videos?.results?.map { video ->
            Videos(
                id = video.id,
                key = video.key,
            )
        }

        val cast = credits?.cast?.map { cast ->
            CastCrewItem(
                character = cast.character,
                name = cast.name,
                profilePath = cast.profilePath,
                id = cast.id,
            )
        }

        val crew = credits?.crew?.map { crew ->
            CastCrewItem(
                job = crew.job,
                name = crew.name,
                profilePath = crew.profilePath,
                id = crew.id,
            )
        }

        val genres = genres?.map { genre ->
            GenresItem(name = genre.name, id = genre.id)
        }

        return Detail(
            originalLanguage = originalLanguage,
            title = title,
            backdropPath = backdropPath,
            cast = cast,
            crew = crew,
            genres = genres,
            id = id,
            overview = overview,
            posterPath = posterPath,
            releaseDate = releaseDate,
            voteAverage = voteAverage,
            videos = videos,
            runtime = runtime
        )
    }

    fun DetailTvShowsResponse.toDetailTvShow(): Detail {
        val videos = videos?.results?.map { video ->
            Videos(
                id = video.id,
                key = video.key,
            )
        }

        val cast = credits?.cast?.map { cast ->
            CastCrewItem(
                character = cast.character,
                name = cast.name,
                profilePath = cast.profilePath,
                id = cast.id,
            )
        }

        val crew = credits?.crew?.map { crew ->
            CastCrewItem(
                job = crew.job,
                name = crew.name,
                profilePath = crew.profilePath,
                id = crew.id,
            )
        }

        val genres = genres?.map { genre ->
            GenresItem(name = genre.name, id = genre.id)
        }

        return Detail(
            originalLanguage = originalLanguage,
            title = name,
            backdropPath = backdropPath,
            cast = cast,
            crew = crew,
            genres = genres,
            id = id,
            overview = overview,
            posterPath = posterPath,
            releaseDate = firstAirDate,
            voteAverage = voteAverage,
            videos = videos,
            runtime = lastEpisodeToAir?.runtime
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
}