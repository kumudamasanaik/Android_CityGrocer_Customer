package com.citygrocer.customer.screens.login

import dagger.Binds
import dagger.Module

@Module
abstract class LoginModule {

    @Binds
    abstract fun loginPresenter(loginPresenter: LoginPresenter): LoginContract.Presenter
}