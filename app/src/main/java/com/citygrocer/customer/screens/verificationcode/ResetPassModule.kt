package com.citygrocer.customer.screens.verificationcode

import dagger.Binds
import dagger.Module

@Module
abstract class ResetPassModule {

    @Binds
    abstract fun resetPasswordPresenter(resetPasswordPresenter: ResetPassPresenter): ResetPassContract.Presenter
}