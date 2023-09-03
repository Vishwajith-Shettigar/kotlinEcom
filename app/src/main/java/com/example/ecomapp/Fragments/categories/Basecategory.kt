package com.example.ecomapp.Fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.Adapters.Bestproductadapter
import com.example.ecomapp.databinding.FragmentBasecategoryBinding

open class Basecategory:Fragment() {
    private lateinit var  binding:FragmentBasecategoryBinding
    protected  val  topadapter:Bestproductadapter by lazy {
        Bestproductadapter()
    }
    protected  val  bottomadapter:Bestproductadapter by lazy {
        Bestproductadapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentBasecategoryBinding.inflate(inflater)
        return  binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUptoprecyclerview()
        setUpbottomrecyclerview()

        binding.basecategorytoprv.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!recyclerView.canScrollVertically(1) && dx !=0)
                {
                     topProductpaging()
                }
            }
        })

        binding.nestedscrollviewbasecategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{ v, scrollX, scrollY, oldScrollX, oldScrollY ->

            if(v.getChildAt(0).bottom<=v.height+scrollY){
                bottomProductpaging()
            }
        })

    }


    fun showToploading(){
binding.basevatepv.visibility=View.VISIBLE
    }
    fun hideToploading()
    {
        binding.basevatepv.visibility=View.GONE


    }

    fun showBottomloading(){
        binding.basecatebestproductpv.visibility=View.VISIBLE
    }
    fun hideBottomloading(){
        binding.basecatebestproductpv.visibility=View.GONE
    }
    open fun topProductpaging(){


    }

    open fun bottomProductpaging(){


    }
    private fun setUpbottomrecyclerview() {


        binding.bottomrecyclerviewbasercate.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

            adapter = bottomadapter
        }
    }

    private fun setUptoprecyclerview() {

        binding.basecategorytoprv.apply {
            layoutManager =
            LinearLayoutManager(requireContext(),  LinearLayoutManager.HORIZONTAL, false)

            adapter = topadapter
        }
    }

}