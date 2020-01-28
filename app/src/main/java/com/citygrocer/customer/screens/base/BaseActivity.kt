package com.citygrocer.customer.screens.base

import android.content.Context
import android.os.Bundle
import android.support.design.internal.ScrimInsetsFrameLayout
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.citygrocer.customer.R
import com.citygrocer.customer.interfaces.OnFragmentInteractionListener
import com.citygrocer.customer.screens.navigationdrawer.NavigationDrawerFragment
import dagger.android.support.DaggerAppCompatActivity

open class BaseActivity : DaggerAppCompatActivity(), OnFragmentInteractionListener {

    var fragmentLayout: LinearLayout? = null
    open var mContext: Context? = null
    var toolbar: Toolbar? = null
    var ivMore: AppCompatImageView? = null
    var ivNotification: AppCompatImageView? = null
    var ivCart: AppCompatImageView? = null
    var ivSearch: AppCompatImageView? = null
    var ivShare: AppCompatImageView? = null
    var ivToolbarLogo: AppCompatImageView? = null
    var appbar: AppBarLayout? = null
    var tvTitle: AppCompatTextView? = null
    var tvLocation: AppCompatTextView? = null
    var ivEditLocation: AppCompatImageView? = null
    var rlLocationDetails: RelativeLayout? = null
    var scrimInsetsFrameLayout: ScrimInsetsFrameLayout? = null
    var drawerLayout: DrawerLayout? = null
    var leftMenuFrag: FrameLayout? = null
    var state: Boolean = false


    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        mContext = this
        initScreen()
        setSupportActionBar(toolbar)
        showMenu()
        setUpNavigationDrawer()
    }

    private fun initScreen() {
        state = false
        fragmentLayout = findViewById<View>(R.id.fragment_layout) as LinearLayout
        tvTitle = findViewById<View>(R.id.tvtitle) as AppCompatTextView
        tvLocation = findViewById<View>(R.id.tv_location) as AppCompatTextView
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        ivMore = findViewById<View>(R.id.iv_more) as AppCompatImageView
        ivNotification = findViewById<View>(R.id.iv_notification) as AppCompatImageView
        ivSearch = findViewById<View>(R.id.iv_search) as AppCompatImageView
        ivShare = findViewById<View>(R.id.iv_share) as AppCompatImageView
        ivCart = findViewById<View>(R.id.iv_cart) as AppCompatImageView
        appbar = findViewById<View>(R.id.appbar) as AppBarLayout
        ivEditLocation = findViewById<View>(R.id.iv_edit_location) as AppCompatImageView
        ivToolbarLogo = findViewById<View>(R.id.iv_toolbar_logo) as AppCompatImageView
        scrimInsetsFrameLayout = findViewById<View>(R.id.scrim_insets_frameLayout) as ScrimInsetsFrameLayout
        drawerLayout = findViewById<View>(R.id.drawerLayout) as DrawerLayout
        leftMenuFrag = findViewById<View>(R.id.navigation_drawer) as FrameLayout
        drawerLayout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        rlLocationDetails = findViewById(R.id.rl_location_details)
    }

    private fun setUpNavigationDrawer() {
        lateinit var newFragment: Fragment
        val ft = supportFragmentManager.beginTransaction()
        newFragment = NavigationDrawerFragment.newInstance()
        ft.replace(R.id.navigation_drawer, newFragment).commit()
        newFragment.setDrawer(drawerLayout!!)
    }

    fun showBack() {
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    fun showClose() {
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    fun showMenu() {
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    fun showCart() {
        ivMore!!.visibility = View.GONE
        ivSearch!!.visibility = View.GONE
        ivNotification!!.visibility = View.GONE
        ivShare!!.visibility = View.GONE
        ivCart!!.visibility = View.VISIBLE
    }

    fun setTitle(title: String?) {
        if (title != null && !TextUtils.isEmpty(title) && title.length > 0) {
            ivToolbarLogo!!.visibility = View.GONE
            tvTitle!!.visibility = View.VISIBLE
            tvTitle!!.text = title
        } else {
            ivToolbarLogo!!.visibility = View.VISIBLE
            tvTitle!!.visibility = View.GONE
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (!state)
                    drawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onFragmentInteraction(data: Any) {

    }
}