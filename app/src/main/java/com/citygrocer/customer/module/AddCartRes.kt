package com.citygrocer.customer.module

data class AddCartRes(val result: AddCartResIp, val summary: Int?) : CommonRes()

data class AddCartResIp(val _id_product: Int?,
                        val _id_sku: Int?,
                        val _id_customer: String?,
                        val quantity: String?,
                        val session_id: String?,
                        val _id_warehouse: String?,
                        val _id: String)