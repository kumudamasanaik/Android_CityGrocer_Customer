package com.citygrocer.customer.screens.termsofservice

import com.citygrocer.customer.BasePresenter
import com.citygrocer.customer.BaseView
import com.citygrocer.customer.module.TermsandConditionsRes

interface TermsOfServiceContract {

    interface View : BaseView {
        fun showTermsOfServiceRes(termsandConditionsRes: TermsandConditionsRes)
    }

    interface Presenter : BasePresenter<TermsOfServiceContract.View> {
        fun displayTermsOfService()
    }
}