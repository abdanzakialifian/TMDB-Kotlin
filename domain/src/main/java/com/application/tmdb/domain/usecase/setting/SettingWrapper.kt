package com.application.tmdb.domain.usecase.setting

import com.application.tmdb.domain.usecase.GetTMDBTheme
import com.application.tmdb.domain.usecase.SaveTMDBTheme
import javax.inject.Inject

data class SettingWrapper @Inject constructor(
    val saveTMDBTheme: SaveTMDBTheme,
    val getTMDBTheme: GetTMDBTheme,
)