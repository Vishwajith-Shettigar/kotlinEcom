package com.example.ecomapp.Fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.example.ecomapp.Adapters.Homeviewpageradapter
import com.example.ecomapp.Fragments.categories.*
import com.example.ecomapp.R
import com.example.ecomapp.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class Home_fragment:Fragment() {
    private  lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentHomeBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryfragments= arrayListOf<Fragment>(

            Maincategory(),
            Chaircategory(),
            Cupboardcategory(),
            Furniturecategory(),
            Tablecategory(),
            Accessorycategory()
        )

        val categoryadapter=Homeviewpageradapter(categoryfragments,childFragmentManager,lifecycle)
        binding.homefragviewpager.adapter=categoryadapter

        TabLayoutMediator(binding.hometablayout,binding.homefragviewpager){tab,position->


            when(position){
                0->tab.text="Main"
                1->tab.text="Chair"
                2->tab.text="Cupboard"
                3->tab.text="Furniture"
                4->tab.text="Table"
                5->tab.text="Accessory"




            }

        }.attach()


    }
}