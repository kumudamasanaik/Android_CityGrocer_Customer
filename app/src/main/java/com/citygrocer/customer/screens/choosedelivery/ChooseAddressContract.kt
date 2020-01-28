package com.citygrocer.customer.screens.choosedelivery

import com.citygrocer.customer.BasePresenter
import com.citygrocer.customer.BaseView
import com.citygrocer.customer.module.AddAddressRes
import com.citygrocer.customer.module.input.DeleteAddressIp
import com.citygrocer.customer.module.input.DeleteCartIp

interface ChooseAddressContract {
    interface View : BaseView {
        fun showChooseAddressRes(res: AddAddressRes)
        fun showDeleteAddressRes(res: AddAddressRes)
        fun showSelectedAddressRes(res: AddAddressRes)
    }

    interface Presenter : BasePresenter<View> {
        fun getChooseAddressDeatails(deleteCartIp: DeleteCartIp)
        fun getDeletaAddressDetails(deleteAddressIp: DeleteAddressIp)
        fun getSelectedAddressData(deleteAddressIp: DeleteAddressIp)

    }
}