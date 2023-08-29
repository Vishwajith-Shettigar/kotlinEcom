package com.example.ecomapp.Viewmodel

import android.util.Log
import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.Data.User
import com.example.ecomapp.Util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewmodel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,


):ViewModel() {

    private  val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login=_login.asSharedFlow()

    private  val _resetpassword = MutableSharedFlow<Resource<String>>()
    val resetPassword=_resetpassword.asSharedFlow()

    private val _validationLogin= Channel<RegisterFieldState>();
    val validationlogin =_validationLogin.receiveAsFlow()


    private val _validationResetPassword= Channel<RegisterFieldState>();
    val validationResetPassword =_validationResetPassword.receiveAsFlow()



    fun login(email:String,password:String) {

        if (checkValidation(email, password)) {
            viewModelScope.launch {
                _login.emit(Resource.Loading())
            }


            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {

                    viewModelScope.launch {
                        it.user?.let {
                            _login.emit(Resource.Success(it))


                        }
                    }
                }
                .addOnFailureListener {
                    viewModelScope.launch {

                        _login.emit(Resource.Error(it.message.toString()))


                    }

                }
        }
        else{
            val registerfieldstate=RegisterFieldState(
                validateEmail(email),
                validatePassword(password)


            )
            runBlocking {

                _validationLogin.send(registerfieldstate);
            }

        }

    }
    private fun checkValidation(email:String, password: String):Boolean {
        val emailval = validateEmail(email);
        val passval = validatePassword(password);
        val bothVali = emailval is RegisterValidation.Success &&
                passval is RegisterValidation.Success

        return bothVali
    }
    private fun checkValidationEmail(email:String): Boolean {
        val emailval = validateEmail(email);


        return emailval  is RegisterValidation.Success
    }

    fun resetPassword(email: String){

      if( checkValidationEmail(email)) {
          viewModelScope.launch {

              _resetpassword.emit(Resource.Loading())
          }
          firebaseAuth.sendPasswordResetEmail(email)
              .addOnSuccessListener {
                  viewModelScope.launch {

                      _resetpassword.emit(Resource.Success(email))
                  }


              }
              .addOnFailureListener {

                  viewModelScope.launch {

                      _resetpassword.emit(Resource.Error(it.message.toString()))
                  }

              }
      }
        else{

          val registerfieldstate=RegisterFieldState(
              validateEmail(email),
              validatePassword(" ")
          )

          runBlocking {

              _validationResetPassword.send(registerfieldstate);
          }
      }

    }
}