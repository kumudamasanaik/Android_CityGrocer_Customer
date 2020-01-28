package com.citygrocer.customer.screens.subcategory

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.citygrocer.customer.R
import kotlinx.android.synthetic.main.category_sort.*

class FragmentSort : BottomSheetDialogFragment(), View.OnClickListener{

    lateinit var mContext: Context
    var mView: View? = null

    private var onDismissListener: FragmentSort.OnDismissListener? = null

    fun setDissmissListener(dissmissListener: FragmentSort.OnDismissListener) {
        this.onDismissListener = dissmissListener
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.category_sort, container, false)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext = this.activity!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_alphabetical.setOnClickListener(this)
        tv_price_low_high.setOnClickListener(this)

    }
    override fun onClick(view: View?) {

        if (view != null) {
            when (view.id) {
                R.id.tv_alphabetical -> {
                    dismiss()
                }
                R.id.tv_price_low_high -> {
                    dismiss()
                }
            }
        }
    }


    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        onDismissListener?.onDismiss(this)
    }


    interface OnDismissListener {
        fun onDismiss(myDialogFragment: FragmentSort)
    }
}