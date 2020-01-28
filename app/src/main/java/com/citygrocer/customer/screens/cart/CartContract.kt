package com.citygrocer.customer.screens.cart

import com.citygrocer.customer.BasePresenter
import com.citygrocer.customer.BaseView
import com.citygrocer.customer.module.AddCartRes
import com.citygrocer.customer.module.CommonRes
import com.citygrocer.customer.module.ProductCategoryRes
import com.citygrocer.customer.module.input.*

interface CartContract {

    interface View : BaseView {
        fun setCartRes(productListRes: ProductCategoryRes)
        fun showCartSummery()
        fun showAddedCartRes(addCartRes: AddCartRes)
        fun showRemoveFromCart(addCartRes: AddCartRes)
        fun showDeleteCartList(res: CommonRes)
    }

    interface Presenter : BasePresenter<View> {
        fun loadCart(loginId: Int)
        fun loadCartCustomerId(productImageViewIp: CustomerIdIP)
        fun loadCartSession(cartSessionIp: CartSessionIp)
        fun displayAddCart(addCartIp: AddCartIp)
        fun diaplayCartDeleteList(deleteCartIp: DeleteCartIp)
        fun diaplayCartSessionDeleteList(cartSessionIp: CartSessionIp)
    }
}