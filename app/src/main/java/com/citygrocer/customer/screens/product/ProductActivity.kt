package com.citygrocer.customer.screens.product

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.customviews.ScreenStateView
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.interfaces.IDialogClickListener
import com.citygrocer.customer.module.AddCartRes
import com.citygrocer.customer.module.DataSku
import com.citygrocer.customer.module.ProductCatResIp
import com.citygrocer.customer.module.ProductCategoryRes
import com.citygrocer.customer.module.input.AddCartIp
import com.citygrocer.customer.module.input.ProductImageViewIp
import com.citygrocer.customer.screens.base.BaseActivity
import com.citygrocer.customer.screens.base.adapter.BannerAdapter
import com.citygrocer.customer.screens.base.adapter.BaseRecAdapter
import com.citygrocer.customer.screens.base.adapter.MyPagerAdapter
import com.citygrocer.customer.screens.cart.CartActivity
import com.citygrocer.customer.screens.search.SearchActivity
import com.citygrocer.customer.util.*
import com.citygrocer.customer.util.schedulers.BaseScheduler
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.view_pager_detailbanner_layout.*
import javax.inject.Inject

class ProductActivity : BaseActivity(), IAdapterClickListener,
        View.OnClickListener, ProductImageContract.View {

    private var mActivity: Context? = null
    private var adapterSimilarProducts: BaseRecAdapter? = null
    private var id: Int = 0
    @Inject
    lateinit var presenter: ProductImagePresenter
    private var productImageRes: ProductCategoryRes? = null
    private var modifiedProd: ArrayList<ProductCatResIp>? = null
    private lateinit var bannerApter: BannerAdapter
    var subscription = CompositeDisposable()
    @Inject
    lateinit var baseScheduler: BaseScheduler
    lateinit var product: ArrayList<ProductCatResIp>


    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LayoutInflater.from(baseContext).inflate(R.layout.activity_product, fragmentLayout)
        mActivity = this
        showBack()
        showCart()
        ivShare!!.visibility = View.VISIBLE
        ivToolbarLogo!!.visibility = View.GONE
        intent.extras?.apply {
            id = getInt(Constants.SOURCE)
        }
        cart_badge.visibility = View.VISIBLE
        initScreen()
    }

    override fun initScreen() {

        fb_search.setOnClickListener(this)
        ivCart!!.setOnClickListener(this)
        error_button.setOnClickListener(this)
        empty_button.setOnClickListener(this)
        iv_share.setOnClickListener(this)
        tv_increment.setOnClickListener(this)
        tv_decrement.setOnClickListener(this)
        rl_add_cart.setOnClickListener(this)
        tv_best_price.setDrawableLeft(R.drawable.ic_tag_icon)
        tv_best_mrp.setDrawableRight(R.drawable.ic_right_arrow_blue)
        tv_quantity.setDrawableRight(R.drawable.ic_down_arrow)
        tv_more_offers.setDrawableRight(R.drawable.ic_right_arrow_orange)
        tv_mrp.strikeThr()

        val fragmentList = ArrayList<Fragment>()
        val fragmentTitles = ArrayList<String>()

        fragmentTitles.add("About")
        fragmentTitles.add("Ingredients")
        fragmentTitles.add("Nutritional Facts")
        fragmentTitles.add("Other Product")
        fragmentList.add(ProductFragment())
        fragmentList.add(ProductFragment())
        fragmentList.add(ProductFragment())
        fragmentList.add(ProductFragment())

        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        fragmentAdapter.apply {
            this.fragmentList = fragmentList
            this.fragmentTitles = fragmentTitles
        }
        vp_product_category.adapter = fragmentAdapter
        tab_sub_category.setupWithViewPager(vp_product_category)


        rv_product!!.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
        rv_product!!.setHasFixedSize(true)
        adapterSimilarProducts = BaseRecAdapter(mActivity!!, R.layout.item_dashboard_product, adapterClickListener = this)
        rv_product!!.adapter = adapterSimilarProducts
        var similarProductList = ArrayList<String>()
        similarProductList.add("")
        similarProductList.add("")
        similarProductList.add("")
        similarProductList.add("")
        adapterSimilarProducts!!.addList(similarProductList)
        getProductImageView()

        bannerApter = BannerAdapter(this, R.layout.item_detail_image_banner)
        main_banner_pager.adapter = bannerApter
        main_banner_indicator.setViewPager(main_banner_pager)
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    private fun getProductImageView() {
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_LOADING
        presenter.displayProductImageView(productImageViewIp = ProductImageViewIp(_id = id,
                _id_customer = CommonUtils.getLoginId(),
                session_id = CommonUtils.getSession()))
    }


    override fun showProductImageViewRes(productCategoryRes: ProductCategoryRes) {
        if (productCategoryRes.isSuccess()) {
            multiState_rview.viewState = ScreenStateView.VIEW_STATE_CONTENT
            productCategoryRes.apply {
                productImageRes = productCategoryRes
                setUpData()
                cart_badge.text = productImageRes!!.summary!![0]!!.cart_count.toString()
            }
        } else {
            multiState_rview.viewState = ScreenStateView.VIEW_STATE_EMPTY
        }
    }

    private fun setUpData() {
        productImageRes.apply {
            tv_discount_price.text = this!!.result!![0].selling_price
            tv_mrp.text = result!![0].mrp
            tv_best_mrp.text = result[0].best_price
            tv_text_name.text = result[0].name
            tv_text.text = result[0].description
            product = productImageRes!!.result!!

            if (productImageRes!!.result!![0].pic!!.isNotEmpty()) {
                bannerApter.addList(productImageRes!!.result!![0].pic!!)
            }

            tv_increment.setOnClickListener { clickForModify(it, product) }
            tv_decrement.setOnClickListener { clickForModify(it, product) }
            rl_add_cart.setOnClickListener { clickForModify(it, product) }

            if (productImageRes!!.result!![0].selSku == null)
                productImageRes!!.result!![0].selSku = productImageRes!!.result!![0].sku!![0]
            calcSavings(productImageRes!!.result!![0].selSku)
            if (productImageRes!!.result!![0].sku != null && productImageRes!!.result!![0].sku!!.size == 1) {
                tv_quantity.setOnClickListener {}
                tv_quantity.removeDrawable()
            } else
                tv_quantity.setOnClickListener {
                    CommonUtils.showSkuDialog(mContext!!, productImageRes!!.result!![0], IDialogClickListener { data -> calcSavings(data as DataSku) })
                }
        }
    }

    @SuppressLint("SetTextI18n")
    fun calcSavings(selSku: DataSku?) {
        selSku?.apply {
            tempMyCart = -1
            tv_quantity.text = size
            setCartCount(this)
            val sellingPrice = selling_price
            val mrp = mrp
            if (mrp != null && mrp > 0 && sellingPrice != null) {
                val savings = mrp - sellingPrice
                val savingsPer = (((mrp - sellingPrice) / mrp) * 100)
                assignProdAmt(sellingPrice, mrp, savings, savingsPer)
            }
        }

    }

    private fun setCartCount(sku: DataSku) {
        sku.apply {
            tv_sku_count.text = if (sku.tempMyCart != -1) sku.tempMyCart.toString() else sku.mycart.toString()
            if (tv_sku_count.text.toString().contentEquals("0")) {
                rl_add_cart.visibility = View.VISIBLE
                layout_quantityView.visibility = View.GONE
            } else {
                rl_add_cart.visibility = View.GONE
                layout_quantityView.visibility = View.VISIBLE
            }
        }
    }

    private fun assignProdAmt(sellingPrice: Float, mrp: Float, savings: Float, savingsPer: Float) {
        tv_discount_price.apply {
            text = CommonUtils.getRupeesSymbol(context, "%.0f".format(sellingPrice))
        }
        tv_mrp.apply {
            text = CommonUtils.getRupeesSymbol(context, "%.0f".format(mrp))
            strikeThr()
        }
        if (savingsPer >= 5) {
            tv_save.text = CommonUtils.getPerSymbol(this, "%.0f".format(savingsPer))
            tv_save_price.text = getString(R.string.off)
        } else {
            tv_save.text = getString(R.string.save)
            tv_save_price.text = CommonUtils.getRupeesSymbol(this, "%.0f".format(savings))
        }


    }

    private fun clickForModify(view: View, productCatResIp: ArrayList<ProductCatResIp>) {
        if (productCatResIp[0].selSku == null)
            return
        when (view.id) {
            R.id.tv_increment, R.id.rl_add_cart -> {
                productCatResIp[0].selSku?.apply {
                    tempMyCart = mycart + 1
                    productCatResIp[0].addCartIp = AddCartIp(_id_customer = CommonUtils.getLoginId(),
                            _id_product = id_product,
                            _id_sku = id,
                            _id_warehouse = 0,
                            session_id = CommonUtils.getSession(),
                            quantity = tempMyCart)
                    modifiedProd = productImageRes!!.result!!
                    modifyOp(if (tempMyCart == 1)
                        Constants.OP_ADD_CART else Constants.OP_MODIFY_CART)
                }
            }
            R.id.tv_decrement -> {
                productCatResIp[0].selSku?.apply {
                    tempMyCart = mycart - 1
                    productCatResIp[0].addCartIp = AddCartIp(_id_customer = CommonUtils.getLoginId(),
                            _id_product = id_product,
                            _id_sku = id,
                            _id_warehouse = 0,
                            session_id = CommonUtils.getSession(),
                            quantity = tempMyCart)
                    modifiedProd = productImageRes!!.result!!
                    modifyOp(if (tempMyCart == 0)
                        Constants.OP_REMOVE_FROM_CART else Constants.OP_MODIFY_CART)
                }
            }
        }
    }

    private fun modifyOp(op: String) {
        when (op) {
            Constants.OP_ADD_CART -> {
                CommonUtils.showLoading(mContext!!, getString(R.string.please_wait), true)
                presenter.displayAddCart(modifiedProd!![0].addCartIp!!)
            }
            Constants.OP_MODIFY_CART -> {
                CommonUtils.showLoading(mContext!!, getString(R.string.please_wait), true)
                presenter.displayAddCart(modifiedProd!![0].addCartIp!!)
            }
            Constants.OP_REMOVE_FROM_CART -> {
                CommonUtils.showLoading(mContext!!, getString(R.string.please_wait), true)
                presenter.displayRemoveFromCart(modifiedProd!![0].addCartIp!!)
            }
        }
    }

    private fun shareProd() {
        productImageRes?.apply {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, result!![0].name)
                type = "text/plain"
            }
            startActivity(sendIntent)
        }
    }

    override fun showAddCartRes(addCartRes: AddCartRes) {
        hideProgress()
        if (addCartRes.isSuccess()) {
            toast(addCartRes.message!!)
            modifiedProd!![0].selSku?.apply {
                mycart = tempMyCart
                tempMyCart = -1
                setCartCount(this)
                cart_badge.text = addCartRes.summary.toString()
            }
        } else {
            modifiedProd!![0].selSku?.apply {
                tempMyCart = -1
            }
        }
    }

    override fun showRemoveFromCart(addCartRes: AddCartRes) {
        hideProgress()
        if (addCartRes.isSuccess()) {
            toast(addCartRes.message!!)
            modifiedProd!![0].selSku?.apply {
                mycart = tempMyCart
                tempMyCart = -1
                setCartCount(this)
                cart_badge.text = addCartRes.summary.toString()
            }
        } else {
            modifiedProd!![0].selSku?.apply {
                tempMyCart = -1
            }
        }
    }

    override fun showErrorMsg(throwable: Throwable, apiType: String) {
        toast(throwable.message ?: getString(R.string.error_something_wrong))
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_ERROR
        error_textView.text = getString(R.string.error_something_wrong)
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {
                R.id.fb_search -> {
                    startActivity(Intent(mActivity, SearchActivity::class.java))
                }
                R.id.iv_cart -> {
                    startActivity(Intent(mActivity, CartActivity::class.java))
                }
                R.id.empty_button -> {
                    initScreen()
                }
                R.id.error_button -> {
                    initScreen()
                }
                R.id.iv_share -> {
                    shareProd()
                }
            }
        }
    }

    override fun showProgress(msg: String) {
        CommonUtils.showLoading(mContext!!, getString(R.string.please_wait), true)
    }

    override fun hideProgress() {
        CommonUtils.hideLoading()
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

    override fun adapterOnclick(itemData: Any, pos: Int, type: Any, op: String) {

    }
}
