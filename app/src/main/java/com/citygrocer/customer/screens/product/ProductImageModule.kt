package com.citygrocer.customer.screens.product

import dagger.Binds
import dagger.Module

@Module
abstract class ProductImageModule {

    @Binds
    abstract fun productImagePresenter(productImagePresenter: ProductImagePresenter): ProductImageContract.Presenter
}