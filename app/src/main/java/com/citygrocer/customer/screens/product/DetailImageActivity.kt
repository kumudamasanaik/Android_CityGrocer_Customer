package com.citygrocer.customer.screens.product

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.module.Pic
import com.citygrocer.customer.screens.base.adapter.BaseRecAdapter
import com.citygrocer.customer.util.CommonUtils
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_detail_image.*

class DetailImageActivity : DaggerAppCompatActivity(), IAdapterClickListener {

    private var mActivity: Context? = null
    private var source: String = ""
    private var imageList: ArrayList<Pic>? = null
    private var cur_pos: Int = 0
    lateinit var adapterImages: BaseRecAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_image)
        mActivity = this
        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
            if (source.contentEquals(Constants.SOURCE_SIGN)) {
                imageList = intent.getParcelableArrayListExtra(Constants.SOURCE_SIGN)
                cur_pos = intent.getIntExtra(Constants.CATEGORY, 0)
            }
        }
        initialiseScreen()
    }

    private fun initialiseScreen() {
        CommonUtils.setImage(mActivity!!, iv_image, imageList!![cur_pos].pic)

        rv_images.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterImages = BaseRecAdapter(mActivity!!, R.layout.item_detail_images, adapterClickListener = this)
        rv_images.adapter = adapterImages

        if (imageList!!.isNotEmpty()) {
            adapterImages.addList(imageList!!)
        }
        btn_close.setOnClickListener {
            finish()
        }
    }

    override fun adapterOnclick(itemData: Any, pos: Int, type: Any, op: String) {
        when (op) {
            Constants.IMAGE -> {
                if (itemData is Pic) {
                    CommonUtils.setImage(mActivity!!, iv_image, itemData.pic)
                }
            }
        }
    }
}
