package com.example.ecomapp.Data.order

sealed class Orderstatus(val status:String) {
    object Ordered:Orderstatus("Ordered")
    object Canceled:Orderstatus("Canceled")
    object Shipped:Orderstatus("Shipped")
    object Delivered:Orderstatus("Delivered")
    object Returned:Orderstatus("Returned")


}