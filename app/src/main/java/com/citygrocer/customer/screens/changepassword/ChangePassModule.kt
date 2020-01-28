package com.citygrocer.customer.screens.changepassword

import dagger.Binds
import dagger.Module

@Module
abstract class ChangePassModule {

    @Binds
    abstract fun changePasswordPresenter(changePasswordPresenter: ChangePassPresenter): ChangePassContract.Presenter
}