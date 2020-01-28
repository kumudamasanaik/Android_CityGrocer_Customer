package com.citygrocer.customer.screens.search.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.module.DataSku
import com.citygrocer.customer.module.ProductCatResIp
import com.citygrocer.customer.module.input.AddCartIp
import com.citygrocer.customer.screens.product.ProductActivity
import com.citygrocer.customer.util.CommonUtils
import com.citygrocer.customer.util.inflate
import com.citygrocer.customer.util.strikeThr
import com.citygrocer.customer.util.withNotNullNorEmpty
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_search.*
import java.util.*

class SearchAdapter(var context: Context,
                    var adapterType: String = "common",
                    var adapterClickListener: IAdapterClickListener? = null) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    var list: ArrayList<ProductCatResIp>? = null
    var modifProd: ProductCatResIp? = null
    private var mLastClickTime: Long = 0

    fun addList(list: ArrayList<ProductCatResIp>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): SearchAdapter.ViewHolder {
        val view = parent.inflate(R.layout.item_search)
        return ViewHolder(view)
    }

    override fun getItemCount() = if (list != null && list!!.size > 0) list!!.size else 0

    override fun onBindViewHolder(viewHolder: SearchAdapter.ViewHolder, pos: Int) {
        if (list!!.size > 0) {
            viewHolder.bind(context, list!![pos], pos)
            return
        }
        viewHolder.bind(context, viewHolder, pos)
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        @SuppressLint("StringFormatMatches")
        fun bind(context: Context, item: Any, pos: Int) {
            tv_mrp.strikeThr()
            if (item is ProductCatResIp) {
                CommonUtils.setImage(context, iv_product_img, item.pic!![0]!!.pic)
                tv_brand_name.text = item.brand
                tv_name.text = item.name
                tv_selling_price.text = item.selling_price
                tv_mrp.text = item.mrp

                if (item.selSku == null && item.sku!!.isNotEmpty())
                    item.selSku = item.sku[0]
                calcSavings(item.selSku)

                tv_increment.setOnClickListener { clickForModify(it, item) }
                tv_decrement.setOnClickListener { clickForModify(it, item) }
                btn_add_to_cart.setOnClickListener { clickForModify(it, item) }

                val intent = Intent(context, ProductActivity::class.java)
                iv_product_img.setOnClickListener {
                    intent.apply {
                        intent.putExtra(Constants.SOURCE, item.id)
                    }
                    context.startActivity(intent)
                }
            }
        }

        private fun calcSavings(selSku: DataSku?) {
            selSku?.apply {
                pic.withNotNullNorEmpty {
                    CommonUtils.setImage(context, iv_product_img, this[0]!!.pic)
                }
                tempMyCart = -1
                tv_sku.text = size
                setCartCount(this@ViewHolder, this)
                val sellingPrice = selling_price
                val mrp = mrp
                if (mrp != null && mrp > 0 && sellingPrice != null) {
                    val savingsPer = (((mrp - sellingPrice) / mrp) * 100)
                    val savings = mrp - sellingPrice
                }
            }
        }

        private fun clickForModify(view: View?, item: ProductCatResIp) {
            if (item.selSku == null)
                return
            when (view!!.id) {
                R.id.tv_increment, R.id.btn_add_to_cart -> {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return
                    }
                    mLastClickTime = SystemClock.elapsedRealtime()
                    item.selSku?.apply {
                        tempMyCart = mycart + 1
                        item.addCartIp = AddCartIp(_id_customer = CommonUtils.getLoginId(),
                                _id_product = id_product,
                                _id_sku = id,
                                _id_warehouse = 0,
                                session_id = CommonUtils.getSession(),
                                quantity = tempMyCart)
                        modifProd = item
                        item.prodPos = adapterPosition
                        adapterClickListener?.adapterOnclick(itemData = item, pos = adapterPosition,
                                op = Constants.OP_ADD_CART, type = adapterType)
                    }
                }
                R.id.tv_decrement -> {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return
                    }
                    mLastClickTime = SystemClock.elapsedRealtime()
                    item.selSku?.apply {
                        tempMyCart = mycart - 1
                        item.addCartIp = AddCartIp(_id_product = id_product,
                                _id_customer = CommonUtils.getLoginId(),
                                _id_sku = id,
                                session_id = CommonUtils.getSession(),
                                quantity = tempMyCart,
                                _id_warehouse = 0
                        )
                        modifProd = item
                        item.prodPos = adapterPosition
                        adapterClickListener?.adapterOnclick(itemData = item, pos = adapterPosition,
                                op = if (tempMyCart == 0)
                                    Constants.OP_REMOVE_FROM_CART else Constants.OP_MODIFY_CART, type = adapterType)
                    }
                }
            }
        }

        private fun setCartCount(viewHolder: ViewHolder, dataSku: DataSku) {
            viewHolder.apply {
                tv_sku_count.text = if (dataSku.tempMyCart != -1) dataSku.tempMyCart.toString() else dataSku.mycart.toString()
                if (tv_sku_count.text.toString().contentEquals("0")) {
                    btn_add_to_cart.visibility = View.VISIBLE
                    btn_inc_dec.visibility = View.GONE
                } else {
                    btn_add_to_cart.visibility = View.GONE
                    btn_inc_dec.visibility = View.VISIBLE
                }
            }
        }

    }

    fun showModifiedRes(type: String) {
        modifProd?.apply {
            when (type) {
                Constants.RES_SUCCESS -> {
                    selSku!!.mycart = selSku!!.tempMyCart
                    selSku!!.tempMyCart = -1
                    notifyItemChanged(prodPos)
                }
                Constants.RES_FAILED -> {
                    selSku!!.tempMyCart = -1
                }
            }
        }
    }
}