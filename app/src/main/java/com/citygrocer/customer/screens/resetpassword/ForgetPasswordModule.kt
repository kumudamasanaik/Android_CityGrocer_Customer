package com.citygrocer.customer.screens.resetpassword

import dagger.Binds
import dagger.Module

@Module
abstract class ForgetPasswordModule {

    @Binds
    abstract fun forgotPasswordPresenter(forgetPasswordPresenter: ForgetPasswordPresenter): ForgetPasswordContract.Presenter
}