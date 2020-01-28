package com.citygrocer.customer.screens.subcategory

import dagger.Binds
import dagger.Module

@Module
abstract class SubCategoryModule {
    @Binds
    abstract fun subCategoryPresenter(subCategoryPresenter: SubCategoryPresenter): SubCategoryContract.Presenter
}