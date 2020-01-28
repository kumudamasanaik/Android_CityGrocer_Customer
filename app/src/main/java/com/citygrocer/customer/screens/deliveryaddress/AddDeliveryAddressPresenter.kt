package com.citygrocer.customer.screens.deliveryaddress

import android.content.Context
import com.citygrocer.customer.R
import com.citygrocer.customer.api.ApiService
import com.citygrocer.customer.module.input.AddAddressIp
import com.citygrocer.customer.module.input.DeleteAddressIp
import com.citygrocer.customer.util.isNotEmail
import com.citygrocer.customer.util.isNotPhone
import com.citygrocer.customer.util.isValidPassword
import com.citygrocer.customer.util.schedulers.BaseScheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AddDeliveryAddressPresenter @Inject constructor(var apiService: ApiService,
                                                      var context: Context,
                                                      var baseScheduler: BaseScheduler) : AddDeliveryAddressContract.Presenter {

    var view: AddDeliveryAddressContract.View? = null
    private val subscription = CompositeDisposable()

    override fun takeView(view: AddDeliveryAddressContract.View?) {
        this.view = view
    }

    override fun validate(addressIp: AddAddressIp): Boolean {

        if (addressIp.pincode!!.isEmpty()) {
            view?.showValidateFieldsError(context.getString(R.string.please_enter_pincode))
            return false
        }
        if (addressIp.state!!.isEmpty()) {
            view?.showValidateFieldsError(context.getString(R.string.please_enter_state))
            return false
        }

        if (addressIp.city!!.isEmpty()) {
            view?.showValidateFieldsError(context.getString(R.string.please_enter_city))
            return false
        }

        if (addressIp.town!!.isEmpty()) {
            view?.showValidateFieldsError(context.getString(R.string.please_enter_town))
            return false
        }
        if (addressIp.address!!.isEmpty()) {
            view?.showValidateFieldsError(context.getString(R.string.please_enter_address))
            return false
        }
        return true
    }

    override fun getAddAddressDeatails(addressIp: AddAddressIp) {
        view?.showProgress()
        subscription.add(apiService.getAddAddressData(addressIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.hideProgress()
                    if (res.isSuccess()) {
                    view?.showAddAddressRes(res)}
                }, { throwable -> view?.showErrorMsg(throwable) }))
    }

    override fun getEditAddressDetails(deleteAddressIp: DeleteAddressIp) {
        view?.showProgress()
        subscription.add(apiService.getEditAddressDetails(deleteAddressIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.hideProgress()
                    view?.showEditAddressRes(res)
                }, { throwable -> view?.showErrorMsg(throwable) }))
    }

    override fun getUpdatedAddressDetails(addressIp: AddAddressIp) {
        view?.showProgress()
        subscription.add(apiService.getUpdatedAddress(addressIp)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe({ res ->
                    view?.hideProgress()
                    view?.showUpdatedAddressRes(res)
                }, { throwable -> view?.showErrorMsg(throwable) }))

    }

    override fun dropView() {
        subscription.clear()
        this.view = null
    }
}