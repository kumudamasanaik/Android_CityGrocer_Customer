package com.citygrocer.customer.util

fun String?.isNotPhone(): Boolean {
    /*val p = "^1([34578])\\d{9}\$".toRegex()
    return matches(p)*/
    if (isNullOrBlank())
        return true
    return (this!!.length < 10)
}

fun String?.isValidPassword(): Boolean {
    return this != null && this.trim().length > 15
}

fun String?.isNotEmail(): Boolean {
    if (isNullOrBlank())
        return true
    val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
    return !this!!.matches(p)
}

fun String.isNumeric(): Boolean {
    val p = "^[0-9]+$".toRegex()
    return matches(p)
}

fun isValidString(string: String?): Boolean {
    return string != null && string.trim { it <= ' ' }.length > 0
}

fun String.equalsIgnoreCase(other: String) = this.toLowerCase().contentEquals(other.toLowerCase())

