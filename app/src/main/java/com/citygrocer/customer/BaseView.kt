package com.citygrocer.customer

import com.citygrocer.customer.module.ProductCategoryRes


interface BaseView {

    fun initScreen()
    fun showErrorMsg(throwable: Throwable, apiType: String = "none")
    fun showSuccessMsg(any: Any) {}
    fun showProgress(msg: String = "Please wait") {}
    fun hideProgress() {}
    fun showViewState(state: Int) {}
}
