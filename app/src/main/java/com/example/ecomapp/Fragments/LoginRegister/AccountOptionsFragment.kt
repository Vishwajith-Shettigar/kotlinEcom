package com.example.ecomapp.Fragments.LoginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecomapp.R
import com.example.ecomapp.databinding.FragmentAccountoptionsBinding
import com.example.ecomapp.databinding.FragmentIntroductionBinding

class AccountOptionsFragment: Fragment() {
    lateinit var binding: FragmentAccountoptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAccountoptionsBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
           regisbtn .setOnClickListener {

                findNavController().navigate(R.id.action_accountOptionsFragment_to_registerFragment)

            }
            loginbtn .setOnClickListener {

                findNavController().navigate(R.id.action_accountOptionsFragment_to_loginFragment)

            }
        }

    }
    
}