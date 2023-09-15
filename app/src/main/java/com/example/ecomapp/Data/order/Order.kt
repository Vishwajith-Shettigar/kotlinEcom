package com.example.ecomapp.Data.order

import com.example.ecomapp.Data.Address
import com.example.ecomapp.Data.Cartproduct

data class Order(
    val orderstatus:String,
    val totalprice:Float,
    val products:List<Cartproduct>,
    val address:Address
)
