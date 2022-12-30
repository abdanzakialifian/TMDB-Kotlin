package com.application.zaki.movies.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

fun ImageView.loadImageUrl(url: String) {
    Glide.with(context)
        .load("https://image.tmdb.org/t/p/w500/$url")
        .into(this)
}

fun ImageView.loadBackdropImageUrl(url: String) {
    Glide.with(context)
        .load("https://image.tmdb.org/t/p/w1280/$url")
        .into(this)
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun String.convertDateText(formatTo: String, formatFrom: String): String {
    return if (this.isNotEmpty()) {
        val fromFormat = SimpleDateFormat(formatFrom, Locale.getDefault())
        val date = fromFormat.parse(this)

        val calendarFormat = SimpleDateFormat(formatTo, Locale.getDefault())
        date?.let { calendarFormat.format(it) }.toString()
    } else {
        this
    }
}

fun String.getInitialName(): String {
    val splitName = this.trim().split(" ")
    val first = splitName.firstOrNull()?.take(1) ?: ""

    return if (splitName.size > 1) {
        val second = splitName.lastOrNull()?.take(1) ?: ""
        (first + second).uppercase()
    } else first.uppercase()
}