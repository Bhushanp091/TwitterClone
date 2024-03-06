package com.example.twitterclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.social_media_app.util.Screens
import com.example.twitterclone.auth.presentation.screen.LoginScreen
import com.example.twitterclone.auth.presentation.screen.SignupScreen
import com.example.twitterclone.auth.presentation.viewmodel.AuthViewModel
import com.example.twitterclone.ui.theme.TwitterCloneTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TwitterCloneTheme {
                SetBarColor(color = MaterialTheme.colorScheme.inverseOnSurface)
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()
                    val authViemodel  = hiltViewModel<AuthViewModel>()
                    NavHost(
                        navController = navController,
                        startDestination = authViemodel.checkUserAlreadySingIn()
                    ) {
                        composable(Screens.SignIn.route){
                            SignupScreen(navController)
                        }
                        composable(Screens.LogIn.route){
                            LoginScreen(navController)
                        }
                        composable(Screens.HomeMain.route){
                            HomeNavScreen(navController)
                        }

                    }
                }
            }
        }
    }
}

@Composable
private fun SetBarColor(color: Color) {
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(key1 = color) {
        systemUiController.setSystemBarsColor(color)
    }
}
