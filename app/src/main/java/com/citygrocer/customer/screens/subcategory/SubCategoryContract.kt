package com.citygrocer.customer.screens.subcategory

import com.citygrocer.customer.BasePresenter
import com.citygrocer.customer.BaseView
import com.citygrocer.customer.module.AddCartRes
import com.citygrocer.customer.module.ProductCategoryRes
import com.citygrocer.customer.module.input.AddCartIp
import com.citygrocer.customer.module.input.ProductCategoryIp
import com.citygrocer.customer.module.input.ProductCategorySessionIp

interface SubCategoryContract {

    interface View : BaseView {
        fun showProductCategoryRes(productCategoryRes: ProductCategoryRes)
        fun showAddedCartRes(addCartRes: AddCartRes)
        fun showRemoveFromCart(addCartRes: AddCartRes)

    }

    interface Presenter : BasePresenter<View> {
        fun displayProductCategory(productCategoryIp: ProductCategoryIp)
        fun displayProductSessionCategory(productCategorySessionIp: ProductCategorySessionIp)
        fun displayAddCart(addCartIp: AddCartIp)
    }
}