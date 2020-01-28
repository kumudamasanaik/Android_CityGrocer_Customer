package com.citygrocer.customer.di

import com.citygrocer.customer.screens.bottomsheet.BottomSheetFragment
import com.citygrocer.customer.screens.bottomsheet.BottomSheetModule
import com.citygrocer.customer.screens.delivery.fragments.DeliverySlotFragment
import com.citygrocer.customer.screens.delivery.fragments.TimeSlotDialogFragment
import com.citygrocer.customer.screens.navigationdrawer.NavigationDrawerFragment
import com.citygrocer.customer.screens.subcategory.CategoryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MyFragmentBuilder {

    @ContributesAndroidInjector
    internal abstract fun fragmentCategoryType(): CategoryFragment

    @ContributesAndroidInjector
    internal abstract fun fragmentNavigationDrawer(): NavigationDrawerFragment

    @ContributesAndroidInjector(modules = [BottomSheetModule::class])
    internal abstract fun fragmentBottomSheet(): BottomSheetFragment

    @ContributesAndroidInjector
    internal abstract fun fragmentDeliverySlot(): DeliverySlotFragment

    @ContributesAndroidInjector
    internal abstract fun fragmentDialogDeliverySlot(): TimeSlotDialogFragment

}
