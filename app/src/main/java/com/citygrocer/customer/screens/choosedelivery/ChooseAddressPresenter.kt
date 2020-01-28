package com.citygrocer.customer.screens.choosedelivery

import android.content.Context
import com.citygrocer.customer.api.ApiService
import com.citygrocer.customer.module.input.DeleteAddressIp
import com.citygrocer.customer.module.input.DeleteCartIp
import com.citygrocer.customer.util.schedulers.BaseScheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ChooseAddressPresenter @Inject constructor(var apiService: ApiService,
                                                 var context: Context,
                                                 var baseScheduler: BaseScheduler) : ChooseAddressContract.Presenter {
    var view: ChooseAddressContract.View? = null
    private val subscription = CompositeDisposable()

    override fun takeView(view: ChooseAddressContract.View?) {
        this.view = view
    }

    override fun getChooseAddressDeatails(deleteCartIp: DeleteCartIp) {
        subscription.add(apiService.getAddressList(deleteCartIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.showChooseAddressRes(res)
                }, { throwable -> view?.showErrorMsg(throwable) }))
    }

    override fun getDeletaAddressDetails(deleteAddressIp: DeleteAddressIp) {
        subscription.add(apiService.getDeleteAddress(deleteAddressIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.showDeleteAddressRes(res)
                }, { throwable -> view?.showErrorMsg(throwable) }))
    }

    override fun getSelectedAddressData(deleteAddressIp: DeleteAddressIp) {
        subscription.add(apiService.getSelectedAddress(deleteAddressIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.showSelectedAddressRes(res)
                }, { throwable -> view?.showErrorMsg(throwable) }))
    }

    override fun dropView() {
        subscription.clear()
        this.view = null
    }
}