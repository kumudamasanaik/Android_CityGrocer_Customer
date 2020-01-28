package com.citygrocer.customer.screens.otpverification

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.citygrocer.customer.R
import com.citygrocer.customer.broadcastreceiver.NetworkReceiver
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.module.CommonRes
import com.citygrocer.customer.module.CustomerRes
import com.citygrocer.customer.module.input.MergeCartIp
import com.citygrocer.customer.module.input.ResetCodeIp
import com.citygrocer.customer.module.input.VerifyOtpIp
import com.citygrocer.customer.screens.cart.CartActivity
import com.citygrocer.customer.screens.home.HomeActivity
import com.citygrocer.customer.screens.registration.RegistrationActivity
import com.citygrocer.customer.util.CommonUtils
import com.citygrocer.customer.util.Validation
import com.citygrocer.customer.util.toast
import com.google.android.gms.auth.api.phone.SmsRetriever
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_opt_verification.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OtpVerificationActivity : DaggerAppCompatActivity(), View.OnClickListener, OtpVerificationContract.View {

    var toolbar: Toolbar? = null
    var appbar: AppBarLayout? = null
    var tvTitle: AppCompatTextView? = null
    private lateinit var mContext: Context
    @Inject
    lateinit var presenter: OtpVerificationPresenter
    private var mobileNo: String? = null
    private var source: String? = null
    private val mySMSBroadcastReceiver by lazy { NetworkReceiver() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opt_verification)
        mContext = this
        intent?.extras?.apply {
            source = getString(Constants.SOURCE)
            mobileNo = getString(Constants.MOBILE)
        }
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
        tvTitle!!.text = "OTP verification"

        btn_verify.setOnClickListener(this)
        tv_edit.setOnClickListener(this)
        tv_resend.setOnClickListener(this)
        tv_mobile_no.text = mobileNo
        setupOTpReader()

        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tv_time.text = ("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished))),
                        (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))))
            }

            override fun onFinish() {
                tv_time.text = getString(R.string.time_out)
                tv_resend.visibility = View.VISIBLE
            }
        }.start()
    }

    companion object {
        private const val FORMAT = "%02d:%02d:%02d"
    }

    private fun setupOTpReader() {

        val client = SmsRetriever.getClient(this)
        val task = client.startSmsRetriever()
        task.addOnSuccessListener {
            val otpListener = object : NetworkReceiver.OTPReceiveListener {
                override fun onOTPReceived(otp: String) {
                    Toast.makeText(this@OtpVerificationActivity, otp, Toast.LENGTH_LONG).show()
                    pinview.value = otp
                    showSubmitOtp()
                }

                override fun onOTPTimeOut() {
                    Toast.makeText(this@OtpVerificationActivity, "TimeOut", Toast.LENGTH_SHORT).show()
                }
            }
            mySMSBroadcastReceiver.initOTPListener(otpListener)
            registerReceiver(mySMSBroadcastReceiver,
                    IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
        }
        task.addOnFailureListener {
            Toast.makeText(this@OtpVerificationActivity, "Problem to start listener", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_verify -> {
                    showSubmitOtp()
                }
                R.id.tv_edit -> {
                    val intent = Intent(this, RegistrationActivity::class.java)
                    intent.putExtra(Constants.SOURCE, Constants.EDIT)
                    startActivity(intent)
                }
                R.id.tv_resend -> {
                    resendOtp()
                }
            }
        }
    }
/*
    override fun onOTPReceived(otp: String) {
        if (otp.isNotEmpty()) {
            pinview.value = CommonUtils.getOTP()
            showSubmitOtp()
        }
    }

    override fun onOTPTimeOut() {
        toast(getString(R.string.error_something_wrong))
    }*/

    override fun showSubmitOtp() {
        if (!pinview.value.isNullOrBlank()) {
            presenter.verifyOtp(VerifyOtpIp(otp = pinview.value, mobile_email = mobileNo!!))
        } else
            pinview.setError(getString(R.string.err_otp_empty))
    }

    override fun showOtpVerifyRes(res: CustomerRes) {
        toast(res.message ?: getString(R.string.error_something_wrong))
        if (res.isSuccess())
            CommonUtils.saveUserRegisteredData(res.result)
        navigateNextScreen()
    }

    override fun resendOtp() {
        if (!mobileNo.isNullOrEmpty()) {
            presenter.resendOtp(ResetCodeIp(mobile_email = mobileNo!!))
        } else
            toast(getString(R.string.please_register_first))
    }

    override fun showOtpReqRes(res: CommonRes) {
        pinview.clearValue()
        if (Validation.isValidStatus(res) && Validation.isValidString(res.code)) {
            toast(res.message ?: getString(R.string.error_something_wrong))
            if (!res.code.isNullOrEmpty())
                pinview.value = res.code
        } else
            toast(res.message ?: getString(R.string.error_something_wrong))
    }

    private fun navigateNextScreen() {
        if (CommonUtils.getCartCount() != 0) {
            showMergeCart()
        } else {
            val intent = Intent(this, HomeActivity::class.java).putExtra("isRegistered", true)
            startActivity(intent)
            finish()
        }
    }

    private fun showMergeCart() {
        val mergeCartIp = MergeCartIp(
                session_id = CommonUtils.getSession(),
                _id_customer = CommonUtils.getLoginId()
        )
        presenter.mergeCart(mergeCartIp)
    }

    override fun showMergeCartRes(res: CommonRes) {
        if (res.isSuccess()) {
            val intent = Intent(this, CartActivity::class.java).putExtra(Constants.SOURCE, Constants.LOGIN)
            startActivity(intent)
            finish()
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
        if (mySMSBroadcastReceiver != null)
            unregisterReceiver(mySMSBroadcastReceiver)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}