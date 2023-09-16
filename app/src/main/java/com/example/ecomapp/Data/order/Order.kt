package com.example.ecomapp.Data.order

import com.example.ecomapp.Data.Address
import com.example.ecomapp.Data.Cartproduct
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random.Default.nextLong

data class Order(
    val orderstatus:String,
    val totalprice:Float,
    val products:List<Cartproduct>,
    val address:Address,
    val date:String=SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date()),
    val orderid:Long=nextLong(0,100_000_000_000)+totalprice.toLong()
){
    constructor():this("",0f, emptyList(),Address())

}
