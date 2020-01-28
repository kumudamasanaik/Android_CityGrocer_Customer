package com.citygrocer.customer.module

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CheckOutDeliveryRes(

        val result: ArrayList<CheckoutResultIp?>?) : CommonRes(), Parcelable

@Parcelize
data class CheckoutResultIp(
        val summary: ArrayList<Summery?>?,
        val productdata_arr: ArrayList<ProductdataArr?>?,
        val address: ArrayList<AddAddressResIP?>?,
        val delivery_slots: ArrayList<DeliverySlot?>?
) : Parcelable

data class Addres(
        val default_address: Int?,
        val _id: Int?,
        val _id_customer: Int?,
        val address: String?,
        val address_type: String?,
        val city: String?,
        val pincode: String?,
        val state: String?,
        val town: String?
)

@Parcelize
data class DeliverySlot(
        val date: String?,
        val data: ArrayList<Data?>?
) : Parcelable {
    @Parcelize
    data class Data(
            val _id: Int?,
            val slot_name: String?,
            val start_time: String?,
            val end_time: String?,
            val cutoff_time: String?,
            val generated_on: String?,
            val warehousee_id: Int?,
            val delivery_available: Boolean?,
            val slot_available: String?,
            var selected: Boolean = false
    ) : Parcelable
}

@Parcelize
data class Summery(
        val cart_count: Int?,
        val realization: String?,
        val cart_mrp_total: Int?,
        val selling_price: Int?,
        val cart_discount: Int?,
        val cart_price_total: Int?,
        val delivery_charge: Int?,
        val order_total: Int?
) : Parcelable

@Parcelize
data class ProductdataArr(
        val id: Int?,
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
        val sku: ArrayList<Sku?>?
) : Parcelable

@Parcelize
data class Sku(
        val id: Int?,
        val id_product: Int?,
        val size: String?,
        val stock: String?,
        val mrp: String?,
        val min_quantity: String?,
        val max_quantity: String?,
        val tax: String?,
        val selling_price: String?,
        val pic: String?,
        val mycart: Int?,
        val warehouse_id: Int?
) : Parcelable

@Parcelize
data class Pics(
        val pic: String?
) : Parcelable


