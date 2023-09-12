package com.example.ecomapp.Fragments.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecomapp.Adapters.Cartproductadapter
import com.example.ecomapp.Firebase.Firebasecommon
import com.example.ecomapp.R
import com.example.ecomapp.Util.Resource
import com.example.ecomapp.Util.Verticalitemdecoration
import com.example.ecomapp.Viewmodel.Cartviewmodel
import com.example.ecomapp.databinding.FragmentCartBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

class Cart_fragment : Fragment(R.layout.fragment_cart) {

    private lateinit var binding: FragmentCartBinding

    private val cartadapter by lazy {
        Cartproductadapter()
    }

    private val viewModel by activityViewModels<Cartviewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpcartrv()
        lifecycleScope.launchWhenStarted {
            viewModel.pricefinal.collectLatest {
                if (it != null) {
                    binding.tvTotalPrice.text = "$" + it.toString()
                }
            }
        }

        cartadapter.onProductClick = {
            val b = Bundle().apply { putParcelable("product", it.product) }
            findNavController().navigate(R.id.action_cart_fragment_to_productDetails_fragment, b)

        }

        cartadapter.onPlusclick = {
            viewModel.changeQuantity(it, Firebasecommon.Quantitychanging.INCREASE)
        }
        cartadapter.onMinusclick = {
            viewModel.changeQuantity(it, Firebasecommon.Quantitychanging.DECREASE)
        }

        lifecycleScope.launchWhenStarted {

            viewModel.deletedialog.collectLatest {
                val alertdialog = AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete item from cart")
                    setMessage("Do  you want to delete this item ?")
                    setNegativeButton("Cancel")
                    { dialog, _ ->
                        dialog.dismiss()

                    }
                    setPositiveButton("Yes")
                    {dialog,_->
                        dialog.dismiss()
                            viewModel.deleteCartproduct(it)


                    }
                }
                alertdialog.create()
                alertdialog.show()
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.cartproducts.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        binding.progressbarCart.visibility = View.INVISIBLE
                        if (it.data!!.isEmpty()) {

                            binding.apply {
                                layoutCarEmpty.visibility = View.VISIBLE
                                rvCart.visibility = View.GONE
                                totalBoxContainer.visibility = View.GONE
                                buttonCheckout.visibility = View.GONE
                            }

                        } else {
                            binding.apply {
                                layoutCarEmpty.visibility = View.INVISIBLE
                                rvCart.visibility = View.VISIBLE
                                totalBoxContainer.visibility = View.VISIBLE
                                buttonCheckout.visibility = View.VISIBLE
                            }
                            cartadapter.differ.submitList(it.data)

                        }
                    }
                    is Resource.Loading -> {
                        binding.progressbarCart.visibility = View.VISIBLE

                    }
                    is Resource.Error -> {
                        binding.progressbarCart.visibility = View.INVISIBLE

                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setUpcartrv() {
        binding.rvCart.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = cartadapter
            addItemDecoration(Verticalitemdecoration()) // giving space b/w in rv
        }
    }

}