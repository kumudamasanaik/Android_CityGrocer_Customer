package com.citygrocer.customer.screens.registration

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.text.InputType
import android.view.View
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.module.CustomerRes
import com.citygrocer.customer.module.input.CustomerIdIP
import com.citygrocer.customer.module.input.RegisterIp
import com.citygrocer.customer.screens.login.LoginActivity
import com.citygrocer.customer.screens.otpverification.OtpVerificationActivity
import com.citygrocer.customer.screens.termsofservice.TermsOfServiceActivity
import com.citygrocer.customer.util.CommonUtils
import com.citygrocer.customer.util.toast
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_registration.*
import javax.inject.Inject

class RegistrationActivity : DaggerAppCompatActivity(), View.OnClickListener, RegisterContract.View {

    var toolbar: Toolbar? = null
    var appbar: AppBarLayout? = null
    var tvTitle: AppCompatTextView? = null
    private var gender: String = ""
    private var source: String = ""

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    @Inject
    lateinit var presenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        initScreen()
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    override fun initScreen() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        appbar = findViewById<View>(R.id.appbar) as AppBarLayout
        tvTitle = findViewById<View>(R.id.tvtitle) as AppCompatTextView

        radio_male.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                gender = "Male"
            else
                gender = "Female"
        }
        radio_female.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                gender = "Female"
            else
                gender = "Male"
        }

        radio_other.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                gender = "Other"
            else
                gender = "Male"
        }

        et_first_name.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        et_last_name.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES

        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        tvTitle!!.text = "Register"

        tv_login.setOnClickListener(this)
        btn_register.setOnClickListener(this)
        iv_show_password.setOnClickListener(this)
        tv_terms.setOnClickListener(this)
        et_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        iv_show_password.setBackgroundResource(R.drawable.ic_hide_password)

        getIntentData()
        if (source == Constants.EDIT) {
            getEditRegisterData()
        }
    }

    private fun getIntentData() {
        intent?.extras?.apply {
            source = getString(Constants.SOURCE)
        }
    }

    private fun getEditRegisterData() {
        presenter.editRegisterData(productImageViewIp = CustomerIdIP(_id = CommonUtils.getId()))
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.tv_login -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                R.id.btn_register -> {
                    if (source == Constants.EDIT) {
                        getUpdateRegisterData()
                    } else {
                        showRegisterLoader()
                    }
                }
                R.id.tv_terms -> {
                    startActivity(Intent(this, TermsOfServiceActivity::class.java))
                }
                R.id.iv_show_password -> {
                    if (et_password.inputType == InputType.TYPE_CLASS_TEXT) {
                        et_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        iv_show_password.setBackgroundResource(R.drawable.ic_hide_password)
                    } else {
                        et_password.inputType = InputType.TYPE_CLASS_TEXT
                        iv_show_password.setBackgroundResource(R.drawable.ic_eye)
                    }
                }
            }
        }
    }

    private fun getUpdateRegisterData() {
        val registerIP = RegisterIp(
                _id = CommonUtils.getId(),
                firstname = et_first_name.text.toString(),
                lastname = et_last_name.text.toString(),
                mobilenumber = et_mobile_no.text.toString(),
                email = et_email.text.toString(),
                password = et_password.text.toString(),
                gender = gender,
                referalcode = referal_code.text.toString(),
                sms_key = CommonUtils.getOtpVerificationHashKey()
        )
        if (presenter.validate(registerIP))
            presenter.updateRegisterData(registerIP)
    }

    override fun showValidateFieldsError(msg: String) {

        when (msg) {
            getString(R.string.please_enter_first_name) -> et_first_name.error = msg
            getString(R.string.err_Mobile) -> et_mobile_no.error = msg
            getString(R.string.err_emailId) -> et_email.error = msg
            getString(R.string.err_password) -> et_password.error = msg
            getString(R.string.err_password_length) -> et_password.error = msg
            getString(R.string.err_gender) -> radio_other.error = msg
        }

    }

    override fun showRegisterLoader() {
        val registerIP = RegisterIp(
                firstname = et_first_name.text.toString(),
                lastname = et_last_name.text.toString(),
                mobilenumber = et_mobile_no.text.toString(),
                email = et_email.text.toString(),
                password = et_password.text.toString(),
                gender = gender,
                referalcode = referal_code.text.toString(),
                sms_key = CommonUtils.getOtpVerificationHashKey()
        )
        if (presenter.validate(registerIP))
            presenter.doRegister(registerIP)
    }

    override fun showRegisterRes(res: CustomerRes) {

        toast(res.message ?: getString(R.string.error_something_wrong))

        if (res.isSuccess()) {
            CommonUtils.saveID(res.result!!._id!!)
            val intent = Intent(this, OtpVerificationActivity::class.java)
            intent.putExtra(Constants.MOBILE, res.result?.mobilenumber)
            startActivity(intent)
            finish()

        }
    }

    override fun showEditResponse(res: CustomerRes) {
        if (res.isSuccess()) {
            et_first_name.setText(res.result!!.firstname.toString())
            et_last_name.setText(res.result.lastname.toString())
            et_mobile_no.setText(res.result.mobilenumber.toString())
            et_email.setText(res.result.email.toString())
            et_password.setText(res.result.password.toString())
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}