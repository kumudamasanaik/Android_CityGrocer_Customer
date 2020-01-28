package com.citygrocer.customer.screens.checkout

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.citygrocer.customer.R
import com.citygrocer.customer.screens.home.HomeActivity
import kotlinx.android.synthetic.main.checkout_similar_products.*

class FragmentSimilarProducts : BottomSheetDialogFragment(), View.OnClickListener {

    lateinit var mContext: Context
    private val TAG = "ItemCheckOutActivity"
    var mView: View? = null


    private var onDismissListener: FragmentSimilarProducts.OnDismissListener? = null
    fun setDissmissListener(dissmissListener: FragmentSimilarProducts.OnDismissListener) {
        this.onDismissListener = dissmissListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.checkout_similar_products, container, false)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext = this.activity!!

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_close.setOnClickListener(this)
        btn_start_shopping.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {
                R.id.iv_close -> {
                    dismiss()
                }
                R.id.btn_start_shopping -> {
                    startActivity(Intent(this.activity,HomeActivity::class.java))
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        onDismissListener?.onDismiss(this)
    }

    interface OnDismissListener {
        fun onDismiss(myDialogFragment: FragmentSimilarProducts)
    }
}