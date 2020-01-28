package com.citygrocer.customer.module

open class CommonRes {

    var status: String? = null
    var message: String? = null
    var otp: String? = null
    var key: String? = null
    var code: String? = null

    fun isSuccess(): Boolean = (status != null && status!!.contentEquals("success"))
}