package com.example.ecomapp.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class Homeviewpageradapter (

    private  val fragments:List<Fragment>,
    fm:FragmentManager,
    lifecyle:Lifecycle

):FragmentStateAdapter(fm,lifecyle)

    {
        override fun getItemCount(): Int {
           return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments.get(position)
        }


    }