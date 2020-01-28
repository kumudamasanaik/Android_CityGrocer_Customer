package com.citygrocer.customer.screens.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.view.View
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.module.CommonRes
import com.citygrocer.customer.module.CustomerRes
import com.citygrocer.customer.module.input.LoginIp
import com.citygrocer.customer.module.input.MergeCartIp
import com.citygrocer.customer.screens.cart.CartActivity
import com.citygrocer.customer.screens.home.HomeActivity
import com.citygrocer.customer.screens.registration.RegistrationActivity
import com.citygrocer.customer.screens.resetpassword.ResetPasswordActivity
import com.citygrocer.customer.util.CommonUtils
import com.citygrocer.customer.util.toast
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : DaggerAppCompatActivity(), View.OnClickListener, LoginContract.View {

    var toolbar: Toolbar? = null
    var appbar: AppBarLayout? = null
    var tvTitle: AppCompatTextView? = null

    @Inject
    lateinit var presenter: LoginPresenter

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
        tvTitle!!.text = "Login"

        tv_register.setOnClickListener(this)
        btn_login.setOnClickListener(this)
        tv_forgot_password.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.tv_register -> {
                    startActivity(Intent(this, RegistrationActivity::class.java).putExtra(Constants.SOURCE, Constants.REGISTER))
                    finish()
                }
                R.id.btn_login -> {
                    showLoginLoader()
                }
                R.id.tv_forgot_password -> {
                    startActivity(Intent(this, ResetPasswordActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun showMergeCart() {
        val mergeCartIp = MergeCartIp(
                session_id = CommonUtils.getSession(),
                _id_customer = CommonUtils.getLoginId()
        )
        presenter.mergeCart(mergeCartIp)
    }

    override fun showValidateFieldsError(msg: String) {
        when (msg) {
            getString(R.string.err_emailIdOrMobile) -> et_email_mobile.error = msg
            getString(R.string.err_password) -> et_password.error = msg
        }
    }

    override fun showLoginLoader() {
        val loginIp = LoginIp(
                mobile_email = et_email_mobile.text.toString(),
                password = et_password.text.toString()
        )
        if (presenter.validate(loginIp))
            presenter.doLogin(loginIp)
    }

    override fun setLoginRes(res: CustomerRes) {

        if (res.isSuccess()) {
            CommonUtils.saveUserRegisteredData(res.result)
            navigateToHomeScreen()
        }
    }

    override fun showMergeCartRes(res: CommonRes) {
        if (res.isSuccess()) {
            val intent = Intent(this, CartActivity::class.java).putExtra(Constants.SOURCE, Constants.LOGIN)
            startActivity(intent)
            finish()
        }
    }

    private fun navigateToHomeScreen() {
        if (CommonUtils.getCartCount() != 0) {
            showMergeCart()
        } else {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra(Constants.SOURCE, Constants.LOGIN).putExtra("isLogin", true)
            startActivity(intent)
            finish()
        }

    }


    override fun showProgress(msg: String) {
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        super.hideProgress()
        progress_bar.visibility = View.GONE
    }

    override fun showErrorMsg(throwable: Throwable, apiType: String) {
        toast(throwable.message ?: getString(R.string.error_something_wrong))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dropView()
    }

    override fun onSupportNavigateUp(): Boolean {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

}