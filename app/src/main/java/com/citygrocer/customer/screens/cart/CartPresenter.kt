package com.citygrocer.customer.screens.cart

import android.content.Context
import com.citygrocer.customer.api.ApiService
import com.citygrocer.customer.module.input.*

import com.citygrocer.customer.util.schedulers.BaseScheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CartPresenter @Inject constructor(var apiService: ApiService,
                                        var context: Context,
                                        var baseScheduler: BaseScheduler) : CartContract.Presenter {

    var view: CartContract.View? = null
    var subscription = CompositeDisposable()

    override fun takeView(view: CartContract.View?) {
        this.view = view
    }

    override fun loadCart(loginId: Int) {
        subscription.add(apiService.getCartData(loginId = loginId)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.setCartRes(res)
                },
                        { throwable -> view?.showErrorMsg(throwable) }))
    }

    override fun displayAddCart(addCartIp: AddCartIp) {
        subscription.add(apiService.addToCart(addCartIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.showAddedCartRes(res)
                }, { throwable -> view?.showErrorMsg(throwable) }))
    }

    override fun diaplayCartDeleteList(deleteCartIp: DeleteCartIp) {
        subscription.add(apiService.getDeleteCartList(deleteCartIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.showDeleteCartList(res)
                }, { throwable -> view?.showErrorMsg(throwable) }))
    }

    override fun diaplayCartSessionDeleteList(cartSessionIp: CartSessionIp) {
        subscription.add(apiService.getDeleteSessionCartList(cartSessionIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.showDeleteCartList(res)
                }, { throwable -> view?.showErrorMsg(throwable) }))
    }

    override fun loadCartCustomerId(productImageViewIp: CustomerIdIP) {
        subscription.add(apiService.getCartIdData(productImageViewIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.setCartRes(res)
                }, { throwable -> view?.showErrorMsg(throwable) }))
    }

    override fun loadCartSession(cartSessionIp: CartSessionIp) {
        subscription.add(apiService.getCartSessionData(cartSessionIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.setCartRes(res)
                }, { throwable -> view?.showErrorMsg(throwable) }))
    }

    override fun dropView() {
        subscription.clear()
        this.view = null
    }
}