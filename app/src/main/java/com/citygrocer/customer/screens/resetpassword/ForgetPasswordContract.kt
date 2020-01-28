package com.citygrocer.customer.screens.resetpassword

import com.citygrocer.customer.BasePresenter
import com.citygrocer.customer.BaseView
import com.citygrocer.customer.module.ForgetPasswordRes
import com.citygrocer.customer.module.input.ForgetPasswordIp

interface ForgetPasswordContract {

    interface View : BaseView {
        fun showValidateFieldsError(msg: String)
        fun showForgotPasswordLoader()
        fun ForgotPasswordRes(res: ForgetPasswordRes)
    }

    interface Presenter : BasePresenter<View> {
        fun validate(forgotPasswordIP: ForgetPasswordIp):Boolean
        fun ForgotPass(forgotPasswordIP: ForgetPasswordIp)
    }
}