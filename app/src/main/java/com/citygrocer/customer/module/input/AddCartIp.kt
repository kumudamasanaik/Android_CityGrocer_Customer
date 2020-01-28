package com.citygrocer.customer.module.input

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddCartIp(val _id_product: Int?,
                     val _id_sku: Int?,
                     val _id_customer: Int?,
                     var quantity: Int?,
                     val session_id: String?,
                     var _id_warehouse: Int?) : Parcelable