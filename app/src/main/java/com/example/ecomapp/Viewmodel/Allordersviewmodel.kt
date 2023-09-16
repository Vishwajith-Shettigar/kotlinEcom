package com.example.ecomapp.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.Data.order.Order
import com.example.ecomapp.Util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Allordersviewmodel @Inject constructor(

    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) :ViewModel(){

    private val  _allorder= MutableStateFlow<Resource<List<Order>>>(Resource.Notspecified())
    val allorder=_allorder.asStateFlow()

    init{
        getAllorders()
    }
    fun getAllorders(){

        viewModelScope.launch {
            _allorder.emit(Resource.Loading())

        }

        firestore.collection("users").document(firebaseAuth.uid!!).collection("orders")
            .get()
            .addOnSuccessListener {

                val orders=it.toObjects(Order::class.java)
                viewModelScope.launch {
                    _allorder.emit(Resource.Success(orders))

                }

            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _allorder.emit(Resource.Error(it.message.toString()))

                }
            }
    }

}