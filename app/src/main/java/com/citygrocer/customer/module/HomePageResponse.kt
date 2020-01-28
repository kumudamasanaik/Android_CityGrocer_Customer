package com.citygrocer.customer.module

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class HomePageResponse(val result: HomeResponseIp) : CommonRes(), Parcelable

@Parcelize
data class HomeResponseIp(
        var banner: ArrayList<BannerIp>?,
        var category: ArrayList<CategoryIp>?,
        var myshopbanner: Myshopbanner?,
        var cgbanner: CGbanner?,
        var products: ArrayList<ProductIp>?,
        var offerzone: ArrayList<OfferZone>?,
        var bannerad: BannerAd?,
        var deals: ArrayList<DealIp>?,
        var bannershare: Bannershare?
) : Parcelable

@Parcelize
data class CategoryIp(
        var _id: Int?,
        var parent: String?,
        var description: String?,
        var name: String?,
        var pic: String?
) : Parcelable

@Parcelize
data class BannerIp(
        var _id: Int?,
        var table: String?,
        var bannerType: String?,
        var picWeb: String?,
        var pic: String?,
        var type: String?,
        var target: String?,
        var sequence: String?,
        var block: String?
) : Parcelable

@Parcelize
data class Myshopbanner(
        var _id: Int,
        var table: String,
        var bannerType: String,
        var picWeb: String,
        var pic: String,
        var type: String,
        var target: String,
        var sequence: String,
        var block: String
) : Parcelable

@Parcelize
data class CGbanner(
        var _id: Int,
        var table: String,
        var bannerType: String,
        var picWeb: String,
        var pic: String,
        var type: String,
        var target: String,
        var sequence: String,
        var block: String
) : Parcelable

@Parcelize
data class ProductIp(
        var _id: Int?,
        var _id_subcategory: Int?,
        var _id_lowcategory: Int?,
        var name: String?,
        var pic: ArrayList<Pic>?,
        var description: String?,
        var article_no: String?,
        var brand: String?,
        var best_price: String?,
        var mrp: String?,
        var selling_price: String?
        //var sku: ArrayList<Sku>
) : Parcelable

/*@Parcelize
data class Sku(
        var id: Int,
        var idProduct: Int,
        var size: String,
        var stock: String,
        var mrp: String,
        var minQuantity: String,
        var maxQuantity: String,
        var tax: String,
        var sellingPrice: String,
        var pic: String
) : Parcelable*/

@Parcelize
data class OfferZone(
        var _id: Int,
        var table: String,
        var bannerType: String,
        var picWeb: String,
        var pic: String,
        var type: String,
        var target: String,
        var sequence: String,
        var block: String
) : Parcelable

@Parcelize
data class BannerAd(
        var _id: Int,
        var table: String,
        var bannerType: String,
        var picWeb: String,
        var pic: String,
        var type: String,
        var target: String,
        var sequence: String,
        var block: String
) : Parcelable

@Parcelize
data class DealIp(
        var _id: String?,
        var idCategory: String?,
        var categoryName: String?,
        var pic: String?,
        var mrp: String?,
        var sellingPrice: String?,
        var productUnit: String?,
        var articalNo: String?,
        var description: String?,
        var sold: String?
) : Parcelable

@Parcelize
data class Bannershare(
        var _id: Int,
        var table: String,
        var bannerType: String,
        var picWeb: String,
        var pic: String,
        var type: String,
        var target: String,
        var sequence: String,
        var block: String
) : Parcelable

