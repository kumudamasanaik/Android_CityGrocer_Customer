package com.citygrocer.customer.module

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class BannerRes(val result: ArrayList<BannerResIP>?) : CommonRes()

@Parcelize
data class BannerResIP(
        val _id: String? = null,
        val pic_web: String? = null,
        val pic: String? = null,
        val type: String? = null,
        val target: String? = null,
        val sequence: String? = null,
        val block: String? = null) : Parcelable