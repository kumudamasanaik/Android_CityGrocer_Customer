package com.citygrocer.customer.screens.subcategory

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.customviews.ScreenStateView
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.module.AddCartRes
import com.citygrocer.customer.module.LowestSubCategoriesIp
import com.citygrocer.customer.module.ProductCatResIp
import com.citygrocer.customer.module.ProductCategoryRes
import com.citygrocer.customer.module.input.ProductCategoryIp
import com.citygrocer.customer.module.input.ProductCategorySessionIp
import com.citygrocer.customer.screens.subcategory.adapter.ProductsAdapter
import com.citygrocer.customer.util.CommonUtils
import com.citygrocer.customer.util.toast
import com.citygrocer.customer.util.withNotNullNorEmpty
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_sub_categories.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.fragment_category.view.*
import kotlinx.android.synthetic.main.layout_header.*
import java.util.*
import javax.inject.Inject

class CategoryFragment : DaggerFragment(),
        IAdapterClickListener,
        SubCategoryContract.View,
        View.OnClickListener {

    lateinit var serviceRequestView: View
    lateinit var mContext: Context
    private var adapterAllCategories: ProductsAdapter? = null
    private var categoryList: ArrayList<ProductCatResIp>? = null
    lateinit var lowerCatData: LowestSubCategoriesIp
    @Inject
    lateinit var subCategoryPresenter: SubCategoryPresenter
    lateinit var mActivity: FragmentActivity
    private var modifiedProd: ProductCatResIp? = null
    private var productRes: ProductCategoryRes? = null


    companion object {
        @JvmStatic
        fun newInstance(lowerCatData: LowestSubCategoriesIp): CategoryFragment {
            val bundle = Bundle()
            bundle.putParcelable(Constants.OBJECT, lowerCatData)
            val fragment = CategoryFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = requireActivity()
        mContext = requireContext()
        subCategoryPresenter.takeView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            lowerCatData = getParcelable(Constants.OBJECT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        serviceRequestView = inflater.inflate(R.layout.fragment_category, container, false)
        return serviceRequestView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initScreen()
    }

    override fun initScreen() {
        error_button.setOnClickListener(this)
        empty_button.setOnClickListener(this)
        getProductsList()
    }

    private fun getProductsList() {
        multiState_rview.viewState = ScreenStateView.VIEW_STATE_LOADING
        if (CommonUtils.getLoginId() != 0) {
            if (lowerCatData != null) {
                subCategoryPresenter.displayProductCategory(productCategoryIp = ProductCategoryIp(lowerCatData._id, CommonUtils.getLoginId()))
            }
        } else {
            if (lowerCatData != null) {
                subCategoryPresenter.displayProductSessionCategory(productCategorySessionIp = ProductCategorySessionIp(lowerCatData._id, CommonUtils.getSession()))
            }
        }
        adapterAllCategories = ProductsAdapter(mContext, adapterClickListener = this)
        serviceRequestView.recyclerView.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = adapterAllCategories
        }

    }

    override fun showProductCategoryRes(productCategoryRes: ProductCategoryRes) {
        if (productCategoryRes.isSuccess()) {
            productCategoryRes.result.withNotNullNorEmpty {
                multiState_rview.viewState = ScreenStateView.VIEW_STATE_CONTENT
                productRes = productCategoryRes
                activity!!.cart_badge.text = productCategoryRes.summary!![0]!!.cart_count.toString()
                CommonUtils.saveCartCount(productCategoryRes)
                categoryList = filterNotNull() as ArrayList<ProductCatResIp>
                categoryList = filter { it.sku != null && it.sku.size > 0 } as ArrayList<ProductCatResIp>
                categoryList.withNotNullNorEmpty {
                    adapterAllCategories!!.addList(productCategoryRes.result!!)
                    val count = categoryList!!.size
                    activity!!.tv_items_count.text = count.toString()
                }
                return
            }
            toast("no products found for ${lowerCatData.name}")
            multiState_rview.viewState = ScreenStateView.VIEW_STATE_EMPTY
        }
    }

    override fun showAddedCartRes(addCartRes: AddCartRes) {
        hideProgress()
        if (addCartRes.isSuccess()) {
            toast(addCartRes.message!!)
            activity!!.cart_badge.text = addCartRes.summary.toString()
            adapterAllCategories!!.showModifiedRes(Constants.RES_SUCCESS)
        } else {
            adapterAllCategories!!.showModifiedRes(Constants.RES_FAILED)
        }
    }

    override fun showRemoveFromCart(addCartRes: AddCartRes) {
        hideProgress()
        activity!!.cart_badge.text = addCartRes.summary.toString()
        adapterAllCategories!!.showModifiedRes(if (addCartRes.isSuccess()) Constants.SUCCESS else Constants.RES_FAILED)
    }

    override fun showProgress(msg: String) {
        CommonUtils.showLoading(mContext, getString(R.string.please_wait), true)
    }

    override fun hideProgress() {
        CommonUtils.hideLoading()
    }

    override fun showErrorMsg(throwable: Throwable, apiType: String) {
        Toast.makeText(mActivity, throwable.message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.empty_button -> {
                initScreen()
            }
            R.id.error_button -> {
                initScreen()
            }
        }
    }

    override fun adapterOnclick(itemData: Any, pos: Int, type: Any, op: String) {
        if (itemData is ProductCatResIp && itemData.selSku?.id_product != null) {
            modifiedProd = itemData
            when (op) {
                Constants.OP_ADD_CART -> {
                    CommonUtils.showLoading(mContext, getString(R.string.please_wait), true)
                    subCategoryPresenter.displayAddCart(itemData.addCartIp!!)
                }
                Constants.OP_MODIFY_CART -> {
                    CommonUtils.showLoading(mContext, getString(R.string.please_wait), true)
                    subCategoryPresenter.displayAddCart(itemData.addCartIp!!)
                }
                Constants.OP_REMOVE_FROM_CART -> {
                    CommonUtils.showLoading(mContext, getString(R.string.please_wait), true)
                    subCategoryPresenter.displayAddCart(itemData.addCartIp!!)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        subCategoryPresenter.dropView()
    }
}
