package com.citygrocer.customer.screens.base.viewholders

import android.content.Context
import android.view.View
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.interfaces.IAdapterClickListener
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_checkout_out_of_stock.*

class ItemCheckViewHolder(override val containerView: View, val adapterType: String = "common", var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {

        tv_find_similar.setOnClickListener { adapterClickListener?.adapterOnclick(itemData = item, pos = pos, op = Constants.FIND_SIMILAR) }

    }
}