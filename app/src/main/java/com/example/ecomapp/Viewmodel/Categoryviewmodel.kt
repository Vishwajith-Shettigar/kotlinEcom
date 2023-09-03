package com.example.ecomapp.Viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.Data.Category
import com.example.ecomapp.Data.Product
import com.example.ecomapp.Util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Categoryviewmodel constructor(
    private val firestore: FirebaseFirestore,
    private val category:Category
) :ViewModel(){

    private val _topproduts= MutableStateFlow<Resource<List<Product>>>(Resource.Notspecified())
    public val topproducts=_topproduts.asStateFlow()

    private val _bottomproduct= MutableStateFlow<Resource<List<Product>>>(Resource.Notspecified())
    public val bottomproduct=_bottomproduct.asStateFlow()

    init {

        fetchTopproducts()
        fetchBottomproducts()
    }

    fun fetchTopproducts(){
        Log.e("#","inside top fetch")
        viewModelScope.launch {
            _topproduts.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category",category.category)
            .whereEqualTo("offerPercentage",null)
            .get()
            .addOnSuccessListener {result->
                val products=result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _topproduts.emit(Resource.Success(products))
                }
            }
            .addOnFailureListener {
                Log.e("#","top cancel")
                viewModelScope.launch {
                    _topproduts.emit(Resource.Error(""))
                }
            }
    }


    fun fetchBottomproducts(){
        Log.e("#","bottom products ");

        viewModelScope.launch {
            _bottomproduct.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category",category.category)
            .whereEqualTo("offerPercentage",null)
            .get()
            .addOnSuccessListener {result->
                val products=result.toObjects(Product::class.java)

                viewModelScope.launch {
                    _bottomproduct.emit(Resource.Success(products))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _bottomproduct.emit(Resource.Error(""))
                }
            }
    }

}