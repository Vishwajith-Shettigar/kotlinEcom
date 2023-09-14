package com.example.ecomapp.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.Data.Address
import com.example.ecomapp.Util.Resource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Billingviewmodel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) :ViewModel(){

    private val _addresss=MutableStateFlow<Resource<List<Address>>>(Resource.Notspecified())
    val address=_addresss.asSharedFlow()

    init{
        getuseraddress()
    }
    fun getuseraddress(){
        viewModelScope.launch {
            _addresss.emit(Resource.Loading())
        }
            firestore.collection("users").document(firebaseAuth.uid!!)
                .collection("address").
                    addSnapshotListener{value,error->
                        if(error!=null){
                            viewModelScope.launch {
                                _addresss.emit(Resource.Error(error.message.toString()))
                            }
                            return@addSnapshotListener
                        }

                        val productaddress=value?.toObjects(Address::class.java)
                            viewModelScope.launch {
                                _addresss.emit(Resource.Success(productaddress!!))
                            }


                    }

    }
}