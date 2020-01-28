package com.citygrocer.customer.screens.home.viewholders

import android.content.Context
import android.view.View
import com.citygrocer.customer.R
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.screens.base.viewholders.BaseViewholder
import com.citygrocer.customer.util.setDrawableLeft
import com.citygrocer.customer.util.setDrawableRight
import com.citygrocer.customer.util.strikeThr
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_dashboard_product.*

class DashboardProductViewHolder(override val containerView: View,  val adapterType: String = "common",var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {

    override fun bind(context: Context, item: Any, pos: Int) {
        tv_best_price.setDrawableLeft(R.drawable.ic_tag_icon)
        tv_best_mrp.setDrawableRight(R.drawable.ic_right_arrow_blue)
        tv_mrp.strikeThr()
    }
}