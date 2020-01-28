package com.citygrocer.customer.screens.allcategories

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.customviews.ScreenStateView
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.module.CategorySubCategoryRes
import com.citygrocer.customer.module.CategorySubCategoryResIP
import com.citygrocer.customer.screens.allcategories.adapter.AllCategoryAdapter
import com.citygrocer.customer.screens.base.BaseActivity
import com.citygrocer.customer.screens.cart.CartActivity
import com.citygrocer.customer.screens.search.SearchActivity
import com.citygrocer.customer.util.CommonUtils
import com.citygrocer.customer.util.toast
import com.citygrocer.customer.util.withNotNullNorEmpty
import kotlinx.android.synthetic.main.activity_all_categories.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.item_all_categories.view.*
import kotlinx.android.synthetic.main.layout_header.*
import javax.inject.Inject

class AllCategoryActivity : BaseActivity(), IAdapterClickListener, View.OnClickListener, AllCategoryContract.View {

    @Inject
    lateinit var presenter: AllCategoryPresenter
    private var mActivity: Context? = null
    private var adapterAllCategories: AllCategoryAdapter? = null
    private var categoryList: ArrayList<CategorySubCategoryResIP>? = null
    private var id: Int? = null

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LayoutInflater.from(baseContext).inflate(R.layout.activity_all_categories, fragmentLayout)
        mActivity = this
        initScreen()
    }

    override fun initScreen() {
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_LOADING
        showBack()
        setTitle("All Categories")
        showCart()
        error_button.setOnClickListener(this)
        empty_button.setOnClickListener(this)
        ivCart!!.setOnClickListener(this)
        fb_search.setOnClickListener(this)
        intent.extras.apply {
            id = getInt(Constants.ID)
        }
        getCategoryData()
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    private fun getCategoryData() {
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_LOADING
        rv_all_categories.layoutManager = LinearLayoutManager(this)
        adapterAllCategories = AllCategoryAdapter(mContext!!, adapterClickListener = this, id = id)
        rv_all_categories.adapter = adapterAllCategories
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
        when (v!!.id) {
            R.id.empty_button -> {
                initScreen()
            }
            R.id.error_button -> {
                initScreen()
            }
            R.id.iv_cart -> {
                startActivity(Intent(mActivity, CartActivity::class.java))
            }
            R.id.fb_search -> {
                startActivity(Intent(mActivity, SearchActivity::class.java))
            }
        }

    }

    override fun adapterOnclick(itemData: Any, pos: Int, type: Any, op: String) {
        if (itemData is CategorySubCategoryResIP && type is View) {
            when (op) {
                Constants.ALLCATEGORY -> {
                    for (i in categoryList!!) {
                        if (itemData.selected && i._id == itemData._id) {
                            type.rv_sub_category.visibility = View.VISIBLE
                            type.iv_show_more.setImageResource(R.drawable.ic_up_arrow)
                            itemData.selected = false
                        } else if (i._id == itemData._id) {
                            type.rv_sub_category.visibility = View.GONE
                            type.iv_show_more.setImageResource(R.drawable.ic_down_arrow)
                            itemData.selected = true
                        }
                    }
                }

            }
        }
    }

    override fun showCategoryRes(subCategoryRes: CategorySubCategoryRes) {
        if (subCategoryRes.isSuccess()) {
            subCategoryRes.category.withNotNullNorEmpty {
                multiState_rview.viewState = ScreenStateView.VIEW_STATE_CONTENT
                categoryList = filterNotNull() as ArrayList<CategorySubCategoryResIP>
                adapterAllCategories!!.addList(categoryList!!)
                if (CommonUtils.getCartCount() > 0) {
                    cart_badge.visibility = View.VISIBLE
                    cart_badge.text = CommonUtils.getCartCount().toString()
                } else {
                    cart_badge.visibility = View.GONE
                }
                return
            }
        }
    }

    override fun showErrorMsg(throwable: Throwable, apiType: String) {
        toast(throwable.message ?: getString(R.string.error_something_wrong))
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_ERROR
        error_textView.text = getString(R.string.error_something_wrong)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dropView()
    }
}