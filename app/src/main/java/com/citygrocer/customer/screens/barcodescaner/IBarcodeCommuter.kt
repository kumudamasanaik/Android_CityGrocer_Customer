package com.citygrocer.customer.screens.barcodescaner

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode

interface IBarcodeCommuter {
    fun handledata(barcodes: List<FirebaseVisionBarcode>)
}