package com.citygrocer.customer.screens.resetpassword

import android.content.Context
import com.citygrocer.customer.R
import com.citygrocer.customer.api.ApiService
import com.citygrocer.customer.module.input.ForgetPasswordIp
import com.citygrocer.customer.util.schedulers.BaseScheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ForgetPasswordPresenter @Inject constructor(var apiService: ApiService,
                                                  var context: Context,
                                                  var baseScheduler: BaseScheduler) : ForgetPasswordContract.Presenter {

    var view: ForgetPasswordContract.View? = null
    private val subscription = CompositeDisposable()


    override fun takeView(view: ForgetPasswordContract.View?) {
        this.view = view
    }

    override fun validate(forgotPasswordIP: ForgetPasswordIp): Boolean {
        if (forgotPasswordIP.mobile_email!!.isEmpty()) {
            view?.showValidateFieldsError(context.getString(R.string.err_emailIdOrMobile))
            return false
        }
        return true
    }

    override fun ForgotPass(forgotPasswordIP: ForgetPasswordIp) {
        view?.showProgress()
        var subscriber2 = apiService.forgetpassword(forgotPasswordIP)
                .observeOn(baseScheduler.ui())
                .subscribeOn(baseScheduler.io())
                .subscribe({ res ->
                    view?.hideProgress()
                    view?.ForgotPasswordRes(res)
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