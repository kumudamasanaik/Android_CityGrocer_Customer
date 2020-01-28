package com.citygrocer.customer.screens.subcategory

import android.content.Context
import com.citygrocer.customer.api.ApiService
import com.citygrocer.customer.module.input.AddCartIp
import com.citygrocer.customer.module.input.ProductCategoryIp
import com.citygrocer.customer.module.input.ProductCategorySessionIp
import com.citygrocer.customer.util.schedulers.BaseScheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SubCategoryPresenter @Inject constructor(var apiService: ApiService,
                                               var context: Context,
                                               var baseScheduler: BaseScheduler) : SubCategoryContract.Presenter {

    var view: SubCategoryContract.View? = null
    private val subscription = CompositeDisposable()

    override fun takeView(view: SubCategoryContract.View?) {
        this.view = view
    }

    override fun displayProductCategory(productCategoryIp: ProductCategoryIp) {
        subscription.add(apiService.getProductCategory(productCategoryIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.showProductCategoryRes(res)
                }, { throwable -> view?.showErrorMsg(throwable) }))
    }

    override fun displayProductSessionCategory(productCategorySessionIp: ProductCategorySessionIp) {
        subscription.add(apiService.getProductCategorySession(productCategorySessionIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.showProductCategoryRes(res)
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