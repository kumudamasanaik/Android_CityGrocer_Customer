package com.citygrocer.customer.screens.checkout

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.screens.base.adapter.BaseRecAdapter
import com.citygrocer.customer.screens.cart.CartActivity
import com.citygrocer.customer.screens.delivery.DeliveryActivity
import com.citygrocer.customer.util.setDrawableLeft
import com.citygrocer.customer.util.setDrawableRight
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_item_check_out.*

class ItemCheckOutActivity : DaggerAppCompatActivity(), IAdapterClickListener, View.OnClickListener {

    var toolbar: Toolbar? = null
    var appbar: AppBarLayout? = null
    var tvTitle: AppCompatTextView? = null
    private var adapterCheckoutProducts: BaseRecAdapter? = null

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_check_out)

        initScreen()
    }

    private fun initScreen() {

        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        appbar = findViewById<View>(R.id.appbar) as AppBarLayout
        tvTitle = findViewById<View>(R.id.tvtitle) as AppCompatTextView
        tv_back_to_cart.setDrawableLeft(R.drawable.ic_navigate_before_black)
        tv_continue.setDrawableRight(R.drawable.ic_right_arrow_green)

        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        tvTitle!!.text = "Items Availability Check"

        rv_items_in_cart.layoutManager = LinearLayoutManager(this)
        adapterCheckoutProducts = BaseRecAdapter(this, R.layout.item_checkout_out_of_stock, adapterClickListener = this)
        rv_items_in_cart!!.adapter = adapterCheckoutProducts
        var checkOutProductList = ArrayList<String>()
        checkOutProductList.add("")
        checkOutProductList.add("")
        checkOutProductList.add("")
        checkOutProductList.add("")
        adapterCheckoutProducts!!.addList(checkOutProductList)

        tv_back_to_cart.setOnClickListener(this)
        tv_continue.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.tv_back_to_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                }
                R.id.tv_continue -> {
                    startActivity(Intent(this, DeliveryActivity::class.java))
                }
            }
        }
    }

    override fun adapterOnclick(itemData: Any, pos: Int, type: Any, op: String) {
        when (op) {
            Constants.FIND_SIMILAR -> {
                val sort = FragmentSimilarProducts()
                sort.show(this.supportFragmentManager, sort.tag)
                sort.isCancelable = false
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
