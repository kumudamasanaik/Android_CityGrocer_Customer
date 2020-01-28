package com.citygrocer.customer.screens.applycoupon

import android.content.Context
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.citygrocer.customer.R
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.screens.base.adapter.BaseRecAdapter
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_all_categories.*
import kotlinx.android.synthetic.main.activity_apply_coupon.*

class ApplyCouponActivity : DaggerAppCompatActivity(), View.OnClickListener, IAdapterClickListener {

    lateinit var mContext: Context
    var toolbar: Toolbar? = null
    var appbar: AppBarLayout? = null
    var etSearch: AppCompatEditText? = null
    var icScan: AppCompatImageView? = null
    var icMike: AppCompatImageView? = null
    var icClose: AppCompatImageView? = null
    var adapterChooseCoupons: BaseRecAdapter? = null

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_coupon)
        mContext = this
        initScreen()
    }

    private fun initScreen() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        appbar = findViewById<View>(R.id.appbar) as AppBarLayout
        etSearch = findViewById<View>(R.id.et_search) as AppCompatEditText
        icScan = findViewById<View>(R.id.iv_scan) as AppCompatImageView
        icMike = findViewById<View>(R.id.iv_mic) as AppCompatImageView
        icClose = findViewById<View>(R.id.iv_close) as AppCompatImageView
        icScan!!.visibility = View.GONE
        icMike!!.visibility = View.GONE
        icClose!!.visibility = View.GONE
        etSearch!!.hint = "Enter coupon code here"

        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        rv_valid_coupons.layoutManager = LinearLayoutManager(this)
        adapterChooseCoupons = BaseRecAdapter(mContext!!, R.layout.item_choose_valid_coupon, adapterClickListener = this)
        rv_valid_coupons.adapter = adapterChooseCoupons
        var couponCodeList = ArrayList<String>()
        couponCodeList.add("")
        couponCodeList.add("")
        adapterChooseCoupons!!.addList(couponCodeList)

        rv_not_valid_coupons.layoutManager = LinearLayoutManager(this)
        adapterChooseCoupons = BaseRecAdapter(mContext!!, R.layout.item_not_valid_coupons, adapterClickListener = this)
        rv_not_valid_coupons.adapter = adapterChooseCoupons
        var notValidcouponCodeList = ArrayList<String>()
        notValidcouponCodeList.add("")
        notValidcouponCodeList.add("")
        adapterChooseCoupons!!.addList(notValidcouponCodeList)

    }

    override fun onClick(v: View?) {

    }

    override fun adapterOnclick(itemData: Any, pos: Int, type: Any, op: String) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
