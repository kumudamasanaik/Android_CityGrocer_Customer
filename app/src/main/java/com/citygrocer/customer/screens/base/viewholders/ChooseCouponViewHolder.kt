package com.citygrocer.customer.screens.base.viewholders

import android.app.Dialog
import android.content.Context
import android.support.v7.app.ActionBar
import android.support.v7.widget.AppCompatButton
import android.view.View
import android.view.Window
import com.citygrocer.customer.R
import com.citygrocer.customer.interfaces.IAdapterClickListener
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_choose_valid_coupon.*

class ChooseCouponViewHolder(override val containerView: View, val adapterType: String = "common", var adapterClickListener: IAdapterClickListener?) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
        lateinit var dialog: Dialog
        view_tc.setOnClickListener {
            dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_terms_conditions)
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
            val window = dialog.getWindow()
            window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
            val close = dialog.findViewById(R.id.btn_got_it) as AppCompatButton
            close.setOnClickListener {
                dialog.dismiss()
            }
        }
    }
}