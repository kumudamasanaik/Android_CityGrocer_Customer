package com.citygrocer.customer.module

data class ResetPasswordRes (val result: ArrayList<ResetResIP>?) : CommonRes()


data class ResetResIP (
        val verified: String? = "",
        val _id: Int? = 0,
        val mobilenumber: String? = "",
        val password: String? = "",
        val email: String? = "",
        val otp: String? = "",
        val image: String? = "",
        val dob: String? = "",
        val lastname: String? = "",
        val firstname: String? = "")


