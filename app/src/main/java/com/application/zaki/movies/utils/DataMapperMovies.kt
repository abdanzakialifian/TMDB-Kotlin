package com.application.zaki.movies.utils

import com.application.zaki.movies.data.source.remote.response.combine.GenreResponse
import com.application.zaki.movies.data.source.remote.response.combine.ResultsItemDiscoverResponse
import com.application.zaki.movies.data.source.remote.response.combine.ReviewItemResponse
import com.application.zaki.movies.data.source.remote.response.movies.DetailMoviesResponse
import com.application.zaki.movies.data.source.remote.response.movies.ListMoviesResponse
import com.application.zaki.movies.domain.model.movies.*

object DataMapperMovies {

    fun mapListMoviesResponseToListMovies(
        listMovies: ListMoviesResponse,
        genreMovies: GenreResponse
    ): ListMovies {
        val listGenreMovies = genreMovies.genres?.map { map ->
            GenreItemMovies(name = map.name, id = map.id)
        }

        return ListMovies(
            overview = listMovies.overview,
            originalLanguage = listMovies.originalLanguage,
            originalTitle = listMovies.originalTitle,
            video = listMovies.video,
            title = listMovies.title,
            genreIds = listMovies.genreIds,
            posterPath = listMovies.posterPath,
            backdropPath = listMovies.backdropPath,
            releaseDate = listMovies.releaseDate,
            popularity = listMovies.popularity,
            voteAverage = listMovies.voteAverage,
            id = listMovies.id,
            adult = listMovies.adult,
            voteCount = listMovies.voteCount,
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

    fun mapReviewMovieResponseToReviewMovie(reviewItemResponse: ReviewItemResponse): ReviewItem {
        val authorDetails = AuthorDetails(
            avatarPath = reviewItemResponse.authorDetails?.avatarPath,
            name = reviewItemResponse.authorDetails?.name,
            rating = reviewItemResponse.authorDetails?.rating,
            username = reviewItemResponse.authorDetails?.username
        )
        return ReviewItem(
            authorDetails = authorDetails,
            updatedAt = reviewItemResponse.updatedAt,
            author = reviewItemResponse.author,
            createdAt = reviewItemResponse.createdAt,
            id = reviewItemResponse.id,
            content = reviewItemResponse.content,
            url = reviewItemResponse.url
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