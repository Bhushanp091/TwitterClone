package com.example.twitterclone.auth.presentation.screen


import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.social_media_app.util.Screens
import com.example.social_media_app.util.ui_components.TextBox
import com.example.twitterclone.auth.presentation.viewmodel.AuthViewModel


@Composable
fun LoginScreen(
    navController: NavController,
) {

    val authViewModel = hiltViewModel<AuthViewModel>()
    val firebaseUser by authViewModel.firebaseUser.observeAsState()
    val error by authViewModel.error.observeAsState()
    val context = LocalContext.current


    LaunchedEffect(firebaseUser) {
        if (firebaseUser != null) {
            navController.navigate(Screens.HomeMain.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }


    LaunchedEffect(error) {
        if (error != null) {
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }


    val inputEmail = remember { mutableStateOf("") }
    val inputPassword = remember { mutableStateOf("") }




    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = "LogIn",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )



            TextBox(
                inputText = inputEmail.value,
                label = "Email",
                onValueChange = { inputEmail.value = it }
            )
            TextBox(
                inputText = inputPassword.value,
                label = "Password",
                onValueChange = { inputPassword.value = it }
            )

            Button(

                onClick = {
                    if (
                        inputPassword.value.isEmpty() &&
                        inputEmail.value.isEmpty()
                    ) {
                        Toast.makeText(
                            context, "Please fill All Details", Toast.LENGTH_LONG
                        ).show()

                    } else {
                        authViewModel.onLogIn(inputEmail.value, inputPassword.value, context)
                    }

                    inputEmail.value = ""
                    inputPassword.value = ""

                },
                modifier = Modifier.padding(8.dp),

                ) {
                Text(text = "LogIn")
            }

            Text(
                text = "Don't have account? Register Here",
                color = Color.Blue,
                modifier = Modifier.clickable {
                    navController.navigate(Screens.SignIn.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }


    }


}