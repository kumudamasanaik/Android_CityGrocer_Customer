package com.citygrocer.customer.screens.base.viewholders

import android.content.Context
import android.view.View
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.module.CategoryIp
import com.citygrocer.customer.util.CommonUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_dashboard_category.*

class CreativeProductsViewHolder(override val containerView: View, val adapterType: String = "common", var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {

    override fun bind(context: Context, item: Any, pos: Int) {

        if (item is CategoryIp) {
            tv_category_name.text = item.name
            CommonUtils.setImage(context, iv_category, item.pic)
        }
        itemView.setOnClickListener { adapterClickListener?.adapterOnclick(itemData = item, pos = pos, op = Constants.CATEGORY) }
    }
}