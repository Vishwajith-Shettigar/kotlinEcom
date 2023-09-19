package com.example.ecomapp.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.Data.Cartproduct
import com.example.ecomapp.Firebase.Firebasecommon
import com.example.ecomapp.Helper.getProductPrice
import com.example.ecomapp.Util.Constants.user_collection
import com.example.ecomapp.Util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Cartviewmodel @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val firebasecommon: Firebasecommon
) : ViewModel() {

    private val _cartproduct =
        MutableStateFlow<Resource<List<Cartproduct>>>(Resource.Notspecified())
    val cartproducts = _cartproduct.asStateFlow()
    private var cartproductdocument = emptyList<DocumentSnapshot>()

    val pricefinal = cartproducts.map {
        when (it) {
            is Resource.Success -> {

                calculatetotalprice(it.data!!)
            }
            else -> 0f
        }
    }

    private val _deletedialog= MutableSharedFlow<Cartproduct>()
     val deletedialog=_deletedialog.asSharedFlow()


fun deleteCartproduct(cartproduct: Cartproduct){

    val index=cartproducts.value.data?.indexOf(cartproduct)
    if (index != null && index != -1) {
        val docid = cartproductdocument.get(index).id
        firebaseFirestore.collection(user_collection)
        .document(firebaseAuth.uid!!)
        .collection("cart")
                .document(docid)
                .delete()
    }





}

    private fun calculatetotalprice(data: List<Cartproduct>): Float {
        return data.sumByDouble {
            (it.product.offerPercentage.getProductPrice(it.product.price) * it.quantity).toDouble()

        }.toFloat()
    }

    init {
        getCartproducts()
    }

    private fun getCartproducts() {

        viewModelScope.launch {
            _cartproduct.emit(Resource.Loading())

        }
        firebaseFirestore.collection(user_collection).document(firebaseAuth.uid!!)
            .collection("cart").addSnapshotListener { value, error ->

                if (error != null || value == null) {
                    viewModelScope.launch {
                        _cartproduct.emit(Resource.Error(error?.message.toString()))

                    }

                } else {
                    cartproductdocument = value.documents
                    val cartproduct = value.toObjects(Cartproduct::class.java)
                    viewModelScope.launch {
                        _cartproduct.emit(Resource.Success(cartproduct))

                    }
                }
            }
    }

    fun changeQuantity(
        cartproduct: Cartproduct,
        quantitychanging: Firebasecommon.Quantitychanging
    ) {

        val index = cartproducts.value.data?.indexOf(cartproduct)
        if (index != null && index != -1) {
            val docid = cartproductdocument.get(index).id
            when (quantitychanging) {
                Firebasecommon.Quantitychanging.INCREASE -> {

                    viewModelScope.launch {
                        _cartproduct.emit(Resource.Loading())
                    }
                    inscreaseQuantity(docid)

                }
                Firebasecommon.Quantitychanging.DECREASE -> {

                    if (cartproduct.quantity == 1) {
                        viewModelScope.launch {
                            _deletedialog.emit(cartproduct)
                        }
                        return
                    }
                    viewModelScope.launch {
                        _cartproduct.emit(Resource.Loading())
                    }
                    decreaseQuantity(docid)
                }
            }
        }


    }

    private fun decreaseQuantity(docid: String) {
        firebasecommon.decreaseQuantity(docid) { result, exception ->
            if (exception == null) {

            } else {

                viewModelScope.launch {
                    _cartproduct.emit(Resource.Error(exception?.message.toString()))

                }
            }

        }


    }

    private fun inscreaseQuantity(docid: String) {
        firebasecommon.increateQueantity(docid) { result, exception ->
            if (exception == null) {

            } else {

                viewModelScope.launch {
                    _cartproduct.emit(Resource.Error(exception?.message.toString()))

                }
            }

        }
    }
}