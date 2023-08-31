package com.example.ecomapp.Viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.ecomapp.Data.Product
import com.example.ecomapp.Util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryviewmodel @Inject constructor(

    private  val firestore:FirebaseFirestore
) :ViewModel() {


    private val _speicalproducts= MutableStateFlow<com.example.ecomapp.Util.Resource<List<Product>>>(Resource.Notspecified())

    public val specialproduts:StateFlow<Resource<List<Product>>> =_speicalproducts


    init {
        fetchSpecialProducts()
    }

   fun  fetchSpecialProducts(){
       viewModelScope.launch {

           _speicalproducts.emit(Resource.Loading())
       }

       firestore.collection("Products").whereEqualTo("category","special product")
           .get().addOnSuccessListener {result->
 var specialproducstlist=result.toObjects(Product::class.java)
Log.e("#",specialproducstlist.get(0).name)


               viewModelScope.launch {

                   _speicalproducts.emit(Resource.Success(specialproducstlist))
               }
           }

           .addOnFailureListener{

               viewModelScope.launch {

                   _speicalproducts.emit(Resource.Error(it.message.toString()))
               }
           }
   }

}


