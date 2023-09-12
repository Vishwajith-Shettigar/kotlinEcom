package com.example.ecomapp.Data

data class Cartproduct(
    val product:Product,
    var quantity:Int,
    val selectedColor:Int?=null,
    val selectedsize:String?=null
)
{

    constructor():this(Product(),1,null,null)

}