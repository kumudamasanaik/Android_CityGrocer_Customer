package com.citygrocer.customer.util

import android.app.Application
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.citygrocer.customer.BuildConfig
import com.citygrocer.customer.customviews.ScreenStateView


fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}


fun FragmentActivity.toast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    if (!text.contains("lateinit"))
        Toast.makeText(this, text, duration).show()
}

fun Fragment.toast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    if (!text.contains("lateinit"))
        Toast.makeText(activity, text, duration).show()
}

fun FragmentActivity.logv(msg: String) {
    if (BuildConfig.DEBUG)
        Log.v(this.javaClass.name, msg)
}

fun FragmentActivity.loge(msg: String) {
    if (BuildConfig.DEBUG)
        Log.e(this.javaClass.name, msg)
}

fun Fragment.logv(msg: String) {
    if (BuildConfig.DEBUG)
        Log.v(this.javaClass.name, msg)
}

fun Application.logv(msg: String) {
    if (BuildConfig.DEBUG)
        Log.v(this.javaClass.name, msg)
}

fun FragmentActivity.toast(@StringRes stringRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, stringRes, duration).show()
}


fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}


fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { replace(frameId, fragment) }
}

fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun setViewState(screenStateView: ScreenStateView, viewState: Int, tvView: TextView, msg: String?) {
    try {
        Log.d("ok", "Set $viewState")
        when (viewState) {
            ScreenStateView.VIEW_STATE_CONTENT -> screenStateView.viewState = ScreenStateView.VIEW_STATE_CONTENT
            ScreenStateView.VIEW_STATE_LOADING -> screenStateView.viewState = ScreenStateView.VIEW_STATE_LOADING
            ScreenStateView.VIEW_STATE_EMPTY -> {
                screenStateView.viewState = ScreenStateView.VIEW_STATE_EMPTY
                if (msg != null)
                    tvView.text = msg
                else
                    tvView.text = "No data to display!!"
            }
            ScreenStateView.VIEW_STATE_EMPTY_BANK -> {
                screenStateView.viewState = ScreenStateView.VIEW_STATE_EMPTY
                if (msg != null)
                    tvView.text = msg
                else
                    tvView.text = "Kindly call 600 54 6000 for assistance in adding your bank"
            }
            ScreenStateView.VIEW_STATE_ERROR -> {
                screenStateView.viewState = ScreenStateView.VIEW_STATE_ERROR
                if (msg != null)
                    tvView.text = msg
                else
                    tvView.text = "Please check your internet connection!"
            }
            ScreenStateView.VIEW_STATE_WRONG -> {
                screenStateView.viewState = ScreenStateView.VIEW_STATE_ERROR
                if (msg != null)
                    tvView.text = msg
                else {
                    if (!SharedPreferenceManager.isLogIn()) {
                        tvView.text = "Login to use this feature!!"
                    } else {
                        tvView.text = "Sorry! Something went wrong"
                    }

                }

            }
            else -> {
            }
        }//	ivView.setImageResource(R.drawable.icn_empty);
        //	ivView.setImageResource(R.drawable.icn_empty);
        //	ivView.setImageResource(R.drawable.icn_no_internet);
        //	ivView.setImageResource(R.drawable.icn_wrong);
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

}