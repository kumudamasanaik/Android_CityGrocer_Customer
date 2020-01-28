package com.citygrocer.customer.module

data class CustomerRes(val result: RegisterResIp?, val token: String?=null) : CommonRes()

data class RegisterResIp(val status: String? = null,
                         val verified: String? = null,
                         val _id: Int? = null,
                         val mobilenumber: String? = null,
                         val email: String? = null,
                         val firstname: String? = null,
                         val lastname: String? = null,
                         val gender: String? = null,
                         val referal_code: String? = null,
                         val password: String? = null,
                         val social_id: String? = null,
                         val otp: String? = null)
