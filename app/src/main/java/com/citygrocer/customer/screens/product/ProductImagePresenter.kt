package com.citygrocer.customer.screens.product

import android.content.Context
import com.citygrocer.customer.api.ApiService
import com.citygrocer.customer.module.input.AddCartIp
import com.citygrocer.customer.module.input.ProductImageViewIp
import com.citygrocer.customer.util.schedulers.BaseScheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ProductImagePresenter @Inject constructor(var apiService: ApiService,
                                                var context: Context,
                                                var baseScheduler: BaseScheduler) : ProductImageContract.Presenter {

    var view: ProductImageContract.View? = null
    private val subscription = CompositeDisposable()

    override fun takeView(view: ProductImageContract.View?) {
        this.view = view
    }

    override fun displayProductImageView(productImageViewIp: ProductImageViewIp) {
        subscription.add(apiService.getProductImageViewSku(productImageViewIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.showProductImageViewRes(res)
                }, { throwable -> view?.showErrorMsg(throwable) }))
    }

    override fun displayAddCart(addCartIp: AddCartIp) {
        subscription.add(apiService.addToCart(addCartIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.showAddCartRes(res)
                }, { throwable -> view?.showErrorMsg(throwable) }))
    }

    override fun displayRemoveFromCart(addCartIp: AddCartIp) {
        subscription.add(apiService.addToCart(addCartIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.showRemoveFromCart(res)
                }, { throwable -> view?.showErrorMsg(throwable) }))

    }

    override fun dropView() {
        subscription.clear()
        this.view = null
    }
}