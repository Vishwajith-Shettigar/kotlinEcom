package com.example.ecomapp.Fragments.LoginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecomapp.Activities.ShoppingActivity
import com.example.ecomapp.R
import com.example.ecomapp.Util.RegisterValidation
import com.example.ecomapp.Util.Resource
import com.example.ecomapp.Viewmodel.LoginViewmodel
import com.example.ecomapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment: Fragment() {
private lateinit var binding:FragmentLoginBinding

private val viewmodel by viewModels<LoginViewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            haveac.setOnClickListener {

                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            loginbtn.setOnClickListener {

                 val email=email.text.toString().trim()
                val password=password.text.toString()
                viewmodel.login(email,password)

            }
        }

lifecycleScope.launchWhenStarted {
    viewmodel.login.collect{

        when(it){

            is Resource.Loading->{
                binding.loginbtn.startAnimation()


            }
            is Resource.Success->{
                binding.loginbtn.revertAnimation()
                Intent(requireActivity(),ShoppingActivity::class.java)
                    .also {intent ->
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }

            }
            is Resource.Error->{
                binding.loginbtn.revertAnimation()
                Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()



            }
            else->{
                Unit
            }

        }
    }
}

        lifecycleScope.launchWhenStarted {
            viewmodel.validationlogin.collect{validation->

                if(validation.email is RegisterValidation.Failure){

                    withContext(Dispatchers.Main){
                        binding.email.apply {
                            requestFocus()
                            error=(validation.email.message)
                        }
                    }
                }
                if(validation.password is RegisterValidation.Failure){

                    withContext(Dispatchers.Main){
                        binding.password.apply {
                            requestFocus()
                            error=(validation.password.message)
                        }
                    }
                }


            }
        }
        }
    }

