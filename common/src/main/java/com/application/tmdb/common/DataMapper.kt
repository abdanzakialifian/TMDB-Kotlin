package com.application.tmdb.common

import com.application.tmdb.core.domain.model.CastCrewItemModel
import com.application.tmdb.core.domain.model.DetailCastModel
import com.application.tmdb.core.domain.model.DetailModel
import com.application.tmdb.core.domain.model.DiscoverItem
import com.application.tmdb.core.domain.model.GenreItemModel
import com.application.tmdb.core.domain.model.MovieTvShowModel
import com.application.tmdb.core.domain.model.ReviewModel
import com.application.tmdb.core.domain.model.VideoItemModel
import com.application.tmdb.core.source.remote.response.movies.DetailMoviesResponse
import com.application.tmdb.core.source.remote.response.movies.ListMoviesResponse
import com.application.tmdb.core.source.remote.response.other.DetailCastResponse
import com.application.tmdb.core.source.remote.response.other.ResultsItemDiscoverResponse
import com.application.tmdb.core.source.remote.response.other.ReviewItemResponse
import com.application.tmdb.core.source.remote.response.tvshows.DetailTvShowsResponse
import com.application.tmdb.core.source.remote.response.tvshows.ListTvShowsResponse

object DataMapper {
    fun ListMoviesResponse.toMovie(): MovieTvShowModel = MovieTvShowModel(
        posterPath = posterPath,
        voteAverage = voteAverage,
        name = title,
        id = id,
        overview = overview,
        releaseDate = releaseDate,
        backdropPath = backdropPath
    )

    fun ListTvShowsResponse.toTvShow(): MovieTvShowModel = MovieTvShowModel(
        posterPath = posterPath,
        voteAverage = voteAverage,
        name = name,
        id = id,
        overview = overview,
        releaseDate = firstAirDate,
        backdropPath = backdropPath
    )

    fun ReviewItemResponse.toReviewItem(): ReviewModel = ReviewModel(
        author = author,
        createdAt = createdAt,
        id = id,
        content = content,
    )

    fun DetailMoviesResponse.toDetailMovie(): DetailModel {
        val videos = videos?.results?.map { video ->
            VideoItemModel(
                id = video.id,
                key = video.key,
            )
        }

        val cast = credits?.cast?.map { cast ->
            CastCrewItemModel(
                character = cast.character,
                name = cast.name,
                profilePath = cast.profilePath,
                id = cast.id,
            )
        }

        val crew = credits?.crew?.map { crew ->
            CastCrewItemModel(
                job = crew.job,
                name = crew.name,
                profilePath = crew.profilePath,
                id = crew.id,
            )
        }

        val genres = genres?.map { genre ->
            GenreItemModel(name = genre.name, id = genre.id)
        }

        val releaseDateResult = releaseDates?.results?.firstOrNull { data ->
            data.releaseDates?.any { releaseDate ->
                !data.iso31661.isNullOrEmpty() && !releaseDate.certification.isNullOrEmpty()
            } ?: false
        }

        val releaseDate = releaseDateResult?.releaseDates?.firstOrNull()

        return DetailModel(
            originalLanguage = releaseDateResult?.iso31661,
            title = title,
            backdropPath = backdropPath,
            cast = cast,
            crew = crew,
            genres = genres,
            id = id,
            overview = overview,
            posterPath = posterPath,
            releaseDate = releaseDate?.releaseDate,
            voteAverage = voteAverage,
            videos = videos,
            runtime = runtime,
            certification = releaseDate?.certification
        )
    }

    fun DetailTvShowsResponse.toDetailTvShow(): DetailModel {
        val videos = videos?.results?.map { video ->
            VideoItemModel(
                id = video.id,
                key = video.key,
            )
        }

        val cast = credits?.cast?.map { cast ->
            CastCrewItemModel(
                character = cast.character,
                name = cast.name,
                profilePath = cast.profilePath,
                id = cast.id,
            )
        }

        val crew = credits?.crew?.map { crew ->
            CastCrewItemModel(
                job = crew.job,
                name = crew.name,
                profilePath = crew.profilePath,
                id = crew.id,
            )
        }

        val genres = genres?.map { genre ->
            GenreItemModel(name = genre.name, id = genre.id)
        }

        val contentRating = contentRating?.results?.firstOrNull { data ->
            !data.iso31661.isNullOrEmpty() ||! data.rating.isNullOrEmpty()
        }

        return DetailModel(
            originalLanguage = contentRating?.iso31661,
            title = name,
            backdropPath = backdropPath,
            cast = cast,
            crew = crew,
            genres = genres,
            id = id,
            overview = overview,
            posterPath = posterPath,
            releaseDate = lastAirDate,
            voteAverage = voteAverage,
            videos = videos,
            runtime = lastEpisodeToAir?.runtime,
            certification = contentRating?.rating
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

    fun DetailCastResponse.toDetailCastModel(): DetailCastModel = DetailCastModel(
        name = name,
        profilePath = profilePath,
        biography = biography,
        placeOfBirth = placeOfBirth
    )
}