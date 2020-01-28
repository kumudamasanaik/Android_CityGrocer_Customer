package com.citygrocer.customer.screens.registration

import android.content.Context
import com.citygrocer.customer.R
import com.citygrocer.customer.api.ApiService
import com.citygrocer.customer.module.input.CustomerIdIP
import com.citygrocer.customer.module.input.RegisterIp
import com.citygrocer.customer.util.*
import com.citygrocer.customer.util.schedulers.BaseScheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RegisterPresenter @Inject constructor(var apiService: ApiService,
                                            var context: Context,
                                            var baseScheduler: BaseScheduler) : RegisterContract.Presenter {

    var view: RegisterContract.View? = null
    private val subscription = CompositeDisposable()

    override fun takeView(view: RegisterContract.View?) {
        this.view = view
    }

    override fun validate(registerIp: RegisterIp): Boolean {

        if (registerIp.firstname!!.isNullOrBlank()) {
            view?.showValidateFieldsError(context.getString(R.string.please_enter_first_name))
            return false
        }
        if (registerIp.mobilenumber!!.isNotPhone()) {
            view?.showValidateFieldsError(context.getString(R.string.err_Mobile))
            return false
        }

        if (registerIp.email!!.isNotEmail()) {
            view?.showValidateFieldsError(context.getString(R.string.err_emailId))
            return false
        }

        if (registerIp.password!!.isValidPassword()) {
            view?.showValidateFieldsError(context.getString(R.string.err_password))
            return false
        }
        if (registerIp.password!!.length < 4) {
            view?.showValidateFieldsError(context.getString(R.string.err_password_length))
            return false
        }
        if (registerIp.gender!!.isNullOrEmpty()) {
            view?.showValidateFieldsError(context.getString(R.string.err_gender))
            return false
        }
        return true
    }

    override fun doRegister(registerIp: RegisterIp) {
        view?.showProgress()
        subscription.add(apiService.register(registerIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe(
                        { res ->
                            view?.hideProgress()
                            if (res.isSuccess()) {
                                view?.showRegisterRes(res)

                            } else
                                view?.showErrorMsg(Throwable(res.message))
                        },
                        { throwable ->
                            view?.hideProgress()
                            view?.showErrorMsg(throwable)
                        }))
    }

    override fun editRegisterData(productImageViewIp: CustomerIdIP) {
        view?.showProgress()
        subscription.add(apiService.editRegisterData(productImageViewIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe(
                        { res ->
                            view?.hideProgress()
                            if (res.isSuccess()) {
                                view?.showEditResponse(res)

                            } else
                                view?.showErrorMsg(Throwable(res.message))
                        },
                        { throwable ->
                            view?.hideProgress()
                            view?.showErrorMsg(throwable)
                        }))
    }

    override fun updateRegisterData(registerIp: RegisterIp) {
        view?.showProgress()
        subscription.add(apiService.updateRegisterData(registerIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe(
                        { res ->
                            view?.hideProgress()
                            if (res.isSuccess()) {
                                view?.showRegisterRes(res)

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