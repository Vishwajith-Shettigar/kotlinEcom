package com.example.ecomapp.Viewmodel.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecomapp.Data.Category
import com.example.ecomapp.Viewmodel.Categoryviewmodel
import com.google.firebase.firestore.FirebaseFirestore

class basefragmentfactory(

    private  val firestore: FirebaseFirestore,
    private  val category: Category
):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
return Categoryviewmodel(firestore,category) as T

    }
}