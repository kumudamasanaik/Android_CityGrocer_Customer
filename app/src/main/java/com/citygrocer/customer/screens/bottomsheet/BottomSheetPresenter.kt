package com.citygrocer.customer.screens.bottomsheet

import android.content.Context
import com.citygrocer.customer.api.ApiService
import com.citygrocer.customer.module.input.MergeCartIp
import com.citygrocer.customer.module.input.SocialRegModelIp
import com.citygrocer.customer.util.schedulers.BaseScheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class BottomSheetPresenter @Inject constructor(
        var apiService: ApiService,
        var context: Context,
        var baseScheduler: BaseScheduler) : BottomSheetContract.Presenter {

    var view: BottomSheetContract.View? = null
    private val subscription = CompositeDisposable()

    override fun takeView(view: BottomSheetContract.View?) {
        this.view = view
    }

    override fun doSocialsignUp(inputModel: SocialRegModelIp) {
        view?.showProgress()
        subscription.add(
                apiService.socialSignUp(inputModel)
                        .subscribeOn(baseScheduler.io())
                        .observeOn(baseScheduler.ui())
                        .subscribe(
                                { res ->
                                    view?.hideProgress()
                                    if (res.isSuccess()) {
                                        view?.socailLoginRes(res)
                                    } else
                                        view?.showErrorMsg(Throwable(res.message))
                                },
                                { throwable ->
                                    view?.hideProgress()
                                    view?.showErrorMsg(throwable)
                                }))
    }

    override fun mergeCart(mergeCartIp: MergeCartIp) {
        view?.showProgress()
        subscription.add(
                apiService.getCartMergeData(mergeCartIp)
                        .subscribeOn(baseScheduler.io())
                        .observeOn(baseScheduler.ui())
                        .subscribe(
                                { res ->
                                    view?.hideProgress()
                                    if (res.isSuccess()) {
                                        view?.showMergeCartRes(res)
                                    } else
                                        view?.showErrorMsg(Throwable(res.message))
                                },
                                { throwable ->
                                    view?.hideProgress()
                                    view?.showErrorMsg(throwable)
                                }))
    }

    override fun dropView() {
        subscription.clear()
        this.view = null
    }
}
