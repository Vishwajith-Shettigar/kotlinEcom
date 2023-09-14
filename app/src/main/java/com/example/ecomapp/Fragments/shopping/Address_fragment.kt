package com.example.ecomapp.Fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecomapp.Data.Address
import com.example.ecomapp.Util.Resource
import com.example.ecomapp.Viewmodel.Addressviewmodel
import com.example.ecomapp.databinding.FargmentAddressBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.SnackbarContentLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class Address_fragment:Fragment(){

    private lateinit var binding: FargmentAddressBinding
    val viewmodel by viewModels<Addressviewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewmodel.addnewaddress.collectLatest {
                when(it){
                    is Resource.Loading->{

                        binding.progressbarAddress.visibility=View.VISIBLE


                    }
                    is Resource.Success->{
                        binding.progressbarAddress.visibility=View.INVISIBLE
                        findNavController().navigateUp()



                    }
                    is Resource.Error->{
                        binding.progressbarAddress.visibility=View.INVISIBLE


                    }
                    else->Unit
                }
            }
        }


        lifecycleScope.launchWhenStarted {
            viewmodel.error.collectLatest{
                binding.progressbarAddress.visibility=View.INVISIBLE
                Snackbar.make(requireView(),it,Snackbar.LENGTH_SHORT)
                    .show()

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FargmentAddressBinding.inflate(inflater)
     return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.apply {
            buttonSave.setOnClickListener{

                val addresstitle=edAddressTitle.text.toString().trim()


                val fname=edFullName.text.toString().trim()
                val street=edStreet.text.toString().trim()

                val pno=edPhone.text.toString().trim()
                val city=edCity.text.toString().trim()




                val state=edState.text.toString().trim()
               val address=Address(addresstitle,fname,street,pno,city, state)
                viewmodel.addAddress(address)
            }
        }

    }

}