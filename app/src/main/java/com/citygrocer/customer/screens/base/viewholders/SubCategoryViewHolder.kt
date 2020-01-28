package com.citygrocer.customer.screens.base.viewholders

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.citygrocer.customer.api.ApiConstants
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.module.SubcategoryIP
import com.citygrocer.customer.screens.subcategory.SubCategoryActivity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_sub_category.*

class SubCategoryViewHolder(override val containerView: View,
                            val adapterType: String = "common",
                            var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
        val intent = Intent(context, SubCategoryActivity::class.java)
        if (item is SubcategoryIP) {
            tv_sub_category.text = item.name
            Glide.with(context).load(ApiConstants.IMAGE_BASE_URL + item.pic)
                    .apply(RequestOptions.centerCropTransform())
                    .into(iv_sub_category)

            itemView.setOnClickListener {
                if (item.low_getdata_result!!.isNotEmpty()) {
                    intent.apply {
                        intent.putExtra(Constants.SOURCE, Constants.SOURCE_SIGN)
                        intent.putExtra(Constants.SOURCE_SIGN, item)
                        intent.putExtra(Constants.CATEGORY, pos)
                    }
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "no category found for ${item.name}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}