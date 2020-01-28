package com.citygrocer.customer.screens.delivery

import dagger.Binds
import dagger.Module

@Module
abstract class DeliveryModule {
    @Binds
    abstract fun deliveryPresenter(deliveryPresenter: DeliveryPresenter): DeliveryContract.Presenter
}