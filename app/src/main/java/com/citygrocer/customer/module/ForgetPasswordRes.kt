package com.citygrocer.customer.module

data class ForgetPasswordRes (val result: ArrayList<ForgotPassResIP>?) : CommonRes()
data class ForgotPassResIP(val otp: String? = null, val mobilenumber: String? = null)