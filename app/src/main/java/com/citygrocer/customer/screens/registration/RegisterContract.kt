package com.citygrocer.customer.screens.registration

import com.citygrocer.customer.BasePresenter
import com.citygrocer.customer.BaseView
import com.citygrocer.customer.module.CustomerRes
import com.citygrocer.customer.module.input.CustomerIdIP
import com.citygrocer.customer.module.input.RegisterIp

interface RegisterContract {

    interface View : BaseView {
        fun showValidateFieldsError(msg: String)
        fun showRegisterLoader()
        fun showRegisterRes(res: CustomerRes)
        fun showEditResponse(res: CustomerRes)
    }

    interface Presenter : BasePresenter<View> {
        fun validate(registerIp: RegisterIp): Boolean
        fun doRegister(registerIp: RegisterIp)
        fun editRegisterData(productImageViewIp: CustomerIdIP)
        fun updateRegisterData(registerIp: RegisterIp)
    }
}