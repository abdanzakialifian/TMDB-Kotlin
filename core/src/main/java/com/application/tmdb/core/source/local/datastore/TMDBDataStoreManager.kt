package com.application.tmdb.core.source.local.datastore

import android.R.attr.key
import android.R.attr.value
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder
import com.application.tmdb.common.utils.Constant
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TMDBDataStoreManager @Inject constructor(@ApplicationContext context: Context) {
    private var dataStore = RxPreferenceDataStoreBuilder(context, Constant.KEY_DATA_STORE_MANAGER).build()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun saveTMDBTheme(isDarkMode: Boolean) {
        dataStore.updateDataAsync { preferences ->
            val mutablePreferences = preferences.toMutablePreferences()
            mutablePreferences[KEY_TMDB_THEME] = isDarkMode
            Single.just(mutablePreferences)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getTMDBTheme(): Flowable<Boolean> = dataStore.data().map { preferences ->
        preferences[KEY_TMDB_THEME]
    }

    companion object {
        private val KEY_TMDB_THEME = booleanPreferencesKey("key_tmdb_theme")
    }
}