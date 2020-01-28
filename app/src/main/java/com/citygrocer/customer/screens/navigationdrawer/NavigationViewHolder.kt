package com.citygrocer.customer.screens.navigationdrawer

import android.content.Context
import android.view.View
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.screens.base.viewholders.BaseViewholder
import com.citygrocer.customer.screens.navigationdrawer.model.NavigationDrawerModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_drawer.*

class NavigationViewHolder (override val containerView: View?, val adapterType: String = "common", var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {

    override fun bind(context: Context, item: Any, pos: Int) {
        if (item is NavigationDrawerModel) {
            tv_title.text = item.title
            iv_icon.setImageDrawable(item.icon)

            itemView.setOnClickListener { adapterClickListener?.adapterOnclick(itemData = item, pos = pos) }
        }
    }
}