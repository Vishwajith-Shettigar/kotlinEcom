package com.example.ecomapp.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.engine.Resource
import com.example.ecomapp.Data.Cartproduct
import com.example.ecomapp.Firebase.Firebasecommon
import com.example.ecomapp.Util.Constants.user_collection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewmodel @Inject constructor(
    val firestore: FirebaseFirestore,
   val firebaseAuth: FirebaseAuth,
   val firebasecommon: Firebasecommon
) :ViewModel(){

    private val _addtocart=MutableStateFlow<com.example.ecomapp.Util.Resource<Cartproduct>>(com.example.ecomapp.Util.Resource.Notspecified())
    public val addtocart=_addtocart.asStateFlow()


    fun addUpdateproductcart(cartproduct:Cartproduct){
        viewModelScope.launch {
            _addtocart.emit(com.example.ecomapp.Util.Resource.Loading())
        }
        firestore.collection(user_collection).document(firebaseAuth.uid!!)
            .collection("cart").whereEqualTo("product.id",cartproduct.product.id)
            .get()
            .addOnSuccessListener {

                it.documents.let {
                    if(it.isEmpty()){ //add new product

        addnewproduct(cartproduct)

                    }else{
                        val products=it.first().toObject(Cartproduct::class.java)
                        if(products==cartproduct){ // if color or size alos same

                         incqtyproduct(it.first().id,cartproduct)
                        }else{ //add new product
                            addnewproduct(cartproduct)
                        }
                    }
                }
            }
            .addOnFailureListener{
                viewModelScope.launch {
                    _addtocart.emit(com.example.ecomapp.Util.Resource.Error(it.toString()))
                }
            }
    }
    private fun addnewproduct(cartproduct:Cartproduct){
        firebasecommon.addProducttocart(cartproduct){addedproduct,e->
            viewModelScope.launch {
                if(e==null){
                    _addtocart.emit(com.example.ecomapp.Util.Resource.Success(addedproduct!!))
                }
                else{
                    _addtocart.emit(com.example.ecomapp.Util.Resource.Error(e.toString()))
                }
            }

        }

    }
    private fun incqtyproduct(docit:String,cartproduct:Cartproduct) {

firebasecommon.increateQueantity(docit){id,e->

    viewModelScope.launch {
        if (e == null) {
            _addtocart.emit(com.example.ecomapp.Util.Resource.Success(cartproduct!!))
        } else {
            _addtocart.emit(com.example.ecomapp.Util.Resource.Error(e.toString()))
        }
    }

}


    }

}