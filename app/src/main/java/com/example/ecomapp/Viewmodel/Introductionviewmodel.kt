package com.example.ecomapp.Viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.Util.Constants.INTRODUCTION_KEY
import com.example.ecomapp.Util.Constants.SHOPPING_ACTIVITY
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Introductionviewmodel @Inject constructor(

 private val   sharedpreference:SharedPreferences,
 private  val  firebaseAuth: FirebaseAuth

):ViewModel() {
    private val _navigate= MutableStateFlow(0)
    val navigate:StateFlow<Int> = _navigate

    companion object{
        const val shoping_activity= SHOPPING_ACTIVITY

    }

    init{
        val isButtonclicked=sharedpreference.getBoolean(INTRODUCTION_KEY,false)
        val user=firebaseAuth.currentUser
        if(user!=null){

                    viewModelScope.launch {
                        _navigate.emit(shoping_activity)
                    }
        }
        else if(isButtonclicked){
Unit
        }
        else{
Unit
        }
    }
    fun startButtonclicked(){
        sharedpreference.edit().putBoolean(INTRODUCTION_KEY,true)
    }
}