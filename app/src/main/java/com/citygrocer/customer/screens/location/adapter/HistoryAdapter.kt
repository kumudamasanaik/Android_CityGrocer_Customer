package com.citygrocer.customer.screens.location.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.citygrocer.customer.R
import com.citygrocer.customer.module.input.HistoryDetails

class HistoryAdapter(historyDetails: List<HistoryDetails>, context: Context) : RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {
    private var historyDetails: List<HistoryDetails>? = null
    private var context: Context
    override fun getItemCount(): Int {
        return historyDetails!!.size
    }

    init {
        this.historyDetails = historyDetails
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_detail, null)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val details1 = historyDetails!![position]
        holder.name.text = details1.name
       // holder.address.text = details1.address
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.tv_address) as TextView
        //var address: TextView = itemView.findViewById(R.id.tv_address_data) as TextView
    }
}