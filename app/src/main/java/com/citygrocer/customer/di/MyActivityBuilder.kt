package com.citygrocer.customer.di


import com.citygrocer.customer.screens.allcategories.AllCategoryActivity
import com.citygrocer.customer.screens.applycoupon.ApplyCouponActivity
import com.citygrocer.customer.screens.bottomsheet.BottomSheetModule
import com.citygrocer.customer.screens.cart.CartActivity
import com.citygrocer.customer.screens.changepassword.ChangePasswordActivity
import com.citygrocer.customer.screens.checkout.ItemCheckOutActivity
import com.citygrocer.customer.screens.choosedelivery.ChooseDeliveryAddressActivity
import com.citygrocer.customer.screens.delivery.DeliveryActivity
import com.citygrocer.customer.screens.deliveryaddress.DeliveryAddressActivity
import com.citygrocer.customer.screens.filter.FilterActivity
import com.citygrocer.customer.screens.home.HomeActivity
import com.citygrocer.customer.screens.home.HomeModule
import com.citygrocer.customer.screens.landing.LandingActivity
import com.citygrocer.customer.screens.location.LocationSearchHistoryActivity
import com.citygrocer.customer.screens.login.LoginActivity
import com.citygrocer.customer.screens.otpverification.OtpVerificationActivity
import com.citygrocer.customer.screens.payment.PaymentActivity
import com.citygrocer.customer.screens.placeorder.PlaceOrderActivity
import com.citygrocer.customer.screens.product.DetailImageActivity
import com.citygrocer.customer.screens.product.ProductActivity
import com.citygrocer.customer.screens.registration.RegistrationActivity
import com.citygrocer.customer.screens.resetpassword.ResetPasswordActivity
import com.citygrocer.customer.screens.search.SearchActivity
import com.citygrocer.customer.screens.splash.SplashActivity
import com.citygrocer.customer.screens.subcategory.SubCategoryActivity
import com.citygrocer.customer.screens.termsofservice.TermsOfServiceActivity
import com.citygrocer.customer.screens.verificationcode.VerificationCodeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MyActivityBuilder {

    @ContributesAndroidInjector
    internal abstract fun splashActivity(): SplashActivity

    @ContributesAndroidInjector
    internal abstract fun landingActivity(): LandingActivity

    @ContributesAndroidInjector(modules = [HomeModule::class, BottomSheetModule::class])
    internal abstract fun homeActivity(): HomeActivity

    @ContributesAndroidInjector
    internal abstract fun loginActivity(): LoginActivity

    @ContributesAndroidInjector
    internal abstract fun registerActivity(): RegistrationActivity

    @ContributesAndroidInjector
    internal abstract fun otpActivity(): OtpVerificationActivity

    @ContributesAndroidInjector
    internal abstract fun verifyCodeActivity(): VerificationCodeActivity

    @ContributesAndroidInjector
    internal abstract fun resetPasswordActivity(): ResetPasswordActivity

    @ContributesAndroidInjector
    internal abstract fun changePasswordActivity(): ChangePasswordActivity

    @ContributesAndroidInjector
    internal abstract fun termsActivity(): TermsOfServiceActivity

    @ContributesAndroidInjector
    internal abstract fun allCategoryActivity(): AllCategoryActivity

    @ContributesAndroidInjector
    internal abstract fun subCategoryActivity(): SubCategoryActivity

    @ContributesAndroidInjector
    internal abstract fun productActivity(): ProductActivity

    @ContributesAndroidInjector
    internal abstract fun searchActivity(): SearchActivity

    @ContributesAndroidInjector
    internal abstract fun cartActivity(): CartActivity

    @ContributesAndroidInjector
    internal abstract fun checkoutActivity(): ItemCheckOutActivity

    @ContributesAndroidInjector
    internal abstract fun deliveryActivity(): DeliveryActivity

    @ContributesAndroidInjector
    internal abstract fun chooseAddressActivity(): ChooseDeliveryAddressActivity

    @ContributesAndroidInjector
    internal abstract fun paymentActivity(): PaymentActivity

    @ContributesAndroidInjector
    internal abstract fun placeOrderActivity(): PlaceOrderActivity

    @ContributesAndroidInjector
    internal abstract fun applyCouponActivity(): ApplyCouponActivity

    @ContributesAndroidInjector
    internal abstract fun deliveryAddressActivity(): DeliveryAddressActivity

    @ContributesAndroidInjector
    internal abstract fun locationSearchActivity(): LocationSearchHistoryActivity

    @ContributesAndroidInjector
    internal abstract fun filterActivity(): FilterActivity

    @ContributesAndroidInjector
    internal abstract fun detailImageActivity(): DetailImageActivity


}
