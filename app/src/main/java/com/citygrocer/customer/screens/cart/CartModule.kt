package com.citygrocer.customer.screens.cart

import dagger.Binds
import dagger.Module

@Module
abstract class CartModule {
    @Binds
    abstract fun cartPresenter(cartPresenter: CartPresenter): CartContract.Presenter
}