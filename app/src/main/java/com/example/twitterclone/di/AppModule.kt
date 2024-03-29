package com.example.social_media_app.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAuth():FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseDatabase():FirebaseDatabase = Firebase.database

    @Provides
    @Singleton
    fun provideFirebaseStorage():FirebaseStorage = Firebase.storage


}

