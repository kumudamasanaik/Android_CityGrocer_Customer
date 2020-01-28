package com.citygrocer.customer.screens.choosedelivery.viewholders

import android.app.Dialog
import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.module.AddAddressResIP
import com.citygrocer.customer.screens.base.viewholders.BaseViewholder
import com.citygrocer.customer.util.CommonUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_choose_delivery_address.*

class ChooseDeliveryViewHolder(override val containerView: View, val adapterType: String = "common", var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
        if (item is AddAddressResIP) {
            tv_name.text = CommonUtils.getName()
            tv_mobile_no.text = CommonUtils.getMobileno()
            tv_items_reserved_next.text = item.getAddressString()


            if (item.default_address == 0) {
                iv_msg.setImageResource(R.drawable.ic_radio_on)
                tv_edit_address.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                rl_address_selection.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAddress))
                CommonUtils.saveDeliveryAddress(item.address!!)
            } else {
                rl_address_selection.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
                iv_msg.setImageResource(R.drawable.ic_radio_off)
            }

            iv_msg.setOnClickListener {
                adapterClickListener?.adapterOnclick(itemData = item, pos = pos,
                        op = Constants.DELIVERY_ADDRESS)
            }

            tv_remove_address.setOnClickListener {
                val pendingTransactionDialog = Dialog(context, R.style.CustomDialogThemeLightBg)
                pendingTransactionDialog.setCanceledOnTouchOutside(true)
                pendingTransactionDialog.setContentView(R.layout.dialog_alert_message)
                (pendingTransactionDialog.findViewById(R.id.dialog_title) as TextView).text = context!!.getString(R.string.remove_address)
                (pendingTransactionDialog.findViewById(R.id.dialog_text) as TextView).text = context!!.getString(R.string.remove_address_message)
                (pendingTransactionDialog.findViewById(R.id.tv_cancel) as TextView).text = context.getString(R.string.cancel).toUpperCase()
                (pendingTransactionDialog.findViewById(R.id.tv_empty_cart) as TextView).text = context.getString(R.string.delete).toUpperCase()
                pendingTransactionDialog.show()

                (pendingTransactionDialog.findViewById(R.id.tv_cancel) as TextView).setOnClickListener {
                    pendingTransactionDialog.dismiss()
                }
                (pendingTransactionDialog.findViewById(R.id.tv_empty_cart) as TextView).setOnClickListener {
                    adapterClickListener?.adapterOnclick(itemData = item, pos = pos,
                            op = Constants.DELETE)
                    pendingTransactionDialog.dismiss()
                }
            }
            tv_edit_address.setOnClickListener {
                adapterClickListener?.adapterOnclick(itemData = item, pos = pos,
                        op = Constants.EDIT)
            }
        }
    }
}