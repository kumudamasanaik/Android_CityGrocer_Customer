package com.citygrocer.customer.screens.deliveryaddress

import com.citygrocer.customer.BasePresenter
import com.citygrocer.customer.BaseView
import com.citygrocer.customer.module.AddAddressRes
import com.citygrocer.customer.module.input.AddAddressIp
import com.citygrocer.customer.module.input.DeleteAddressIp

interface AddDeliveryAddressContract {

    interface View : BaseView {
        fun showValidateFieldsError(msg: String)
        fun showAddAddressRes(res: AddAddressRes)
        fun showEditAddressRes(res: AddAddressRes)
        fun showUpdatedAddressRes(res: AddAddressRes)
    }

    interface Presenter : BasePresenter<View> {
        fun validate(addressIp: AddAddressIp): Boolean
        fun getAddAddressDeatails(addressIp: AddAddressIp)
        fun getEditAddressDetails(deleteAddressIp: DeleteAddressIp)
        fun getUpdatedAddressDetails(addressIp: AddAddressIp)
    }
}