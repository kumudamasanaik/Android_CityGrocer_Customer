package com.citygrocer.customer.interfaces

interface IAdapterClickListener {
    fun adapterOnclick(itemData: Any, pos: Int = 0, type: Any = "none", op: String = "none")
}