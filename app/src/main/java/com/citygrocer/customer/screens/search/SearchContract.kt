package com.citygrocer.customer.screens.search

import com.citygrocer.customer.BasePresenter
import com.citygrocer.customer.BaseView
import com.citygrocer.customer.module.AddCartRes
import com.citygrocer.customer.module.ProductCategoryRes
import com.citygrocer.customer.module.input.AddCartIp
import com.citygrocer.customer.module.input.SearchIp

interface SearchContract {

    interface View : BaseView {
        fun showSearchRes(searchRes: ProductCategoryRes)
        fun showAddedCartRes(addCartRes: AddCartRes)
        fun showRemoveFromCart(addCartRes: AddCartRes)
    }

    interface Presenter : BasePresenter<View> {
        fun displaysearchData(searchIp: SearchIp)
        fun displayAddCart(addCartIp: AddCartIp)
    }
}