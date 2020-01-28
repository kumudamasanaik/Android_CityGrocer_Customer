package com.citygrocer.customer.screens.search

import android.content.Context
import com.citygrocer.customer.api.ApiService
import com.citygrocer.customer.module.input.AddCartIp
import com.citygrocer.customer.module.input.SearchIp
import com.citygrocer.customer.util.schedulers.BaseScheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SearchPresenter @Inject constructor(var apiService: ApiService,
                                          var context: Context,
                                          var baseScheduler: BaseScheduler) : SearchContract.Presenter {

    var view: SearchContract.View? = null
    private val subscription = CompositeDisposable()

    override fun takeView(view: SearchContract.View?) {
        this.view = view
    }

    override fun displaysearchData(searchIp: SearchIp) {
        subscription.add(apiService.getSearchViewData(searchIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.showSearchRes(res)
                }, { throwable -> view?.showErrorMsg(throwable) }))
    }

    override fun displayAddCart(addCartIp: AddCartIp) {
        subscription.add(apiService.addToCart(addCartIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.showAddedCartRes(res)
                }, { throwable -> view?.showErrorMsg(throwable) }))
    }

    override fun dropView() {
        subscription.clear()
        this.view = null
    }
}