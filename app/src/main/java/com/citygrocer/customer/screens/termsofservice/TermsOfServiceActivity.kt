package com.citygrocer.customer.screens.termsofservice

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.view.View
import com.citygrocer.customer.R
import com.citygrocer.customer.customviews.ScreenStateView
import com.citygrocer.customer.module.TermsandConditionsRes
import com.citygrocer.customer.util.toast
import com.citygrocer.customer.util.withNotNullNorEmpty
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_terms.*
import kotlinx.android.synthetic.main.error_view.*
import javax.inject.Inject

class TermsOfServiceActivity : DaggerAppCompatActivity(), View.OnClickListener, TermsOfServiceContract.View {


    var toolbar: Toolbar? = null
    var appbar: AppBarLayout? = null
    var tvTitle: AppCompatTextView? = null
    @Inject
    lateinit var presenter: TermsOfServicePresenter

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
        initScreen()
    }

    override fun initScreen() {
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_LOADING
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        appbar = findViewById<View>(R.id.appbar) as AppBarLayout
        tvTitle = findViewById<View>(R.id.tvtitle) as AppCompatTextView

        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        tvTitle!!.text = "Terms of service"

    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.empty_button -> {
                    initScreen()
                }
                R.id.error_button -> {
                    initScreen()
                }
            }
        }
    }

    override fun showTermsOfServiceRes(termsandConditionsRes: TermsandConditionsRes) {
        if (termsandConditionsRes.isSuccess()) {
            termsandConditionsRes.result.withNotNullNorEmpty {
                multiState_rview.viewState = ScreenStateView.VIEW_STATE_CONTENT
                tv_head.text = termsandConditionsRes.result!![0].topic1
                tv_head2.text = termsandConditionsRes.result[0].topic2
                tv_sub_head.text = termsandConditionsRes.result[0].value1
                tv_sub_head1.text = termsandConditionsRes.result[0].value2
            }
        }
    }

    override fun showErrorMsg(throwable: Throwable, apiType: String) {
        toast(throwable.message ?: getString(R.string.error_something_wrong))
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_ERROR
        error_textView.text = getString(R.string.error_something_wrong)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}