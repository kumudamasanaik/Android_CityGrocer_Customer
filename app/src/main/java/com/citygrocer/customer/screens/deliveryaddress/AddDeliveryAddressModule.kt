package com.citygrocer.customer.screens.deliveryaddress

import dagger.Binds
import dagger.Module

@Module
abstract class AddDeliveryAddressModule {
    @Binds
    abstract fun changePasswordPresenter(addDeliveryAddressPresenter: AddDeliveryAddressPresenter): AddDeliveryAddressContract.Presenter
}