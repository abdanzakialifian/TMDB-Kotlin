package com.application.zaki.movies.utils

import com.application.zaki.movies.data.source.remote.response.tvshows.DetailTvShowsResponse
import com.application.zaki.movies.data.source.remote.response.tvshows.ListTvShowsResponse
import com.application.zaki.movies.domain.model.other.Genres
import com.application.zaki.movies.domain.model.other.GenresItem
import com.application.zaki.movies.domain.model.tvshows.*

object DataMapperTvShows {

    fun ListTvShowsResponse.toListTvShows(): ListTvShows = ListTvShows(
        originalLanguage = originalLanguage,
        genreIds = genreIds,
        posterPath = posterPath,
        voteAverage = voteAverage,
        originalName = originalName,
        name = name,
        id = id,
        genres = listOf()
    )

    fun ListTvShows.toMergeListTvShowsGenres(genres: Genres): ListTvShows {
        val listGenresTvShows = genres.genres?.map { map ->
            GenresItem(name = map.name, id = map.id)
        }

        return ListTvShows(
            originalLanguage = originalLanguage,
            genreIds = genreIds,
            posterPath = posterPath,
            voteAverage = voteAverage,
            originalName = originalName,
            name = name,
            id = id,
            genres = listGenresTvShows
        )
    }

    fun DetailTvShowsResponse.toDetailTvShows(): DetailTvShows {
        val results = ArrayList<ResultsItem>()
        videos?.results?.forEach {
            it.let { data ->
                results.add(
                    ResultsItem(
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
        val videos = Videos(results = results)

        val networks = ArrayList<NetworksItem>()
        networks.forEach {
            it.let { data ->
                networks.add(
                    NetworksItem(
                        logoPath = data.logoPath,
                        name = data.name,
                        id = data.id,
                        originCountry = data.originCountry,
                    ),
                )
            }
        }

        val cast = ArrayList<CastItem>()
        credits?.cast?.forEach {
            it.let { data ->
                cast.add(
                    CastItem(
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
        val crew = ArrayList<CrewItem>()
        credits?.crew?.forEach {
            it.let { data ->
                crew.add(
                    CrewItem(
                        gender = data.gender,
                        creditId = data.creditId,
                        knownForDepartment = data.knownForDepartment,
                        originalName = data.originalName,
                        popularity = data.popularity,
                        name = data.name,
                        profilePath = data.profilePath,
                        id = data.id,
                        adult = data.adult,
                        department = data.department,
                        job = data.job,
                    ),
                )
            }
        }
        val credits = Credits(cast = cast, crew = crew)

        val genres = ArrayList<GenresItem>()
        genres.forEach {
            it.let { data ->
                genres.add(GenresItem(name = data.name, id = data.id))
            }
        }

        val productionCountries = ArrayList<ProductionCountriesItem>()
        productionCountries.forEach {
            it.let { data ->
                productionCountries.add(
                    ProductionCountriesItem(
                        iso31661 = data.iso31661,
                        name = data.name,
                    ),
                )
            }
        }

        val seasons = ArrayList<SeasonsItem>()
        seasons.forEach {
            it.let { data ->
                seasons.add(
                    SeasonsItem(
                        airDate = data.airDate,
                        overview = data.overview,
                        episodeCount = data.episodeCount,
                        name = data.name,
                        seasonNumber = data.seasonNumber,
                        id = data.id,
                        posterPath = data.posterPath,
                    ),
                )
            }
        }

        val lastEpisodeToAir = LastEpisodeToAir(
            productionCode = lastEpisodeToAir?.productionCode,
            airDate = lastEpisodeToAir?.airDate,
            overview = lastEpisodeToAir?.overview,
            episodeNumber = lastEpisodeToAir?.episodeNumber,
            showId = lastEpisodeToAir?.showId,
            voteAverage = lastEpisodeToAir?.voteAverage,
            name = lastEpisodeToAir?.name,
            seasonNumber = lastEpisodeToAir?.seasonNumber,
            runtime = lastEpisodeToAir?.runtime,
            id = lastEpisodeToAir?.id,
            stillPath = lastEpisodeToAir?.stillPath,
            voteCount = lastEpisodeToAir?.voteCount,
        )

        val spokenLanguages = ArrayList<SpokenLanguagesItem>()
        spokenLanguages.forEach {
            it.let { data ->
                spokenLanguages.add(
                    SpokenLanguagesItem(
                        name = data.name,
                        iso6391 = data.iso6391,
                        englishName = data.englishName,
                    ),
                )
            }
        }

        val productionCompanies = ArrayList<ProductionCompaniesItem>()
        productionCompanies.forEach {
            it.let { data ->
                productionCompanies.add(
                    ProductionCompaniesItem(
                        logoPath = data.logoPath,
                        name = data.name,
                        id = data.id,
                        originCountry = data.originCountry,
                    ),
                )
            }
        }

        return DetailTvShows(
            originalLanguage = originalLanguage,
            numberOfEpisodes = numberOfEpisodes,
            videos = videos,
            networks = networks,
            type = type,
            backdropPath = backdropPath,
            credits = credits,
            genres = genres,
            popularity = popularity,
            productionCountries = productionCountries,
            id = id,
            numberOfSeasons = numberOfSeasons,
            voteCount = voteCount,
            firstAirDate = firstAirDate,
            overview = overview,
            seasons = seasons,
            languages = languages,
            createdBy = createdBy,
            lastEpisodeToAir = lastEpisodeToAir,
            posterPath = posterPath,
            originCountry = originCountry,
            spokenLanguages = spokenLanguages,
            productionCompanies = productionCompanies,
            originalName = originalName,
            voteAverage = voteAverage,
            name = name,
            tagline = tagline,
            episodeRunTime = episodeRunTime,
            adult = adult,
            nextEpisodeToAir = nextEpisodeToAir,
            inProduction = inProduction,
            lastAirDate = lastAirDate,
            homepage = homepage,
            status = status,
        )
    }
}