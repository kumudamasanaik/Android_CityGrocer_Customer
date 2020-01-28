package com.citygrocer.customer.screens.changepassword

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.module.ChangePassRes
import com.citygrocer.customer.module.input.ChangePassIp
import com.citygrocer.customer.screens.login.LoginActivity
import com.citygrocer.customer.util.CommonUtils
import com.citygrocer.customer.util.SharedPreferenceManager
import com.citygrocer.customer.util.toast
import com.citygrocer.customer.util.withNotNullNorEmpty
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_registration.*
import javax.inject.Inject

class ChangePasswordActivity : DaggerAppCompatActivity(), View.OnClickListener, ChangePassContract.View {

    var toolbar: Toolbar? = null
    var appbar: AppBarLayout? = null
    var tvTitle: AppCompatTextView? = null
    var mContext: Context? = null
    var id: Int = 0
    @Inject
    lateinit var presenter: ChangePassPresenter

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        mContext = this
        intent.extras?.apply {
            id = getInt(Constants.ID)
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
        tvTitle!!.text = "Change Password"

        btn_submit.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_submit -> {
                    if (et_new_pass.text.toString() == et_re_pass.text.toString()) {
                        showChangePasswordLoader()
                    } else {
                        toast(R.string.password_mismatch)
                    }
                }
            }
        }
    }

    override fun showValidateFieldsError(msg: String) {
        when (msg) {
            getString(R.string.err_new_password) -> et_new_pass.error = msg
            getString(R.string.err_re_password) -> et_re_pass.error = msg
            getString(R.string.err_password_length) -> et_new_pass.error = msg
        }
    }

    override fun showChangePasswordLoader() {

        val passwordIP = ChangePassIp(
                password = et_new_pass.text.toString(),
                re_password = et_re_pass.text.toString(),
                _id = id.toString()
        )
        if (presenter.validate(passwordIP))
            presenter.changePassword(passwordIP)

    }

    override fun changePassRes(res: ChangePassRes) {

        if (res.isSuccess()) {
            res.result.withNotNullNorEmpty {
                showSuccessDialog()
            }
        }

    }

    override fun showErrorMsg(throwable: Throwable, apiType: String) {
        toast(throwable.message ?: getString(R.string.error_something_wrong))
    }

    private fun showSuccessDialog() {
        val pendingTransactionDialog = Dialog(mContext, R.style.CustomDialogThemeLightBg)
        pendingTransactionDialog.setCanceledOnTouchOutside(true)
        pendingTransactionDialog.setContentView(R.layout.dialog_ok_cancel)
        (pendingTransactionDialog.findViewById(R.id.dialog_title) as TextView).text = mContext!!.getString(R.string.congratulations)
        (pendingTransactionDialog.findViewById(R.id.dialog_text) as TextView).text = mContext!!.getString(R.string.password_reset_success)
        (pendingTransactionDialog.findViewById(R.id.dialog_action) as TextView).text = mContext!!.getString(R.string.continue_verification).toUpperCase()
        (pendingTransactionDialog.findViewById(R.id.dialog_action) as TextView).setOnClickListener {
            pendingTransactionDialog.dismiss()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        pendingTransactionDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dropView()
    }

    override fun onSupportNavigateUp(): Boolean {
        startActivity(Intent(mContext, LoginActivity::class.java))
        finish()
        return true
    }

}