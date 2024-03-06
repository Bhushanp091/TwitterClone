package com.example.twitterclone.auth.presentation.screen

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.compose.ui.platform.LocalContext


import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.social_media_app.util.Screens
import com.example.social_media_app.util.ui_components.TextBox

import com.example.twitterclone.R
import com.example.twitterclone.auth.presentation.viewmodel.AuthViewModel


@Composable
fun SignupScreen(navController: NavController) {

    val authViewmodel = hiltViewModel<AuthViewModel>()
    val firebaseUser by authViewmodel.firebaseUser.observeAsState(null)

    val inputName = remember { mutableStateOf("") }
    val inputEmail = remember { mutableStateOf("") }
    val inputPassword = remember { mutableStateOf("") }
    val inputBio = remember { mutableStateOf("") }
    val inputUsername = remember { mutableStateOf("") }
    var imageUrl = remember { mutableStateOf<Uri?>(null) }
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


    val permissionRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(
                    context, "Permission isn Granted", Toast.LENGTH_LONG
                ).show()

            } else {
                Toast.makeText(
                    context, "Permission isn't Granted", Toast.LENGTH_LONG
                ).show()

            }
        }


    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            imageUri?.let {
                imageUrl.value = it
            }
        }





    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign UP",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
            Box(
                modifier = Modifier.size(150.dp), contentAlignment = Alignment.BottomEnd
            ) {

                Image(
                    painter = if (imageUrl.value == null) {
                        painterResource(id = R.drawable.empty_profile)
                    } else {
                        rememberAsyncImagePainter(model = imageUrl.value)
                    },
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(Color.Blue)

                ) {
                    IconButton(onClick = {
                        val isgranted = ContextCompat.checkSelfPermission(
                            context, permissionRequest
                        ) == PackageManager.PERMISSION_GRANTED

                        if (isgranted) {
                            galleryLauncher.launch("image/*")
                        } else {
                            permissionLauncher.launch(permissionRequest)
                        }

                    }) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "camera",
                            modifier = Modifier.clip(CircleShape),
                            tint = Color.White

                        )
                    }
                }
            }
            TextBox(
                inputText = inputName.value,
                label = "Name",
                onValueChange = { inputName.value = it }
            )
            TextBox(
                inputText = inputUsername.value,
                label = "UserName",
                onValueChange = { inputUsername.value = it }
            )
            TextBox(
                inputText = inputBio.value,
                label = "Bio",
                onValueChange = { inputBio.value = it }
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
                    if (inputName.value.isEmpty() &&
                        inputPassword.value.isEmpty() &&
                        inputEmail.value.isEmpty() &&
                        inputBio.value.isEmpty() &&
                        inputUsername.value.isEmpty()
                    ) {
                        Toast.makeText(
                            context, "Please fill All Details", Toast.LENGTH_LONG
                        ).show()

                    } else {
                        authViewmodel.onRegister(
                            inputEmail.value,
                            inputPassword.value,
                            inputBio.value,
                            inputUsername.value,
                            inputName.value,
                            imageUrl.value,
                            context
                        )
                    }

                    inputName.value = ""
                    inputEmail.value = ""
                    inputPassword.value = ""
                    inputBio.value = ""
                    inputUsername.value = ""

                },
                modifier = Modifier.padding(8.dp),

                ) {
                Text(text = "Register")
            }

            Text(
                text = "Already have account? Login Here",
                color = Color.Blue,
                modifier = Modifier.clickable {
                    navController.navigate(Screens.LogIn.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                })
        }

    }

}