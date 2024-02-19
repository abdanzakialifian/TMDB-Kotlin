package com.application.tmdb.domain.usecase

import com.application.tmdb.domain.interfaces.IOtherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveTMDBTheme @Inject constructor(private val iOtherRepository: IOtherRepository){
    operator fun invoke(isDarkMode: Boolean) {
        iOtherRepository.saveTMDBTheme(isDarkMode)
    }
}