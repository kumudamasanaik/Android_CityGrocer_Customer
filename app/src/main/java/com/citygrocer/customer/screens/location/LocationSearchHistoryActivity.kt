package com.citygrocer.customer.screens.location

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.customviews.DatabaseHelper
import com.citygrocer.customer.module.input.HistoryDetails
import com.citygrocer.customer.screens.location.adapter.HistoryAdapter
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_location_search_history.*
import java.util.*


class LocationSearchHistoryActivity : DaggerAppCompatActivity() {

    lateinit var mContext: Context
    var toolbar: Toolbar? = null
    var appbar: AppBarLayout? = null
    var etSearch: AppCompatEditText? = null
    private var REQUEST_CODE_AUTOCOMPLETE = 123
    private val historyDetails = ArrayList<HistoryDetails>()
    private var recyclerView: RecyclerView? = null
    private var mAdapter: HistoryAdapter? = null
    lateinit var databaseHelper: DatabaseHelper

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_search_history)
        mContext = this
        initScreen()
    }

    private fun initScreen() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        appbar = findViewById<View>(R.id.appbar) as AppBarLayout
        etSearch = findViewById<View>(R.id.et_search) as AppCompatEditText

        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        databaseHelper = DatabaseHelper(this)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView = findViewById<View>(R.id.rv_history) as RecyclerView
        recyclerView!!.layoutManager = mLayoutManager

        etSearch!!.setOnClickListener {
            //openAutocompleteActivity()
            startActivity(Intent(mContext, LocationActivity::class.java).putExtra(Constants.SOURCE, Constants.ADDRESS_LOC))
        }
        rl_use_current_location.setOnClickListener {
            startActivity(Intent(mContext, LocationActivity::class.java).putExtra(Constants.SOURCE, Constants.OBJECT))
        }
        displayData()
    }

    private fun openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this)
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
        } catch (e: GooglePlayServicesRepairableException) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show()
        } catch (e: GooglePlayServicesNotAvailableException) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            val message = ("Google Play Services is not available: " + GoogleApiAvailability.getInstance().getErrorString(e.errorCode))
            Log.e("Tag", message)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == Activity.RESULT_OK) {
                // Get the user's selected place from the Intent.
                val place = PlaceAutocomplete.getPlace(this, data)
                Log.i("TAG", "Place Selected: " + place.name)

                val latLng = place.latLng
                // Toast.makeText(this, latLng.toString(), Toast.LENGTH_SHORT).show()
                val args = Bundle()
                args.putParcelable("latlang", latLng)
                val i = Intent(this, LocationActivity::class.java)
                        .putExtra(Constants.SOURCE, Constants.ADDRESS_LOC)
                        .putExtra(Constants.ADDRESS_LOC, args)
                startActivity(i)

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(this, data)
                Log.e("TAG", "Error: Status = " + status.toString())
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }

    fun displayData() {
        val db = databaseHelper.readableDatabase
        val cursor = db.rawQuery("select * from log", null)
        if (cursor.moveToFirst()) {
            do {
                val c = cursor.getString(0)
                val name = cursor.getString(1)
                //val address = cursor.getString(2)
                historyDetails.add(HistoryDetails(c, name))
                mAdapter = HistoryAdapter(historyDetails, this)
                recyclerView!!.adapter = mAdapter
                // Toast.makeText(this,"History:" +c+ "," +name,Toast.LENGTH_LONG).show();
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
