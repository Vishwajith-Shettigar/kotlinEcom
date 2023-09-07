package com.example.ecomapp.Fragments.shopping

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecomapp.Activities.ShoppingActivity
import com.example.ecomapp.Adapters.Colorsadapter
import com.example.ecomapp.Adapters.Imagesadapter
import com.example.ecomapp.Adapters.Sizeadapter
import com.example.ecomapp.Data.Product
import com.example.ecomapp.R
import com.example.ecomapp.Util.hideBottomnavigation
import com.example.ecomapp.databinding.FragmentProductDetailsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductDetails_fragment:Fragment() {

    private val args by navArgs<ProductDetails_fragmentArgs>()
   private lateinit var binding: FragmentProductDetailsBinding
   private val imagesadapter by lazy{
       Imagesadapter()
   }

    private val sizeadapter by lazy{
        Sizeadapter()
    }

    private val coloradapter by lazy{
        Colorsadapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            hideBottomnavigation()
            binding= FragmentProductDetailsBinding.inflate(inflater)
        return binding.root


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val product=args.product

        setUpsizezrv()
        setUpcolorsrv()
        setUpimagesviewpager()

        binding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.apply {
            productname.text=product.name
            productprice.text="$"+product.price.toString()
            productDesc.text=product.description

            imagesadapter.differ.submitList(product.images)
            product.colors?.let {
                coloradapter.differ.submitList(it)
            }

            product.sizes?.let {
                sizeadapter.differ.submitList(it)
            }
        }


    }

    private fun setUpimagesviewpager() {
  binding.apply {
      viewpagerproductimages.adapter=imagesadapter
  }
    }

    private fun setUpcolorsrv() {
       binding.colorsrv.apply {
           adapter=coloradapter
           layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

       }
    }

    private fun setUpsizezrv() {

        binding.sizerv.apply {
            adapter=sizeadapter
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        }
    }

}