package com.citygrocer.customer.screens.filter

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.citygrocer.customer.R
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.screens.base.adapter.BaseRecAdapter
import com.citygrocer.customer.util.font
import com.citygrocer.customer.util.setDrawableLeft
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_filter.*

class FilterActivity : DaggerAppCompatActivity(), View.OnClickListener, IAdapterClickListener {

    lateinit var mContext: Context
    private var adapterAllCategories: BaseRecAdapter? = null

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        mContext = this
        initialiseScreen()
    }

    private fun initialiseScreen() {
        et_search.setDrawableLeft(R.drawable.ic_search)

        recyclerView!!.layoutManager = LinearLayoutManager(mContext)
        recyclerView!!.setHasFixedSize(true)
        adapterAllCategories = BaseRecAdapter(mContext!!, R.layout.item_filter, adapterClickListener = this)
        recyclerView.adapter = adapterAllCategories
        var categoryList = ArrayList<String>()
        categoryList.add("")
        categoryList.add("")
        categoryList.add("")
        categoryList.add("")
        categoryList.add("")
        categoryList.add("")
        categoryList.add("")
        categoryList.add("")
        adapterAllCategories!!.addList(categoryList)

        tv_category.setOnClickListener(this)
        tv_size.setOnClickListener(this)
        tv_discount.setOnClickListener(this)
        tv_price.setOnClickListener(this)
        tv_food_preference.setOnClickListener(this)
        btn_close.setOnClickListener(this)
        updateSelection(tv_category)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_category -> {
                updateSelection(tv_category)
            }
            R.id.tv_size -> {
                updateSelection(tv_size)
            }
            R.id.tv_discount -> {
                updateSelection(tv_discount)
            }
            R.id.tv_price -> {
                updateSelection(tv_price)
            }
            R.id.tv_food_preference -> {
                updateSelection(tv_food_preference)
            }
            R.id.btn_close -> {
                finish()
            }
        }

    }

    private fun updateSelection(selectedFilter: AppCompatTextView?) {
        tv_category.apply {
            tv_category.font(R.font.sourcesanspro_regular)
            tv_category.setBackgroundColor(resources.getColor(R.color.colorLocationText))
        }
        tv_size.apply {
            tv_size.font(R.font.sourcesanspro_regular)
            tv_size.setBackgroundColor(resources.getColor(R.color.colorLocationText))
        }
        tv_discount.apply {
            tv_discount.font(R.font.sourcesanspro_regular)
            tv_discount.setBackgroundColor(resources.getColor(R.color.colorLocationText))
        }
        tv_price.apply {
            tv_price.font(R.font.sourcesanspro_regular)
            tv_price.setBackgroundColor(resources.getColor(R.color.colorLocationText))
        }
        tv_food_preference.apply {
            tv_food_preference.font(R.font.sourcesanspro_regular)
            tv_food_preference.setBackgroundColor(resources.getColor(R.color.colorLocationText))
        }
        selectedFilter!!.apply {
            font(R.font.sourcesanspro_semibold)
            setBackgroundColor(resources.getColor(R.color.bg))
        }

    }

    override fun adapterOnclick(itemData: Any, pos: Int, type: Any, op: String) {

    }
}
