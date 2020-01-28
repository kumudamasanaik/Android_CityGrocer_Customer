package com.citygrocer.customer.screens.delivery.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.module.DeliverySlot
import com.citygrocer.customer.screens.base.adapter.BaseRecAdapter
import com.citygrocer.customer.util.logv
import com.citygrocer.customer.util.withNotNullNorEmpty
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_delivery_slot.*
import kotlinx.android.synthetic.main.item_delivery_slot.view.*

class DeliverySlotFragment : DaggerFragment(),
        IAdapterClickListener {

    lateinit var adapterDeliverySlot: BaseRecAdapter
    lateinit var mContext: Context
    lateinit var mActivity: FragmentActivity
    private var dataList: DeliverySlot? = null
    private var deliveryDataList: ArrayList<DeliverySlot.Data?>? = null

    companion object {
        @JvmStatic
        fun newInstance(deliveryData: DeliverySlot): DeliverySlotFragment {
            val bundle = Bundle()
            bundle.putParcelable(Constants.OBJECT, deliveryData)
            val fragment = DeliverySlotFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_delivery_slot, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = requireActivity()
        mContext = requireContext()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            dataList = getParcelable(Constants.OBJECT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initScreen()
    }

    private fun initScreen() {

        adapterDeliverySlot = BaseRecAdapter(mContext, R.layout.item_delivery_slot, adapterClickListener = this)
        val myDivider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        myDivider.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.divider)!!)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = adapterDeliverySlot
            addItemDecoration(myDivider)
        }
        dataList!!.data.withNotNullNorEmpty {
            deliveryDataList = dataList!!.data!!
            deliveryDataList.withNotNullNorEmpty {
                adapterDeliverySlot.addList(deliveryDataList!!)
            }
        }
    }

    override fun adapterOnclick(itemData: Any, pos: Int, type: Any, op: String) {
        if (itemData is DeliverySlot.Data && type is View) {
            when (op) {
                Constants.DELIVERY_SLOT -> {
                    for (i in deliveryDataList!!) {
                        if (itemData.selected && i!!._id == itemData._id) {
                            type.ll_address_selection.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorAddress))
                            type.iv_msg.setImageResource(R.drawable.ic_radio_on)
                            val selectedId = itemData._id
                            itemData.selected = false
                        } else if (i!!._id == itemData._id) {
                            type.ll_address_selection.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorWhite))
                            type.iv_msg.setImageResource(R.drawable.ic_radio_off)
                            itemData.selected = true
                        }
                    }
                }
            }
        }
    }
}