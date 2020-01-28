package com.citygrocer.customer.util

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.citygrocer.customer.R
import com.citygrocer.customer.api.ApiConstants
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.interfaces.IDialogClickListener
import com.citygrocer.customer.module.ProductCatResIp
import com.citygrocer.customer.module.ProductCategoryRes
import com.citygrocer.customer.module.RegisterResIp
import com.google.gson.Gson
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CommonUtils {
    companion object {
        fun setImage(mContext: Context, imageView: ImageView, photo: String?) {
            Glide.with(mContext).load(ApiConstants.IMAGE_BASE_URL + photo)
                    .apply(RequestOptions.centerInsideTransform())
                    .into(imageView)
        }

        fun getCurrentTimeMilliSecons(): Long {
            return System.currentTimeMillis()
        }

        private var pDialog: ProgressDialog? = null
        fun showLoading(mContext: Context, message: String = mContext.getString(R.string.please_wait), cancelable: Boolean = false) {
            try {
                pDialog = ProgressDialog(mContext, R.style.AppTheme_Loading_Dialog)
                pDialog?.apply {
                    setMessage(message)
                    setCancelable(cancelable)
                    setOnCancelListener { dismiss() }
                    show()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }

        fun hideLoading() {
            pDialog?.apply {
                if (isShowing) {
                    dismiss()
                    pDialog = null
                }
            }
        }

        fun saveUserRegisteredData(registerResIP: RegisterResIp?) {
            registerResIP?.apply {
                SharedPreferenceManager.setPrefVal(SharedPreferenceManager.USER_ID, _id
                        ?: "", SharedPreferenceManager.VALUE_TYPE.INTEGER)
                SharedPreferenceManager.setPrefVal(SharedPreferenceManager.MOBILE_NO, mobilenumber
                        ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                SharedPreferenceManager.setPrefVal(SharedPreferenceManager.EMAIL_ID, email
                        ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                SharedPreferenceManager.setPrefVal(SharedPreferenceManager.PASSWORD, password
                        ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                SharedPreferenceManager.setPrefVal(SharedPreferenceManager.NAME, firstname
                        ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                SharedPreferenceManager.setPrefVal(SharedPreferenceManager.LASTNAME, lastname
                        ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                SharedPreferenceManager.setPrefVal(SharedPreferenceManager.OTP, otp
                        ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                SharedPreferenceManager.setPrefVal(SharedPreferenceManager.SOCIAL_ID, social_id
                        ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)

            }

        }

        fun saveCartCount(productListRes: ProductCategoryRes) {
            productListRes.summary.apply {
                SharedPreferenceManager.setPrefVal(SharedPreferenceManager.COUNT, productListRes.summary!![0]!!.cart_count
                        ?: "", SharedPreferenceManager.VALUE_TYPE.INTEGER)
            }
        }

        fun getCartCount(): Int {
            return SharedPreferenceManager.getPrefVal(SharedPreferenceManager.COUNT, 0, SharedPreferenceManager.VALUE_TYPE.INTEGER) as Int
        }

        fun saveDeliveryAddress(count: String) {
            SharedPreferenceManager.setPrefVal(SharedPreferenceManager.DELIVERY_ADDRESS, count
                    , SharedPreferenceManager.VALUE_TYPE.STRING)

        }

        fun getDeliveryAddress(): String {
            return SharedPreferenceManager.getPrefVal(SharedPreferenceManager.DELIVERY_ADDRESS, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String

        }

        fun saveOtpVerificationHashKey(otpStaticKey: String) {
            SharedPreferenceManager.setPrefVal(SharedPreferenceManager.OTP_VERIFICATION_KEY, otpStaticKey, SharedPreferenceManager.VALUE_TYPE.STRING)

        }

        fun getOtpVerificationHashKey(): String {
            return SharedPreferenceManager.getPrefVal(SharedPreferenceManager.OTP_VERIFICATION_KEY, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String

        }

        fun setAuthorizationkey(key: String?) {
            SharedPreferenceManager.setPrefVal(Constants.AUTHORIZATION_KEY, key!!, SharedPreferenceManager.VALUE_TYPE.STRING)
        }

        private fun generateSession(): String {
            return try {
                val chars = "abcdefghijklmnopqrstuvwxyz".toCharArray()
                val sb = StringBuilder()
                val random = Random()
                for (i in 0..15) {
                    val c = chars[random.nextInt(chars.size)]
                    sb.append(c)
                }
                val randomString = sb.toString() + "_" + SimpleDateFormat("ddMMyyyyhhmmssSSS").format(java.util.Date())
                SharedPreferenceManager.setPrefVal(Constants._SESION, randomString, SharedPreferenceManager.VALUE_TYPE.STRING)
                randomString
            } catch (ex: Exception) {
                ex.printStackTrace()
                return ""
            }
        }

        fun getSession(): String {
            val session = SharedPreferenceManager.getPrefVal(Constants._SESION, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
            if (session.isEmpty()) {
                return generateSession()
            }
            return session
        }

        fun getLoginId(): Int {
            return SharedPreferenceManager.getPrefVal(SharedPreferenceManager.USER_ID, 0, SharedPreferenceManager.VALUE_TYPE.INTEGER) as Int
        }

        fun saveID(id: Int?) {
            SharedPreferenceManager.setPrefVal(SharedPreferenceManager.ID, id!!, SharedPreferenceManager.VALUE_TYPE.INTEGER)
        }

        fun getId(): Int {
            return SharedPreferenceManager.getPrefVal(SharedPreferenceManager.ID, 0, SharedPreferenceManager.VALUE_TYPE.INTEGER) as Int
        }

        fun getOTP(): String {
            return SharedPreferenceManager.getPrefVal(Constants.OTP, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }

        fun getName(): String {
            return SharedPreferenceManager.getPrefVal(SharedPreferenceManager.NAME, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String +
                    " " + SharedPreferenceManager.getPrefVal(SharedPreferenceManager.LASTNAME, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String

        }

        fun getMobileno(): String {
            return SharedPreferenceManager.getPrefVal(SharedPreferenceManager.MOBILE_NO, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }

        fun getRupeesSymbol(context: Context, price: String): String {
            return context.getString(R.string.rssymbol) + price
        }

        fun getNegRupeesSymbol(context: Context, price: String): String {
            return context.getString(R.string.negrssymbol) + price
        }

        fun getPerSymbol(context: Context, price: String): String {
            return price + context.getString(R.string.persymbol)
        }

        fun saveCurrentLocation(address: Address?) {
            SharedPreferenceManager.setPrefVal(SharedPreferenceManager.PREF_LOC, Gson().toJson(address), SharedPreferenceManager.VALUE_TYPE.STRING)

        }

        fun getCurrentLocation(): Address? {
            val json = SharedPreferenceManager.getPrefVal(SharedPreferenceManager.PREF_LOC, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
            return try {
                Gson().fromJson<Address>(json, Address::class.java) as Address
            } catch (exp: java.lang.Exception) {
                null
            }
        }

        fun saveMyAddress(address: Address?) {
            SharedPreferenceManager.setPrefVal(SharedPreferenceManager.MY_ADDRESS, Gson().toJson(address), SharedPreferenceManager.VALUE_TYPE.STRING)

        }

        fun getMyAddress(): Address? {
            val json = SharedPreferenceManager.getPrefVal(SharedPreferenceManager.MY_ADDRESS, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
            return try {
                Gson().fromJson<Address>(json, Address::class.java) as Address
            } catch (exp: java.lang.Exception) {
                null
            }
        }

        fun getCurrentLocationAddress(context: Context, latitude: Double, longitude: Double): Address? {
            var fetchedAddress: Address? = null
            val geocoder = Geocoder(context, Locale.ENGLISH)
            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)

                if (addresses.size > 0) {
                    fetchedAddress = addresses[0]
                    val strAddress = StringBuilder()
                    for (i in 0 until fetchedAddress!!.maxAddressLineIndex) {
                        strAddress.append(fetchedAddress.getAddressLine(i)).append(" ")
                    }

                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
            return fetchedAddress
        }

        fun showSkuDialog(context: Context, data: ProductCatResIp, dialogClickListener: IDialogClickListener) {
            val skuArrayList = data.sku
            val dialog = Dialog(context, R.style.CustomDialog)
            val lp = WindowManager.LayoutParams()
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.gravity = Gravity.BOTTOM
            dialog.window!!.attributes = lp
            dialog.setCanceledOnTouchOutside(true)
            val inflater = LayoutInflater.from(context)
            val dailogview = inflater.inflate(R.layout.category_item_size, null)
            val dynamicLayout = dailogview.findViewById(R.id.dynamic_layout) as LinearLayout
            val productName = dailogview.findViewById(R.id.tv_text) as TextView
            val brandName = dailogview.findViewById(R.id.tv_brand) as TextView

            productName.text = data.name
            brandName.text = data.brand
            dialog.setContentView(dailogview)
            dynamicLayout.removeAllViews()

            for (sku in skuArrayList!!) {
                val childLayout = LayoutInflater.from(context).inflate(R.layout.item_category_size, null)
                val title = childLayout.findViewById(R.id.tv_size) as TextView
                val sellingPrice = childLayout.findViewById(R.id.tv_selling_price) as TextView
                val mrp = childLayout.findViewById(R.id.tv_mrp) as TextView
                mrp.strikeThr()
                if (!sku!!.size.isNullOrBlank())
                    title.text = sku.size
                sellingPrice.text = CommonUtils.getRupeesSymbol(context, "%.0f".format(sku.selling_price))
                mrp.text = CommonUtils.getRupeesSymbol(context, "%.0f".format(sku.mrp))
                childLayout.setOnClickListener {
                    data.selSku = sku
                    data.selSku?.tempMyCart = -1
                    dialogClickListener.selectedItem(sku)
                    dialog.dismiss()
                }
                dynamicLayout.addView(childLayout)
            }
            dialog.show()
        }
    }
}

inline fun <E : Any, T : Collection<E?>> T?.withNotNullNorEmpty(func: T.() -> Unit): Unit {
    if (this != null && this.isNotEmpty()) {
        with(this) { func() }
    }
}



