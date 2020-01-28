package com.citygrocer.customer.screens.base.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.citygrocer.customer.R
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.screens.base.viewholders.*
import com.citygrocer.customer.screens.choosedelivery.viewholders.ChooseDeliveryViewHolder
import com.citygrocer.customer.screens.delivery.viewholders.DeliverySlotViewHolder
import com.citygrocer.customer.screens.home.viewholders.DashboardProductViewHolder
import com.citygrocer.customer.screens.home.viewholders.HomeDealsViewHolder
import com.citygrocer.customer.screens.home.viewholders.HomeProductViewHolder
import com.citygrocer.customer.screens.navigationdrawer.NavigationViewHolder
import com.citygrocer.customer.screens.product.viewholders.DetailImageViewHolder
import com.citygrocer.customer.util.inflate
import com.citygrocer.customer.util.withNotNullNorEmpty


class BaseRecAdapter(context: Context, type: Int, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : RecyclerView.Adapter<BaseViewholder>() {
    var context = context
    var type = type

    var list: ArrayList<*>? = null


    fun addList(list: ArrayList<*>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewholder {
        var view = parent.inflate(type)
        lateinit var holder: BaseViewholder
        when (type) {
            R.layout.item_creative_products -> holder = HomeProductViewHolder(view, adapterType, adapterClickListener)
            R.layout.item_sub_category -> holder = SubCategoryViewHolder(view, adapterType, adapterClickListener)
            R.layout.item_dashboard_product -> holder = DashboardProductViewHolder(view, adapterType, adapterClickListener)
            R.layout.item_dashboard_category -> holder = CreativeProductsViewHolder(view, adapterType, adapterClickListener)
            R.layout.item_filter -> holder = CreativeProductsViewHolder(view, adapterType, adapterClickListener)
            //R.layout.item_cart_grocery_staples -> holder = CartItemsViewHolder(view, adapterType, adapterClickListener)
            R.layout.item_checkout_out_of_stock -> holder = ItemCheckViewHolder(view, adapterType, adapterClickListener)
            R.layout.item_choose_delivery_address -> holder = ChooseDeliveryViewHolder(view, adapterType, adapterClickListener)
            R.layout.item_drawer -> holder = NavigationViewHolder(view, adapterType, adapterClickListener!!)
            R.layout.item_choose_valid_coupon -> holder = ChooseCouponViewHolder(view, adapterType, adapterClickListener!!)
            R.layout.item_not_valid_coupons -> holder = ChooseCouponViewHolder(view, adapterType, adapterClickListener!!)
            R.layout.item_home_page_deals -> holder = HomeDealsViewHolder(view, adapterType, adapterClickListener!!)
            R.layout.item_delivery_slot -> holder = DeliverySlotViewHolder(view, adapterClickListener!!)
            R.layout.item_detail_images -> holder = DetailImageViewHolder(view, adapterClickListener!!)

            else -> {
                holder = CreativeProductsViewHolder(view, adapterType, adapterClickListener)
            }
        }
        return holder
    }

    override fun getItemCount(): Int {
        if (list != null) {
            return list!!.size
        }
        return 6
    }

    override fun onBindViewHolder(holder: BaseViewholder, position: Int) {

        list.withNotNullNorEmpty {
            holder.bind(context, list!![position], position)
            return
        }

        holder.bind(context, holder, position)
    }

}