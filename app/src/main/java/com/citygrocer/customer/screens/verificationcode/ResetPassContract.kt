package com.citygrocer.customer.screens.verificationcode

import com.citygrocer.customer.BasePresenter
import com.citygrocer.customer.BaseView
import com.citygrocer.customer.module.CommonRes
import com.citygrocer.customer.module.CustomerRes
import com.citygrocer.customer.module.ResetPasswordRes
import com.citygrocer.customer.module.input.ResendOtpRes
import com.citygrocer.customer.module.input.ResetCodeIp
import com.citygrocer.customer.module.input.VerifyOtpIp

interface ResetPassContract {

    interface View : BaseView {
        fun showValidateFieldsError(msg: String)
        fun showResetPassword()
        fun resetPasswordRes(res : CustomerRes)
        fun resendOtp()
        fun showOtpReqRes(res: CommonRes)
    }

    interface Presenter : BasePresenter<View> {
        fun validate(resetCodeIP: ResetCodeIp):Boolean
        fun resetPassword(resetCodeIP: ResetCodeIp)
        fun resendOtp(resetCodeIp: ResetCodeIp)
    }
}