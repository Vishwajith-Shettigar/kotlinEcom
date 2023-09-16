package com.example.ecomapp.Data.order

sealed class Orderstatus(val status:String) {
    object Ordered:Orderstatus("Ordered")
    object Canceled:Orderstatus("Canceled")
    object Confirmed:Orderstatus("Confirmed")
    object Shipped:Orderstatus("Shipped")
    object Delivered:Orderstatus("Delivered")
    object Returned:Orderstatus("Returned")



}

fun getOrderstatus(status:String):Orderstatus{

    return  when(status){
        "Ordered"->{
            Orderstatus.Ordered
        }
        "Canceled"->{
            Orderstatus.Canceled
        }
        "Confirmed"->{
            Orderstatus.Confirmed
        }
        "Shipped"->{
            Orderstatus.Shipped
        }
        "Delivered"->{
            Orderstatus.Delivered
        }
        else->{
            Orderstatus.Returned
        }



    }
}