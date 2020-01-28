package com.citygrocer.customer.screens.otpverification

import dagger.Binds
import dagger.Module

@Module
abstract class OtpVerificationModule {

    @Binds
    abstract fun otpPresenter(otpPresenter: OtpVerificationPresenter): OtpVerificationContract.Presenter
}