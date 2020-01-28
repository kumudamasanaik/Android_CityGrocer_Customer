package com.citygrocer.customer

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.StrictMode
import android.support.multidex.MultiDex
import android.view.WindowManager
import com.citygrocer.customer.api.ApiService
import com.citygrocer.customer.broadcastreceiver.NetworkReceiver
import com.citygrocer.customer.customviews.AppSignatureHelper
import com.citygrocer.customer.di.AppComponent
import com.citygrocer.customer.di.DaggerAppComponent
import com.citygrocer.customer.receivers.ConnectivityReceiver
import com.citygrocer.customer.util.logv
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.google.firebase.messaging.FirebaseMessaging
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.fabric.sdk.android.Fabric
import javax.inject.Inject


class MyApplication : DaggerApplication(), Application.ActivityLifecycleCallbacks {

    @Inject
    lateinit var apiService: ApiService

    companion object {
        lateinit var appComponent: AppComponent
        lateinit var myApplication: MyApplication
        internal lateinit var mNetworkReceiver: NetworkReceiver
        var mActivity: Activity? = null
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {

        super.onCreate()
        myApplication = this
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        Stetho.initializeWithDefaults(this)
        logv("$apiService.toString()")
        Fabric.with(this, Crashlytics())
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build())
        }
        AppSignatureHelper(this).appSignatures
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {
        mActivity = activity
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        mNetworkReceiver = NetworkReceiver()
    }

    override fun onActivityStarted(activity: Activity) {
        mActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        mActivity = activity
        registerNetworkBroadcastForNougat()
    }

    override fun onActivityPaused(activity: Activity) {
        mActivity = null
        unregisterReceiver(mNetworkReceiver)
    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    private fun registerNetworkBroadcastForNougat() {
        registerReceiver(mNetworkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    fun setConnectivityListener(listener: ConnectivityReceiver.ConnectivityReceiverListener) {
        ConnectivityReceiver.connectivityReceiverListener = listener
    }
}