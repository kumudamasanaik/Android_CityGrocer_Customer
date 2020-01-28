package com.citygrocer.customer.screens.verificationcode

import android.content.Context
import com.citygrocer.customer.R
import com.citygrocer.customer.api.ApiService
import com.citygrocer.customer.module.input.ResetCodeIp
import com.citygrocer.customer.module.input.VerifyOtpIp
import com.citygrocer.customer.util.schedulers.BaseScheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ResetPassPresenter @Inject constructor(var apiService: ApiService,
                                             var context: Context,
                                             var baseScheduler: BaseScheduler) : ResetPassContract.Presenter {

    var view: ResetPassContract.View? = null
    private val subscription = CompositeDisposable()

    override fun takeView(view: ResetPassContract.View?) {
        this.view = view
    }

    override fun validate(resetCodeIP: ResetCodeIp): Boolean {

        if (resetCodeIP.otp!!.isEmpty()) {
            view?.showValidateFieldsError(context.getString(R.string.err_otp_empty))
            return false
        }
        return true

    }

    override fun resetPassword(resetCodeIP: ResetCodeIp) {

        view?.showProgress()
        var subscriber1 = apiService.resetpassword(resetCodeIP)
                .observeOn(baseScheduler.ui())
                .subscribeOn(baseScheduler.io())
                .subscribe({ res ->
                    view?.hideProgress()
                    view?.resetPasswordRes(res)
                }, { throwable ->
                    view?.hideProgress()
                    view?.showErrorMsg(throwable)
                })
        subscription.add(subscriber1)

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
        subscription.add(subscriber2)
    }

    override fun dropView() {
        subscription.clear()
        this.view = null
    }
}