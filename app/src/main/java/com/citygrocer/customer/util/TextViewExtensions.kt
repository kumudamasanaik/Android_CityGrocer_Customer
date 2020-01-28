package com.citygrocer.customer.util

import android.graphics.Paint
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.content.res.AppCompatResources
import android.widget.TextView

fun TextView.underLine() {
    paint.flags = paint.flags or Paint.UNDERLINE_TEXT_FLAG
    paint.isAntiAlias = true
}

fun TextView.font(font: Int) {
    typeface = ResourcesCompat.getFont(this.context, font)
}

fun TextView.strikeThr() {
    paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

fun TextView.deleteLine() {
    paint.flags = paint.flags or Paint.STRIKE_THRU_TEXT_FLAG
    paint.isAntiAlias = true
}

fun TextView.setDrawableLeft(left: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this.context, left), null, null, null)
}
fun TextView.setDrawableRight(right: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(this.context, right), null)
}
fun TextView.removeDrawable() {
    this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
}


