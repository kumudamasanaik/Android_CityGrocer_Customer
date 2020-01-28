package com.citygrocer.customer.screens.otpverification

import com.citygrocer.customer.BasePresenter
import com.citygrocer.customer.BaseView
import com.citygrocer.customer.module.CommonRes
import com.citygrocer.customer.module.CustomerRes
import com.citygrocer.customer.module.input.MergeCartIp
import com.citygrocer.customer.module.input.ResetCodeIp
import com.citygrocer.customer.module.input.VerifyOtpIp

interface OtpVerificationContract {

    interface View : BaseView {
        fun showSubmitOtp()
        fun showOtpVerifyRes(res: CustomerRes)
        fun resendOtp()
        fun showOtpReqRes(res: CommonRes)
        fun showMergeCartRes(res: CommonRes)
    }

    interface Presenter : BasePresenter<View> {
        fun verifyOtp(verifyOtpIp: VerifyOtpIp)
        fun resendOtp(resetCodeIp: ResetCodeIp)
        fun mergeCart(mergeCartIp: MergeCartIp)
    }
}