package com.example.ecomapp.Data

sealed class Category(  val category:String) {


    object Chair:Category("chair")
    object Cupboard:Category("cupboard")
    object Table:Category("table")
    object Accessory:Category("accessory")
    object Furniture:Category("furiture")






}