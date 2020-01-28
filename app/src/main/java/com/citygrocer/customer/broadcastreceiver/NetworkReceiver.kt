package com.citygrocer.customer.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Pattern

class NetworkReceiver : BroadcastReceiver() {
    var p = Pattern.compile("\\d{4}")
    //var TAG = "message"
    private var otpReceiver: OTPReceiveListener? = null

    fun initOTPListener(receiver: OTPReceiveListener) {
        this.otpReceiver = receiver
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.apply {
            if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras
                val status = extras!!.get(SmsRetriever.EXTRA_STATUS) as Status

                when (status.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        // Get SMS message contents
                        val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                        val m = p.matcher(message)
                        //message = message.replace("<#> Your otp code is : ", "").split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                        Log.e("message", message)
                        if (m.find()) {
                            if (otpReceiver != null) {
                                otpReceiver!!.onOTPReceived(m.group(0))
                                return
                            }
                        }
                    }
                    CommonStatusCodes.TIMEOUT -> {
                        if (otpReceiver != null) {
                            otpReceiver!!.onOTPTimeOut()
                        }
                    }
                }
            }
        }
    }

    interface OTPReceiveListener {
        fun onOTPReceived(otp: String)

        fun onOTPTimeOut()
    }
}