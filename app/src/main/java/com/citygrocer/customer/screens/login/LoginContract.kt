package com.citygrocer.customer.screens.login

import com.citygrocer.customer.BasePresenter
import com.citygrocer.customer.BaseView
import com.citygrocer.customer.module.CommonRes
import com.citygrocer.customer.module.CustomerRes
import com.citygrocer.customer.module.input.LoginIp
import com.citygrocer.customer.module.input.MergeCartIp

interface LoginContract {

    interface View : BaseView {
        fun showValidateFieldsError(msg: String)
        fun showLoginLoader()
        fun setLoginRes(res: CustomerRes)
        fun showMergeCartRes(res: CommonRes)
    }

    interface Presenter : BasePresenter<View> {
        fun validate(loginIp: LoginIp): Boolean
        fun doLogin(loginIp: LoginIp)
        fun mergeCart(mergeCartIp: MergeCartIp)
    }
}