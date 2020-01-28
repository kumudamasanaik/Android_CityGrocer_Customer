package com.citygrocer.customer.screens.termsofservice

import dagger.Binds
import dagger.Module

@Module
abstract class TermsOfServiceModule {
    @Binds
    abstract fun termsOfServicePresenter(termsOfServicePresenter: TermsOfServicePresenter): TermsOfServiceContract.Presenter
}