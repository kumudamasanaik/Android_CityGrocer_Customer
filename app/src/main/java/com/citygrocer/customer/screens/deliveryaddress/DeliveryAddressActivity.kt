package com.citygrocer.customer.screens.deliveryaddress

import android.content.Context
import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.view.View
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.module.AddAddressRes
import com.citygrocer.customer.module.input.AddAddressIp
import com.citygrocer.customer.module.input.DeleteAddressIp
import com.citygrocer.customer.screens.choosedelivery.ChooseDeliveryAddressActivity
import com.citygrocer.customer.screens.location.LocationActivity
import com.citygrocer.customer.util.CommonUtils
import com.citygrocer.customer.util.Validation
import com.citygrocer.customer.util.toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_delivery_address.*
import kotlinx.android.synthetic.main.error_view.*
import javax.inject.Inject

class DeliveryAddressActivity : DaggerAppCompatActivity(),
        AddDeliveryAddressContract.View,
        IAdapterClickListener, View.OnClickListener {

    var toolbar: Toolbar? = null
    var appbar: AppBarLayout? = null
    var tvTitle: AppCompatTextView? = null
    private var mActivity: Context? = null
    private var address_type = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    @Inject
    lateinit var presenter: AddDeliveryAddressPresenter
    private var address_id: Int? = 0
    private var source: String = ""
    private var address: Address? = null

    private var mLastClickTime: Long = 0

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_address)
        initScreen()
    }

    override fun initScreen() {
        mActivity = this
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        appbar = findViewById<View>(R.id.appbar) as AppBarLayout
        tvTitle = findViewById<View>(R.id.tvtitle) as AppCompatTextView
        setSupportActionBar(toolbar)

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        tvTitle!!.text = "Delivery Address"

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        radio_home.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                address_type = 1
            else
                address_type = 0
        }
        radio_office.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                address_type = 0
            else
                address_type = 1
        }
        rl_location_details.setOnClickListener(this)
        tv_save_addresss.setOnClickListener(this)
        tv_back_to_cart.setOnClickListener(this)
        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
        }
        getIntentData()
        if (source == Constants.EDIT_ADDRESS) {
            getEditAddress()
        }
        if (CommonUtils.getMyAddress() != null) {
            address = CommonUtils.getMyAddress()
            tv_location.text = address!!.getAddressLine(0)
        } else {
            startActivity(Intent(this, LocationActivity::class.java).putExtra(Constants.SOURCE, Constants.ADD_ADDRESS))
            finish()
        }
    }

    private fun getIntentData() {
        intent?.extras?.apply {
            if (source.isNotEmpty() && source.contentEquals(Constants.SOURCE_LOCATION_ACTIVITY)) {
                address = getParcelable(Constants.ADDRESS_LOC)
                source = getString(Constants.SOURCE)
                setUpAddressData()
                return
            } else {
                source = getString(Constants.SOURCE)
                address_id = getInt(Constants.ID)
            }
        }
    }

    private fun setUpAddressData() {
        CommonUtils.saveCurrentLocation(address)
        address?.apply {

            if (Validation.isValidString(postalCode))
                et_pincode.setText(postalCode)
            if (Validation.isValidString(adminArea))
                et_state.setText(adminArea)
            if (Validation.isValidString(locality))
                et_city.setText(locality)
            if (Validation.isValidString(subLocality))
                et_locality.setText(subLocality)

        }
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.rl_location_details -> {
                    startActivity(Intent(this, LocationActivity::class.java).putExtra(Constants.SOURCE, Constants.ADD_ADDRESS))
                    finish()
                }
                R.id.tv_save_addresss -> {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return
                    }
                    mLastClickTime = SystemClock.elapsedRealtime()
                    if (source == Constants.EDIT_ADDRESS) {
                        getUpdateAddress()
                    } else
                        AddAddress()
                }
                R.id.tv_back_to_cart -> {
                    onBackPressed()
                }
            }
        }
    }

    override fun showProgress(msg: String) {
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        super.hideProgress()
        progress_bar.visibility = View.GONE
    }

    override fun showValidateFieldsError(msg: String) {

        when (msg) {
            getString(R.string.please_enter_pincode) -> et_pincode.error = msg
            getString(R.string.please_enter_state) -> et_state.error = msg
            getString(R.string.please_enter_city) -> et_city.error = msg
            getString(R.string.please_enter_town) -> et_locality.error = msg
            getString(R.string.please_enter_address) -> address_details.error = msg
        }

    }

    private fun AddAddress() {
        val addressIp = AddAddressIp(_id_customer = CommonUtils.getLoginId(),
                city = et_city.text.toString(),
                pincode = et_pincode.text.toString(),
                state = et_state.text.toString(),
                town = et_locality.text.toString(),
                address = address_details.text.toString(),
                address_type = address_type)
        if (presenter.validate(addressIp))
            presenter.getAddAddressDeatails(addressIp)
    }

    private fun getEditAddress() {
        presenter.getEditAddressDetails(deleteAddressIp = DeleteAddressIp(_id_customer = CommonUtils.getLoginId(),
                _id = address_id))
    }

    private fun getUpdateAddress() {
        val addressIp = AddAddressIp(_id_customer = CommonUtils.getLoginId(),
                _id = address_id,
                city = et_city.text.toString(),
                pincode = et_pincode.text.toString(),
                state = et_state.text.toString(),
                town = et_locality.text.toString(),
                address = address_details.text.toString(),
                address_type = address_type)
        if (presenter.validate(addressIp))
            presenter.getUpdatedAddressDetails(addressIp)
    }

    override fun showAddAddressRes(res: AddAddressRes) {
        if (res.isSuccess()) {
            toast(res.message!!)
            startActivity(Intent(this, ChooseDeliveryAddressActivity::class.java))
            finish()
        }
    }

    override fun showEditAddressRes(res: AddAddressRes) {
        if (res.isSuccess()) {
            et_city.setText(res.result!![0]!!.city.toString())
            et_pincode.setText(res.result[0]!!.pincode.toString())
            et_state.setText(res.result[0]!!.state.toString())
            et_locality.setText(res.result[0]!!.town.toString())
            address_details.setText(res.result[0]!!.address.toString())
        }
    }

    override fun showUpdatedAddressRes(res: AddAddressRes) {
        if (res.isSuccess()) {
            toast(res.message!!)
            startActivity(Intent(this, ChooseDeliveryAddressActivity::class.java))
            finish()
        }
    }

    override fun showErrorMsg(throwable: Throwable, apiType: String) {
        error_textView.text = getString(R.string.error_something_wrong)
    }

    override fun adapterOnclick(itemData: Any, pos: Int, type: Any, op: String) {

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
