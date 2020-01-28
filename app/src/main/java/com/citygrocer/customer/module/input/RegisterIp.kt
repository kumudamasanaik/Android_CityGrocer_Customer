package com.citygrocer.customer.module.input

data class RegisterIp(var mobilenumber: String? = null,
                      var email: String? = null,
                      var firstname: String? = null,
                      var lastname: String? = null,
                      var password: String? = null,
                      var referalcode: String? = null,
                      var gender: String? = null,
                      var sms_key: String? = null,
                      var _id: Int? = 0)