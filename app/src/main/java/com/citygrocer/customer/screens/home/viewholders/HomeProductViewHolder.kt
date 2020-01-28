package com.citygrocer.customer.screens.home.viewholders

import android.content.Context
import android.view.View
import com.citygrocer.customer.R
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.module.ProductIp
import com.citygrocer.customer.screens.base.viewholders.BaseViewholder
import com.citygrocer.customer.util.CommonUtils
import com.citygrocer.customer.util.setDrawableLeft
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_creative_products.*

class HomeProductViewHolder(override val containerView: View, val adapterType: String = "common", var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
        tv_mprice.setDrawableLeft(R.drawable.ic_m_price)
        if (item is ProductIp) {
            tv_m_price.text = CommonUtils.getRupeesSymbol(context, item.selling_price!!)
            tv_cg_price.text = CommonUtils.getRupeesSymbol(context, item.best_price!!)
            tv_mrp.text = CommonUtils.getRupeesSymbol(context, item.mrp!!)
            CommonUtils.setImage(context, iv_product, item.pic!![0].pic)
        }
    }
}