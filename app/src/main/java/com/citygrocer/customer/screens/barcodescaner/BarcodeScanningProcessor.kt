package com.citygrocer.customer.screens.barcodescaner

import android.util.Log
import com.citygrocer.customer.screens.barcodescaner.common.FrameMetadata
import com.citygrocer.customer.screens.barcodescaner.common.GraphicOverlay
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import java.io.IOException

/** Barcode Detector Demo.  */
class BarcodeScanningProcessor(var commuter: IBarcodeCommuter) : VisionProcessorBase<List<FirebaseVisionBarcode>>() {


    // Note that if you know which format of barcode your app is dealing with, detection will be
    // faster to specify the supported barcode formats one by one, e.g.
    // FirebaseVisionBarcodeDetectorOptions.Builder()
    //     .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
    //     .build()
    private val detector: FirebaseVisionBarcodeDetector by lazy {
        FirebaseVision.getInstance().visionBarcodeDetector
    }

    override fun stop() {
        try {
            detector.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception thrown while trying to close Barcode Detector: $e")
        }

    }

    override fun detectInImage(image: FirebaseVisionImage): Task<List<FirebaseVisionBarcode>> {
        return detector.detectInImage(image)
    }

    override fun onSuccess(barcodes: List<FirebaseVisionBarcode>, frameMetadata: FrameMetadata,
                           graphicOverlay: GraphicOverlay) {
        graphicOverlay.clear()
        for (i in barcodes.indices) {
            val barcode = barcodes[i]
            val barcodeGraphic = BarcodeGraphic(graphicOverlay, barcode)
            graphicOverlay.add(barcodeGraphic)
        }
        if (barcodes.isNotEmpty())
            commuter.handledata(barcodes)
    }

    override fun onFailure(e: Exception) {
        Log.e(TAG, "Barcode detection failed $e")
    }

    companion object {
        private const val TAG = "BarcodeScanProc"
    }
}