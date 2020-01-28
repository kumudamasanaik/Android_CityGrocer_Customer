package com.citygrocer.customer.screens.navigationdrawer

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.interfaces.IAdapterClickListener
import com.citygrocer.customer.interfaces.OnFragmentInteractionListener
import com.citygrocer.customer.screens.allcategories.AllCategoryActivity
import com.citygrocer.customer.screens.base.adapter.BaseRecAdapter
import com.citygrocer.customer.screens.landing.LandingActivity
import com.citygrocer.customer.screens.location.LocationSearchHistoryActivity
import com.citygrocer.customer.screens.login.LoginActivity
import com.citygrocer.customer.screens.navigationdrawer.model.NavigationDrawerModel
import com.citygrocer.customer.screens.termsofservice.TermsOfServiceActivity
import com.citygrocer.customer.util.CommonUtils
import com.citygrocer.customer.util.SharedPreferenceManager
import com.citygrocer.customer.util.Validation
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_navigation_drawer.*
import kotlinx.android.synthetic.main.navigation_drawer_header.*
import java.util.*


class NavigationDrawerFragment : DaggerFragment(), IAdapterClickListener {

    private var mDrawerLayout: DrawerLayout? = null
    internal var navigationView: View? = null
    var mContext: Context? = null
    lateinit var mActivity: FragmentActivity
    lateinit var dialog: Dialog
    lateinit var navAdapter: BaseRecAdapter
    lateinit var navList: ArrayList<NavigationDrawerModel>
    private var mListener: OnFragmentInteractionListener? = null
    private var address: Address? = null

    companion object {

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }

        fun newInstance(): NavigationDrawerFragment {
            return NavigationDrawerFragment()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = this.activity!!
        mActivity = requireActivity()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        navigationView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false)
        return navigationView
    }

    override fun onStart() {
        super.onStart()
        mContext = activity
        setUpNavDrawer()

    }

    fun setUpNavDrawer() {
        navAdapter = BaseRecAdapter(context = mContext!!, adapterClickListener = this, type = R.layout.item_drawer)
        rv_drawer.apply {
            layoutManager = LinearLayoutManager(mContext) as RecyclerView.LayoutManager
            adapter = navAdapter
        }
        navAdapter.addList(getList())

        val myDivider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        myDivider.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.divider)!!)
        rv_drawer.addItemDecoration(myDivider)
        showHeader()
    }

    private fun showHeader() {

        if (CommonUtils.getLoginId() != 0) {
            tv_welcome_name.text = CommonUtils.getName()
            tv_login_register.visibility = View.GONE
            rl_logout.visibility = View.VISIBLE
            tv_logout.setOnClickListener {
                SharedPreferenceManager.clearPreferences()
                startActivity(Intent(activity, LoginActivity::class.java))
                activity!!.finish()
            }

        } else {
            tv_login_register.setOnClickListener {
                startActivity(Intent(mContext, LandingActivity::class.java))
                activity!!.finish()
            }
            rl_logout.visibility = View.GONE
        }
        if (Validation.isValidObject(CommonUtils.getMyAddress())) {
            address = CommonUtils.getMyAddress()
            tv_frag_location.text = address!!.getAddressLine(0)
        } else {
            tv_frag_location.text = "your location"
        }
        rl_location_details.setOnClickListener {
            startActivity(Intent(mContext, LocationSearchHistoryActivity::class.java))
        }
    }

    private fun getList(): ArrayList<NavigationDrawerModel> {

        navList = ArrayList()
        val titles = activity!!.resources.getStringArray(R.array.nav_list)
        val icons = resources.obtainTypedArray(R.array.nav_icon)
        for (i in titles.indices) {
            val current = NavigationDrawerModel()
            current.id = i
            current.title = titles[i % titles.size]
            if (i < icons.length())
                current.icon = ContextCompat.getDrawable(mContext!!, icons.getResourceId(i, -1))
            navList.add(current)
        }
        return navList
    }

    override fun adapterOnclick(itemData: Any, pos: Int, type: Any, op: String) {
        (itemData as NavigationDrawerModel).apply {
            when (title) {
                getString(R.string.menu_shop_by_category) -> {
                    startActivity(Intent(mContext, AllCategoryActivity::class.java).putExtra(Constants.ID, 0))
                }
                getString(R.string.menu_cg_points) -> {
                    showCGPointsDialogue()
                }
                getString(R.string.menu_privacy_policy) -> {
                    startActivity(Intent(mContext, TermsOfServiceActivity::class.java))
                }
            }
        }
    }

    private fun showCGPointsDialogue() {
        dialog = Dialog(mContext!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.alert_redeem_cg_points)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val window = dialog.getWindow()
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
        val close = dialog.findViewById(R.id.btn_cancel) as AppCompatButton
        close.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun setDrawer(drawerLayout: DrawerLayout) {
        mDrawerLayout = drawerLayout
    }

    fun closeDrawer() {
        mDrawerLayout!!.closeDrawer(Gravity.START)
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}