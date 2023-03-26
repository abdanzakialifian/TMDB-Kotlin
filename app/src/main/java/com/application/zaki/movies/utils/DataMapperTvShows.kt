package com.application.zaki.movies.utils

import com.application.zaki.movies.data.source.remote.response.tvshows.*
import com.application.zaki.movies.domain.model.tvshows.*

object DataMapperTvShows {

    fun ListTvShowsResponse.toListTvShows(): ListTvShows = ListTvShows(
        originalLanguage = this.originalLanguage,
        genreIds = this.genreIds,
        posterPath = this.posterPath,
        voteAverage = this.voteAverage,
        originalName = this.originalName,
        name = this.name,
        id = this.id,
        genres = listOf()
    )

    fun mapDetailTvShowsResponseToDetailTvShows(detailTvShowsResponse: DetailTvShowsResponse): DetailTvShows {
        val results = ArrayList<ResultsItem>()
        detailTvShowsResponse.videos?.results?.forEach {
            it?.let { data ->
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
        detailTvShowsResponse.networks?.forEach {
            it?.let { data ->
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
        detailTvShowsResponse.credits?.cast?.forEach {
            it?.let { data ->
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
        detailTvShowsResponse.credits?.crew?.forEach {
            it?.let { data ->
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
        detailTvShowsResponse.genres?.forEach {
            it?.let { data ->
                genres.add(GenresItem(name = data.name, id = data.id))
            }
        }

        val productionCountries = ArrayList<ProductionCountriesItem>()
        detailTvShowsResponse.productionCountries?.forEach {
            it?.let { data ->
                productionCountries.add(
                    ProductionCountriesItem(
                        iso31661 = data.iso31661,
                        name = data.name,
                    ),
                )
            }
        }

        val seasons = ArrayList<SeasonsItem>()
        detailTvShowsResponse.seasons?.forEach {
            it?.let { data ->
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
            productionCode = detailTvShowsResponse.lastEpisodeToAir?.productionCode,
            airDate = detailTvShowsResponse.lastEpisodeToAir?.airDate,
            overview = detailTvShowsResponse.lastEpisodeToAir?.overview,
            episodeNumber = detailTvShowsResponse.lastEpisodeToAir?.episodeNumber,
            showId = detailTvShowsResponse.lastEpisodeToAir?.showId,
            voteAverage = detailTvShowsResponse.lastEpisodeToAir?.voteAverage,
            name = detailTvShowsResponse.lastEpisodeToAir?.name,
            seasonNumber = detailTvShowsResponse.lastEpisodeToAir?.seasonNumber,
            runtime = detailTvShowsResponse.lastEpisodeToAir?.runtime,
            id = detailTvShowsResponse.lastEpisodeToAir?.id,
            stillPath = detailTvShowsResponse.lastEpisodeToAir?.stillPath,
            voteCount = detailTvShowsResponse.lastEpisodeToAir?.voteCount,
        )

        val spokenLanguages = ArrayList<SpokenLanguagesItem>()
        detailTvShowsResponse.spokenLanguages?.forEach {
            it?.let { data ->
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
        detailTvShowsResponse.productionCompanies?.forEach {
            it?.let { data ->
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
            originalLanguage = detailTvShowsResponse.originalLanguage,
            numberOfEpisodes = detailTvShowsResponse.numberOfEpisodes,
            videos = videos,
            networks = networks,
            type = detailTvShowsResponse.type,
            backdropPath = detailTvShowsResponse.backdropPath,
            credits = credits,
            genres = genres,
            popularity = detailTvShowsResponse.popularity,
            productionCountries = productionCountries,
            id = detailTvShowsResponse.id,
            numberOfSeasons = detailTvShowsResponse.numberOfSeasons,
            voteCount = detailTvShowsResponse.voteCount,
            firstAirDate = detailTvShowsResponse.firstAirDate,
            overview = detailTvShowsResponse.overview,
            seasons = seasons,
            languages = detailTvShowsResponse.languages,
            createdBy = detailTvShowsResponse.createdBy,
            lastEpisodeToAir = lastEpisodeToAir,
            posterPath = detailTvShowsResponse.posterPath,
            originCountry = detailTvShowsResponse.originCountry,
            spokenLanguages = spokenLanguages,
            productionCompanies = productionCompanies,
            originalName = detailTvShowsResponse.originalName,
            voteAverage = detailTvShowsResponse.voteAverage,
            name = detailTvShowsResponse.name,
            tagline = detailTvShowsResponse.tagline,
            episodeRunTime = detailTvShowsResponse.episodeRunTime,
            adult = detailTvShowsResponse.adult,
            nextEpisodeToAir = detailTvShowsResponse.nextEpisodeToAir,
            inProduction = detailTvShowsResponse.inProduction,
            lastAirDate = detailTvShowsResponse.lastAirDate,
            homepage = detailTvShowsResponse.homepage,
            status = detailTvShowsResponse.status,
        )
    }
}