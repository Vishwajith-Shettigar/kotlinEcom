package com.example.ecomapp.Data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cartproduct(
    val product:Product,
    var quantity:Int,
    val selectedColor:Int?=null,
    val selectedsize:String?=null
):Parcelable
{

    constructor():this(Product(),1,null,null)

}