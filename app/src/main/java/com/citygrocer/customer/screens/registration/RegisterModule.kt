package com.citygrocer.customer.screens.registration

import dagger.Binds
import dagger.Module

@Module
abstract class RegisterModule {

    @Binds
    abstract fun registerPresenter(registerPresenter: RegisterPresenter): RegisterContract.Presenter
}