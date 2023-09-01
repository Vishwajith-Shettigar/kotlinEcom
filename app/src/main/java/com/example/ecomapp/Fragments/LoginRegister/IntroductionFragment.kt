package com.example.ecomapp.Fragments.LoginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecomapp.Activities.ShoppingActivity
import com.example.ecomapp.R
import com.example.ecomapp.Util.Constants.SHOPPING_ACTIVITY
import com.example.ecomapp.Viewmodel.Introductionviewmodel
import com.example.ecomapp.databinding.FragmentIntroductionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class IntroductionFragment: Fragment() {
    lateinit var binding:FragmentIntroductionBinding
    private  val viewmodel by viewModels<Introductionviewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentIntroductionBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launchWhenStarted {

            viewmodel.navigate.collect{

                when(it){
                    SHOPPING_ACTIVITY ->{
                        Intent(requireActivity(), ShoppingActivity::class.java)
                            .also {intent ->
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }

                    }
                        else
                            ->
                            Unit
                }
            }
        }
        binding.apply {
            startbtn.setOnClickListener {

                findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsFragment)

            }
        }

    }
}