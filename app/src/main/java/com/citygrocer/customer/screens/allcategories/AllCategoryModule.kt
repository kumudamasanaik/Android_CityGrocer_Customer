package com.citygrocer.customer.screens.allcategories

import dagger.Binds
import dagger.Module

@Module
abstract class AllCategoryModule {

    @Binds
    abstract fun allCategoryPresenter(allCategoryPresenter: AllCategoryPresenter): AllCategoryContract.Presenter
}