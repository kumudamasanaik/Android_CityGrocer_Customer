package com.citygrocer.customer.screens.changepassword

import com.citygrocer.customer.BasePresenter
import com.citygrocer.customer.BaseView
import com.citygrocer.customer.module.ChangePassRes
import com.citygrocer.customer.module.input.ChangePassIp

interface ChangePassContract {

    interface View : BaseView {
        fun showValidateFieldsError(msg: String)
        fun showChangePasswordLoader()
        fun changePassRes(res : ChangePassRes)
    }

    interface Presenter : BasePresenter<View> {
        fun validate(changePassIp: ChangePassIp):Boolean
        fun changePassword(changePassIp: ChangePassIp)
    }
}