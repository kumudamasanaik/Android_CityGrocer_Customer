package com.citygrocer.customer.module


data class SocialLoginRes(
        val status: String?,
        val message: String?,
        val result: Result?,
        val token: String?
)

    data class Result(
            val status: String?,
            val verified: String?,
            val _id: String?,
            val mobilenumber: String?,
            val email: String?,
            val firstname: String?,
            val lastname: String?,
            val gender: String?,
            val referal_code: String?,
            val social_id: String?
    )
