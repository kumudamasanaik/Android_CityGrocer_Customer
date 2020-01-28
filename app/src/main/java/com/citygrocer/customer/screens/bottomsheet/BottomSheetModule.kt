package com.citygrocer.customer.screens.bottomsheet

import dagger.Binds
import dagger.Module

@Module
abstract class BottomSheetModule {
    @Binds
    abstract fun socialLoginPresenter(loginPresenter: BottomSheetPresenter): BottomSheetContract.Presenter
}