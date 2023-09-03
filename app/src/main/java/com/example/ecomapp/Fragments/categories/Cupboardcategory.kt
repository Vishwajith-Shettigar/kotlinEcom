package com.example.ecomapp.Fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ecomapp.Data.Category
import com.example.ecomapp.Viewmodel.Categoryviewmodel
import com.example.ecomapp.Viewmodel.Factory.basefragmentfactory
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class Cupboardcategory:Basecategory() {
    @Inject
    lateinit var firestore: FirebaseFirestore


    val viewmodel by viewModels<Categoryviewmodel> {
        basefragmentfactory(firestore, Category.Cupboard)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.fetchTopproducts()
        viewmodel.fetchBottomproducts()
        lifecycleScope.launchWhenStarted {
            viewmodel.topproducts.collectLatest{
                when(it){

                    is com.example.ecomapp.Util.Resource.Loading->
                    {
                        showToploading()
                    }

                    is com.example.ecomapp.Util.Resource.Success->{
                        hideToploading()
                        topadapter.differ.submitList(it.data)

                    }
                    is com.example.ecomapp.Util.Resource.Error->{

                        hideToploading()
                    }
                    else->{
                        Unit
                    }
                }



            }
        }


        lifecycleScope.launchWhenStarted {
            viewmodel.bottomproduct.collectLatest{
                when(it){

                    is com.example.ecomapp.Util.Resource.Loading->
                    {
                        showBottomloading()
                    }

                    is com.example.ecomapp.Util.Resource.Success->{

                        bottomadapter.differ.submitList(it.data)
                        hideBottomloading()

                    }
                    is com.example.ecomapp.Util.Resource.Error->{

                        hideBottomloading()
                    }
                    else->{
                        Unit
                    }
                }



            }
        }}
}