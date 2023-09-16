package com.example.ecomapp.Fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecomapp.Adapters.Allordersadapter
import com.example.ecomapp.Util.Resource
import com.example.ecomapp.Viewmodel.Allordersviewmodel
import com.example.ecomapp.databinding.FragmentAllordersBinding

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class Allorder_fragment:Fragment() {

    private  lateinit var binding: FragmentAllordersBinding
    val viewmodel by viewModels<Allordersviewmodel>()
val ordersadapter by lazy{
    Allordersadapter()
}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAllordersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUprecyclerview()

        lifecycleScope.launchWhenStarted {
            viewmodel.allorder.collectLatest {
                when(it){
                    is Resource.Loading->{
                        binding.progressbarAllOrders.visibility=View.VISIBLE

                    }
                    is Resource.Success->{
                        binding.progressbarAllOrders.visibility=View.GONE
                        ordersadapter.differ.submitList(it.data)
                        if(it.data.isNullOrEmpty()){
                            binding.tvEmptyOrders.visibility=View.VISIBLE
                        }
                    }
                    is Resource.Error->{
                        binding.progressbarAllOrders.visibility=View.GONE
                    }
                    else->Unit
                }
            }
        }
    }

    private fun setUprecyclerview() {
        binding.rvAllOrders.apply {
            adapter=ordersadapter
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        }
    }
}