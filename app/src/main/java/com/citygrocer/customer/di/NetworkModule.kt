package com.citygrocer.customer.di

import android.content.Context
import com.citygrocer.customer.MyApplication
import com.citygrocer.customer.api.*
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class NetworkModule  {

    @Provides
    fun provideContext(application: MyApplication): Context {
        return application.applicationContext
    }


    @Singleton
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient.Builder): ApiService {
        return retrofit2.Retrofit.Builder()
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ApiConstants.BASE_URL)
                .build()
                .create(ApiService::class.java)
    }


    @Singleton
    @Provides
    fun provideOkhttpBuilder(networkMonitor: NetworkMonitor): OkHttpClient.Builder {
        val okHttpBuilder = OkHttpClient.Builder()
        okHttpBuilder.addNetworkInterceptor(StethoInterceptor())
        okHttpBuilder.addInterceptor { chain ->
            if (networkMonitor.isConnected())
                return@addInterceptor chain.proceed(chain.request()) else throw  NoNetworkException()
        }
        return okHttpBuilder
    }


    @Singleton
    @Provides
    fun networkMonitor(context: Context): NetworkMonitor {
        return LiveNetworkMonitor(context)
    }

}