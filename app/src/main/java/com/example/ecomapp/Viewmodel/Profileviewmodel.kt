package com.example.ecomapp.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.Data.User
import com.example.ecomapp.Util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Profileviewmodel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) :ViewModel(){


  private val _user=MutableStateFlow<Resource<User>>(Resource.Notspecified())
    val user=_user.asStateFlow()
    init {
        getUser()
    }

    fun getUser(){
        viewModelScope.launch {
            _user.emit(Resource.Loading())

        }
        firestore.collection("users")
            .document(firebaseAuth.uid!!)
            .addSnapshotListener { value, error ->

                if(error!=null) {
                    viewModelScope.launch {
                        _user.emit(Resource.Error(error.message.toString()))

                    }

                    return@addSnapshotListener
                }
                val userobject=value?.toObject(User::class.java)
                viewModelScope.launch {
                    _user.emit(Resource.Success(userobject!!))

                }

            }
    }

    fun logout() {
        firebaseAuth.signOut()
    }


}