package com.citygrocer.customer.module

data class TermsandConditionsRes(val result: ArrayList<ConditionsResIp>?) : CommonRes()
data class ConditionsResIp(
        val _id: String?,
        val topic1 : String?,
        val topic2 : String?,
        val value1: String?,
        val value2: String?)