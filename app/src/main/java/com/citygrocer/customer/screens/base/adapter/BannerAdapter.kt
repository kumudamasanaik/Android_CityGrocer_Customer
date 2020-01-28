package com.citygrocer.customer.screens.base.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.module.BannerIp
import com.citygrocer.customer.module.Pic
import com.citygrocer.customer.screens.product.DetailImageActivity
import com.citygrocer.customer.util.CommonUtils
import com.citygrocer.customer.util.withNotNullNorEmpty
import java.util.*

class BannerAdapter (var context: Context, var type: Int, var clickListener: IAdapterClickListener? = null) : PagerAdapter() {

    var itemList: ArrayList<*>? = null
    lateinit var itemView: View

    override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
    }

    override fun getCount(): Int {
        return if (itemList != null && itemList!!.size > 0) itemList!!.size else 4
    }

    fun addList(itemList: ArrayList<*>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        when (type) {
            R.layout.item_only_image_banner -> {
                itemView = LayoutInflater.from(context).inflate(type, container, false)
                itemList.withNotNullNorEmpty {
                    if (this[position] is BannerIp) {
                        val offer: BannerIp = this[position] as BannerIp
                        if (!offer.pic.isNullOrEmpty())
                            CommonUtils.setImage(context, itemView as ImageView, offer.pic!!)
                    }
                }
            }
            R.layout.item_detail_image_banner -> {
                itemView = LayoutInflater.from(context).inflate(type, container, false)
                val intent = Intent(context, DetailImageActivity::class.java)
                itemList.withNotNullNorEmpty {
                    if (this[position] is Pic) {
                        val offer: Pic = this[position] as Pic
                        if (!offer.pic.isNullOrEmpty())
                            CommonUtils.setImage(context, itemView as ImageView, offer.pic!!)
                    }
                    itemView.setOnClickListener {
                        intent.apply {
                            intent.putExtra(Constants.SOURCE, Constants.SOURCE_SIGN)
                            intent.putExtra(Constants.SOURCE_SIGN, itemList)
                            intent.putExtra(Constants.CATEGORY, position)
                        }
                        context.startActivity(intent)
                    }
                }
            }
        }
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
