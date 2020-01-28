package com.citygrocer.customer.screens.product

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.citygrocer.customer.R
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.screens.base.adapter.BaseRecAdapter

class ProductFragment  : Fragment(), IAdapterClickListener {
    lateinit var serviceRequestView: View
    lateinit var mContext: Context
    private var adapterAllCategories: BaseRecAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        serviceRequestView = inflater.inflate(R.layout.fragment_product, container, false)
        return serviceRequestView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext = this.activity!!
    }
    override fun adapterOnclick(itemData: Any, pos: Int, type: Any, op: String) {

    }
}