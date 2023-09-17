package com.example.ecomapp.Fragments.shopping

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ecomapp.Util.Resource
import com.example.ecomapp.Util.showBottomnavigation


import com.example.ecomapp.Activities.LoginRegisterActivity
import com.example.ecomapp.BuildConfig
import com.example.ecomapp.Data.Product
import com.example.ecomapp.R
import com.example.ecomapp.Viewmodel.Profileviewmodel
import com.example.ecomapp.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class Profile_fragment:Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<Profileviewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.user.collectLatest {
                when(it){

                 is   Resource.Loading->{
binding.progressbarSettings.visibility=View.VISIBLE

                    }


                  is  Resource.Success->{

                      binding.progressbarSettings.visibility=View.GONE
                      Glide.with(requireView()).load(it.data!!.imagePath).error(ColorDrawable(Color.BLACK))
                          .into(binding.imageUser)
                      binding.tvUserName.text=it.data.firstName


                    }



                   is Resource.Error->{
                       binding.progressbarSettings.visibility=View.GONE

                    }

                    else->Unit
                }
            }
        }

        binding.constraintProfile.setOnClickListener{
                findNavController().navigate(R.id.action_profile_fragment_to_useraccount_fragment)
        }

        binding.linearAllOrders.setOnClickListener{
            findNavController().navigate(R.id.action_profile_fragment_to_order_fragment)
        }

        binding.linearBilling.setOnClickListener{
            val b = Bundle().apply{
                putFloat("Price", 0f)
                putParcelableArray("Products", emptyArray())
            }
            findNavController().navigate(R.id.action_profile_fragment_to_billing_fragment,b)

        }

        binding.linearLogOut.setOnClickListener {
            viewModel.logout()
            val intent=Intent(requireActivity(),LoginRegisterActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.tvVersion.text="version ${BuildConfig.VERSION_CODE}"

    }

    override fun onResume() {
        super.onResume()

        showBottomnavigation()
    }

}