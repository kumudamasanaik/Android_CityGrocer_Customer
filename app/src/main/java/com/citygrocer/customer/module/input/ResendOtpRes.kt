package com.citygrocer.customer.module.input

import com.citygrocer.customer.module.CommonRes

data class ResendOtpRes (val result: ArrayList<ResendOtpIp>?): CommonRes()

data class ResendOtpIp(
        val verified: String? = "",
        val _id: Int? = 0,
        val mobilenumber: String? = "",
        val email: String? = "",
        val firstname: String? = "",
        val lastname: String? = "",
        val password: String? = "",
        val referal_code: String ? = "",
        val gender : String? = "",
        val otp: String? = ""
)