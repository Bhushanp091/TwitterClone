package com.example.social_media_app.util

abstract class Screens(val route:String){
    object SignIn : Screens("signIn")
    object LogIn : Screens("login")
    object HomeMain : Screens("home-main")
    object Home : Screens("home")
    object Search : Screens("search")
    object AddPost : Screens("add-post")
    object Profile : Screens("profile")
    object AddComment : Screens("addComment/{postId}")
    object OtherUserProfile : Screens("otherUserProfile")
    object Posts : Screens("posts/{postId}")

}