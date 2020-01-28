package com.citygrocer.customer.module

data class CategoryRes(val result: ArrayList<CategoryResIP>?) : CommonRes()

data class CategoryResIP(
        val _id: String?,
        val name: String?,
        val parent: String?,
        val pic: String?,
        val description: String?)
