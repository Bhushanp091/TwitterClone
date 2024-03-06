package com.example.twitterclone.util.ui_components

import android.content.Context
import android.service.autofill.UserData
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.InsertComment
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.social_media_app.util.Screens
import com.example.twitterclone.auth.model.UserModel
import com.example.twitterclone.main.domain.Comment
import com.example.twitterclone.main.domain.Post
import com.example.twitterclone.ui.theme.fontFamily
import com.example.twitterclone.util.SharedPref

@Composable
fun CommentCard(
    comment: Comment,
    userModel: UserModel,
    bottomNavController: NavController,
) {

    val context = LocalContext.current
    val profilImage = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context).data(userModel.imageUri).size(Size.ORIGINAL)
            .build()
    ).state


    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,

            ) {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(45.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable {
                        bottomNavController.navigate(Screens.Profile.route)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (profilImage is AsyncImagePainter.State.Error) {
                    Icon(
                        modifier = Modifier.size(5.dp),
                        imageVector = Icons.Default.ImageNotSupported,
                        contentDescription = null,
                    )
                }
                if (profilImage is AsyncImagePainter.State.Success) {
                    Image(
                        modifier = Modifier,
                        painter = profilImage.painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }

            }
            Column(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .padding(start = 10.dp)
            ) {
                CommentInfo(comment, userModel, context)
            }

        }

    }


}

@Composable
fun CommentInfo(
    comment: Comment,
    userData: UserModel,
    context: Context,
) {


    val postImage = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context).data(comment.imageUrl).size(Size.ORIGINAL)
            .build()
    ).state



    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = userData.name,
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = comment.timeStamp,
            fontSize = 10.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray
        )
    }
    Text(
        text = comment.comment,
        fontSize = 13.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        color = Color.LightGray,
    )
    if (comment.imageUrl.isNotEmpty()) {
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(5))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {

            if (postImage is AsyncImagePainter.State.Error) {
                Icon(
                    modifier = Modifier.size(10.dp),
                    imageVector = Icons.Default.ImageNotSupported,
                    contentDescription = null,
                )
            }
            if (postImage is AsyncImagePainter.State.Loading) {
                CircularProgressIndicator()
            }
            if (postImage is AsyncImagePainter.State.Success) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = postImage.painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }


    }
    Spacer(modifier = Modifier.padding(8.dp))


}












