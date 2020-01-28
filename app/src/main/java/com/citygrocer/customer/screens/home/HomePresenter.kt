package com.citygrocer.customer.screens.home

import android.content.Context
import com.citygrocer.customer.api.ApiService
import com.citygrocer.customer.util.schedulers.BaseScheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HomePresenter @Inject constructor(var apiService: ApiService,
                                        var context: Context,
                                        var baseScheduler: BaseScheduler) : HomeContract.Presenter {
    var view: HomeContract.View? = null
    private val subscription = CompositeDisposable()

    override fun takeView(view: HomeContract.View?) {
        this.view = view
        displayHomePageData()
    }
    override fun displayHomePageData() {
        subscription.add(apiService.getHomeData()
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.showHomePageRes(res)
                },
                        { throwable -> view?.showErrorMsg(throwable) }))
    }

    override fun dropView() {
        subscription.clear()
        this.view = null
    }
}