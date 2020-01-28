package com.citygrocer.customer.screens.changepassword

import android.content.Context
import com.citygrocer.customer.R
import com.citygrocer.customer.api.ApiService
import com.citygrocer.customer.module.input.ChangePassIp
import com.citygrocer.customer.util.schedulers.BaseScheduler
import com.citygrocer.customer.util.withNotNullNorEmpty
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ChangePassPresenter @Inject constructor(var apiService: ApiService,
                                              var context: Context,
                                              var baseScheduler: BaseScheduler) : ChangePassContract.Presenter {
    var view: ChangePassContract.View? = null
    private val subscription = CompositeDisposable()

    override fun takeView(view: ChangePassContract.View?) {
        this.view = view
    }

    override fun validate(changePassIp: ChangePassIp): Boolean {
        if (changePassIp.password!!.isNullOrBlank()) {
            view?.showValidateFieldsError(context.getString(R.string.err_new_password))
            return false
        }
        if (changePassIp.re_password!!.isNullOrBlank()) {
            view?.showValidateFieldsError(context.getString(R.string.err_re_password))
            return false
        }
        if (changePassIp.password!!.length < 4) {
            view?.showValidateFieldsError(context.getString(R.string.err_password_length))
            return false
        }

        return true
    }

    override fun changePassword(changePassIp: ChangePassIp) {
        view?.showProgress()
        subscription.add(apiService.changepassword(changePassIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe(
                        { res ->
                            view?.hideProgress()
                            if (res.isSuccess()) {
                                res.result.withNotNullNorEmpty {
                                    view?.changePassRes(res)
                                }
                                //view?.showErrorMsg(Throwable(res.message))
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