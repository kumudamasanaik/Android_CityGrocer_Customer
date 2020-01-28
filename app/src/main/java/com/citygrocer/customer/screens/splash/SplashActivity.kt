package com.citygrocer.customer.screens.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.citygrocer.customer.R
import com.citygrocer.customer.screens.home.HomeActivity
import dagger.android.support.DaggerAppCompatActivity

class SplashActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed(Runnable {
            launchHomeScreen()
        }, 4000)
    }

    private fun launchHomeScreen() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
