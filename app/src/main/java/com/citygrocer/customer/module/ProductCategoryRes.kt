package com.citygrocer.customer.module

import android.os.Parcelable
import com.citygrocer.customer.module.input.AddCartIp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductCategoryRes(val result: ArrayList<ProductCatResIp>?,
                              val summary: ArrayList<Summary?>?) : CommonRes(), Parcelable

@Parcelize
data class ProductCatResIp(val id: Int?,
                           val id_category: Int?,
                           val id_subcategory: Int?,
                           val id_lowcategory: Int?,
                           val name: String?,
                           val article_no: String?,
                           val mrp: String?,
                           val selling_price: String?,
                           val best_price: String?,
                           val description: String?,
                           val brand: String?,
                           val pic: ArrayList<Pic?>?,
                           var prodPos: Int = -1,
                           val sku: ArrayList<DataSku?>?,
                           var addCartIp: AddCartIp? = null,
                           var selSku: DataSku?) : Parcelable

@Parcelize
data class Pic(val pic: String?) : Parcelable

@Parcelize
data class DataSku(val id: Int?,
                   val id_product: Int?,
                   val size: String?,
                   val stock: String?,
                   val mrp: Float?,
                   val min_quantity: String?,
                   val max_quantity: String?,
                   val tax: String?,
                   val selling_price: Float?,
                   val pic: ArrayList<Pic?>?,
                   val product_name: String?,
                   var mycart: Int = 0,
                   var tempMyCart: Int = -1) : Parcelable

@Parcelize
data class Summary(val cart_count: Int? = 0,
                   val realization: String? = "0",
                   val mrp: Int? = 0,
                   val selling_price: Int? = 0,
                   val delivery_charge: Int? = 50,
                   val grand_total: Int? = 0) : Parcelable
