package com.example.twitterclone.main.presentation.screen

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.social_media_app.util.Screens
import com.example.twitterclone.auth.presentation.viewmodel.AuthViewModel
import com.example.twitterclone.main.presentation.viewmodel.MainViewModel
import com.example.twitterclone.ui.theme.fontFamily
import com.example.twitterclone.util.SharedPref
import com.example.twitterclone.util.ui_components.TweetCard

@Composable
fun ProfileScreen(navController: NavController,bottomNavController:NavController) {

    val authViewModel = hiltViewModel<AuthViewModel>()
    val firebaseUser by authViewModel.firebaseUser.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(firebaseUser) {
        if (firebaseUser == null) {
            navController.navigate(Screens.LogIn.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }


    val userImage = SharedPref.getImage(context)
    val userUserName = SharedPref.getUsername(context)
    val userBio = SharedPref.getBio(context)
    val userMail = SharedPref.getEmail(context)
    val username = SharedPref.getName(context)


    val backgroundImage = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context).data(userImage).size(Size.ORIGINAL).build()
    ).state
    val profilImage = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context).data(userImage).size(Size.ORIGINAL).build()
    ).state
    val mainViewModel = hiltViewModel<MainViewModel>()
    val currentUserPostsAndData by mainViewModel.postsAndData.observeAsState(null)





    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy((-50).dp),
            horizontalAlignment = Alignment.Start
        ) {

            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .height(150.dp)
                    .border(border = BorderStroke(1.dp, Color.Black)),

                ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (backgroundImage is AsyncImagePainter.State.Error) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(80.dp),
                                imageVector = Icons.Default.ImageNotSupported,
                                contentDescription = null,
                            )
                        }

                    }
                    if (backgroundImage is AsyncImagePainter.State.Loading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    if (backgroundImage is AsyncImagePainter.State.Success) {
                        Image(
                            modifier = Modifier
                                .fillMaxSize(),
                            painter = backgroundImage.painter,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            modifier = Modifier.padding(8.dp),
                            shape = RoundedCornerShape(50),
                            color = Color.Black.copy(alpha = 0.3f),
                            contentColor = Color.White,
                        ) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Rounded.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        }
                        Surface(
                            modifier = Modifier.padding(8.dp),
                            shape = RoundedCornerShape(50),
                            color = Color.Black.copy(alpha = 0.3f),
                            contentColor = Color.White,
                        ) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Rounded.MoreVert,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }


            }
            Box(
                modifier = Modifier
                    .padding(start = 17.dp)
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (profilImage is AsyncImagePainter.State.Error) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Default.ImageNotSupported,
                        contentDescription = null,
                    )
                }
                if (profilImage is AsyncImagePainter.State.Loading) {
                    CircularProgressIndicator()
                }
                if (profilImage is AsyncImagePainter.State.Success) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = profilImage.painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }


            }


        }
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = username, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                    Text(text = "@$userUserName", color = Color.Gray)
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(text = userBio, fontWeight = FontWeight.Light)
                }

                Button(onClick = { authViewModel.onLogOut() }) {
                    Text(text = "Log Out ")
                }

            }
            Spacer(modifier = Modifier.padding(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(text = "452 Following ", fontFamily =  fontFamily)
                Spacer(modifier = Modifier.padding(10.dp))
                Text(text = "521Followers ", style = MaterialTheme.typography.bodyLarge,
                    fontFamily = fontFamily)
            }

            Spacer(modifier = Modifier.padding(8.dp))
            Divider(color = Color.DarkGray)
            Text(
                text = "Posts",
                fontWeight = FontWeight.W900,
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp),
                textDecoration = TextDecoration.Underline
            )
            if (currentUserPostsAndData.isNullOrEmpty()){
                Box (
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ){
                    CircularProgressIndicator()
                }
            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(currentUserPostsAndData?: emptyList() ){
                    if (it.first.userId == firebaseUser?.uid){
                        TweetCard(post = it.first, userModel = it.second,bottomNavController)
                    }

                }
            }
        }
    }
}









