package com.example.ecomapp.Util

import android.view.View
import androidx.fragment.app.Fragment
import com.example.ecomapp.Activities.ShoppingActivity
import com.example.ecomapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.showBottomnavigation(){
    val bottomnavigationview=(  activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottomnavigation)
    bottomnavigationview.visibility= View.VISIBLE
}

fun Fragment.hideBottomnavigation(){
    val bottomnavigationview=(  activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottomnavigation)
    bottomnavigationview.visibility= View.GONE
}