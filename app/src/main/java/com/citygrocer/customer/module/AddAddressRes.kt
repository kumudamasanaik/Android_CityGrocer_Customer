package com.citygrocer.customer.module

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class AddAddressRes(val result: ArrayList<AddAddressResIP?>?) : CommonRes()

@Parcelize
data class AddAddressResIP(
        val _id_customer: Int?,
        val pincode: String?,
        val state: String?,
        val city: String?,
        val address: String?,
        val address_type: Int?,
        val _id: Int?,
        val town: String?,
        val default_address: Int?
) : Parcelable {

    fun getAddressString(): String {
        return StringBuilder().append(address).append("," + "\t").append(town).append("," + "\t").append(city).append("," + "\t").append(state).append("-").append(pincode).toString()
    }
}
