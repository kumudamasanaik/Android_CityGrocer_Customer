package com.citygrocer.customer.screens.base.viewholders

import android.content.Context
import android.view.View
import com.citygrocer.customer.R
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.util.setDrawableLeft
import com.citygrocer.customer.util.setDrawableRight
import com.citygrocer.customer.util.strikeThr
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_cart_grocery_staples.*

class CartItemsViewHolder(override val containerView: View, val adapterType: String = "common", var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {

    override fun bind(context: Context, item: Any, pos: Int) {
        tv_mrp.strikeThr()
        tv_discount_price_staples.setDrawableLeft(R.drawable.ic_m_price)
        drop_down.setDrawableRight(R.drawable.ic_down_arrow)


    }
}