package com.citygrocer.customer.screens.home

import com.citygrocer.customer.BasePresenter
import com.citygrocer.customer.BaseView
import com.citygrocer.customer.module.HomePageResponse

interface HomeContract {

    interface View : BaseView {
        fun showHomePageRes(homeRes: HomePageResponse)
    }

    interface Presenter : BasePresenter<View> {
        fun displayHomePageData()
    }
}