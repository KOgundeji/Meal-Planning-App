package com.kunle.aisle9b.util

import android.graphics.Typeface
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration


fun Spanned.toAnnotatedString(): AnnotatedString = buildAnnotatedString {
    val spanned = this@toAnnotatedString
    append(spanned.toString())
    getSpans(0, spanned.length, Any::class.java).forEach { span ->
        val start = getSpanStart(span)
        val end = getSpanEnd(span)
        when (span) {
            is StyleSpan -> when (span.style) {
                Typeface.BOLD -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
                Typeface.ITALIC -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
                Typeface.BOLD_ITALIC -> addStyle(SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic), start, end)
            }
            is UnderlineSpan -> addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
            is ForegroundColorSpan -> addStyle(SpanStyle(color = Color(span.foregroundColor)), start, end)
        }
    }
}


private val tags = linkedMapOf(
    "<b>" to "</b>",
    "<i>" to "</i>",
    "<u>" to "</u>"
)

fun String.parseHtml(): AnnotatedString {
    val newlineReplace = this.replace("<br>", "\n")

    return buildAnnotatedString {
        recurse(newlineReplace, this)
    }
}


private fun recurse(string: String, to: AnnotatedString.Builder) {
    val startTag = tags.keys.find { string.startsWith(it) }
    val endTag = tags.values.find { string.startsWith(it) }

    when {
        tags.any { string.startsWith(it.value) } -> {
            to.pop()
            recurse(string.removeRange(0, endTag!!.length), to)
        }
        tags.any { string.startsWith(it.key) } -> {
            to.pushStyle(tagToStyle(startTag!!))
            recurse(string.removeRange(0, startTag.length), to)
        }
        tags.any { string.contains(it.key) || string.contains(it.value) } -> {
            val firstStart = tags.keys.map { string.indexOf(it) }.filterNot { it == -1 }.minOrNull() ?: -1
            val firstEnd = tags.values.map { string.indexOf(it) }.filterNot { it == -1 }.minOrNull() ?: -1
            val first = when {
                firstStart == -1 -> firstEnd
                firstEnd == -1 -> firstStart
                else -> minOf(firstStart, firstEnd)
            }

            to.append(string.substring(0, first))

            recurse(string.removeRange(0, first), to)
        }
        else -> {
            to.append(string)
        }
    }
}

private fun tagToStyle(tag: String): SpanStyle {
    return when (tag) {
        "<b>" -> {
            SpanStyle(fontWeight = FontWeight.Bold)
        }
        "<i>" -> {
            SpanStyle(fontStyle = FontStyle.Italic)
        }
        "<u>" -> {
            SpanStyle(textDecoration = TextDecoration.Underline)
        }
        else -> throw IllegalArgumentException("Tag $tag is not valid.")
    }
}