package com.citygrocer.customer.screens.search

import dagger.Binds
import dagger.Module

@Module
abstract class SearchModule {
    @Binds
    abstract fun searchPresenter(searchPresenter: SearchPresenter): SearchContract.Presenter
}