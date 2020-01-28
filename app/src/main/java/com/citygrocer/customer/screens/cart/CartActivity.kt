package com.citygrocer.customer.screens.cart

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.customviews.ScreenStateView
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.module.AddCartRes
import com.citygrocer.customer.module.CommonRes
import com.citygrocer.customer.module.ProductCatResIp
import com.citygrocer.customer.module.ProductCategoryRes
import com.citygrocer.customer.module.input.CartSessionIp
import com.citygrocer.customer.module.input.CustomerIdIP
import com.citygrocer.customer.module.input.DeleteCartIp
import com.citygrocer.customer.screens.base.BaseActivity
import com.citygrocer.customer.screens.choosedelivery.ChooseDeliveryAddressActivity
import com.citygrocer.customer.screens.deliveryaddress.DeliveryAddressActivity
import com.citygrocer.customer.screens.home.HomeActivity
import com.citygrocer.customer.screens.landing.LandingActivity
import com.citygrocer.customer.util.CommonUtils
import com.citygrocer.customer.util.Validation
import com.citygrocer.customer.util.toast
import com.citygrocer.customer.util.withNotNullNorEmpty
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import javax.inject.Inject


class CartActivity : BaseActivity(), View.OnClickListener, IAdapterClickListener, CartContract.View {

    private var mActivity: Context? = null
    @Inject
    lateinit var presenter: CartPresenter
    var loginId: Int? = null
    lateinit var prodAdapter: CartAdapter
    var productList: ArrayList<ProductCatResIp>? = null
    lateinit var cartList: ProductCategoryRes
    private var modifiedProd: ProductCatResIp? = null

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LayoutInflater.from(baseContext).inflate(R.layout.activity_cart, fragmentLayout)

        mActivity = this
        showBack()
        setTitle("My Cart")
        showCart()
        ivSearch!!.visibility = View.VISIBLE
        loginId = CommonUtils.getLoginId()
        initScreen()
    }

    override fun initScreen() {
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_LOADING
        presenter.takeView(this)
        ivCart!!.setOnClickListener(this)
        btn_checkout.setOnClickListener(this)
        btn_add.setOnClickListener(this)
        error_button.setOnClickListener(this)
        empty_button.setOnClickListener(this)
        /* drop_down.setDrawableRight(R.drawable.ic_down_arrow)
         tv_mrp.strikeThr()
         tv_mrp1.strikeThr()*/
        setUpProd()

        /*  rv_last_minute_buys.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
          adapterLastMinuteProducts = BaseRecAdapter(mActivity!!, R.layout.item_dashboard_product, adapterClickListener = this)
          rv_last_minute_buys.adapter = adapterLastMinuteProducts
          var headerProductsList = java.util.ArrayList<String>()
          headerProductsList.add("")
          headerProductsList.add("")
          headerProductsList.add("")
          headerProductsList.add("")
          adapterLastMinuteProducts!!.addList(headerProductsList)*/
    }

    private fun setUpProd() {
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_LOADING
        if (CommonUtils.getLoginId() != 0) {
            if (loginId != null) {
                presenter.loadCartCustomerId(productImageViewIp = CustomerIdIP(CommonUtils.getLoginId()))
            }
        } else {
            presenter.loadCartSession(cartSessionIp = CartSessionIp(CommonUtils.getSession()))
        }
        prodAdapter = CartAdapter(context = mActivity!!, adapterClickListener = this)
        val layoutManager = LinearLayoutManager(mActivity!!, LinearLayoutManager.VERTICAL, false)
        rv_grocery_staples.apply {
            setLayoutManager(layoutManager)
            rv_grocery_staples.adapter = prodAdapter
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_cart -> {
                    if (prodAdapter.itemCount > 0)
                        AlertMessage()
                }
                R.id.btn_checkout -> {
                    if (CommonUtils.getLoginId() != 0 && CommonUtils.getDeliveryAddress().isNotEmpty()) {
                        startActivity(Intent(this, ChooseDeliveryAddressActivity::class.java))
                    } else if (CommonUtils.getLoginId() != 0) {
                        startActivity(Intent(this, DeliveryAddressActivity::class.java)
                                .putExtra(Constants.SOURCE, Constants.CART))
                    } else {
                        startActivity(Intent(this, LandingActivity::class.java))
                        finish()
                    }
                }
                R.id.btn_add -> {
                    ll_add.visibility = View.GONE
                    ll_remove.visibility = View.VISIBLE
                    become_member.visibility = View.GONE
                    member.visibility = View.VISIBLE
                }
                R.id.error_button -> {
                    initScreen()
                }
                R.id.empty_button -> {
                    initScreen()
                }
            }
        }
    }

    override fun setCartRes(productListRes: ProductCategoryRes) {
        if (productListRes.isSuccess()) {
            cartList = productListRes
            if (Validation.isValidArrayList(productListRes.result)) {
                productListRes.result.withNotNullNorEmpty {
                    productList = filterNotNull() as ArrayList<ProductCatResIp>
                    productList = filter { it?.sku != null && it.sku!!.size > 0 } as ArrayList<ProductCatResIp>
                    productList.withNotNullNorEmpty {
                        multiState_rview.viewState = ScreenStateView.VIEW_STATE_CONTENT
                        showCartSummery()
                        CommonUtils.saveCartCount(productListRes)
                        prodAdapter.addList(this)
                        ll_empty_cart.visibility = View.GONE
                        scroll.visibility = View.VISIBLE
                        ll_footer.visibility = View.VISIBLE
                    }
                }
            } else {
                multiState_rview.viewState = ScreenStateView.VIEW_STATE_CONTENT
                ll_empty_cart.visibility = View.VISIBLE
                scroll.visibility = View.GONE
                ll_footer.visibility = View.GONE
                CommonUtils.saveCartCount(productListRes)
                btn_start_shopping.setOnClickListener {
                    startActivity(Intent(this, HomeActivity::class.java))
                }
            }

        } else {
            multiState_rview.viewState = ScreenStateView.VIEW_STATE_ERROR
        }

    }

    override fun showCartSummery() {
        cartList.summary.apply {
            tv_cart_item_count.text = "(" + cartList.summary!![0]?.cart_count.toString() + ")"
            tv_price.text = CommonUtils.getRupeesSymbol(mActivity!!, cartList.summary!![0]!!.grand_total.toString())
            tv_total_amount.text = CommonUtils.getRupeesSymbol(mActivity!!, cartList.summary!![0]!!.grand_total.toString())
        }
    }

    override fun showErrorMsg(throwable: Throwable, apiType: String) {
        error_textView.text = getString(R.string.error_something_wrong)
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_ERROR
    }

    private fun AlertMessage() {
        val pendingTransactionDialog = Dialog(mContext, R.style.CustomDialogThemeLightBg)
        pendingTransactionDialog.setCanceledOnTouchOutside(true)
        pendingTransactionDialog.setContentView(R.layout.dialog_alert_message)
        (pendingTransactionDialog.findViewById(R.id.dialog_title) as TextView).text = mContext!!.getString(R.string.empty_your_cart)
        (pendingTransactionDialog.findViewById(R.id.dialog_text) as TextView).text = mContext!!.getString(R.string.message)
        (pendingTransactionDialog.findViewById(R.id.tv_cancel) as TextView).text = mContext!!.getString(R.string.cancel).toUpperCase()
        (pendingTransactionDialog.findViewById(R.id.tv_empty_cart) as TextView).text = mContext!!.getString(R.string.empty_cart).toUpperCase()

        (pendingTransactionDialog.findViewById(R.id.tv_cancel) as TextView).setOnClickListener {
            pendingTransactionDialog.dismiss()
        }

        (pendingTransactionDialog.findViewById(R.id.tv_empty_cart) as TextView).setOnClickListener {
            pendingTransactionDialog.dismiss()
            getDeleteCartList()

        }
        pendingTransactionDialog.show()
    }

    private fun getDeleteCartList() {
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_LOADING
        if (CommonUtils.getLoginId() != 0) {
            presenter.diaplayCartDeleteList(deleteCartIp = DeleteCartIp(CommonUtils.getLoginId()))
        } else {
            presenter.diaplayCartSessionDeleteList(cartSessionIp = CartSessionIp(CommonUtils.getSession()))
        }
    }

    override fun showDeleteCartList(res: CommonRes) {
        if (res.isSuccess()) {
            multiState_rview.viewState = ScreenStateView.VIEW_STATE_CONTENT
            ll_empty_cart.visibility = View.VISIBLE
            scroll.visibility = View.GONE
            ll_footer.visibility = View.GONE
            btn_start_shopping.setOnClickListener {
                startActivity(Intent(this, HomeActivity::class.java))
            }

        }
    }

    override fun showAddedCartRes(addCartRes: AddCartRes) {
        hideProgress()
        if (addCartRes.isSuccess()) {
            toast(addCartRes.message!!)
            initScreen()
        } else {
            toast(addCartRes.message!!)
        }
    }

    override fun showRemoveFromCart(addCartRes: AddCartRes) {
        hideProgress()
        prodAdapter.showModifiedRes(if (addCartRes.isSuccess()) Constants.SUCCESS else Constants.RES_FAILED)
    }


    override fun showProgress(msg: String) {
        CommonUtils.showLoading(mContext!!, getString(R.string.please_wait), true)
    }

    override fun hideProgress() {
        CommonUtils.hideLoading()
    }


    override fun adapterOnclick(itemData: Any, pos: Int, type: Any, op: String) {

        if (itemData is ProductCatResIp && itemData.selSku!!.id_product != null) {
            modifiedProd = itemData
            when (op) {

                /*Constants.SOURCE_SIGN -> {
                    if (itemData is ProductCatResIp) {
                        val skuList = itemData.sku?.filterNotNull()
                        skuList.withNotNullNorEmpty {
                            val sort = SkuDialogFragment.newInstance(skuList as ArrayList<DataSku>)
                            sort.show(supportFragmentManager, sort.tag)
                            sort.isCancelable = true
                        }
                    }
                }*/
                Constants.OP_ADD_CART -> {
                    presenter.displayAddCart(itemData.addCartIp!!)
                }
                Constants.OP_MODIFY_CART -> {
                    presenter.displayAddCart(itemData.addCartIp!!)
                }
                Constants.OP_REMOVE_FROM_CART -> {
                    presenter.displayAddCart(itemData.addCartIp!!)
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dropView()
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
