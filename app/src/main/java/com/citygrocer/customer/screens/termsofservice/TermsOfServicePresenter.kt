package com.citygrocer.customer.screens.termsofservice

import android.content.Context
import com.citygrocer.customer.api.ApiService
import com.citygrocer.customer.util.schedulers.BaseScheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class TermsOfServicePresenter @Inject constructor(var apiService: ApiService,
                                                  var context: Context,
                                                  var baseScheduler: BaseScheduler) : TermsOfServiceContract.Presenter {

    var view: TermsOfServiceContract.View? = null
    private val subscription = CompositeDisposable()

    override fun takeView(view: TermsOfServiceContract.View?) {
        this.view = view
        displayTermsOfService()
    }

    override fun displayTermsOfService() {
        subscription.add(apiService.getTermsAndConditions()
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.showTermsOfServiceRes(res)
                }, { throwable -> view?.showErrorMsg(throwable) }))
    }

    override fun dropView() {
        subscription.clear()
        this.view = null
    }
}