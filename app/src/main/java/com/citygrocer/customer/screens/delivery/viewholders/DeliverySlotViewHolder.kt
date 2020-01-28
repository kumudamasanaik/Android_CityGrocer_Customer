package com.citygrocer.customer.screens.delivery.viewholders

import android.content.Context
import android.view.View
import android.widget.Toast
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.module.DeliverySlot
import com.citygrocer.customer.screens.base.viewholders.BaseViewholder
import com.citygrocer.customer.util.strikeThr
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_delivery_slot.*

class DeliverySlotViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
        if (item is DeliverySlot.Data) {
            if (item.delivery_available == true) {
                tv_time_slot.text = item.start_time + " - " + item.end_time
                tv_slot_availability.visibility = View.GONE
            } else {
                tv_time_slot.text = item.start_time + " - " + item.end_time
                tv_time_slot.strikeThr()
                tv_slot_availability.visibility = View.VISIBLE
            }
            item.selected = true
            itemView.setOnClickListener {
                if (item.delivery_available == true) {
                    adapterClickListener?.adapterOnclick(itemData = item, pos = pos, type = itemView, op = Constants.DELIVERY_SLOT)
                } else {
                    Toast.makeText(context, "Slot Unavailable", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}
