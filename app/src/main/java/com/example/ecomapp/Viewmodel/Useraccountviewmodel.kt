package com.example.ecomapp.Viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.Data.User
import com.example.ecomapp.Util.RegisterFieldState
import com.example.ecomapp.Util.RegisterValidation
import com.example.ecomapp.Util.Resource
import com.example.ecomapp.Util.validateEmail
import com.example.ecomapp.Ecom
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class Useraccountviewmodel @Inject constructor(

    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val storage: StorageReference,
    app: Application

) : AndroidViewModel(app) {


    private val _user = MutableStateFlow<Resource<User>>(Resource.Notspecified())
    val user = _user.asSharedFlow()

    private val _editinfo = MutableStateFlow<Resource<User>>(Resource.Notspecified())
    val editinfo = _editinfo.asSharedFlow()

    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch {
            _user.emit(Resource.Loading())

        }

        firebaseFirestore.collection("users").document(firebaseAuth.uid!!)
            .get()
            .addOnSuccessListener {
                viewModelScope.launch {
                    val user = it.toObject(User::class.java)
                    _user.emit(Resource.Success(user!!))

                }
            }
            .addOnFailureListener {
                viewModelScope.launch {

                    _user.emit(Resource.Error(it.message.toString()))

                }
            }
    }

    fun updateUser(user: User, imageuri: Uri?) {
        viewModelScope.launch {
            _editinfo.emit(Resource.Loading())

        }
        val areInputvalid = validateEmail(user.email) is RegisterValidation.Success
                && user.firstName.trim().isNotEmpty() &&
                user.lastName.trim().isNotEmpty()

        if (!areInputvalid) {
            viewModelScope.launch {

                _editinfo.emit(Resource.Error("Enter correct input"))

            }
            return
        }
        if (imageuri == null) {
            saveUserinformation(user, true)
        } else {
            saveUserinformationwithnewImage(user, imageuri)

        }


    }

    private fun saveUserinformationwithnewImage(user: User, imageuri: Uri) {

        viewModelScope.launch {
            try {
                    val imagebitmap=MediaStore.Images.Media.getBitmap(
                        getApplication<Ecom>().contentResolver,imageuri
                    )

                val bytearrayoutputstrea=ByteArrayOutputStream()
                imagebitmap.compress(Bitmap.CompressFormat.JPEG,96,bytearrayoutputstrea)
                val imagebytearray=bytearrayoutputstrea.toByteArray()
                val imageDirectory= storage.child("profileimages").child(firebaseAuth.uid!!)
                    .child(UUID.randomUUID().toString())
                val result=imageDirectory.putBytes(imagebytearray).await()
                val imageUrl=result.storage.downloadUrl.await().toString()
                saveUserinformation(user.copy(imagePath = imageUrl),false)
            }
            catch (e:Exception){
                viewModelScope.launch {
                    _editinfo.emit(Resource.Error(e.message.toString()))
                }
            }
        }

    }

    private fun saveUserinformation(user: User, shouldretrieoldimage: Boolean) {

        firebaseFirestore.runTransaction { transaction ->
            val docref = firebaseFirestore.collection("users").document(firebaseAuth.uid!!)
            if (shouldretrieoldimage) {

                val currentuser = transaction.get(docref).toObject(User::class.java)
                val newuser = user.copy(imagePath = currentuser?.imagePath ?: "")
                transaction.set(docref, newuser)

            } else {
                transaction.set(docref, user)

            }
        }
            .addOnSuccessListener {
                viewModelScope.launch {
                    _editinfo.emit(Resource.Success(user))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _editinfo.emit(Resource.Error(it.message.toString()))
                }
            }

    }


}