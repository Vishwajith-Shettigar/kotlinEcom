package com.example.ecomapp.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.ecomapp.Firebase.Firebasecommon
import com.example.ecomapp.Util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance();
    }
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance();
    }

    @Provides
    @Singleton
    fun privideFirestorage()= FirebaseStorage.getInstance().reference


    @Provides
    fun provideIntroductionSp(
        application: Application
    )= application.getSharedPreferences(Constants.INTRODUCTION_SP,MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideFirebasecommon(

        firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore
    )=Firebasecommon(firestore,firebaseAuth)



}