package com.example.ecomapp.Helper

fun Float?.getProductPrice(price:Float):Float{
    if(this==null){
        return price
    }

    val remainingpercentabe=1f-this;
    val priceafteroffer=price-remainingpercentabe
    return  priceafteroffer
}