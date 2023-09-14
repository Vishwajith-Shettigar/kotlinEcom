package com.example.ecomapp.Data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val addressTitle:String,
    val fullname:String,
    val stree:String,
    val pno:String,
    val city:String,
    val state:String
):Parcelable{

    constructor():this("","","","","","")

}
