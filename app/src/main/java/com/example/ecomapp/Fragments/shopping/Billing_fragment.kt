package com.example.ecomapp.Fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.ecomapp.Adapters.Addressadapter
import com.example.ecomapp.Adapters.Billingproductadapter
import com.example.ecomapp.Data.Cartproduct
import com.example.ecomapp.R
import com.example.ecomapp.Util.Horizontalitemdecoration
import com.example.ecomapp.Util.Resource
import com.example.ecomapp.Viewmodel.Billingviewmodel
import com.example.ecomapp.databinding.FragmentBillingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class Billing_fragment: Fragment() {

    private lateinit var binding:FragmentBillingBinding
    private val addressadapter by lazy {
        Addressadapter()
    }
    private val billingproductadapter by lazy {
        Billingproductadapter()
    }

    private  val viewmodel by viewModels<Billingviewmodel>()


    private val args by navArgs<Billing_fragmentArgs>()
    private var products=emptyList<Cartproduct>()
    private var totalprice=0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        products=args.products.toList()
        totalprice=args.price
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentBillingBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBillingproductRv()
        setUpAddressRV()


        binding.imageAddAddress.setOnClickListener{
            findNavController().navigate(R.id.action_billing_fragment_to_address_fragment)
        }

        lifecycleScope.launchWhenStarted {
            viewmodel.address.collectLatest {
                when(it){

                    is Resource.Loading->{
                        binding.progressbarAddress.visibility=View.VISIBLE
                    }
                    is Resource.Success->{
                            addressadapter.differ.submitList(it.data)
                        binding.progressbarAddress.visibility=View.GONE
                    }
                    is Resource.Error->{

                        binding.progressbarAddress.visibility=View.GONE
                    }
                    else->Unit
                }
            }
        }

        billingproductadapter.difffer.submitList(products)
        binding.tvTotalPrice.text=totalprice.toString()
    }

    private fun setUpAddressRV() {
        binding.rvAddress.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=addressadapter
            addItemDecoration(Horizontalitemdecoration())
        }
    }

    private fun setBillingproductRv() {

        binding.rvProducts.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=billingproductadapter
            addItemDecoration(Horizontalitemdecoration())
        }
    }
}