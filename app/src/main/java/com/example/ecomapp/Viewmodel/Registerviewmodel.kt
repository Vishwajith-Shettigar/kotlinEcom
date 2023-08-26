package com.example.ecomapp.Viewmodel

import androidx.lifecycle.ViewModel
import com.example.ecomapp.Data.User
import com.example.ecomapp.Util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class Registerviewmodel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore:FirebaseFirestore
    ) : ViewModel() {

   private val _register= MutableStateFlow<Resource<User>>(Resource.Notspecified());
    val register:Flow<Resource<User>> =_register

    private val _validation= Channel<RegisterFieldState>();
    val validation =_validation.receiveAsFlow()


        fun createAccountWithEmail(user:User,password:String){

           if( checkValidation(user, password)) {
               runBlocking {
                   _register.emit(Resource.Loading())
               }

               firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                   .addOnSuccessListener {
                       it.user?.let {

                           saveUser(it.uid,user)


                       }

                   }
                   .addOnFailureListener {
                       _register.value = Resource.Error(it.message.toString())

                   }
           }else{
               val registerfieldstate=RegisterFieldState(
                   validateEmail(user.email),
                   validatePassword(password)


               )
               runBlocking {
                   _validation.send(registerfieldstate);
               }
           }


        }

    private fun saveUser(UID: String,user:User) {
        firestore.collection(Constants.user_collection).
        document(UID).set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }
            .addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }


    }

    private fun checkValidation(user: User, password: String):Boolean {
        val emailval = validateEmail(user.email);
        val passval = validatePassword(password);
        val bothVali = emailval is RegisterValidation.Success &&
                passval is RegisterValidation.Success

        return bothVali
    }

}