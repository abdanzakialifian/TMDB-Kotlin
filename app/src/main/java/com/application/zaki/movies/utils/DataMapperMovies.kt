package com.application.zaki.movies.utils

import com.application.zaki.movies.data.source.remote.response.movies.*
import com.application.zaki.movies.domain.model.movies.*

object DataMapperMovies {
    fun mapPopularMoviesResponseToPopularMovies(input: PopularMoviesResponse?): PopularMovies {
        val listPopularMovies = input?.results?.map { map ->
            map?.let {
                ListPopularMovies(
                    overview = it.overview,
                    originalLanguage = it.originalLanguage,
                    originalTitle = it.originalTitle,
                    video = it.video,
                    title = it.title,
                    genreIds = it.genreIds,
                    posterPath = it.posterPath,
                    backdropPath = it.backdropPath,
                    releaseDate = it.releaseDate,
                    popularity = it.popularity,
                    voteAverage = it.voteAverage,
                    id = it.id,
                    adult = it.adult,
                    voteCount = it.voteCount
                )
            }
        }

        return PopularMovies(
            page = input?.page,
            results = listPopularMovies,
            totalResults = input?.totalResults,
            totalPages = input?.totalPages
        )
    }

    fun mapNowPlayingMoviesResponseToNowPlayingMovies(input: NowPlayingMoviesResponse?): NowPlayingMovies {
        val listNowPlayingMovies = input?.results?.map { map ->
            map?.let {
                ListNowPlayingMovies(
                    overview = it.overview,
                    originalLanguage = it.originalLanguage,
                    originalTitle = it.originalTitle,
                    video = it.video,
                    title = it.originalTitle,
                    genreIds = it.genreIds,
                    posterPath = it.posterPath,
                    backdropPath = it.backdropPath,
                    releaseDate = it.releaseDate,
                    popularity = it.popularity,
                    voteAverage = it.voteAverage,
                    id = it.id,
                    adult = it.adult,
                    voteCount = it.voteCount
                )
            }
        }

        val dates = Dates(maximum = input?.dates?.maximum, minimum = input?.dates?.minimum)

        return NowPlayingMovies(
            dates = dates,
            page = input?.page,
            totalPages = input?.totalPages,
            results = listNowPlayingMovies,
            totalResults = input?.totalResults
        )
    }

    fun mapTopRatedMoviesResponseToTopRatedMovies(input: TopRatedMoviesResponse?): TopRatedMovies {
        val listTopRatedMovies = input?.results?.map { map ->
            map?.let {
                ListTopRatedMovies(
                    overview = it.overview,
                    originalLanguage = it.originalLanguage,
                    originalTitle = it.originalTitle,
                    video = it.video,
                    title = it.originalTitle,
                    genreIds = it.genreIds,
                    posterPath = it.posterPath,
                    backdropPath = it.backdropPath,
                    releaseDate = it.releaseDate,
                    popularity = it.popularity,
                    voteAverage = it.voteAverage,
                    id = it.id,
                    adult = it.adult,
                    voteCount = it.voteCount
                )
            }
        }

        return TopRatedMovies(
            page = input?.page,
            totalPages = input?.totalPages,
            results = listTopRatedMovies,
            totalResults = input?.totalResults
        )
    }

    fun mapUpComingMoviesResponseToUpComingMovies(input: UpComingMoviesResponse?): UpComingMovies {
        val listUpComingMovies = input?.results?.map { map ->
            map?.let {
                ListUpComingMovies(
                    overview = it.overview,
                    originalLanguage = it.originalLanguage,
                    originalTitle = it.originalTitle,
                    video = it.video,
                    title = it.originalTitle,
                    genreIds = it.genreIds,
                    posterPath = it.posterPath,
                    backdropPath = it.backdropPath,
                    releaseDate = it.releaseDate,
                    popularity = it.popularity,
                    voteAverage = it.voteAverage,
                    id = it.id,
                    adult = it.adult,
                    voteCount = it.voteCount
                )
            }
        }


        val dates =
            DatesUpComingMovies(maximum = input?.dates?.maximum, minimum = input?.dates?.minimum)

        return UpComingMovies(
            dates = dates,
            page = input?.page,
            totalPages = input?.totalPages,
            results = listUpComingMovies,
            totalResults = input?.totalResults
        )
    }

    fun mapListPopularMoviesResponseToListPopularMovies(
        listPopularMovies: ListPopularMoviesResponse?,
        genreMovies: GenreMoviesResponse?,
    ): ListPopularMovies {
        // adding response genre to list popular movies
        val listGenreMovies = genreMovies?.genres?.map { map ->
            map?.let {
                GenreItemPopularMovies(it.name, it.id)
            }
        }
        return ListPopularMovies(
            overview = listPopularMovies?.overview,
            originalLanguage = listPopularMovies?.originalLanguage,
            originalTitle = listPopularMovies?.originalTitle,
            video = listPopularMovies?.video,
            title = listPopularMovies?.originalTitle,
            genreIds = listPopularMovies?.genreIds,
            posterPath = listPopularMovies?.posterPath,
            backdropPath = listPopularMovies?.backdropPath,
            releaseDate = listPopularMovies?.releaseDate,
            popularity = listPopularMovies?.popularity,
            voteAverage = listPopularMovies?.voteAverage,
            id = listPopularMovies?.id,
            adult = listPopularMovies?.adult,
            voteCount = listPopularMovies?.voteCount,
            genres = listGenreMovies
        )
    }

    fun mapListTopRatedMoviesResponseToListTopRatedMovies(
        listTopRatedMovies: ListTopRatedMoviesResponse?,
        genreMovies: GenreMoviesResponse?,
    ): ListTopRatedMovies {
        val listGenreMovies = genreMovies?.genres?.map { map ->
            map?.let {
                GenreItemTopRatedMovies(name = it.name, id = it.id)
            }
        }

        return ListTopRatedMovies(
            overview = listTopRatedMovies?.overview,
            originalLanguage = listTopRatedMovies?.originalLanguage,
            originalTitle = listTopRatedMovies?.originalTitle,
            video = listTopRatedMovies?.video,
            title = listTopRatedMovies?.title,
            genreIds = listTopRatedMovies?.genreIds,
            posterPath = listTopRatedMovies?.posterPath,
            backdropPath = listTopRatedMovies?.backdropPath,
            releaseDate = listTopRatedMovies?.releaseDate,
            popularity = listTopRatedMovies?.popularity,
            voteAverage = listTopRatedMovies?.voteAverage,
            id = listTopRatedMovies?.id,
            adult = listTopRatedMovies?.adult,
            voteCount = listTopRatedMovies?.voteCount,
            genres = listGenreMovies
        )
    }

    fun mapListUpComingMoviesResponseToListUpComingMovies(
        listUpComingMovies: ListUpComingMoviesResponse?,
        genreMovies: GenreMoviesResponse?,
    ): ListUpComingMovies {
        val listGenreMovies = genreMovies?.genres?.map { map ->
            map?.let {
                GenreItemUpComingMovies(name = it.name, id = it.id)
            }
        }

        return ListUpComingMovies(
            overview = listUpComingMovies?.overview,
            originalLanguage = listUpComingMovies?.originalLanguage,
            originalTitle = listUpComingMovies?.originalTitle,
            video = listUpComingMovies?.video,
            title = listUpComingMovies?.title,
            genreIds = listUpComingMovies?.genreIds,
            posterPath = listUpComingMovies?.posterPath,
            backdropPath = listUpComingMovies?.backdropPath,
            releaseDate = listUpComingMovies?.releaseDate,
            popularity = listUpComingMovies?.popularity,
            voteAverage = listUpComingMovies?.voteAverage,
            id = listUpComingMovies?.id,
            adult = listUpComingMovies?.adult,
            voteCount = listUpComingMovies?.voteCount,
            genres = listGenreMovies
        )
    }

    fun mapDetailMovieResponseToDetailMovie(
        detailMoviesResponse: DetailMoviesResponse
    ): DetailMovies {
        val results = ArrayList<ResultsItem>()
        detailMoviesResponse.videos?.results?.forEach {
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

        val cast = ArrayList<CastItem>()
        detailMoviesResponse.credits?.cast?.forEach {
            it?.let { data ->
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
        val crew = ArrayList<CrewItem>()
        detailMoviesResponse.credits?.crew?.forEach {
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
        detailMoviesResponse.genres?.forEach {
            it?.let { data ->
                genres.add(GenresItem(name = data.name, id = data.id))
            }
        }

        val productionCountries = ArrayList<ProductionCountriesItem>()
        detailMoviesResponse.productionCountries?.forEach {
            it?.let { data ->
                productionCountries.add(
                    ProductionCountriesItem(
                        iso31661 = data.iso31661,
                        name = data.name,
                    ),
                )
            }
        }

        val spokenLanguages = ArrayList<SpokenLanguagesItem>()
        detailMoviesResponse.spokenLanguages?.forEach {
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
        detailMoviesResponse.productionCompanies?.forEach {
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

        val belongsToCollection = BelongsToCollection(
            backdropPath = detailMoviesResponse.belongsToCollection?.backdropPath,
            name = detailMoviesResponse.belongsToCollection?.name,
            id = detailMoviesResponse.belongsToCollection?.id,
            posterPath = detailMoviesResponse.belongsToCollection?.posterPath,
        )

        return DetailMovies(
            originalLanguage = detailMoviesResponse.originalLanguage,
            imdbId = detailMoviesResponse.imdbId,
            videos = videos,
            video = detailMoviesResponse.video,
            title = detailMoviesResponse.title,
            backdropPath = detailMoviesResponse.backdropPath,
            revenue = detailMoviesResponse.revenue,
            credits = credits,
            genres = genres,
            popularity = detailMoviesResponse.popularity,
            productionCountries = productionCountries,
            id = detailMoviesResponse.id,
            voteCount = detailMoviesResponse.voteCount,
            budget = detailMoviesResponse.budget,
            overview = detailMoviesResponse.overview,
            originalTitle = detailMoviesResponse.originalTitle,
            runtime = detailMoviesResponse.runtime,
            posterPath = detailMoviesResponse.posterPath,
            spokenLanguages = spokenLanguages,
            productionCompanies = productionCompanies,
            releaseDate = detailMoviesResponse.releaseDate,
            voteAverage = detailMoviesResponse.voteAverage,
            belongsToCollection = belongsToCollection,
            tagline = detailMoviesResponse.tagline,
            adult = detailMoviesResponse.adult,
            homepage = detailMoviesResponse.homepage,
            status = detailMoviesResponse.status,
        )
    }

    fun mapReviewMovieResponseToReviewMovie(reviewsMovieResponse: ReviewsMovieResponse): ReviewsMovie {
        val results = ArrayList<ReviewItem>()
        reviewsMovieResponse.results?.forEach {
            it?.let { data ->
                val authorDetails = AuthorDetails(
                    avatarPath = data.authorDetails?.avatarPath,
                    name = data.authorDetails?.name,
                    rating = data.authorDetails?.rating,
                    username = data.authorDetails?.username
                )
                results.add(
                    ReviewItem(
                        authorDetails = authorDetails,
                        updatedAt = data.updatedAt,
                        author = data.author,
                        createdAt = data.createdAt,
                        id = data.id,
                        content = data.content,
                        url = data.url
                    )
                )
            }
        }
        return ReviewsMovie(
            id = reviewsMovieResponse.id,
            page = reviewsMovieResponse.id,
            totalPages = reviewsMovieResponse.totalPages,
            results = results,
            totalResults = reviewsMovieResponse.totalResults
        )
    }

    fun mapResultItemDiscoverResponseToResultItemDiscover(resultsItemDiscoverResponse: ResultsItemDiscoverResponse): ResultsItemDiscover {
        return ResultsItemDiscover(
            overview = resultsItemDiscoverResponse.overview,
            originalLanguage = resultsItemDiscoverResponse.originalLanguage,
            originalTitle = resultsItemDiscoverResponse.originalTitle,
            video = resultsItemDiscoverResponse.video,
            title = resultsItemDiscoverResponse.title,
            genreIds = resultsItemDiscoverResponse.genreIds,
            posterPath = resultsItemDiscoverResponse.posterPath,
            backdropPath = resultsItemDiscoverResponse.backdropPath,
            releaseDate = resultsItemDiscoverResponse.releaseDate,
            popularity = resultsItemDiscoverResponse.popularity,
            voteAverage = resultsItemDiscoverResponse.voteAverage,
            id = resultsItemDiscoverResponse.id,
            adult = resultsItemDiscoverResponse.adult,
            voteCount = resultsItemDiscoverResponse.voteCount
        )
    }
}