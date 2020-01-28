package com.citygrocer.customer.screens.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.util.Log
import android.view.View
import android.view.WindowManager.BadTokenException
import android.widget.*
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.customviews.DatabaseHelper
import com.citygrocer.customer.module.input.Details
import com.citygrocer.customer.screens.deliveryaddress.DeliveryAddressActivity
import com.citygrocer.customer.screens.home.HomeActivity
import com.citygrocer.customer.util.CommonUtils
import com.citygrocer.customer.util.Validation
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_location.*

class LocationActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback, PlaceSelectionListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private lateinit var mMap: GoogleMap
    lateinit var mContext: Context
    private var mLocationRequest: LocationRequest? = null
    private var service: LocationManager? = null
    private var enabled: Boolean? = null
    var locationButton: View? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLastLocation: Location? = null
    private var REQUEST_LOCATION_CODE = 101
    lateinit var mapFragment: SupportMapFragment
    lateinit var resultText: AppCompatTextView
    private var center: LatLng? = null
    internal var mCurentAddress = ""
    internal var mapAddress: Address? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var autocompleteFragment: PlaceAutocompleteFragment? = null
    private var pendingTransactionDialog: Dialog? = null
    private lateinit var source: String
    private var databaseHelper: DatabaseHelper? = null
    private var details: Details? = null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        mContext = this
        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
        }
        databaseHelper = DatabaseHelper(this)
        mapDataInitialise()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun mapDataInitialise() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        resultText = findViewById(R.id.tv_location)
        btn_use_location.setOnClickListener(this)

        autocompleteFragment = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment
        autocompleteFragment!!.setOnPlaceSelectedListener(this)

        service = this.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        enabled = service!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

        val backIcon = (autocompleteFragment!!.view as LinearLayout).getChildAt(0) as ImageView
        backIcon.setImageDrawable(resources.getDrawable(R.drawable.ic_arrow_back))
        backIcon.setOnClickListener {
            finish()
        }
        val editText = (autocompleteFragment!!.view as LinearLayout).getChildAt(1) as EditText
        editText.hint = "Search for your delivery location"
        editText.setHintTextColor(resources.getColor(R.color.colorHintGrey))
        editText.textSize = 16.0f
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMyLocationButtonEnabled = true

        locationButton = (findViewById<View>(Integer.parseInt("1")).parent as View).findViewById(Integer.parseInt("2"))
        fab.setOnClickListener {
            if (mMap != null) {
                if (locationButton != null)
                    locationButton!!.callOnClick()
            }
        }
        with(mMap) {
            uiSettings.isZoomControlsEnabled = true
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
            ) {
                buildGoogleApiClient()
                mMap.isMyLocationEnabled = true
            } else {
                checkLocationPermission()
            }
        } else {
            buildGoogleApiClient()
            mMap.isMyLocationEnabled = true
        }
        if (CommonUtils.getCurrentLocation() != null) {
            mapAddress = CommonUtils.getCurrentLocation()
            val latLng = LatLng(mapAddress!!.latitude, mapAddress!!.longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))

        }
        initCameraIdle()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("Allow CityGrocer to\n" +
                                "access this device’s\n" +
                                "location?")
                        .setPositiveButton("OK") { dialog, which ->
                            ActivityCompat.requestPermissions(
                                    this,
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    REQUEST_LOCATION_CODE
                            )
                        }
                        .create()
                        .show()
            } else ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_CODE
            )
        }
    }

    private fun initCameraIdle() {
        mMap.setOnCameraIdleListener {
            center = mMap.cameraPosition.target
            getCurrentAddress(center!!.latitude, center!!.longitude).execute()
        }
    }

    @Synchronized
    fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        mGoogleApiClient!!.connect()
    }


    override fun onPlaceSelected(place: Place?) {
        if (Validation.isValidObject(place) && Validation.isValidObject(place!!.latLng))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 16f))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place!!.latLng, 12.0f))
        iv_search.visibility = View.GONE
        details = Details(id = "1", name = place.address.toString())
        databaseHelper!!.addDetails(details!!)
    }

    override fun onError(p0: Status?) {

    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 1000
        mLocationRequest!!.fastestInterval = 1000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        if (!enabled!!) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }

        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //  LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient!!, mLocationRequest!!, this)
            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            val latLng = LatLng(location.latitude, location.longitude)

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f))
                        }
                    }
        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onLocationChanged(location: Location?) {
        mLastLocation = location

        val latLng = LatLng(location!!.latitude, location.longitude)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f))
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_use_location -> {
                if (source.isNotEmpty() && source.contentEquals(Constants.ADD_ADDRESS)) {
                    val intent = Intent(this, DeliveryAddressActivity::class.java)
                    intent.apply {
                        putExtra(Constants.ADDRESS_LOC, mapAddress)
                        putExtra(Constants.SOURCE, Constants.SOURCE_LOCATION_ACTIVITY)
                        CommonUtils.saveMyAddress(mapAddress)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    CommonUtils.saveMyAddress(mapAddress)
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_LOCATION_CODE -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient()
                        }
                        mMap.isMyLocationEnabled = true
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    inner class getCurrentAddress(internal var Latitude: Double?, internal var Longitude: Double?) : AsyncTask<Void, Void, String>() {


        override fun onPreExecute() {
            super.onPreExecute()
            resultText.setText(R.string.please_wait)
        }

        override fun doInBackground(vararg voids: Void): String {
            mapAddress = CommonUtils.getCurrentLocationAddress(mContext, Latitude!!, Longitude!!)
            return if (Validation.isValidObject(mapAddress)) mapAddress!!.getAddressLine(0) else ""

        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            mCurentAddress = s
            if (Validation.isValidString(mCurentAddress)) {
                resultText.text = mCurentAddress
            } else {
                if (!(mContext as Activity).isFinishing) {
                    try {
                        resultText.setText(R.string.woops_something_went_wrong)
                        pendingTransactionDialog = Dialog(mContext, R.style.CustomDialogThemeLightBg)
                        pendingTransactionDialog!!.setCanceledOnTouchOutside(true)
                        pendingTransactionDialog!!.setContentView(R.layout.dialog_alert_message)
                        (pendingTransactionDialog!!.findViewById(R.id.dialog_title) as TextView).text = mContext.getString(R.string.unable_to_identify)
                        (pendingTransactionDialog!!.findViewById(R.id.dialog_text) as TextView).text = mContext.getString(R.string.please_enter_manually)
                        (pendingTransactionDialog!!.findViewById(R.id.tv_cancel) as TextView).text = mContext.getString(R.string.manually).toUpperCase()
                        (pendingTransactionDialog!!.findViewById(R.id.tv_empty_cart) as TextView).text = mContext.getString(R.string.retry).toUpperCase()
                        pendingTransactionDialog!!.show()

                        (pendingTransactionDialog!!.findViewById(R.id.tv_cancel) as TextView).setOnClickListener {
                            pendingTransactionDialog!!.dismiss()
                        }
                        (pendingTransactionDialog!!.findViewById(R.id.tv_empty_cart) as TextView).setOnClickListener {
                            mapDataInitialise()
                            pendingTransactionDialog!!.dismiss()

                        }
                    } catch (e: BadTokenException) {
                        Log.e("WindowManagerBad ", e.toString())
                    }
                }

            }
        }
    }
}

