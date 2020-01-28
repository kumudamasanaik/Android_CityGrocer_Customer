package com.citygrocer.customer.screens.payment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Window
import com.citygrocer.customer.R
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.screens.applycoupon.ApplyCouponActivity
import com.citygrocer.customer.screens.placeorder.PlaceOrderActivity
import com.citygrocer.customer.util.setDrawableLeft
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : DaggerAppCompatActivity(), IAdapterClickListener, View.OnClickListener {

    var toolbar: Toolbar? = null
    var appbar: AppBarLayout? = null
    var tvTitle: AppCompatTextView? = null
    lateinit var dialog:Dialog

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        initScreen()
    }

    private fun initScreen() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        appbar = findViewById<View>(R.id.appbar) as AppBarLayout
        tvTitle = findViewById<View>(R.id.tvtitle) as AppCompatTextView
        tv_coupon.setDrawableLeft(R.drawable.ic_coupon_black)
        tv_cg_points.setDrawableLeft(R.drawable.ic_cg_points)

        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        tvTitle!!.text = "Payment"

        btn_place_order.setOnClickListener(this)
        rl_apply_coupon.setOnClickListener(this)
        tv_apply_coupon.setOnClickListener(this)
        rl_redeem_points.setOnClickListener(this)
        tv_redeem_points.setOnClickListener(this)
    }

    override fun adapterOnclick(itemData: Any, pos: Int, type: Any, op: String) {

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_place_order -> {
                    startActivity(Intent(this, PlaceOrderActivity::class.java))
                }
                R.id.rl_apply_coupon -> {
                    startActivity(Intent(this, ApplyCouponActivity::class.java))
                }
                R.id.tv_apply_coupon -> {
                    startActivity(Intent(this, ApplyCouponActivity::class.java))
                }
                R.id.rl_redeem_points -> {
                    dialogPopup()
                }
                R.id.tv_redeem_points -> {
                    dialogPopup()
                }

            }

        }

    }

    private fun dialogPopup() {

        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.alert_redeem_cg_points)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val window = dialog.getWindow()
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
        val cancel = dialog.findViewById(R.id.btn_cancel) as AppCompatButton
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        val redeem = dialog.findViewById(R.id.btn_redeem) as AppCompatButton
        redeem.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}
