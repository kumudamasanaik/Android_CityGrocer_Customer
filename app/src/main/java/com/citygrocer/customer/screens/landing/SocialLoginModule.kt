package com.citygrocer.customer.screens.landing

import dagger.Binds
import dagger.Module

@Module
abstract class SocialLoginModule {
    @Binds
    abstract fun socialLoginPresenter(loginPresenter: SocialLoginPresenter): SocialLoginContract.Presenter
}