package com.citygrocer.customer.api

import com.citygrocer.customer.module.*
import com.citygrocer.customer.module.input.*
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {

    @POST(ApiConstants.SIGN_UP)
    fun register(@Body signup: RegisterIp): Observable<CustomerRes>

    @POST(ApiConstants.LOGIN)
    fun login(@Body loginIp: LoginIp): Observable<CustomerRes>

    @POST(ApiConstants.EDIT_REGISTER_DATA)
    fun editRegisterData(@Body productImageViewIp: CustomerIdIP): Observable<CustomerRes>

    @POST(ApiConstants.UPDATE_REGISTER_DATA)
    fun updateRegisterData(@Body signup: RegisterIp): Observable<CustomerRes>

    @POST(ApiConstants.SOCIAL_LOGIN_SIGNUP)
    fun socialSignUp(@Body socialRegModelIp: SocialRegModelIp): Observable<CustomerRes>

    @POST(ApiConstants.FORGOT_PASSWORD)
    fun forgetpassword(@Body forgetPasswordIp: ForgetPasswordIp): Observable<ForgetPasswordRes>

    @POST(ApiConstants.RESET_PASSWORD)
    fun resetpassword(@Body resetCodeIp: ResetCodeIp): Observable<CustomerRes>

    @POST(ApiConstants.CHANGE_PASSWORD)
    fun changepassword(@Body changePassIp: ChangePassIp): Observable<ChangePassRes>

    @POST(ApiConstants.VERIFY_OTP)
    fun otpVerify(@Body otpIp: VerifyOtpIp): Observable<CustomerRes>

    @POST(ApiConstants.RESEND_OTP)
    fun resendotp(@Body resetCodeIp: ResetCodeIp): Observable<ResendOtpRes>

    @GET(ApiConstants.CATEGORY_SUB_CATEGORIES)
    fun categorySubCategory(): Observable<CategorySubCategoryRes>

    @GET(ApiConstants.HOMEPAGE)
    fun getHomeData(): Observable<HomePageResponse>

    @POST(ApiConstants.PRODUCT_CATEGORY)
    fun getProductCategory(@Body productCategoryIp: ProductCategoryIp): Observable<ProductCategoryRes>

    @POST(ApiConstants.PRODUCT_CATEGORY_SESSION)
    fun getProductCategorySession(@Body productCategorySessionIp: ProductCategorySessionIp): Observable<ProductCategoryRes>

    @POST(ApiConstants.SEARCH_DATA)
    fun getSearchViewData(@Body searchIp: SearchIp): Observable<ProductCategoryRes>

    @GET(ApiConstants.TERMS_AND_CONDITIONS)
    fun getTermsAndConditions(): Observable<TermsandConditionsRes>

    @POST(ApiConstants.CART_ADD)
    fun addToCart(@Body cartIp: AddCartIp): Observable<AddCartRes>

    @POST(ApiConstants.PRODUCT_IMAGEVIEW_SKU)
    fun getProductImageViewSku(@Body productImageViewIp: ProductImageViewIp): Observable<ProductCategoryRes>

    @GET(ApiConstants.GET_CART + "{loginId}")
    fun getCartData(@Path("loginId", encoded = true) loginId: Int): Observable<ProductCategoryRes>

    @POST(ApiConstants.DELETE_CART_LIST)
    fun getDeleteCartList(@Body deleteCartIp: DeleteCartIp): Observable<CommonRes>

    @POST(ApiConstants.DELETE_CART_LIST_SESSION)
    fun getDeleteSessionCartList(@Body cartSessionIp: CartSessionIp): Observable<CommonRes>

    @POST(ApiConstants.GET_CART_SESSION)
    fun getCartSessionData(@Body cartSessionIp: CartSessionIp): Observable<ProductCategoryRes>

    @POST(ApiConstants.GET_CART_ID)
    fun getCartIdData(@Body productImageViewIp: CustomerIdIP): Observable<ProductCategoryRes>

    @POST(ApiConstants.ADD_ADDRESS)
    fun getAddAddressData(@Body addAddressIp: AddAddressIp): Observable<AddAddressRes>

    @POST(ApiConstants.CHOOSE_ADDRESS)
    fun getAddressList(@Body deleteCartIp: DeleteCartIp): Observable<AddAddressRes>

    @POST(ApiConstants.DELETE_ADDRESS)
    fun getDeleteAddress(@Body deleteAddressIp: DeleteAddressIp): Observable<AddAddressRes>

    @POST(ApiConstants.EDIT_ADDRESS)
    fun getEditAddressDetails(@Body deleteAddressIp: DeleteAddressIp): Observable<AddAddressRes>

    @POST(ApiConstants.UPDATE_ADDRESS)
    fun getUpdatedAddress(@Body addAddressIp: AddAddressIp): Observable<AddAddressRes>

    @POST(ApiConstants.SELECT_ADDRESS)
    fun getSelectedAddress(@Body deleteAddressIp: DeleteAddressIp): Observable<AddAddressRes>

    @POST(ApiConstants.MERGE_CART_DATA)
    fun getCartMergeData(@Body mergeCartIp: MergeCartIp): Observable<CommonRes>

    @POST(ApiConstants.CHECKOUT)
    fun getCheckoutData(@Body checkOutIp: CheckOutIp): Observable<CheckOutDeliveryRes>
}