package com.example.ecomapp.Viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.Data.order.Order
import com.example.ecomapp.Util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Orderviewmodel @Inject constructor(
    val firestore: FirebaseFirestore,
    val firebaseAuth: FirebaseAuth
):ViewModel(){

    private val _order= MutableStateFlow<Resource<Order>>(Resource.Notspecified())
    val order=_order.asSharedFlow()


    fun placeOrder(order:Order){

        viewModelScope.launch {
            _order.emit(Resource.Loading())
        }

        firestore.runBatch{batch->
            // add orderes in user-order collectiom
            // add orderes in order collection
            // delete cart from user-cart

            firestore.collection("users")
                .document(firebaseAuth.uid!!)
                .collection("orders")
                .document()
                .set(order)

            firestore.collection("orders").document()
                .set(order)


            firestore.collection("users")
                .document(firebaseAuth.uid!!)
                .collection("cart")
                .get()
                .addOnSuccessListener {
                    it.documents.forEach{
                        it.reference.delete()
                    }
                }


        }
            .addOnSuccessListener {
                viewModelScope.launch {
                    _order.emit(Resource.Success(order))
                }
            }
            .addOnFailureListener{
                viewModelScope.launch {
                    _order.emit(Resource.Error(it.message.toString()))
                }
            }

    }
}