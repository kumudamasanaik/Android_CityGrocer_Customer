package com.citygrocer.customer.di

import com.citygrocer.customer.MyApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class,
    AndroidInjectionModule::class,
    NetworkModule::class,
    MyActivityBuilder::class,
    MyFragmentBuilder::class,
    BaseSchedulerModule::class])

interface AppComponent : AndroidInjector<MyApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MyApplication>()

}