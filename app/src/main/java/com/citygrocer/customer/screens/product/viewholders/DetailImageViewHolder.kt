package com.citygrocer.customer.screens.product.viewholders

import android.content.Context
import android.view.View
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.module.Pic
import com.citygrocer.customer.screens.base.viewholders.BaseViewholder
import com.citygrocer.customer.util.CommonUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_detail_images.*

class DetailImageViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {

    override fun bind(context: Context, item: Any, pos: Int) {

        if (item is Pic) {
            CommonUtils.setImage(context, iv_category, item.pic)
        }
        itemView.setOnClickListener { adapterClickListener?.adapterOnclick(itemData = item, pos = pos, op = Constants.IMAGE) }
    }
}