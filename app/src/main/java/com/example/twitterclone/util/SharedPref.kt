package com.example.twitterclone.util

import android.content.Context
import android.content.Context.MODE_PRIVATE

object SharedPref{


    fun storeAdd(
        email: String,
        password: String,
        bio: String,
        username: String,
        name:String,
        imageUri: String,
        uid:String,
        context:Context
    ){

        val editor = context.getSharedPreferences("user",MODE_PRIVATE).edit()
        editor.putString("email",email)
        editor.putString("password",password)
        editor.putString("bio",bio)
        editor.putString("username",username)
        editor.putString("name",name)
        editor.putString("uid",uid)
        editor.putString("imageUri",imageUri)
        editor.apply()

    }

    fun getEmail(context: Context):String{
        val getData = context.getSharedPreferences("user", MODE_PRIVATE)
        return getData.getString("email","")!!
    }

    fun getBio(context: Context):String{
        val getData = context.getSharedPreferences("user", MODE_PRIVATE)
        return getData.getString("bio","")!!
    }

    fun getUsername(context: Context):String{
        val getData = context.getSharedPreferences("user", MODE_PRIVATE)
        return getData.getString("username","")!!
    }

    fun getName(context: Context):String{
        val getData = context.getSharedPreferences("user", MODE_PRIVATE)
        return getData.getString("name","")!!
    }

    fun getImage(context: Context):String{
        val getData = context.getSharedPreferences("user", MODE_PRIVATE)
        return getData.getString("imageUri","")!!
    }


}