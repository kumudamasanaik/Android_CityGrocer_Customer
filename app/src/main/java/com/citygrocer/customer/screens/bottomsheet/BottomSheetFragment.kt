package com.citygrocer.customer.screens.bottomsheet

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.citygrocer.customer.R
import com.citygrocer.customer.constants.Constants
import com.citygrocer.customer.module.CommonRes
import com.citygrocer.customer.module.CustomerRes
import com.citygrocer.customer.module.input.MergeCartIp
import com.citygrocer.customer.module.input.SocialRegModelIp
import com.citygrocer.customer.screens.cart.CartActivity
import com.citygrocer.customer.screens.home.HomeActivity
import com.citygrocer.customer.screens.login.LoginActivity
import com.citygrocer.customer.screens.registration.RegistrationActivity
import com.citygrocer.customer.util.CommonUtils
import com.citygrocer.customer.util.toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.bottom_sheet.*
import java.util.*
import javax.inject.Inject

class BottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, BottomSheetContract.View, HasSupportFragmentInjector {

    lateinit var mContext: Context
    private val TAG = "HomeActivity"
    var mView: View? = null
    private var mAuth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null
    private var socialType: String? = null
    private val GMAIL_REQUEST_CODE = 2
    private var mGoogleApiClient: GoogleApiClient? = null
    lateinit var mCallbackManager: CallbackManager
    var task: Task<GoogleSignInAccount>? = null
    private var socialSignInputModel: SocialRegModelIp? = null
    @Inject
    lateinit var presenter: BottomSheetPresenter

    private var onDismissListener: OnDismissListener? = null

    fun setDissmissListener(dissmissListener: OnDismissListener) {
        this.onDismissListener = dissmissListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.bottom_sheet, container, false)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext = requireContext()
        facebookSignIn()
        googleSignIn()
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initScreen()
    }

    override fun initScreen() {
        iv_close.setOnClickListener(this)
        tv_login.setOnClickListener(this)
        tv_register.setOnClickListener(this)
        ll_fb.setOnClickListener(this)
        ll_google.setOnClickListener(this)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return this.supportFragmentInjector()
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    private fun facebookSignIn() {
        FacebookSdk.sdkInitialize(mContext)
        mCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError:" + error.message)
            }
        })
    }

    /** facebook*/
    private fun handleFacebookAccessToken(token: AccessToken) {
        socialType = Constants.SOCIAL_TYPE_FACEBOOK

        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = mAuth!!.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception!!)
                        toast(getString(R.string.woops_something_went_wrong))
                    }
                }
    }

    //google integration
    private fun googleSignIn() {
        mAuth = FirebaseAuth.getInstance()

        FirebaseApp.initializeApp(mContext)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(this.context!!)
                .enableAutoManage(this.activity!!, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    override fun onClick(v: View?) {
        var intent: Intent? = null
        if (v != null) {
            when (v.id) {
                R.id.iv_close -> {
                    dismiss()
                }
                R.id.tv_login -> {
                    dismiss()
                    intent = Intent(mContext, LoginActivity::class.java)
                    startActivity(intent)
                }
                R.id.tv_register -> {
                    dismiss()
                    intent = Intent(mContext, RegistrationActivity::class.java).putExtra(Constants.SOURCE, Constants.REGISTER)
                    startActivity(intent)

                }
                R.id.ll_fb -> {
                    LoginManager.getInstance().logInWithReadPermissions(this.activity, Arrays.asList("email", "public_profile"))
                }
                R.id.ll_google -> {
                    signIn(Constants.SOCIAL_TYPE_GOOGLE)
                }
            }
        }
    }

    private fun signIn(socialType: String?) {
        try {
            if (socialType!!.contentEquals(Constants.SOCIAL_TYPE_GOOGLE)) {
                val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
                CommonUtils.showLoading(mContext, getString(R.string.please_wait), true)
                startActivityForResult(signInIntent, GMAIL_REQUEST_CODE)
            }
        } catch (exp: Exception) {
            exp.printStackTrace()
        }

    }

    override fun onStart() {
        super.onStart()
        currentUser = mAuth?.currentUser
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
        hideProgress()
        if (requestCode == GMAIL_REQUEST_CODE) {
            task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task?.getResult(ApiException::class.java)

                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        socialType = Constants.SOCIAL_TYPE_GOOGLE
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this.activity!!) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithCredential:success")
                        val user = mAuth!!.currentUser
                        updateUI(user)

                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(mContext, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                    }
                }
    }


    override fun callSocialSignInApi() {
        if (socialSignInputModel != null) {
            presenter.doSocialsignUp(socialSignInputModel!!)
        }
    }

    override fun socailLoginRes(res: CustomerRes) {
        if (res.isSuccess()) {
            if (res.result != null) {
                hideProgress()
                CommonUtils.saveUserRegisteredData(res.result)
                navigatetoHomeScreen()
            }
        } else
            hideProgress()
        toast(res.message!!)
    }

    override fun showErrorMsg(throwable: Throwable, apiType: String) {
        toast(throwable.message ?: getString(R.string.error_something_wrong))
    }


    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {

            var firname = ""
            var email_id = ""
            var mbl_nmbr = ""

            if (!user.displayName.isNullOrEmpty())
                firname = user.displayName!!

            if (!user.email.isNullOrEmpty())
                email_id = user.email!!

            if (!user.phoneNumber.isNullOrEmpty())
                mbl_nmbr = user.phoneNumber!!

            if (socialType!!.contentEquals(Constants.SOCIAL_TYPE_GOOGLE)) {
                socialSignInputModel = SocialRegModelIp(mobilenumber = mbl_nmbr
                        ?: "", email = email_id, firstname = firname, lastname = "", gender = "", social_id = user.uid)
            } else {
                socialSignInputModel = SocialRegModelIp(mobilenumber = mbl_nmbr
                        ?: "", email = email_id, firstname = firname, lastname = "", gender = "", social_id = user.uid)
            }
            callSocialSignInApi()
            signOut(socialType!!)

        } else
            Toast.makeText(mContext, getString(R.string.woops_something_went_wrong), Toast.LENGTH_SHORT).show()
    }

    private fun signOut(socialType: String) {
        try {
            mAuth!!.signOut()
            if (socialType.equals(Constants.SOCIAL_TYPE_GOOGLE, ignoreCase = true)) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback {
                    Log.e("ok", "SignOut  Done")
                }
            } else {
                LoginManager.getInstance().logOut()
            }
        } catch (exp: Exception) {
            Log.e(TAG, "$socialType:social logout error ")
        }
    }

    private fun navigatetoHomeScreen() {
        if (CommonUtils.getCartCount() != 0) {
            showMergeCart()
        } else {
            startActivity(Intent(context, HomeActivity::class.java).putExtra("isLogin", true))
            dismiss()
        }
    }

    private fun showMergeCart() {
        val mergeCartIp = MergeCartIp(
                session_id = CommonUtils.getSession(),
                _id_customer = CommonUtils.getLoginId()
        )
        presenter.mergeCart(mergeCartIp)
    }

    override fun showMergeCartRes(res: CommonRes) {
        if (res.isSuccess()) {
            startActivity(Intent(context, CartActivity::class.java).putExtra(Constants.SOURCE, Constants.LOGIN))
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        onDismissListener?.onDismiss(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dropView()
    }

    interface OnDismissListener {
        fun onDismiss(myDialogFragment: BottomSheetFragment)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

}