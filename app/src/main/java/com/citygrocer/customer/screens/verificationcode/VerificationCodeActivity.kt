package com.citygrocer.customer.screens.verificationcode

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.citygrocer.customer.R
import com.citygrocer.customer.broadcastreceiver.NetworkReceiver
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.module.CommonRes
import com.citygrocer.customer.module.CustomerRes
import com.citygrocer.customer.module.input.ResetCodeIp
import com.citygrocer.customer.screens.changepassword.ChangePasswordActivity
import com.citygrocer.customer.util.Validation
import com.citygrocer.customer.util.toast
import com.google.android.gms.auth.api.phone.SmsRetriever
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_verification_code.*
import javax.inject.Inject

class VerificationCodeActivity : DaggerAppCompatActivity(), View.OnClickListener, ResetPassContract.View {

    var toolbar: Toolbar? = null
    var appbar: AppBarLayout? = null
    var tvTitle: AppCompatTextView? = null
    lateinit var context: Context
    @Inject
    lateinit var presenter: ResetPassPresenter
    private var mobile_no: String? = null


    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    private val mySMSBroadcastReceiver by lazy { NetworkReceiver() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_code)
        context = this
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
        tvTitle!!.text = "Verification Code"

        btn_continue.setOnClickListener(this)
        tv_resend.setOnClickListener(this)
        getIntentData()

        val client = SmsRetriever.getClient(this)
        val retriever = client.startSmsRetriever()
        retriever.addOnSuccessListener {
            val otpListener = object : NetworkReceiver.OTPReceiveListener {
                override fun onOTPReceived(otp: String) {
                    Toast.makeText(this@VerificationCodeActivity, otp, Toast.LENGTH_LONG).show()
                    et_code.setText(otp)
                    showResetPassword()
                }

                override fun onOTPTimeOut() {
                    Toast.makeText(this@VerificationCodeActivity, "TimeOut", Toast.LENGTH_SHORT).show()
                }
            }
            mySMSBroadcastReceiver.initOTPListener(otpListener)
            registerReceiver(mySMSBroadcastReceiver,
                    IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
        }
        retriever.addOnFailureListener {
            Toast.makeText(this@VerificationCodeActivity, "Problem to start listener", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_continue -> {
                    showResetPassword()
                }
                R.id.tv_resend -> {
                    resendOtp()
                }
            }
        }
    }

    private fun getIntentData() {
        intent?.extras?.apply {
            mobile_no = getString(Constants.MOBILE)
        }
    }

    override fun showValidateFieldsError(msg: String) {
        when (msg) {
            getString(R.string.err_otp_empty) -> et_code.error = msg
        }
    }

    override fun showResetPassword() {
        val resetPasswordIP = ResetCodeIp(otp = et_code.text.toString(), mobile_email = mobile_no)
        if (presenter.validate(resetPasswordIP))
            presenter.resetPassword(resetPasswordIP)
    }

    override fun resetPasswordRes(res: CustomerRes) {
        toast(res.message ?: getString(R.string.error_something_wrong))
        if (res.isSuccess()) {
            val intent = Intent(context, ChangePasswordActivity::class.java).putExtra(Constants.ID, res.result!!._id)
            startActivity(intent)
            finish()

        }
    }

    override fun resendOtp() {
        if (!mobile_no.isNullOrEmpty()) {
            presenter.resendOtp(ResetCodeIp(mobile_email = mobile_no))
        } else {
            toast(getString(R.string.please_register_first))
        }
    }

    override fun showOtpReqRes(res: CommonRes) {
        if (Validation.isValidStatus(res) && Validation.isValidString(res.code)) {
            toast(res.message ?: getString(R.string.error_something_wrong))
            if (!res.code.isNullOrEmpty())
                et_code.setText(res.code)
        } else
            toast(res.message ?: getString(R.string.error_something_wrong))
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
        if (mySMSBroadcastReceiver != null)
            unregisterReceiver(mySMSBroadcastReceiver)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}