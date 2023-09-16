package com.example.ecomapp.Fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecomapp.Adapters.Billingproductadapter
import com.example.ecomapp.Data.order.Orderstatus
import com.example.ecomapp.Data.order.getOrderstatus
import com.example.ecomapp.Util.Verticalitemdecoration
import com.example.ecomapp.databinding.FragmentAllordersBinding
import com.example.ecomapp.databinding.FragmentOrderDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Orderdetails_fragment:Fragment() {
    private  lateinit var binding: FragmentOrderDetailBinding
    private val billingproductadapter by lazy{
        Billingproductadapter()
    }
    private val args by navArgs<Orderdetails_fragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentOrderDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUprecyclerview()
        val order=args.order
        binding.apply {

            tvOrderId.text="order #${order.orderid.toString()}"
            stepView.setSteps(
                mutableListOf(
                    Orderstatus.Ordered.status,
                    Orderstatus.Confirmed.status,
                    Orderstatus.Shipped.status,
                    Orderstatus.Delivered.status,

                )
            )

            val currentorderstatus=when(getOrderstatus(order.orderstatus)){
                Orderstatus.Ordered->{
                        0
                }
                Orderstatus.Confirmed->{
                       1
                }
                Orderstatus.Shipped->{
                      2
                }
                Orderstatus.Delivered->{
                    3
                }
                else->0

            }

            stepView.go(currentorderstatus,true)
            if(currentorderstatus==3){
                stepView.done(true)
            }

            tvFullName.text=order.address.fullname
            tvAddress.text="${order.address.stree} ${order.address.city}"
            tvPhoneNumber.text=order.address.pno

            tvTotalPrice.text="$"+order.totalprice.toString()





        }
        billingproductadapter.difffer.submitList(order.products)
    }

    private fun setUprecyclerview() {
        binding.rvProducts.apply {
            adapter=billingproductadapter
            layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            addItemDecoration(Verticalitemdecoration())
        }
    }

}