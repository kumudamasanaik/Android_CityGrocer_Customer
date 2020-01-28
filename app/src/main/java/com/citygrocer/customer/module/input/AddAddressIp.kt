package com.citygrocer.customer.module.input


data class AddAddressIp(
        val _id_customer: Int?,
        val pincode: String?,
        val state: String?,
        val city: String?,
        val town: String?,
        val address: String?,
        val address_type: Int?,
        val _id: Int? = 0
)