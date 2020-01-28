package com.citygrocer.customer.screens.resetpassword

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.view.View
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.module.ForgetPasswordRes
import com.citygrocer.customer.module.ForgotPassResIP
import com.citygrocer.customer.module.input.ForgetPasswordIp
import com.citygrocer.customer.screens.login.LoginActivity
import com.citygrocer.customer.screens.verificationcode.VerificationCodeActivity
import com.citygrocer.customer.util.toast
import com.citygrocer.customer.util.withNotNullNorEmpty
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_reset_password.*
import javax.inject.Inject

class ResetPasswordActivity : DaggerAppCompatActivity(), View.OnClickListener, ForgetPasswordContract.View {

    var toolbar: Toolbar? = null
    var appbar: AppBarLayout? = null
    var mobile_no: String? = null
    var tvTitle: AppCompatTextView? = null
    @Inject
    lateinit var presenter: ForgetPasswordPresenter

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        initScreen()
    }

    override fun initScreen() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        appbar = findViewById<View>(R.id.appbar) as AppBarLayout
        tvTitle = findViewById<View>(R.id.tvtitle) as AppCompatTextView

        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        tvTitle!!.text = "Reset Password"

        btn_next.setOnClickListener(this)
        tv_login.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_next -> {
                    showForgotPasswordLoader()
                }
                R.id.tv_login -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun navigateToNextScreen(arrayList: ArrayList<ForgotPassResIP>) {
        val intent = Intent(this, VerificationCodeActivity::class.java)
        mobile_no = et_email_mobile_no.text.toString()
        intent.putExtra(Constants.MOBILE, mobile_no)
        startActivity(intent)
        finish()
    }

    override fun showValidateFieldsError(msg: String) {
        when (msg) {
            getString(R.string.err_emailIdOrMobile) -> et_email_mobile_no.error = msg
        }
    }

    override fun showForgotPasswordLoader() {

        val forgotPasswordIP = ForgetPasswordIp(mobile_email = et_email_mobile_no.text.toString())
        if (presenter.validate(forgotPasswordIP))
            presenter.ForgotPass(forgotPasswordIP)

    }

    override fun ForgotPasswordRes(res: ForgetPasswordRes) {
        toast(res.message ?: getString(R.string.error_something_wrong))
        if (res.isSuccess()) {
            res.result.withNotNullNorEmpty {
                navigateToNextScreen(this)
                return@withNotNullNorEmpty
            }
        }
    }


    override fun showErrorMsg(throwable: Throwable, apiType: String) {
        toast(throwable.message ?: getString(R.string.error_something_wrong))
    }

    override fun showProgress(msg: String) {
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        super.hideProgress()
        progress_bar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dropView()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}