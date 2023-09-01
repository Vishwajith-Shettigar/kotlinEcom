package com.example.ecomapp.Fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecomapp.Adapters.Bestdealsadapter
import com.example.ecomapp.Adapters.Bestproductadapter
import com.example.ecomapp.Adapters.Specialproductadapter
import com.example.ecomapp.Util.Resource
import com.example.ecomapp.Viewmodel.MainCategoryviewmodel
import com.example.ecomapp.databinding.FragmentMaincategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class Maincategory: Fragment() {

    lateinit var binding:FragmentMaincategoryBinding

    private lateinit var  specialproductadapter:Specialproductadapter
    private  lateinit var bestdealsadapter: Bestdealsadapter
    private  lateinit var bestproductadapter: Bestproductadapter

private val viewmodel by viewModels<MainCategoryviewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMaincategoryBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewmodel.fetchbestproducts()
      viewmodel.fetchSpecialProducts()
        viewmodel.fetchBestdealsproducts()
        setUpbestdealrecyclerview()
        setUprecyclerview()
        setUpbestproductrecyclerview()



        lifecycleScope.launchWhenStarted {

            viewmodel.specialproduts.collectLatest {
                when(it){
                    is Resource.Loading -> showLoading()
                    is Resource.Success->{
                        specialproductadapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error->{

                        hideLoading()
                    }

                    else -> {
                        Unit
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {

            viewmodel.bestdealproducts.collectLatest {
                when(it){
                    is Resource.Loading -> showLoading()
                    is Resource.Success->{
                        bestdealsadapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error->{

                        hideLoading()
                    }

                    else -> {
                        Unit
                    }
                }
            }
        }



        lifecycleScope.launchWhenStarted {

            viewmodel.bestproducts.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        binding.paginationloadprogressbar.visibility=View.VISIBLE
                    }
                    is Resource.Success->{
                        bestproductadapter.differ.submitList(it.data)
                        binding.paginationloadprogressbar.visibility=View.GONE
                    }
                    is Resource.Error->{

                        hideLoading()
                        binding.paginationloadprogressbar.visibility=View.GONE
                    }

                    else -> {
                        Unit
                    }
                }
            }
        }

        binding.nestedscrollview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{v, scrollX, scrollY, oldScrollX, oldScrollY ->

            if(v.getChildAt(0).bottom<=v.height+scrollY){
                viewmodel.fetchbestproducts()
            }
        })
    }

    private fun setUpbestproductrecyclerview() {
        bestproductadapter=Bestproductadapter()
        binding.bestproductsrv.apply {
            layoutManager=GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)

            adapter=bestproductadapter


        }
    }

    private fun setUpbestdealrecyclerview() {
        bestdealsadapter= Bestdealsadapter()
        binding.bestdealsproductsrv.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

            adapter=bestdealsadapter


        }
    }

    private fun hideLoading() {
        binding.maincategoryprogressbar.visibility=View.INVISIBLE
    }

    private fun showLoading() {
        binding.maincategoryprogressbar.visibility=View.VISIBLE
    }

    private fun setUprecyclerview() {
        specialproductadapter=Specialproductadapter()
        binding.specialproductsrv.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

            adapter=specialproductadapter


        }

    }

}