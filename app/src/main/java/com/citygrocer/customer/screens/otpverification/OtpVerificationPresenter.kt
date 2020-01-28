package com.citygrocer.customer.screens.otpverification

import com.citygrocer.customer.api.ApiService
import com.citygrocer.customer.module.input.MergeCartIp
import com.citygrocer.customer.module.input.ResetCodeIp
import com.citygrocer.customer.module.input.VerifyOtpIp
import com.citygrocer.customer.util.schedulers.BaseScheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class OtpVerificationPresenter  @Inject constructor(var apiService: ApiService,
                                                    var baseScheduler: BaseScheduler) : OtpVerificationContract.Presenter {

    var view: OtpVerificationContract.View? = null
    private val subscriptions = CompositeDisposable()

    override fun takeView(view: OtpVerificationContract.View?) {
        this.view = view
    }

    override fun verifyOtp(verifyOtpIp: VerifyOtpIp) {
        view?.showProgress()
        var subscriber1 = apiService.otpVerify(verifyOtpIp)
                .observeOn(baseScheduler.ui())
                .subscribeOn(baseScheduler.io())
                .subscribe({ res ->
                    view?.hideProgress()
                    view?.showOtpVerifyRes(res)
                }, { throwable ->
                    view?.hideProgress()
                    view?.showErrorMsg(throwable)
                })
        subscriptions.add(subscriber1)
    }

    override fun resendOtp(resetCodeIp: ResetCodeIp) {
        view?.showProgress()
        var subscriber2 = apiService.resendotp(resetCodeIp)
                .observeOn(baseScheduler.ui())
                .subscribeOn(baseScheduler.io())
                .subscribe({ res ->
                    view?.hideProgress()
                    view?.showOtpReqRes(res)
                }, { throwable ->
                    view?.hideProgress()
                    view?.showErrorMsg(throwable)
                })
        subscriptions.add(subscriber2)
    }

    override fun mergeCart(mergeCartIp: MergeCartIp) {
        view?.showProgress()
        subscriptions.add(
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
        subscriptions.clear()
        this.view = null
    }

}