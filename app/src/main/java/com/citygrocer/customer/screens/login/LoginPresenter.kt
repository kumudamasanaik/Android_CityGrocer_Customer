package com.citygrocer.customer.screens.login

import android.content.Context
import com.citygrocer.customer.R
import com.citygrocer.customer.api.ApiService
import com.citygrocer.customer.util.*
import com.citygrocer.customer.module.input.LoginIp
import com.citygrocer.customer.module.input.MergeCartIp
import com.citygrocer.customer.util.schedulers.BaseScheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LoginPresenter @Inject constructor(
        var apiService: ApiService,
        var context: Context,
        var baseScheduler: BaseScheduler) : LoginContract.Presenter {

    var view: LoginContract.View? = null
    private val subscription = CompositeDisposable()

    override fun takeView(view: LoginContract.View?) {
        this.view = view
    }

    override fun validate(loginIp: LoginIp): Boolean {
        if (loginIp.mobile_email!!.isEmpty()) {
            view?.showValidateFieldsError(context.getString(R.string.err_emailIdOrMobile))
            return false
        }
        if (loginIp.password!!.isEmpty()) {
            view?.showValidateFieldsError(context.getString(R.string.err_password))
            return false
        }
        return true
    }

    override fun doLogin(loginIp: LoginIp) {
        view?.showProgress()
        subscription.add(
                apiService.login(loginIp)
                        .subscribeOn(baseScheduler.io())
                        .observeOn(baseScheduler.ui())
                        .subscribe(
                                { res ->
                                    view?.hideProgress()
                                    if (res.isSuccess()) {
                                        view?.setLoginRes(res)
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