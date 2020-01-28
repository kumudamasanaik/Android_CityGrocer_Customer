package com.citygrocer.customer.screens.product

import com.citygrocer.customer.BasePresenter
import com.citygrocer.customer.BaseView
import com.citygrocer.customer.module.AddCartRes
import com.citygrocer.customer.module.ProductCategoryRes
import com.citygrocer.customer.module.input.AddCartIp
import com.citygrocer.customer.module.input.ProductImageViewIp

interface ProductImageContract {

    interface View : BaseView {
        fun showProductImageViewRes(productCategoryRes: ProductCategoryRes)
        fun showAddCartRes(addCartRes: AddCartRes)
        fun showRemoveFromCart(addCartRes: AddCartRes)
    }

    interface Presenter : BasePresenter<ProductImageContract.View> {
        fun displayProductImageView(productImageViewIp: ProductImageViewIp)
        fun displayAddCart(addCartIp: AddCartIp)
        fun displayRemoveFromCart(addCartIp: AddCartIp)
    }
}