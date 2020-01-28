package com.citygrocer.customer.screens.delivery

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.view.View
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.customviews.ScreenStateView
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.module.CheckOutDeliveryRes
import com.citygrocer.customer.module.DeliverySlot
import com.citygrocer.customer.module.input.CheckOutIp
import com.citygrocer.customer.screens.choosedelivery.ChooseDeliveryAddressActivity
import com.citygrocer.customer.screens.delivery.fragments.TimeSlotDialogFragment
import com.citygrocer.customer.screens.payment.PaymentActivity
import com.citygrocer.customer.util.CommonUtils
import com.citygrocer.customer.util.setDrawableLeft
import com.citygrocer.customer.util.setDrawableRight
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_delivery.*
import kotlinx.android.synthetic.main.error_view.*
import java.util.*
import javax.inject.Inject


class DeliveryActivity : DaggerAppCompatActivity(), IAdapterClickListener, View.OnClickListener, DeliveryContract.View {

    var toolbar: Toolbar? = null
    var appbar: AppBarLayout? = null
    var tvTitle: AppCompatTextView? = null
    var source: String? = null
    lateinit var deliveryslot: ArrayList<DeliverySlot?>
    @Inject
    lateinit var presenter: DeliveryPresenter

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery)

        initScreen()
    }

    override fun initScreen() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        appbar = findViewById<View>(R.id.appbar) as AppBarLayout
        tvTitle = findViewById<View>(R.id.tvtitle) as AppCompatTextView
        tv_back_to_cart.setDrawableLeft(R.drawable.ic_navigate_before_black)
        tv_payment.setDrawableRight(R.drawable.ic_right_arrow_green)

        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        tvTitle!!.text = "Delivery"

        tv_change_address.setOnClickListener(this)
        tv_payment.setOnClickListener(this)
        tv_slot_time.setOnClickListener(this)
        getIntentData()
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    private fun getIntentData() {
        intent?.extras?.apply {
            source = getString(Constants.SOURCE)
        }
        getCheckOutData()
    }

    private fun getCheckOutData() {
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_LOADING
        presenter.getDeliveryDeatails(checkOutIp = CheckOutIp(zone_name = source,
                _id_customer = CommonUtils.getLoginId()))
    }

    override fun showCheckoutDetails(res: CheckOutDeliveryRes) {
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_CONTENT
        if (res.result!!.isNotEmpty()) {
            deliveryslot = res.result[0]?.delivery_slots!!
            if (res.result[0]!!.address!![0]!!.default_address == 0) {
                tv_delivery_address.text = res.result[0]!!.address!![0]!!.getAddressString()
            }
            if (res.result[0]!!.summary!!.isNotEmpty()) {
                tv_cart_mrp_total.text = CommonUtils.getRupeesSymbol(this, res.result[0]!!.summary!![0]!!.cart_mrp_total.toString())
                tv_cart_dicsount_total.text = CommonUtils.getNegRupeesSymbol(this, res.result[0]!!.summary!![0]!!.cart_discount.toString())
                tv_cart_price_total.text = CommonUtils.getRupeesSymbol(this, res.result[0]!!.summary!![0]!!.cart_price_total.toString())
                tv_delivery_charges_total.text = CommonUtils.getRupeesSymbol(this, res.result[0]!!.summary!![0]!!.delivery_charge.toString())
                tv_total_price_total.text = CommonUtils.getRupeesSymbol(this, res.result[0]!!.summary!![0]!!.order_total.toString())
                tv_count.text = StringBuilder().append("(").append(res.result[0]!!.summary!![0]!!.cart_count.toString()).append(" items").append(")")
            }
            if (res.result[0]?.delivery_slots!![0]?.date!!.isNotEmpty()) {
                val mDate = res.result[0]?.delivery_slots!![1]?.date!!
                //DateFormat.format("MMMM d, HH:mm", mDate).toString()
                tv_day.text = res.result[0]?.delivery_slots!![1]?.date!!
            }
        } else {
            multiState_rview.viewState = ScreenStateView.VIEW_STATE_ERROR
        }
    }

    override fun showErrorMsg(throwable: Throwable, apiType: String) {
        error_textView.text = getString(R.string.error_something_wrong)
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_ERROR
    }

    override fun adapterOnclick(itemData: Any, pos: Int, type: Any, op: String) {

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_change_address -> {
                startActivity(Intent(this, ChooseDeliveryAddressActivity::class.java))
            }
            R.id.tv_slot_time -> {
                showTheDialog()
            }
            R.id.tv_payment -> {
                startActivity(Intent(this, PaymentActivity::class.java))
            }
        }

    }

    private fun showTheDialog() {
        val newFragment = TimeSlotDialogFragment.newInstance(deliveryslot)
        newFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0)
        newFragment.show(supportFragmentManager, "dialog")
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}


