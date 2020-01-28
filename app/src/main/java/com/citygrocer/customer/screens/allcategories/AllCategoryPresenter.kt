package com.citygrocer.customer.screens.allcategories

import android.content.Context
import com.citygrocer.customer.api.ApiService
import com.citygrocer.customer.util.schedulers.BaseScheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AllCategoryPresenter @Inject constructor(var apiService: ApiService,
                                               var context: Context,
                                               var baseScheduler: BaseScheduler) : AllCategoryContract.Presenter {

    var view: AllCategoryContract.View? = null
    private val subscription = CompositeDisposable()

    override fun takeView(view: AllCategoryContract.View?) {
        this.view = view
        displayCategory()
    }

    override fun displayCategory() {
        subscription.add(apiService.categorySubCategory()
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.showCategoryRes(res) },
                        { throwable -> view?.showErrorMsg(throwable) })) }

    override fun dropView() {
        subscription.clear()
        this.view = null
    }
}
