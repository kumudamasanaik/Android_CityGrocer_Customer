package com.citygrocer.customer.screens.landing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
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
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_landing.*
import java.util.*
import javax.inject.Inject

class LandingActivity : DaggerAppCompatActivity(),
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        SocialLoginContract.View {

    lateinit var mContext: Context
    var toolbar: Toolbar? = null
    var appbar: AppBarLayout? = null
    var tvTitle: AppCompatTextView? = null
    private val TAG = "LandingActivity"
    private var socialType: String? = null
    private var mAuth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null
    private val GMAIL_REQUEST_CODE = 2
    private var mGoogleApiClient: GoogleApiClient? = null
    lateinit var mCallbackManager: CallbackManager
    private var socialSignInputModel: SocialRegModelIp? = null
    @Inject
    lateinit var presenter: SocialLoginPresenter

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        mContext = this
        //printhashkey()
        facebookSignIn()
        googleSignIn()
        initScreen()
    }

    override fun initScreen() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        appbar = findViewById<View>(R.id.appbar) as AppBarLayout
        tvTitle = findViewById<View>(R.id.tvtitle) as AppCompatTextView

        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        tvTitle!!.text = "Log in / Register"

        tv_login.setOnClickListener(this)
        tv_register.setOnClickListener(this)
        ll_fb.setOnClickListener(this)
        ll_google.setOnClickListener(this)

    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    private fun facebookSignIn() {
        FacebookSdk.sdkInitialize(applicationContext)
        mCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:${loginResult.accessToken}")

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
                .addOnCompleteListener(this) { task ->
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

        FirebaseApp.initializeApp(this)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }


    override fun onClick(v: View?) {
        var intent: Intent? = null
        if (v != null) {
            when (v.id) {

                R.id.tv_login -> {
                    intent = Intent(mContext, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.tv_register -> {
                    intent = Intent(mContext, RegistrationActivity::class.java).putExtra(Constants.SOURCE, Constants.REGISTER)
                    startActivity(intent)
                    finish()
                }
                R.id.ll_fb -> {
                    LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"))
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
                CommonUtils.showLoading(this, getString(R.string.please_wait), true)
                startActivityForResult(signInIntent, GMAIL_REQUEST_CODE)
            }
        } catch (exp: Exception) {
            exp.printStackTrace()
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
                CommonUtils.saveUserRegisteredData(res.result)
                navigatetoHomeScreen()
            } else
                toast(res.message ?: getString(R.string.woops_something_went_wrong))
        }
    }

    override fun showErrorMsg(throwable: Throwable, apiType: String) {
        toast(throwable.message ?: getString(R.string.error_something_wrong))
    }

    public override fun onStart() {
        super.onStart()
        currentUser = mAuth?.currentUser
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
        hideProgress()

        if (requestCode == GMAIL_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                finish()
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        socialType = Constants.SOCIAL_TYPE_GOOGLE
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithCredential:success")
                        val user = mAuth!!.currentUser
                        updateUI(user)

                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                    }
                }
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
            Toast.makeText(this, getString(R.string.woops_something_went_wrong), Toast.LENGTH_SHORT).show()
    }

    private fun navigatetoHomeScreen() {
        if (CommonUtils.getCartCount() != 0) {
            showMergeCart()
        } else {
            startActivity(Intent(this, HomeActivity::class.java).putExtra("isLogin", true))
            Toast.makeText(mContext, "Logined Successfully", Toast.LENGTH_SHORT).show()
            finish()
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
            val intent = Intent(this, CartActivity::class.java).putExtra(Constants.SOURCE, Constants.LOGIN)
            startActivity(intent)
            finish()
        }
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

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dropView()
    }

    /*  private fun printhashkey() {

          try {
              val info = packageManager.getPackageInfo("com.citygrocer.customer",
                      PackageManager.GET_SIGNATURES)
              for (signature: android.content.pm.Signature in info.signatures) {
                  val md = MessageDigest.getInstance("SHA")
                  md.update(signature.toByteArray())
                  Log.d("KeyHash:", android.util.Base64.encodeToString(md.digest(), android.util.Base64.DEFAULT))
              }
          } catch (exce: Exception) {
              exce.printStackTrace()
          }

      }*/

    override fun onSupportNavigateUp(): Boolean {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
        return true
    }

}