package com.citygrocer.customer.screens.home

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SlidingPaneLayout
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.*
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.customviews.ScreenStateView
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.module.CategoryIp
import com.citygrocer.customer.module.DealIp
import com.citygrocer.customer.module.HomePageResponse
import com.citygrocer.customer.module.ProductIp
import com.citygrocer.customer.screens.allcategories.AllCategoryActivity
import com.citygrocer.customer.screens.base.BaseActivity
import com.citygrocer.customer.screens.base.adapter.BannerAdapter
import com.citygrocer.customer.screens.base.adapter.BaseRecAdapter
import com.citygrocer.customer.screens.bottomsheet.BottomSheetFragment
import com.citygrocer.customer.screens.cart.CartActivity
import com.citygrocer.customer.screens.landing.LandingActivity
import com.citygrocer.customer.screens.location.LocationSearchHistoryActivity
import com.citygrocer.customer.screens.search.SearchActivity
import com.citygrocer.customer.util.CommonUtils
import com.citygrocer.customer.util.Validation
import com.citygrocer.customer.util.schedulers.BaseScheduler
import com.citygrocer.customer.util.setDrawableLeft
import com.citygrocer.customer.util.toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.home_layout.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.view_pager_banner_layout.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeActivity : BaseActivity(), View.OnClickListener, IAdapterClickListener, HomeContract.View {

    @Inject
    lateinit var presenter: HomePresenter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mActivity: Context? = null
    private var popupWindow: PopupWindow? = null
    private var adapterHomeProducts: BaseRecAdapter? = null
    private var adapterHeaderProducts: BaseRecAdapter? = null
    private var adapterHomeDeals: BaseRecAdapter? = null
    private var adapterCategory: BaseRecAdapter? = null
    private var categoryList: ArrayList<CategoryIp>? = null
    private var productList: ArrayList<ProductIp>? = null
    private var dealsList: ArrayList<DealIp>? = null
    private var doubleBackToExitPressedOnce = false
    lateinit var mainBannerAdapter: BannerAdapter
    private var address: Address? = null
    var bottomSheetFragment: BottomSheetFragment? = null
    var subscription = CompositeDisposable()
    @Inject
    lateinit var baseScheduler: BaseScheduler

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LayoutInflater.from(baseContext).inflate(R.layout.activity_home, fragmentLayout)
        if (isFirstTime()) {
            showLocationChangePopUp()
        }
        initScreen()
    }

    private fun isFirstTime(): Boolean {
        val preferences = getPreferences(MODE_PRIVATE)
        val ranBefore = preferences.getBoolean("RanBefore", false)
        if (!ranBefore) {
            // first time
            val editor = preferences.edit()
            editor.putBoolean("RanBefore", true)
            editor.commit()
        }
        return !ranBefore
    }

    override fun initScreen() {
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_LOADING
        mActivity = this
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        showMenu()
        ivShare!!.visibility = View.GONE
        tv_deals_time.setDrawableLeft(R.drawable.ic_timer)

        rlLocationDetails!!.visibility = View.VISIBLE

        rl_location_details!!.setOnClickListener(this)
        ivSearch!!.setOnClickListener(this)
        ivCart!!.setOnClickListener(this)
        ivMore!!.setOnClickListener(this)
        error_button.setOnClickListener(this)
        empty_button.setOnClickListener(this)

        if (intent != null && intent.getBooleanExtra("isRegistered", false)) {
            showWelcomeDialog()
        } else if (intent != null && intent.getBooleanExtra("isLogin", false)) {

        } else if (CommonUtils.getLoginId() != 0) {

        } else {
            showBottomSheetDialog()
        }
        getDashboardData()
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)

        if (Validation.isValidObject(CommonUtils.getMyAddress())) {
            address = CommonUtils.getMyAddress()
            tvLocation!!.text = address!!.getAddressLine(0)
        } else
            tvLocation!!.text = "BTM Layout,Bangalore,560036"

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.rl_location_details -> {
                    val intent = Intent(this, LocationSearchHistoryActivity::class.java)
                    startActivity(intent)
                }
                R.id.iv_search -> {
                    startActivity(Intent(mActivity, SearchActivity::class.java))
                }
                R.id.iv_cart -> {
                    startActivity(Intent(mActivity, CartActivity::class.java))
                }

                R.id.iv_more -> {
                    showMenuPopUp()
                }
                R.id.fb_all_category -> {
                    startActivity(Intent(mActivity, AllCategoryActivity::class.java).putExtra(Constants.ID, 0))
                }
                R.id.error_button -> {
                    getDashboardData()
                }
                R.id.empty_button -> {
                    getDashboardData()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        bottomSheetFragment?.onActivityResult(requestCode, resultCode, data)
    }

    override fun adapterOnclick(itemData: Any, pos: Int, type: Any, op: String) {
        when (op) {
            Constants.CATEGORY -> {
                if (itemData is CategoryIp) {
                    startActivity(Intent(this, AllCategoryActivity::class.java).putExtra(Constants.ID, itemData._id))
                }
            }
        }
    }

    private fun getDashboardData() {
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_LOADING
        mainBannerAdapter = BannerAdapter(this, R.layout.item_only_image_banner)
        main_banner_pager.adapter = mainBannerAdapter
        tab_layout.setupWithViewPager(main_banner_pager)

        rv_category.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterCategory = BaseRecAdapter(mActivity!!, R.layout.item_dashboard_category, adapterClickListener = this)
        rv_category.adapter = adapterCategory

        rv_creative_products.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterHomeProducts = BaseRecAdapter(mActivity!!, R.layout.item_creative_products, adapterClickListener = this)
        rv_creative_products.adapter = adapterHomeProducts
        rv_creative_products.isNestedScrollingEnabled = false

        /* rv_header_products.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
         adapterHeaderProducts = BaseRecAdapter(mActivity!!, R.layout.item_dashboard_product, adapterClickListener = this)
         rv_header_products.adapter = adapterHeaderProducts
         var headerProductsList = ArrayList<String>()
         headerProductsList.add("")
         headerProductsList.add("")
         headerProductsList.add("")
         headerProductsList.add("")
         adapterHeaderProducts!!.addList(headerProductsList)*/


        rv_deals.layoutManager = GridLayoutManager(this, 2)
        rv_deals.isNestedScrollingEnabled = false
        adapterHomeDeals = BaseRecAdapter(mActivity!!, R.layout.item_home_page_deals, adapterClickListener = this)
        rv_deals.adapter = adapterHomeDeals

        sliding_panel.setPanelSlideListener(SliderListener())

        sliding_panel.openPane()
        fb_all_category.setOnClickListener(this)
    }

    override fun showHomePageRes(homeRes: HomePageResponse) {
        if (homeRes.result != null) {
            multiState_rview.viewState = ScreenStateView.VIEW_STATE_CONTENT
            if (homeRes.result.category!!.isNotEmpty()) {
                categoryList = homeRes.result.category!!
                adapterCategory!!.addList(categoryList!!)
            }
            if (homeRes.result.banner!!.isNotEmpty()) {
                mainBannerAdapter.addList(homeRes.result.banner!!)
                startAutoScroll()
            }
            if (homeRes.result.products!!.isNotEmpty()) {
                productList = homeRes.result.products
                adapterHomeProducts!!.addList(productList!!)
            }
            if (homeRes.result.deals!!.isNotEmpty()) {
                dealsList = homeRes.result.deals!!
                adapterHomeDeals!!.addList(dealsList!!)
            }
            if (homeRes.result.myshopbanner != null) {
                CommonUtils.setImage(mContext!!, iv_shop_banner, homeRes.result.myshopbanner!!.pic)
            }
            if (homeRes.result.cgbanner != null) {
                CommonUtils.setImage(mContext!!, iv_creative_banner, homeRes.result.cgbanner!!.pic)
            } else {
                iv_creative_banner.visibility = View.GONE
            }
            if (homeRes.result.offerzone!!.isNotEmpty()) {
                CommonUtils.setImage(mContext!!, iv_offer1, homeRes.result.offerzone!![0].pic)
                CommonUtils.setImage(mContext!!, iv_offer2, homeRes.result.offerzone!![1].pic)
                CommonUtils.setImage(mContext!!, iv_offer3, homeRes.result.offerzone!![2].pic)
                CommonUtils.setImage(mContext!!, iv_offer4, homeRes.result.offerzone!![3].pic)
            } else {
                iv_offer1.visibility = View.GONE
                iv_offer2.visibility = View.GONE
                iv_offer3.visibility = View.GONE
                iv_offer4.visibility = View.GONE
            }
            if (homeRes.result.bannerad != null) {
                CommonUtils.setImage(mContext!!, iv_banner, homeRes.result.bannerad!!.pic)
            } else {
                iv_banner.visibility = View.GONE
            }
            if (homeRes.result.bannershare != null) {
                CommonUtils.setImage(mContext!!, iv_refer_earn, homeRes.result.bannershare!!.pic)
            } else {
                iv_refer_earn.visibility = View.GONE
            }
            if (CommonUtils.getCartCount() > 0) {
                cart_badge.visibility = View.VISIBLE
                cart_badge.text = CommonUtils.getCartCount().toString()
            } else {
                cart_badge.visibility = View.GONE
            }
        }
    }


    private fun showBottomSheetDialog() {
        bottomSheetFragment = BottomSheetFragment()
        bottomSheetFragment?.setDissmissListener(object : BottomSheetFragment.OnDismissListener {
            override fun onDismiss(myDialogFragment: BottomSheetFragment) {
            }
        })
        bottomSheetFragment?.show(supportFragmentManager, bottomSheetFragment!!.tag)
        bottomSheetFragment?.isCancelable = true
    }

    private fun showWelcomeDialog() {
        val pendingTransactionDialog = Dialog(mContext!!, R.style.CustomDialogThemeLightBg)
        pendingTransactionDialog.setCanceledOnTouchOutside(true)
        pendingTransactionDialog.setContentView(R.layout.dialog_ok_cancel)
        (pendingTransactionDialog.findViewById(R.id.dialog_title) as TextView).text = mContext!!.getString(R.string.welcome_)
        (pendingTransactionDialog.findViewById(R.id.dialog_text) as TextView).text = mContext!!.getString(R.string.registered_success)
        (pendingTransactionDialog.findViewById(R.id.dialog_action) as TextView).text = mContext!!.getString(R.string.continue_verification).toUpperCase()
        (pendingTransactionDialog.findViewById(R.id.dialog_action) as TextView).setOnClickListener {
            pendingTransactionDialog.dismiss()
        }
        pendingTransactionDialog.show()
    }

    private fun showLocationChangePopUp() {
        var layoutInflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var customView = layoutInflater.inflate(R.layout.pop_up_location_change, null)
        //instantiate popup window
        popupWindow = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        //display the popup window
        rl_location_details?.post {
            popupWindow?.showAtLocation(ivEditLocation, (Gravity.TOP and Gravity.END), locateView(iv_edit_location).left, locateView(iv_edit_location).bottom)
        }
        var close: Button = customView.findViewById(R.id.btn_got_it)
        var parent: LinearLayout = customView.findViewById(R.id.ll_pop_up_location_parent)
        parent.setBackgroundResource(R.drawable.bg_window_blue)
        close.setOnClickListener {
            popupWindow!!.dismiss()
        }
    }

    private fun showMenuPopUp() {
        var layoutInflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var customView = layoutInflater.inflate(R.layout.pop_up_menu, null)
        //instantiate popup window
        popupWindow = PopupWindow(customView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        popupWindow!!.isOutsideTouchable = true
        popupWindow!!.isFocusable = true
        popupWindow!!.setBackgroundDrawable(BitmapDrawable())
        popupWindow?.showAtLocation(ivMore, (Gravity.TOP or Gravity.START), locateView(iv_more).left, locateView(iv_more).bottom)
        var tvLoginRegister: TextView = customView.findViewById(R.id.tv_login_register)
        var tvOrders: TextView = customView.findViewById(R.id.tv_orders)
        var tvMyShops: TextView = customView.findViewById(R.id.tv_my_shop)
        var tvContactUs: TextView = customView.findViewById(R.id.tv_contact_us)

        if (CommonUtils.getLoginId() != 0) {
            tvLoginRegister.text = CommonUtils.getName()
        } else {
            //display the popup window
            tvLoginRegister.setOnClickListener {
                startActivity(Intent(mActivity, LandingActivity::class.java))
                popupWindow!!.dismiss()
            }
        }
        tvLoginRegister.setDrawableLeft(R.drawable.ic_login_user)
        tvOrders.setDrawableLeft(R.drawable.ic_orders)
        tvMyShops.setDrawableLeft(R.drawable.ic_my_shop)
        tvContactUs.setDrawableLeft(R.drawable.ic_contact_us)

        tvOrders.setOnClickListener {
            popupWindow!!.dismiss()
        }
        tvMyShops.setOnClickListener {
            popupWindow!!.dismiss()
        }
        tvContactUs.setOnClickListener {
            popupWindow!!.dismiss()
        }
    }

    private fun locateView(v: View): Rect {
        val loc_int = IntArray(2)
        if (v == null) return null!!
        try {
            v.getLocationOnScreen(loc_int)
        } catch (npe: NullPointerException) {
            //Happens when the view doesn't exist on screen anymore.
            return null!!
        }
        val location = Rect()
        location.left = loc_int[0]
        location.top = loc_int[1]
        location.right = location.left + v.getWidth()
        location.bottom = location.top + v.getHeight()
        return location
    }

    private fun startAutoScroll() {
        subscription.clear()
        subscription.add(Observable.interval(0, 4, TimeUnit.SECONDS)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .subscribe {
                    main_banner_pager.apply {
                        if (currentItem < mainBannerAdapter.count - 1)
                            currentItem += 1
                        else
                            currentItem = 0
                    }
                })
    }

    override fun showErrorMsg(throwable: Throwable, apiType: String) {
        toast(throwable.message ?: getString(R.string.error_something_wrong))
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_ERROR
        error_textView.text = getString(R.string.error_something_wrong)
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.back_exit_confirmation), Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dropView()
    }

    inner class SliderListener : SlidingPaneLayout.SimplePanelSlideListener() {

        override fun onPanelOpened(panel: View) {
            super.onPanelOpened(panel)
            //loge("onPanelOpened")
            custom_scroll_view.postDelayed({ custom_scroll_view.isEnableScrolling = true }, 100)
        }

        override fun onPanelSlide(panel: View, slideOffset: Float) {
            super.onPanelSlide(panel, slideOffset)
            //loge("onPanelSlide")
            custom_scroll_view.isEnableScrolling = false
        }

        override fun onPanelClosed(panel: View) {
            super.onPanelClosed(panel)
            //loge("onPanelClosed")
            custom_scroll_view.postDelayed({ custom_scroll_view.isEnableScrolling = true }, 100)
        }
    }
}

