package com.citygrocer.customer.screens.choosedelivery

import android.content.Context
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
import com.citygrocer.customer.customviews.ScreenStateView
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.module.AddAddressRes
import com.citygrocer.customer.module.AddAddressResIP
import com.citygrocer.customer.module.input.DeleteAddressIp
import com.citygrocer.customer.module.input.DeleteCartIp
import com.citygrocer.customer.screens.base.adapter.BaseRecAdapter
import com.citygrocer.customer.screens.delivery.DeliveryActivity
import com.citygrocer.customer.screens.deliveryaddress.DeliveryAddressActivity
import com.citygrocer.customer.util.*
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_choose_delivery_address.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import javax.inject.Inject

class ChooseDeliveryAddressActivity : DaggerAppCompatActivity(),
        ChooseAddressContract.View,
        IAdapterClickListener, View.OnClickListener {

    var toolbar: Toolbar? = null
    var appbar: AppBarLayout? = null
    var tvTitle: AppCompatTextView? = null
    lateinit var mContext: Context
    private var adapterCheckoutProducts: BaseRecAdapter? = null
    @Inject
    lateinit var presenter: ChooseAddressPresenter
    private var addressDeleteposition: Int = -1
    lateinit var chooseDeliveryAdressList: ArrayList<AddAddressResIP?>

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_delivery_address)
        initScreen()
    }

    override fun initScreen() {
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_LOADING
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        appbar = findViewById<View>(R.id.appbar) as AppBarLayout
        tvTitle = findViewById<View>(R.id.tvtitle) as AppCompatTextView
        tv_continue.setDrawableRight(R.drawable.ic_right_arrow_green)

        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        tvTitle!!.text = "Choose Delivery Address"
        tv_add_new_address.setOnClickListener(this)
        error_button.setOnClickListener(this)
        empty_button.setOnClickListener(this)
        tv_continue.setOnClickListener(this)
        getAddressList()
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    private fun getAddressList() {
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_LOADING
        presenter.getChooseAddressDeatails(deleteCartIp = DeleteCartIp(CommonUtils.getLoginId()))
        rv_add_address.layoutManager = LinearLayoutManager(this)
        adapterCheckoutProducts = BaseRecAdapter(this, R.layout.item_choose_delivery_address, adapterClickListener = this)
        rv_add_address!!.adapter = adapterCheckoutProducts
    }

    override fun showChooseAddressRes(res: AddAddressRes) {
        if (res.isSuccess()) {
            multiState_rview.viewState = ScreenStateView.VIEW_STATE_CONTENT
            if (Validation.isValidArrayList(res.result)) {
                res.result.withNotNullNorEmpty {
                    chooseDeliveryAdressList = filterNotNull() as ArrayList<AddAddressResIP?>
                    adapterCheckoutProducts!!.addList(this)
                }
            } else {
                multiState_rview.viewState = ScreenStateView.VIEW_STATE_EMPTY
            }
        } else {
            multiState_rview.viewState = ScreenStateView.VIEW_STATE_ERROR
        }
    }

    override fun showDeleteAddressRes(res: AddAddressRes) {
        if (res.isSuccess()) {
            chooseDeliveryAdressList.removeAt(addressDeleteposition)
            adapterCheckoutProducts!!.notifyItemRemoved(addressDeleteposition)
            initScreen()
        } else
            toast(res.message ?: getString(R.string.error_something_wrong))
    }

    override fun showErrorMsg(throwable: Throwable, apiType: String) {
        error_textView.text = getString(R.string.error_something_wrong)
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_ERROR
    }

    override fun adapterOnclick(itemData: Any, pos: Int, type: Any, op: String) {
        if (itemData is AddAddressResIP && !itemData._id.toString().isNullOrBlank()) {
            when (op) {
                Constants.DELETE -> {
                    addressDeleteposition = pos
                    presenter.getDeletaAddressDetails(deleteAddressIp = DeleteAddressIp(_id_customer = CommonUtils.getLoginId(),
                            _id = itemData._id))
                }
                Constants.EDIT -> {
                    val intent = Intent(this, DeliveryAddressActivity::class.java)
                    intent.putExtra(Constants.SOURCE, Constants.EDIT_ADDRESS)
                    intent.putExtra(Constants.ID, itemData._id)
                    startActivity(intent)
                    finish()
                }
                Constants.DELIVERY_ADDRESS -> {
                    if (itemData.default_address == 1) {
                        selectDeliveryAddressApi(itemData)
                    } else
                        toast("address already selected")
                }
            }
        }
    }

    private fun selectDeliveryAddressApi(itemData: AddAddressResIP) {
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_LOADING
        if (CommonUtils.getLoginId() != 0) {
            presenter.getSelectedAddressData(deleteAddressIp = DeleteAddressIp(CommonUtils.getLoginId(), itemData._id))
        } else {
            multiState_rview.viewState = ScreenStateView.VIEW_STATE_ERROR
        }
    }

    override fun showSelectedAddressRes(res: AddAddressRes) {
        if (res.isSuccess()) {
            multiState_rview.viewState = ScreenStateView.VIEW_STATE_CONTENT
            if (Validation.isValidArrayList(res.result)) {
                res.result.withNotNullNorEmpty {
                    getAddressList()
                }
            } else {
                multiState_rview.viewState = ScreenStateView.VIEW_STATE_EMPTY
            }
        } else {
            multiState_rview.viewState = ScreenStateView.VIEW_STATE_ERROR
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_add_new_address -> {
                startActivity(Intent(this, DeliveryAddressActivity::class.java)
                        .putExtra(Constants.SOURCE, Constants.ADD_ADDRESS))
                finish()
            }
            R.id.empty_button -> {
                initScreen()
            }
            R.id.error_button -> {
                initScreen()
            }
            R.id.tv_continue -> {
                if (chooseDeliveryAdressList[0]?.default_address == 0) {
                    startActivity(Intent(this, DeliveryActivity::class.java).putExtra(Constants.SOURCE, chooseDeliveryAdressList[0]?.town))
                    finish()
                }else{
                    toast("Please select default address")
                }
            }
        }
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
