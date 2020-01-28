package com.citygrocer.customer.screens.delivery

import android.content.Context
import com.citygrocer.customer.api.ApiService
import com.citygrocer.customer.module.input.CheckOutIp
import com.citygrocer.customer.util.schedulers.BaseScheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class DeliveryPresenter @Inject constructor(var apiService: ApiService,
                                            var context: Context,
                                            var baseScheduler: BaseScheduler) : DeliveryContract.Presenter {
    var view: DeliveryContract.View? = null
    private val subscription = CompositeDisposable()

    override fun takeView(view: DeliveryContract.View?) {
        this.view = view
    }

    override fun getDeliveryDeatails(checkOutIp: CheckOutIp) {
        subscription.add(apiService.getCheckoutData(checkOutIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    if (res.isSuccess()) {
                        view?.showCheckoutDetails(res)
                    }
                }, { throwable -> view?.showErrorMsg(throwable) }))
    }

    override fun dropView() {
        subscription.clear()
        this.view = null
    }

}