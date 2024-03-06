package com.example.twitterclone.auth.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.social_media_app.util.Screens
import com.example.twitterclone.auth.model.UserModel
import com.example.twitterclone.util.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase,
    private val storage: FirebaseStorage
) : ViewModel() {


    val useref = database.getReference("user")
    val imageRef = storage.reference.child("users/${UUID.randomUUID()}.jpg")

    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val firebaseUser: LiveData<FirebaseUser?> = _firebaseUser
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    init {
        _firebaseUser.value = auth.currentUser
    }


    fun checkUserAlreadySingIn():String{
        if (_firebaseUser.value != null){
            return Screens.HomeMain.route
        }else{
            return Screens.SignIn.route
        }
    }

    fun onLogIn(email: String, password: String,context: Context) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)
                    getUserData(context)
                } else {
                    _error.postValue("Invalid Credentials")
                }
            }
    }


    private fun getUserData(context: Context){
        useref.child(auth.currentUser!!.uid).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserModel::class.java)
                SharedPref.storeAdd(userData!!.email, userData.password,userData.bio,
                    userData.username, userData.name, userData.imageUri,userData.uid,context)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun onRegister(
        email: String,
        password: String,
        bio: String,
        username: String,
        name: String,
        imageUrl: Uri?,
        context: Context
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)
                    saveImageData(email, password, bio, username, name,imageUrl, auth.currentUser?.uid,context)
                } else {
                    _error.postValue("Something Went Wrong")
                }
            }
    }

    private fun saveImageData(
        email: String,
        password: String,
        bio: String,
        username: String,
        name: String,
        imageUrl: Uri?,
        uid: String?,
        context: Context
    ) {

        val taskResult = imageUrl?.let { imageRef.putFile(it) }

        taskResult?.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveUserData(email, password, bio, username, name, it.toString(), uid,context)
            }
        }


    }

    private fun saveUserData(
        email: String,
        password: String,
        bio: String,
        username: String,
        name: String,
        imageUri: String,
        uid: String?,
        context:Context
    ){
        val userData = UserModel(email,password,bio,username,name,imageUri,uid!!)
        useref.child(uid).setValue(userData).addOnSuccessListener {
            SharedPref.storeAdd(
                email,password,bio,username,name,imageUri,uid,context
            )

        }.addOnFailureListener {

        }

    }

    fun onLogOut(){
        auth.signOut()
        _firebaseUser.postValue(null)
    }

}


















