package com.citygrocer.customer.screens.delivery

import com.citygrocer.customer.BasePresenter
import com.citygrocer.customer.BaseView
import com.citygrocer.customer.module.CheckOutDeliveryRes
import com.citygrocer.customer.module.input.CheckOutIp

interface DeliveryContract {

    interface View : BaseView {
        fun showCheckoutDetails(res: CheckOutDeliveryRes)
    }

    interface Presenter : BasePresenter<View> {
        fun getDeliveryDeatails(checkOutIp: CheckOutIp)
    }
}