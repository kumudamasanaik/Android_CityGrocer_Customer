package com.citygrocer.customer.screens.subcategory.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.interfaces.IDialogClickListener
import com.citygrocer.customer.module.DataSku
import com.citygrocer.customer.module.ProductCatResIp
import com.citygrocer.customer.module.input.AddCartIp
import com.citygrocer.customer.screens.product.ProductActivity
import com.citygrocer.customer.util.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_product.*
import java.util.*

class ProductsAdapter(var context: Context,
                      var adapterType: String = "common",
                      var adapterClickListener: IAdapterClickListener? = null) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    var list: ArrayList<ProductCatResIp>? = null
    var modifProd: ProductCatResIp? = null
    private var mLastClickTime: Long = 0

    fun addList(list: ArrayList<ProductCatResIp>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        var view = parent.inflate(R.layout.item_product)
        return ViewHolder(view)
    }

    override fun getItemCount() = if (list != null && list!!.size > 0) list!!.size else 0

    override fun onBindViewHolder(viewHolder: ViewHolder, pos: Int) {
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
            tv_best_price.setDrawableLeft(R.drawable.ic_tag_icon)
            tv_best_mrp.setDrawableRight(R.drawable.ic_right_arrow_blue)
            drop_down.setDrawableRight(R.drawable.ic_down_arrow)
            tv_more_offers.setDrawableRight(R.drawable.ic_right_arrow_orange)

            if (item is ProductCatResIp) {
                tv_discount_price.text = item.selling_price
                tv_mrp.text = item.mrp
                tv_best_mrp.text = item.best_price
                tv_description.text = item.name
                tv_brand.text = item.brand

                if (item.selSku == null)
                    item.selSku = item.sku!![0]
                calcSavings(item.selSku)
                if (item.sku != null && item.sku.size == 1) {
                    drop_down.setOnClickListener {}
                    drop_down.removeDrawable()
                } else
                    drop_down.setOnClickListener {
                        CommonUtils.showSkuDialog(context, item, IDialogClickListener { data -> calcSavings(data as DataSku) })
                    }

                val intent = Intent(context, ProductActivity::class.java)
                iv_product_img.setOnClickListener {
                    intent.apply {
                        intent.putExtra(Constants.SOURCE, item.id)
                    }
                    context.startActivity(intent)
                }
                tv_increment.setOnClickListener { clickForModify(it, item) }
                tv_decrement.setOnClickListener { clickForModify(it, item) }
                btn_add_to_cart.setOnClickListener { clickForModify(it, item) }

            }
        }

        fun calcSavings(selSku: DataSku?) {
            selSku?.apply {
                pic.withNotNullNorEmpty {
                    CommonUtils.setImage(context, iv_product_img, this[0]!!.pic)
                }
                if (stock!!.contentEquals("0")) {
                    tv_out_of_stock.visibility = View.VISIBLE
                    btn_notify_me.visibility = View.VISIBLE
                    btn_add_to_cart.visibility = View.GONE
                } else {
                    tv_out_of_stock.visibility = View.GONE
                    btn_notify_me.visibility = View.GONE
                    setCartCount(this@ViewHolder, this)
                }
                btn_notify_me.setOnClickListener {
                    val dialog = Dialog(context)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setContentView(R.layout.popup_notify)
                    dialog.show()

                    val t = Timer()
                    t.schedule(object : TimerTask() {
                        override fun run() {
                            dialog.dismiss()
                            t.cancel()
                        }
                    }, 5000)
                }
                tempMyCart = -1
                drop_down.text = size
                val sellingPrice = selling_price
                val mrp = mrp
                if (mrp == sellingPrice) {
                    ll_discount.visibility = View.GONE
                }
                if (mrp != null && mrp > 0 && sellingPrice != null) {
                    val savingsPer = (((mrp - sellingPrice) / mrp) * 100)
                    val savings = mrp - sellingPrice
                    assignProdAmt(sellingPrice, mrp, savings, savingsPer)
                }
            }

        }

        private fun assignProdAmt(sellingPrice: Float, mrp: Float, savings: Float, savingsPer: Float) {
            tv_discount_price.apply {
                text = CommonUtils.getRupeesSymbol(context, "%.0f".format(sellingPrice))
            }
            tv_mrp.apply {
                text = CommonUtils.getRupeesSymbol(context, "%.0f".format(mrp))
                strikeThr()
            }
            if (savingsPer >= 5) {
                tv_save.text = CommonUtils.getPerSymbol(context, "%.0f".format(savingsPer))
                tv_save_amount.text = context.getString(R.string.off)
            } else {
                tv_save.text = context.getString(R.string.save)
                tv_save_amount.text = CommonUtils.getRupeesSymbol(context, "%.0f".format(savings))
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