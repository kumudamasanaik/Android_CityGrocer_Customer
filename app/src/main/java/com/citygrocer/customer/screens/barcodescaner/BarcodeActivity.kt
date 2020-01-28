package com.citygrocer.customer.screens.barcodescaner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.util.Log
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.screens.barcodescaner.common.CameraSource
import com.google.firebase.ml.common.FirebaseMLException
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_barcode.*
import java.io.IOException


class BarcodeActivity : AppCompatActivity(), IBarcodeCommuter {
    var TAG = "BarcodeActivity"
    private var cameraSource: CameraSource? = null
    lateinit var rxPermissions: RxPermissions
    var subscription = CompositeDisposable()

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode)
        rxPermissions = RxPermissions(this)

        subscription.add(rxPermissions
                .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted) {
                        createCameraSource()
                        startCameraSource()
                    } else {

                    }
                })
    }

    private fun createCameraSource() {

        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = CameraSource(this, fireFaceOverlay)
        }
        try {
            cameraSource?.let {
                it.setMachineLearningFrameProcessor(BarcodeScanningProcessor(this))
            }
        } catch (e: FirebaseMLException) {
            Log.e(TAG, "can not create camera source")
        }
    }

    private fun startCameraSource() {
        cameraSource.let {
            try {
                if (firePreview == null) {
                    Log.d(TAG, "resume: Preview is null")
                }
                if (fireFaceOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null")
                }
                firePreview.start(it, fireFaceOverlay)
            } catch (e: IOException) {
                Log.e(TAG, "Unable to start camera source.", e)
                it?.release()
                cameraSource = null
            }
        }
    }

    override fun handledata(barcodes: List<FirebaseVisionBarcode>) {
        var barcode: FirebaseVisionBarcode? = null
        for (i in barcodes.indices) {
            barcode = barcodes[i]
        }
        val returnIntent = Intent()
        returnIntent.putExtra(Constants.KEY_BARCODE, barcode?.displayValue)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription.clear()
        cameraSource?.release()
    }
}