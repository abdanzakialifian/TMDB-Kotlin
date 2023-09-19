package com.application.zaki.movies.utils

import com.application.zaki.movies.data.source.remote.response.movies.DetailMoviesResponse
import com.application.zaki.movies.data.source.remote.response.movies.ListMoviesResponse
import com.application.zaki.movies.domain.model.movies.BelongsToCollection
import com.application.zaki.movies.domain.model.movies.CastItem
import com.application.zaki.movies.domain.model.movies.Credits
import com.application.zaki.movies.domain.model.movies.CrewItem
import com.application.zaki.movies.domain.model.movies.DetailMovies
import com.application.zaki.movies.domain.model.movies.ListMovies
import com.application.zaki.movies.domain.model.movies.ProductionCompaniesItem
import com.application.zaki.movies.domain.model.movies.ProductionCountriesItem
import com.application.zaki.movies.domain.model.movies.ResultsItem
import com.application.zaki.movies.domain.model.movies.SpokenLanguagesItem
import com.application.zaki.movies.domain.model.movies.Videos
import com.application.zaki.movies.domain.model.other.AuthorDetails
import com.application.zaki.movies.domain.model.other.Genres
import com.application.zaki.movies.domain.model.other.GenresItem
import com.application.zaki.movies.domain.model.other.ReviewItem

object DataMapperMovies {
    fun ListMoviesResponse.toListMovies(): ListMovies = ListMovies(
        originalTitle = originalTitle,
        title = title,
        genreIds = genreIds,
        posterPath = posterPath,
        voteAverage = voteAverage,
        id = id,
        genres = listOf()
    )

    fun ListMovies.toListMoviesWithGenres(genres: Genres): ListMovies {
        val listGenreMovies = genres.genres?.map { genreItem ->
            GenresItem(name = genreItem.name, id = genreItem.id)
        }

        return ListMovies(
            originalTitle = originalTitle,
            title = title,
            genreIds = genreIds,
            posterPath = posterPath,
            voteAverage = voteAverage,
            id = id,
            genres = listGenreMovies
        )
    }

    fun DetailMoviesResponse.toDetailMovie(): DetailMovies {
        val results = ArrayList<ResultsItem>()
        videos?.results?.forEach { resultItemResponse ->
            resultItemResponse.let { data ->
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
        val crew = ArrayList<CrewItem>()
        credits?.crew?.forEach { crewItemResponse ->
            crewItemResponse.let { data ->
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
        this.genres?.forEach { genreItemResponse ->
            genreItemResponse.let { data ->
                genres.add(GenresItem(name = data.name, id = data.id))
            }
        }

        val productionCountries = ArrayList<ProductionCountriesItem>()
        this.productionCountries?.forEach { productionCountriesItemResponse ->
            productionCountriesItemResponse.let { data ->
                productionCountries.add(
                    ProductionCountriesItem(
                        iso31661 = data.iso31661,
                        name = data.name,
                    ),
                )
            }
        }

        val spokenLanguages = ArrayList<SpokenLanguagesItem>()
        this.spokenLanguages?.forEach { spokenLanguagesItemResponse ->
            spokenLanguagesItemResponse.let { data ->
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
        this.productionCompanies?.forEach { productionCompaniesItemResponse ->
            productionCompaniesItemResponse.let { data ->
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
            backdropPath = belongsToCollection?.backdropPath,
            name = belongsToCollection?.name,
            id = belongsToCollection?.id,
            posterPath = belongsToCollection?.posterPath,
        )

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

        return DetailMovies(
            originalLanguage = originalLanguage,
            imdbId = imdbId,
            videos = videos,
            video = video,
            title = title,
            backdropPath = backdropPath,
            revenue = revenue,
            credits = credits,
            genres = genres,
            popularity = popularity,
            productionCountries = productionCountries,
            id = id,
            voteCount = voteCount,
            budget = budget,
            overview = overview,
            originalTitle = originalTitle,
            runtime = runtime,
            posterPath = posterPath,
            spokenLanguages = spokenLanguages,
            productionCompanies = productionCompanies,
            releaseDate = releaseDate,
            voteAverage = voteAverage,
            belongsToCollection = belongsToCollection,
            tagline = tagline,
            adult = adult,
            homepage = homepage,
            status = status,
            reviews = reviews
        )
    }
}