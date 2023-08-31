package com.example.ecomapp.Fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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


        setUprecyclerview()
      viewmodel.fetchSpecialProducts()

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