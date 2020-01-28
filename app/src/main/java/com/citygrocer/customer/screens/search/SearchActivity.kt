package com.citygrocer.customer.screens.search

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.customviews.ScreenStateView
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.module.AddCartRes
import com.citygrocer.customer.module.ProductCatResIp
import com.citygrocer.customer.module.ProductCategoryRes
import com.citygrocer.customer.module.input.SearchIp
import com.citygrocer.customer.screens.allcategories.AllCategoryActivity
import com.citygrocer.customer.screens.barcodescaner.BarcodeActivity
import com.citygrocer.customer.screens.product.ProductActivity
import com.citygrocer.customer.screens.search.adapter.SearchAdapter
import com.citygrocer.customer.util.CommonUtils
import com.citygrocer.customer.util.toast
import com.citygrocer.customer.util.withNotNullNorEmpty
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import java.util.*
import javax.inject.Inject

class SearchActivity : DaggerAppCompatActivity(), View.OnClickListener, IAdapterClickListener, SearchContract.View {

    lateinit var mContext: Context
    var toolbar: Toolbar? = null
    var appbar: AppBarLayout? = null
    var etSearch: AppCompatEditText? = null
    var icScan: AppCompatImageView? = null
    var icMike: AppCompatImageView? = null
    var icClose: AppCompatImageView? = null
    private var adapterSearch: SearchAdapter? = null
    private var categoryList: ArrayList<ProductCatResIp>? = null
    private var modifiedProd: ProductCatResIp? = null
    @Inject
    lateinit var searchPresenter: SearchPresenter


    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        mContext = this
        initScreen()
    }

    override fun initScreen() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        appbar = findViewById<View>(R.id.appbar) as AppBarLayout
        etSearch = findViewById<View>(R.id.et_search) as AppCompatEditText
        icScan = findViewById<View>(R.id.iv_scan) as AppCompatImageView
        icMike = findViewById<View>(R.id.iv_mic) as AppCompatImageView
        icClose = findViewById<View>(R.id.iv_close) as AppCompatImageView

        error_button.setOnClickListener(this)
        empty_button.setOnClickListener(this)

        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        icScan!!.setOnClickListener(this)
        icMike!!.setOnClickListener(this)
        icClose!!.setOnClickListener(this)
        fb_all_category.setOnClickListener(this)

        etSearch!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                if (etSearch!!.text.toString().isNullOrBlank()) {
                    icScan!!.visibility = View.VISIBLE
                    icMike!!.visibility = View.VISIBLE
                    icClose!!.visibility = View.GONE
                } else {
                    icClose!!.visibility = View.VISIBLE
                    icScan!!.visibility = View.GONE
                    icMike!!.visibility = View.GONE
                    tv_show_results.visibility = View.VISIBLE
                    tv_text.text = etSearch!!.text.toString()
                }
                getSearchData()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    override fun onResume() {
        super.onResume()
        searchPresenter.takeView(this)
    }

    private fun getSearchData() {
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_LOADING
        searchPresenter.displaysearchData(searchIp = SearchIp(etSearch!!.text.toString()))
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        adapterSearch = SearchAdapter(mContext, adapterClickListener = this)
        recyclerView.adapter = adapterSearch
    }

    override fun showSearchRes(searchRes: ProductCategoryRes) {
        if (searchRes.isSuccess()) {
            searchRes.result.withNotNullNorEmpty {
                multiState_rview.viewState = ScreenStateView.VIEW_STATE_CONTENT
                categoryList = filterNotNull() as ArrayList<ProductCatResIp>
                adapterSearch!!.addList(categoryList!!)
            }
        } else {
            multiState_rview.viewState = ScreenStateView.VIEW_STATE_EMPTY
        }
    }

    override fun showAddedCartRes(addCartRes: AddCartRes) {
        hideProgress()
        if (addCartRes.isSuccess()) {
            toast(addCartRes.message!!)
            adapterSearch!!.showModifiedRes(Constants.RES_SUCCESS)
        } else {
            adapterSearch!!.showModifiedRes(Constants.RES_FAILED)
        }
    }

    override fun showRemoveFromCart(addCartRes: AddCartRes) {
        hideProgress()
        adapterSearch!!.showModifiedRes(if (addCartRes.isSuccess()) Constants.SUCCESS else Constants.RES_FAILED)
    }

    override fun showErrorMsg(throwable: Throwable, apiType: String) {
        toast(throwable.message ?: getString(R.string.error_something_wrong))
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_ERROR
        error_textView.text = getString(R.string.error_something_wrong)
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

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_scan -> {
                    startActivityForResult(Intent(mContext, BarcodeActivity::class.java), Constants.CODE_BARCODE_ACT)
                }
                R.id.iv_mic -> {
                    promptSpeechInput()
                }
                R.id.iv_close -> {
                    etSearch!!.setText("")
                }
                R.id.empty_button -> {
                    initScreen()
                }
                R.id.error_button -> {
                    initScreen()
                }
                R.id.fb_all_category -> {
                    startActivity(Intent(mContext, AllCategoryActivity::class.java).putExtra(Constants.ID, 0))
                }
            }
        }
    }


    override fun adapterOnclick(itemData: Any, pos: Int, type: Any, op: String) {
        if (itemData is ProductCatResIp && itemData.selSku?.id_product != null) {
            modifiedProd = itemData
            when (op) {
                Constants.OP_ADD_CART -> {
                    CommonUtils.showLoading(mContext, getString(R.string.please_wait), true)
                    searchPresenter.displayAddCart(itemData.addCartIp!!)
                }
                Constants.OP_MODIFY_CART -> {
                    CommonUtils.showLoading(mContext, getString(R.string.please_wait), true)
                    searchPresenter.displayAddCart(itemData.addCartIp!!)
                }
                Constants.OP_REMOVE_FROM_CART -> {
                    CommonUtils.showLoading(mContext, getString(R.string.please_wait), true)
                    searchPresenter.displayAddCart(itemData.addCartIp!!)
                }
            }
        }
    }

    private fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        if (intent.resolveActivity(mContext.packageManager) != null) {
            startActivityForResult(intent, Constants.CODE_VOICE_ACT)
        } else {
            toast("Your Device Don't Support Speech Input")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constants.CODE_VOICE_ACT -> if (resultCode == Activity.RESULT_OK && data != null) {
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                etSearch!!.setText(result[0])
                recyclerView.visibility = View.VISIBLE
            }
            Constants.CODE_BARCODE_ACT -> if (resultCode == Activity.RESULT_OK && data != null) {
                etSearch!!.setText(data?.getStringExtra(Constants.KEY_BARCODE))
                startActivity(Intent(mContext, ProductActivity::class.java))
            }
        }
    }

    override fun showProgress(msg: String) {
        CommonUtils.showLoading(mContext, getString(R.string.please_wait), true)
    }

    override fun hideProgress() {
        CommonUtils.hideLoading()
    }

}
