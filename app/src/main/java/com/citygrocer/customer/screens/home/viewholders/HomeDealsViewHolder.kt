package com.citygrocer.customer.screens.home.viewholders

import android.content.Context
import android.view.View
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.module.DealIp
import com.citygrocer.customer.module.HomePageResponse
import com.citygrocer.customer.screens.base.viewholders.BaseViewholder
import com.citygrocer.customer.util.CommonUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_home_page_deals.*

class HomeDealsViewHolder(override val containerView: View, val adapterType: String = "common", var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
        if (item is DealIp)
            CommonUtils.setImage(context, iv_deal1, item.pic)
    }
}