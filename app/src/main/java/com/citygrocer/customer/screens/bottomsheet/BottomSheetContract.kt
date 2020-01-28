package com.citygrocer.customer.screens.bottomsheet

import com.citygrocer.customer.BasePresenter
import com.citygrocer.customer.BaseView
import com.citygrocer.customer.module.CommonRes
import com.citygrocer.customer.module.CustomerRes
import com.citygrocer.customer.module.input.MergeCartIp
import com.citygrocer.customer.module.input.SocialRegModelIp

interface BottomSheetContract {

    interface View : BaseView {
        fun callSocialSignInApi()
        fun socailLoginRes(res: CustomerRes)
        fun showMergeCartRes(res: CommonRes)
    }

    interface Presenter : BasePresenter<View> {
        fun doSocialsignUp(inputModel: SocialRegModelIp)
        fun mergeCart(mergeCartIp: MergeCartIp)
    }
}