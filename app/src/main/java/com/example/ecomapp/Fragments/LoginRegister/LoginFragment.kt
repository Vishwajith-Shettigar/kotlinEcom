package com.example.ecomapp.Fragments.LoginRegister

import android.content.Intent
import android.os.Bundle
import android.provider.Contacts.Intents.UI
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
import com.example.ecomapp.dialog.setBottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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

            forgotpass.setOnClickListener{

                    setBottomSheetDialog{email->

 viewmodel.resetPassword(email)

                    }
            }
        }


        lifecycleScope.launchWhenStarted {
            viewmodel.resetPassword.collect {

                when (it) {

                    is Resource.Loading -> {



                    }
                    is Resource.Success -> {
Snackbar.make(requireView(),"Reset link sent to your email",Snackbar.LENGTH_SHORT).show()


                    }
                    is Resource.Error -> {
                        Snackbar.make(requireView(),it.message.toString(),Snackbar.LENGTH_SHORT).show()

                    }
                    else -> {
                        Unit
                    }

                }
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


        lifecycleScope.launchWhenStarted {

            viewmodel.validationResetPassword.collect{validation->


                if( validation.email is RegisterValidation.Failure ){









                }
            }
        }

        }

    }

