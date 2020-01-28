package com.citygrocer.customer.di

import com.citygrocer.customer.util.schedulers.BaseScheduler
import com.citygrocer.customer.util.schedulers.Scheduler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BaseSchedulerModule {

    @Singleton
    @Provides
    fun baseScheduler(): BaseScheduler {
        return Scheduler()
    }
}