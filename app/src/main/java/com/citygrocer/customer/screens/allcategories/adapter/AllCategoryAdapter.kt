package com.citygrocer.customer.screens.allcategories.adapter

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.citygrocer.customer.R
import com.citygrocer.customer.api.ApiConstants
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.module.CategorySubCategoryResIP
import com.citygrocer.customer.module.SubcategoryIP
import com.citygrocer.customer.screens.base.adapter.BaseRecAdapter
import com.citygrocer.customer.util.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_all_categories.*

class AllCategoryAdapter(var context: Context,
                         var adapterType: String = "common",
                         var adapterClickListener: IAdapterClickListener? = null, var id: Int?) : RecyclerView.Adapter<AllCategoryAdapter.ViewHolder>() {

    private var list: ArrayList<CategorySubCategoryResIP>? = null
    private var subCategoryList: ArrayList<SubcategoryIP>? = null

    fun addList(list: ArrayList<CategorySubCategoryResIP>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCategoryAdapter.ViewHolder {
        var view = parent.inflate(R.layout.item_all_categories)
        return ViewHolder(view)
    }

    override fun getItemCount() = if (list != null && list!!.size > 0) list!!.size else 0

    override fun onBindViewHolder(viewHolder: AllCategoryAdapter.ViewHolder, pos: Int) {
        if (list!!.size > 0) {
            viewHolder.bind(context, list!![pos], pos)
            return
        }
        viewHolder.bind(context, viewHolder, pos)
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(context: Context, item: Any, pos: Int) {
            if (item is CategorySubCategoryResIP) {
                tv_cat_name.text = item.name
                tv_descript.text = item.description
                Glide.with(context).load(ApiConstants.IMAGE_BASE_URL + item.pic)
                        .apply(RequestOptions.centerCropTransform())
                        .into(iv_allcat)
                subCategoryList = item.sub_cat_data
                if (subCategoryList!!.isEmpty()) {
                    iv_show_more.visibility = View.GONE
                } else {
                    iv_show_more.visibility = View.VISIBLE
                    iv_show_more.setImageResource(R.drawable.ic_down_arrow)
                }
                rv_sub_category.layoutManager = GridLayoutManager(context, 3)
                val adapterSubCategories = BaseRecAdapter(context, R.layout.item_sub_category, adapterType)
                rv_sub_category.adapter = adapterSubCategories
                item.selected = true
                adapterSubCategories.addList(subCategoryList!!)

                if (id == item._id) {
                    rv_sub_category.visibility = View.VISIBLE
                    iv_show_more.setImageResource(R.drawable.ic_up_arrow)
                }
            }
            itemView.setOnClickListener {
                adapterClickListener?.adapterOnclick(itemData = item, pos = adapterPosition, type = itemView, op = Constants.ALLCATEGORY)
            }
        }
    }
}