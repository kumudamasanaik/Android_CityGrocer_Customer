package com.citygrocer.customer.screens.subcategory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.module.DataSku
import com.citygrocer.customer.module.LowestSubCategoriesIp
import com.citygrocer.customer.module.SubcategoryIP
import com.citygrocer.customer.screens.base.BaseActivity
import com.citygrocer.customer.screens.base.adapter.MyPagerAdapter
import com.citygrocer.customer.screens.cart.CartActivity
import com.citygrocer.customer.screens.filter.FilterActivity
import com.citygrocer.customer.screens.search.SearchActivity
import com.citygrocer.customer.util.setDrawableLeft
import com.citygrocer.customer.util.withNotNullNorEmpty
import kotlinx.android.synthetic.main.activity_sub_categories.*
import kotlinx.android.synthetic.main.layout_header.*

class SubCategoryActivity : BaseActivity(), View.OnClickListener {

    private var mActivity: Context? = null
    private var source: String = ""
    private var cur_pos: Int = 0
    private var subCategoryList: SubcategoryIP? = null
    lateinit var fragmentAdapter: MyPagerAdapter
    private var lastLevcat: ArrayList<LowestSubCategoriesIp>? = null

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LayoutInflater.from(baseContext).inflate(R.layout.activity_sub_categories, fragmentLayout)
        mActivity = this
        showBack()
        showCart()
        ivToolbarLogo!!.visibility = View.GONE
        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
            if (source.contentEquals(Constants.SOURCE_SIGN)) {
                subCategoryList = intent.getParcelableExtra(Constants.SOURCE_SIGN)
                cur_pos = intent.getIntExtra(Constants.CATEGORY, 0)
            }
        }
        iv_cart.setOnClickListener(this)
        cart_badge.visibility = View.VISIBLE
        initialiseScreen()
    }

    private fun initialiseScreen() {

        tv_sort.setDrawableLeft(R.drawable.ic_sort)
        tv_filter.setDrawableLeft(R.drawable.ic_filter)
        tv_sort.setOnClickListener(this)
        tv_filter.setOnClickListener(this)
        fb_search.setOnClickListener(this)
        setTitle(subCategoryList?.name)
        getViewPagerHeaderData()
    }

    private fun getViewPagerHeaderData() {
        fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        subCategoryList?.low_getdata_result.withNotNullNorEmpty {
            lastLevcat = filterNotNull() as ArrayList<LowestSubCategoriesIp>
            lastLevcat = filter { it.name != null && it._id != null } as ArrayList<LowestSubCategoriesIp>
            lastLevcat.withNotNullNorEmpty {
                for (locCat in this)
                    fragmentAdapter.addList(CategoryFragment.newInstance(locCat), locCat.name!!)

                vp_sub_category.adapter = fragmentAdapter
                tab_sub_category.setupWithViewPager(vp_sub_category)
            }
            return
        }
    }

    override fun onResume() {
        super.onResume()
        initialiseScreen()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fb_search -> {
                startActivity(Intent(mActivity, SearchActivity::class.java))
            }
            R.id.tv_sort -> {
                val sort = FragmentSort()
                sort.show(supportFragmentManager, sort.tag)
                sort.isCancelable = false
            }
            R.id.tv_filter -> {
                startActivity(Intent(mActivity, FilterActivity::class.java))
            }
            R.id.iv_cart -> {
                startActivity(Intent(mActivity, CartActivity::class.java))
            }
        }

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