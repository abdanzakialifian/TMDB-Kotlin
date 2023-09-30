package com.application.zaki.movies.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.zaki.movies.R
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

fun ImageView.loadImageUrl(url: String) {
    Glide.with(context)
        .load("https://image.tmdb.org/t/p/w500/$url")
        .placeholder(R.color.grey200)
        .error(R.drawable.ic_broken_image)
        .into(this)
}

fun ImageView.loadBackdropImageUrl(url: String) {
    Glide.with(context)
        .load("https://image.tmdb.org/t/p/w1280/$url")
        .placeholder(R.color.grey200)
        .error(R.drawable.ic_broken_image)
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

fun <T> MutableLiveData<T>.toLiveData(): LiveData<T> = this

fun Activity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val inputMethodManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Int.fromMinutesToHHmm(): String {
    val hours = TimeUnit.MINUTES.toHours(this.toLong())
    val remainMinutes = this - TimeUnit.HOURS.toMinutes(hours)
    return String.format("%2dh %02dmin", hours, remainMinutes)
}