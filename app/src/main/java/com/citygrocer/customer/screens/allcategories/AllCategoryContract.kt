package com.citygrocer.customer.screens.allcategories

import com.citygrocer.customer.BasePresenter
import com.citygrocer.customer.BaseView
import com.citygrocer.customer.module.CategorySubCategoryRes

interface AllCategoryContract {

    interface View : BaseView {
        fun showCategoryRes(subCategoryRes: CategorySubCategoryRes)
    }

    interface Presenter : BasePresenter<View> {
        fun displayCategory()
    }
}