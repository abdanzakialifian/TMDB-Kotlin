package com.application.tmdb.core.source.local

import com.application.tmdb.core.source.local.datastore.TMDBDataStoreManager
import io.reactivex.Flowable
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val tmdbDataStoreManager: TMDBDataStoreManager) {
    fun saveTMDBTheme(isDarkMode: Boolean) {
        tmdbDataStoreManager.saveTMDBTheme(isDarkMode)
    }

    fun getTMDBTheme(): Flowable<Boolean> = tmdbDataStoreManager.getTMDBTheme()
}