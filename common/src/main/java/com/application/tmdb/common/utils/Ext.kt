package com.application.tmdb.common.utils

import android.animation.LayoutTransition
import android.app.Activity
import android.content.Context
import android.text.Layout
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.StaticLayout
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.tmdb.common.R
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

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
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun String.convertDateText(formatTo: String, formatFrom: String): String {
    return if (isNotEmpty()) {
        val fromFormat = SimpleDateFormat(formatFrom, Locale.getDefault())
        val date = fromFormat.parse(this)

        val calendarFormat = SimpleDateFormat(formatTo, Locale.getDefault())
        date?.let { calendarFormat.format(it) }.toString()
    } else {
        this
    }
}

fun String.getInitialName(): String {
    val splitName = trim().split(" ")
    val first = splitName.firstOrNull()?.take(1) ?: ""

    return if (splitName.size > 1) {
        val second = splitName.lastOrNull()?.take(1) ?: ""
        (first + second).uppercase()
    } else first.uppercase()
}

fun <T> MutableLiveData<T>.toLiveData(): LiveData<T> = this

fun Activity.hideKeyboard() {
    val view = currentFocus
    if (view != null) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Int.fromMinutesToHHmm(): String {
    val hours = TimeUnit.MINUTES.toHours(toLong())
    val remainMinutes = this - TimeUnit.HOURS.toMinutes(hours)
    return String.format("%2dh %02dmin", hours, remainMinutes)
}

@Suppress("DEPRECATION")
fun TextView.setResizableText(
    fullText: String,
    maxLines: Int,
    viewMore: Boolean,
    viewGroup: ViewGroup? = null,
    applyExtraHighlights: ((Spannable) -> (Spannable))? = null,
) {
    val width = width
    if (width <= 0) {
        post {
            setResizableText(fullText, maxLines, viewMore, viewGroup, applyExtraHighlights)
        }
        return
    }
    movementMethod = LinkMovementMethod.getInstance()
    // Since we take the string character by character, we don't want to break up the Windows-style
    // line endings.
    val adjustedText = fullText.replace("\r\n", "\n")
    // Check if even the text has to be resizable.
    val textLayout = StaticLayout(
        adjustedText,
        paint,
        width - paddingLeft - paddingRight,
        Layout.Alignment.ALIGN_NORMAL,
        lineSpacingMultiplier,
        lineSpacingExtra,
        includeFontPadding
    )
    if (textLayout.lineCount <= maxLines || adjustedText.isEmpty()) {
        // No need to add 'read more' / 'read less' since the text fits just as well (less than max lines #).
        val htmlText = adjustedText.replace("\n", "<br/>")
        text = addClickablePartTextResizable(
            fullText,
            maxLines,
            HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
            null,
            viewMore,
            viewGroup,
            applyExtraHighlights,
        )
        return
    }
    val charactersAtLineEnd = textLayout.getLineEnd(maxLines - 1)
    val suffixText =
        if (viewMore) resources.getString(R.string.resizable_text_read_more) else resources.getString(
            R.string.resizable_text_read_less
        )
    var charactersToTake = charactersAtLineEnd - suffixText.length / 2 // Good enough first guess
    if (charactersToTake <= 0) {
        // Happens when text is empty
        val htmlText = adjustedText.replace("\n", "<br/>")
        text = addClickablePartTextResizable(
            fullText,
            maxLines,
            HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
            null,
            viewMore,
            viewGroup,
            applyExtraHighlights,
        )
        return
    }
    if (!viewMore) {
        // We can set the text immediately because nothing needs to be measured
        val htmlText = adjustedText.replace("\n", "<br/>")
        text = addClickablePartTextResizable(
            fullText,
            maxLines,
            HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
            suffixText,
            false,
            viewGroup,
            applyExtraHighlights,
        )
        return
    }
    val lastHasNewLine =
        adjustedText.substring(
            textLayout.getLineStart(maxLines - 1),
            textLayout.getLineEnd(maxLines - 1)
        )
            .contains("\n")
    val linedText = if (lastHasNewLine) {
        val charactersPerLine =
            textLayout.getLineEnd(0) / (textLayout.getLineWidth(0) / textLayout.ellipsizedWidth.toFloat())
        val lineOfSpaces =
            "\u00A0".repeat(charactersPerLine.roundToInt()) // non breaking space, will not be thrown away by HTML parser
        charactersToTake += lineOfSpaces.length - 1
        adjustedText.take(textLayout.getLineStart(maxLines - 1)) +
                adjustedText.substring(
                    textLayout.getLineStart(maxLines - 1),
                    textLayout.getLineEnd(maxLines - 1)
                )
                    .replace("\n", lineOfSpaces) +
                adjustedText.substring(textLayout.getLineEnd(maxLines - 1))
    } else {
        adjustedText
    }
    // Check if we perhaps need to even add characters? Happens very rarely, but can be possible if there was a long word just wrapped
    val shortenedString = linedText.take(charactersToTake)
    val shortenedStringWithSuffix = shortenedString + suffixText
    val shortenedStringWithSuffixLayout = StaticLayout(
        shortenedStringWithSuffix,
        paint,
        width - paddingLeft - paddingRight,
        Layout.Alignment.ALIGN_NORMAL,
        lineSpacingMultiplier,
        lineSpacingExtra,
        includeFontPadding
    )
    val modifier: Int
    if (shortenedStringWithSuffixLayout.getLineEnd(maxLines - 1) >= shortenedStringWithSuffix.length) {
        modifier = 1
        charactersToTake-- // We might just be at the right position already
    } else {
        modifier = -1
    }
    do {
        charactersToTake += modifier
        val baseString = linedText.take(charactersToTake)
        val appended = baseString + suffixText
        val newLayout = StaticLayout(
            appended,
            paint,
            width - paddingLeft - paddingRight,
            Layout.Alignment.ALIGN_NORMAL,
            lineSpacingMultiplier,
            lineSpacingExtra,
            includeFontPadding
        )
    } while ((modifier < 0 && newLayout.getLineEnd(maxLines - 1) < appended.length) ||
        (modifier > 0 && newLayout.getLineEnd(maxLines - 1) >= appended.length)
    )
    if (modifier > 0) {
        charactersToTake-- // We went overboard with 1 char, fixing that
    }
    // We need to convert newlines because we are going over to HTML now
    val htmlText = linedText.take(charactersToTake).replace("\n", "<br/>")
    text = addClickablePartTextResizable(
        fullText,
        maxLines,
        HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
        suffixText,
        true,
        viewGroup,
        applyExtraHighlights,
    )
}

private fun TextView.addClickablePartTextResizable(
    fullText: String,
    maxLines: Int,
    shortenedText: Spanned,
    clickableText: String?,
    viewMore: Boolean,
    viewGroup: ViewGroup? = null,
    applyExtraHighlights: ((Spannable) -> (Spannable))? = null,
): Spannable {
    val builder = SpannableStringBuilder(shortenedText)
    if (clickableText != null) {
        if (viewMore) {
            builder.append(" ... ").append(clickableText)
        } else {
            builder.append(" ").append(clickableText)
        }
        builder.setSpan(
            object : NoUnderlineClickSpan(context) {
                override fun onClick(widget: View) {
                    if (viewMore) {
                        setResizableText(fullText, maxLines, false, viewGroup, applyExtraHighlights)
                    } else {
                        setResizableText(fullText, maxLines, true, viewGroup, applyExtraHighlights)
                    }
                    val transition = LayoutTransition()
                    transition.setDuration(300)
                    transition.enableTransitionType(LayoutTransition.CHANGING)
                    viewGroup?.layoutTransition = transition
                }
            },
            builder.indexOf(clickableText) + 0,
            builder.indexOf(clickableText) + clickableText.length,
            0
        )
    }
    if (applyExtraHighlights != null) {
        return applyExtraHighlights(builder)
    }
    return builder
}

open class NoUnderlineClickSpan(private val context: Context) : ClickableSpan() {
    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.isUnderlineText = false
        textPaint.color = ContextCompat.getColor(context, R.color.red)
    }
    override fun onClick(widget: View) {}
}