package com.example.twitterclone.main.presentation.screen

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.social_media_app.util.Screens
import com.example.twitterclone.auth.presentation.viewmodel.AuthViewModel
import com.example.twitterclone.main.domain.Post
import com.example.twitterclone.main.presentation.viewmodel.MainViewModel
import com.example.twitterclone.ui.theme.BlueTheme
import com.example.twitterclone.util.SharedPref
import com.example.twitterclone.util.ui_components.AddPostTextField

@Composable
fun AddCommentScreen(
    post: String,
    navController: NavController,
    bottomNavController: NavController
) {

    val mainViewModel = hiltViewModel<MainViewModel>()
    val authViewModel = hiltViewModel<AuthViewModel>()
    val isCommentsPosted by mainViewModel.isCommentPosted.observeAsState(false)
    val firebaseUser by authViewModel.firebaseUser.observeAsState()
    val context = LocalContext.current
    val userImage = SharedPref.getImage(context)
    val profilImage = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context).data(userImage).size(Size.ORIGINAL).build()
    ).state


    var inputComment = remember { mutableStateOf("") }
    var imageUrl = remember { mutableStateOf<Uri?>(null) }
    var isPosted = remember { mutableStateOf(false) }


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

    LaunchedEffect(isCommentsPosted) {
        if (isCommentsPosted) {
            Toast.makeText(context, "Reply Added", Toast.LENGTH_LONG).show()
            bottomNavController.navigate(Screens.Home.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
        inputComment.value = ""
        imageUrl.value = null
        isPosted.value = false
    }





    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { bottomNavController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }
                Text(text = "Add Comment")
            }
            if (!isPosted.value) {
                Button(onClick = {
                    isPosted.value = true
                    if (imageUrl.value == null) {
                        mainViewModel.saveCommentData(
                            inputComment.value,
                            "",
                            firebaseUser?.uid,
                            post
                        )
                    } else {
                        mainViewModel.saveCommentImageData(
                            inputComment.value,
                            imageUrl.value,
                            firebaseUser?.uid,
                            post
                        )
                    }
                }) {
                    Text(text = "Reply")
                }
            } else {
                Box (
                    modifier = Modifier.padding(10.dp),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator(color = BlueTheme, modifier = Modifier.size(30.dp))
                }

            }
        }
        Spacer(modifier = Modifier.padding(5.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(45.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (profilImage is AsyncImagePainter.State.Error) {
                    Icon(
                        modifier = Modifier.size(10.dp),
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
            AddPostTextField(
                inputComment.value,
                "Post your reply",
                navController
            ) { inputComment.value = it }
        }
        Spacer(modifier = Modifier.padding(50.dp))
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.background(Color.LightGray),
                contentAlignment = Alignment.TopEnd
            ) {
                if (imageUrl.value != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = imageUrl.value),
                        contentDescription = "post Image",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RectangleShape),
                        contentScale = ContentScale.Crop
                    )
                    Box() {
                        IconButton(onClick = { imageUrl.value = null }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = null)
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = {
                val isgranted = ContextCompat.checkSelfPermission(
                    context, permissionRequest
                ) == PackageManager.PERMISSION_GRANTED

                if (isgranted) {
                    galleryLauncher.launch("image/*")
                } else {
                    permissionLauncher.launch(permissionRequest)
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Icon(imageVector = Icons.Default.Image, contentDescription = null)
            }
        }

    }
}