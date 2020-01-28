package com.citygrocer.customer.module

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategorySubCategoryRes(val category: ArrayList<CategorySubCategoryResIP>?, val cart_count: Int?) : CommonRes(), Parcelable

@Parcelize
data class CategorySubCategoryResIP(
        val _id: Int?,
        val name: String?,
        val pic: String?,
        val description: String?,
        var selected: Boolean = false,
        val sub_cat_data: ArrayList<SubcategoryIP>?) : Parcelable

@Parcelize
data class SubcategoryIP(val _id: Int?,
                         val name: String?,
                         val _id_category: Int?,
                         val pic: String?,
                         val description: String?,
                         val low_getdata_result: ArrayList<LowestSubCategoriesIp>?) : Parcelable

@Parcelize
data class LowestSubCategoriesIp(val _id: Int?,
                                 val name: String?,
                                 val _id_subcategory: Int?,
                                 val description: String?) : Parcelable

