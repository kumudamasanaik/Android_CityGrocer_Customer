package com.citygrocer.customer.interfaces

import android.support.v4.widget.SlidingPaneLayout
import android.util.Log
import android.view.View

interface PaneListener : SlidingPaneLayout.PanelSlideListener {

    override fun onPanelClosed(p0: View) {
        Log.v("PaneListener", "onPanelClosed")
    }

    override fun onPanelOpened(p0: View) {
        Log.v("PaneListener", "onPanelOpened")
    }

    override fun onPanelSlide(p0: View, p1: Float) {
        Log.v("PaneListener", "onPanelSlide")
    }
}