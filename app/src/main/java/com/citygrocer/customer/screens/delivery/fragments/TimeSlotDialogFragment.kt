package com.citygrocer.customer.screens.delivery.fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.module.DeliverySlot
import com.citygrocer.customer.screens.base.adapter.MyPagerAdapter
import com.citygrocer.customer.util.withNotNullNorEmpty


class TimeSlotDialogFragment : DialogFragment() {
    lateinit var mContext: FragmentActivity
    private var mView: View? = null
    private var deliverySlot: ArrayList<DeliverySlot?>? = null
    lateinit var mViewPager: ViewPager
    lateinit var tabLayout: TabLayout

    companion object {
        @JvmStatic
        fun newInstance(deliverySlot: ArrayList<DeliverySlot?>): TimeSlotDialogFragment {
            val bundle = Bundle()
            bundle.putParcelableArrayList(Constants.OBJECT, deliverySlot)
            val fragment = TimeSlotDialogFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        arguments?.apply {
            deliverySlot = getParcelableArrayList(Constants.OBJECT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.dialog_time_slot, container, false)

        mViewPager = mView!!.findViewById(R.id.vp_time_slot)
        tabLayout = mView!!.findViewById(R.id.tab_time_slot)

        getViewPagerHeaderData()

        val linearLayout = tabLayout.getChildAt(0) as LinearLayout
        linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        val drawable = GradientDrawable()
        drawable.setColor(Color.GRAY)
        drawable.setSize(1, 1)
        linearLayout.dividerDrawable = drawable
        return mView
    }

    private fun getViewPagerHeaderData() {

        val fragmentAdapter = MyPagerAdapter(childFragmentManager)
        deliverySlot.withNotNullNorEmpty {
            for (loccat in this)
                fragmentAdapter.addList(DeliverySlotFragment.newInstance(loccat!!), loccat.date!!)
        }
        mViewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(mViewPager)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //initScreen()
    }

    private fun initScreen() {

    }

}