package com.citygrocer.customer.screens.choosedelivery

import dagger.Binds
import dagger.Module

@Module
abstract class ChooseAddressModule {
    @Binds
    abstract fun chooseAddressPresenter(chooseAddressPresenter: ChooseAddressPresenter): ChooseAddressContract.Presenter
}