package com.citygrocer.customer.module


data class ProductImageViewRes(
        val data: ArrayList<ProductImageViewResIp?>?) : CommonRes()

data class ProductImageViewResIp(
        val _id: Int?,
        val _id_category: Int?,
        val _id_subcategory: Int?,
        val _id_lowcategory: Int?,
        val best_price: String?,
        val mrp: String?,
        val selling_price: String?,
        val pic: String?,
        val name: String?,
        val description: String?,
        val article_no: String?,
        val brand: String?,
        val createOn: String?,
        val modifyOn: String?,
        val skudata: ArrayList<Skudata?>?,
        var selSku: Skudata? = null
)

data class Skudata(
        var cart_count: Int = 0,
        var tempMyCart: Int = -1,
        val _id: Int?,
        val _id_product: Int?,
        val size: String?,
        val stock: String?,
        val mrp: String?,
        val min_quantity: String?,
        val max_quantity: String?,
        val tax: String?,
        val selling_price: String?,
        val pic: String?,
        val createOn: String?,
        val modifyOn: String?
)

