package com.example.ecomapp.Viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.ecomapp.Data.Address
import com.example.ecomapp.Util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Addressviewmodel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    private val _addnewaddress = MutableStateFlow<Resource<Address>>(Resource.Notspecified())
    val addnewaddress = _addnewaddress.asSharedFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    fun addAddress(address: Address) {
        viewModelScope.launch {
            _addnewaddress.emit(Resource.Loading())
        }

        val validateinput = validateInput(address)
        if (validateinput) {
            firestore.collection("users")
                .document(firebaseAuth.uid!!)
                .collection("address").document()
                .set(address)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        _addnewaddress.emit(Resource.Success(address))
                    }
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _addnewaddress.emit(Resource.Error(it.message.toString()))
                    }

                }
        } else {
            Log.e("#","hello i am batman")
            viewModelScope.launch {
                _error.emit("All fields are required")
            }
        }
    }

    private fun validateInput(address: Address): Boolean {


        return !address.addressTitle.trim().isEmpty() &&
                !address.fullname.trim().isEmpty() &&
                !address.stree.trim().isEmpty() &&
               !address.pno.trim().isEmpty() &&
               !address.city.trim().isEmpty()


    }


}