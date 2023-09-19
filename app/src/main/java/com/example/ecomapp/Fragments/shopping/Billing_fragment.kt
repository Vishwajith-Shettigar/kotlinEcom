package com.example.ecomapp.Fragments.shopping

import android.app.AlertDialog
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
import com.example.ecomapp.Data.Address
import com.example.ecomapp.Data.Cartproduct
import com.example.ecomapp.Data.order.Order
import com.example.ecomapp.Data.order.Orderstatus
import com.example.ecomapp.R
import com.example.ecomapp.Util.Horizontalitemdecoration
import com.example.ecomapp.Util.Resource
import com.example.ecomapp.Viewmodel.Billingviewmodel
import com.example.ecomapp.Viewmodel.Orderviewmodel
import com.example.ecomapp.databinding.FragmentBillingBinding
import com.google.android.material.snackbar.Snackbar
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

    private  val billingviewmodel by viewModels<Billingviewmodel>()
    private  val orderviewmodel by viewModels<Orderviewmodel>()

    private val args by navArgs<Billing_fragmentArgs>()
    private var products=emptyList<Cartproduct>()
    private var totalprice=0f
    private var selectedaddress:Address?=null


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



        if(!args.payment){
            binding.apply {
                buttonPlaceOrder.visibility=View.INVISIBLE
                totalBoxContainer.visibility=View.INVISIBLE
                middleLine.visibility=View.INVISIBLE
                bottomLine.visibility=View.INVISIBLE
            }
        }

        binding.imageAddAddress.setOnClickListener{
            findNavController().navigate(R.id.action_billing_fragment_to_address_fragment)
        }

        lifecycleScope.launchWhenStarted {
            billingviewmodel.address.collectLatest {
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

        lifecycleScope.launchWhenStarted {
            orderviewmodel.order.collectLatest {
                when(it){

                    is Resource.Loading->{
                        binding.buttonPlaceOrder.startAnimation()
                    }
                    is Resource.Success->{
                        Snackbar.make(requireView(),"Order placed",Snackbar.LENGTH_SHORT)
                            .show()
                        binding.buttonPlaceOrder.revertAnimation()
                        findNavController().navigateUp()
                    }
                    is Resource.Error->{

                        binding.buttonPlaceOrder.revertAnimation()
                    }
                    else->Unit
                }
            }
        }

        billingproductadapter.difffer.submitList(products)
        binding.tvTotalPrice.text=totalprice.toString()

        addressadapter.onclick={
            selectedaddress=it
            if(!args.payment) {
                val b = Bundle().apply {
                    putParcelable("Address", selectedaddress)
                }
                findNavController().navigate(R.id.action_billing_fragment_to_address_fragment, b)
            }


        }

        binding.buttonPlaceOrder.setOnClickListener{
            if(selectedaddress==null){
                Snackbar.make(requireView(),"Please select address",Snackbar.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }else{
                showOrderconfirmationdialog()
            }
        }
    }

    private fun showOrderconfirmationdialog() {
        val alertdialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Order items")
            setMessage("Place order ?")
            setNegativeButton("Cancel")
            { dialog, _ ->
                dialog.dismiss()

            }
            setPositiveButton("Yes")
            {dialog,_->

                val order=Order(
                    Orderstatus.Ordered.status,
                    totalprice,
                    products,
                    selectedaddress!!
                )
                orderviewmodel.placeOrder(order)
                dialog.dismiss()



            }
        }
        alertdialog.create()
        alertdialog.show()
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