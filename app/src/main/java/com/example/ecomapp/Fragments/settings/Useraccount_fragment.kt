package com.example.ecomapp.Fragments.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ecomapp.Data.User
import com.example.ecomapp.Util.Resource
import com.example.ecomapp.Viewmodel.Addressviewmodel
import com.example.ecomapp.Viewmodel.Useraccountviewmodel
import com.example.ecomapp.databinding.FargmentAddressBinding
import com.example.ecomapp.databinding.FragmentUserAccountBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class Useraccount_fragment: Fragment() {

    private lateinit var binding: FragmentUserAccountBinding
    val viewmodel by viewModels<Useraccountviewmodel>()
    private var imageUri: Uri?=null

    private lateinit var imageactivityresultlauncher:ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageactivityresultlauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            imageUri=it.data?.data
            Glide.with(requireView()).load(imageUri).into(binding.imageUser)

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentUserAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewmodel.user.collectLatest {
                when (it) {
                    is Resource.Success -> {


                        hideUserLoading()
                        showUserinformation(it.data!!)

                    }
                    is Resource.Loading -> {
                        showUserLoading()

                    }
                    is Resource.Error -> {
                        hideUserLoading()

                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewmodel.editinfo.collectLatest {
                when (it) {
                    is Resource.Success -> {

                        binding.buttonSave.revertAnimation()
                        findNavController().navigateUp()


                    }
                    is Resource.Loading -> {
                        binding.buttonSave.startAnimation()


                    }
                    is Resource.Error -> {
                        binding.buttonSave.revertAnimation()

                    }
                    else -> Unit
                }
            }
        }
        binding.buttonSave.setOnClickListener {
            binding.apply {
                val fn=edFirstName.text.toString().trim()
                val ln=edLastName.text.toString().trim()
                val email=edEmail.text.toString().trim()
                val user=User(fn,ln,email)
                viewmodel.updateUser(user,imageUri)
            }
        }

        binding.imageEdit.setOnClickListener {

            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type="image/*"
            imageactivityresultlauncher.launch(intent)
        }
    }

    private fun showUserinformation(user:User) {

        binding.apply {
            Glide.with(requireView()).load(user.imagePath).into(imageUser)
            edFirstName.setText(user.firstName)
            edLastName.setText(user.lastName)
            edEmail.setText(user.email)

        }


    }

    private fun showUserLoading() {

        binding.apply {
            progressbarAccount.visibility=View.VISIBLE
            imageUser.visibility=View.INVISIBLE
            imageEdit.visibility=View.INVISIBLE
            edFirstName.visibility=View.INVISIBLE
            edLastName.visibility=View.INVISIBLE
            edEmail.visibility=View.INVISIBLE
            tvUpdatePassword.visibility=View.INVISIBLE
            buttonSave.visibility=View.INVISIBLE
        }
    }

    private fun hideUserLoading() {
        binding.apply {
            progressbarAccount.visibility=View.GONE
            imageUser.visibility=View.VISIBLE
            imageEdit.visibility=View.VISIBLE
            edFirstName.visibility=View.VISIBLE
            edLastName.visibility=View.VISIBLE
            edEmail.visibility=View.VISIBLE
            tvUpdatePassword.visibility=View.VISIBLE
            buttonSave.visibility=View.VISIBLE
        }
    }
}