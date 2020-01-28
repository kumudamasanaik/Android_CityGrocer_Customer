package com.citygrocer.customer.module

data class ChangePassRes (val result: ArrayList<ChangePasswordResIP>?) : CommonRes()

data class ChangePasswordResIP (
        val verified: String? = null,
        val _id: String? = null,
        val mobilenumber: String? = null,
        val email: String? = null,
        val password: String? = null,
        val otp: String? = null,
        val dob: String? = null,
        val lastname: String? = null,
        val firstname: String? = null)
