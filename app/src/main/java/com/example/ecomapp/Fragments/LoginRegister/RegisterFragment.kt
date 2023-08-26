package com.example.ecomapp.Fragments.LoginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecomapp.Data.User
import com.example.ecomapp.R
import com.example.ecomapp.Util.RegisterValidation
import com.example.ecomapp.Util.Resource
import com.example.ecomapp.Viewmodel.Registerviewmodel
import com.example.ecomapp.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment: Fragment() {

    private  lateinit var binding:FragmentRegisterBinding
    private val  viewmodel by viewModels<Registerviewmodel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentRegisterBinding.inflate(inflater);

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            donthaveac.setOnClickListener {

                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
            regbtn.setOnClickListener {
                val user=User(fname.text.toString().trim(),
                    lname.text.toString().trim(),
                    email.text.toString().trim(),



                    )
                val password=password.text.toString()
                viewmodel.createAccountWithEmail(user,password)

            }
        }

        lifecycleScope.launchWhenStarted {
            viewmodel.register.collect{

                when(it){
                    is Resource.Loading->{

                        binding.regbtn.startAnimation()

                    }
                    is Resource.Success->{

        Log.e("#",it.data.toString())
                        binding.regbtn.revertAnimation()
                    }

                    is Resource.Error->{
                        Log.e("#",it.message.toString())
                        binding.regbtn.revertAnimation()

                    }
                    else ->Unit


                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewmodel.validation.collect{validation->

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


