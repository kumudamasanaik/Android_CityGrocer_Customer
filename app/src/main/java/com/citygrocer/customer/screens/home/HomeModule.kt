package com.citygrocer.customer.screens.home

import dagger.Binds
import dagger.Module

@Module
abstract class HomeModule {

    @Binds
    abstract fun homePresenter(homePresenter: HomePresenter): HomeContract.Presenter
}