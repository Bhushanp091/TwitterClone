package com.example.twitterclone.main.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.twitterclone.auth.model.UserModel
import com.example.twitterclone.main.domain.Post
import com.example.twitterclone.main.presentation.viewmodel.MainViewModel
import com.example.twitterclone.util.ui_components.CommentCard
import com.example.twitterclone.util.ui_components.TweetCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    postId: String,
    bottomNavController: NavController
) {

    val mainViewModel = hiltViewModel<MainViewModel>()
    val commentsAndUserData by mainViewModel.commentsAndData.observeAsState(null)
    val postAndUserData by mainViewModel.postsAndData.observeAsState(null)



    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Post",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },

                navigationIcon = {
                    IconButton(
                        onClick = { bottomNavController.popBackStack() },
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
            Divider(color = Color.DarkGray)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            if (postAndUserData.isNullOrEmpty() || commentsAndUserData.isNullOrEmpty()){
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            LazyRow{
                items(postAndUserData?: emptyList()){
                    if (it.first.postId == postId){
                        TweetCard(post = it.first, userModel = it.second, bottomNavController =bottomNavController )
                    }
                }
            }



            Spacer(modifier = Modifier.padding(3.dp))
            Text(
                text = "Comments",
                fontWeight = FontWeight.W900,
                fontSize = 18.sp,
                modifier = Modifier.padding(8.dp),
                color = Color.LightGray
            )
            Divider(color = Color.DarkGray)
            LazyColumn(modifier = Modifier.padding(10.dp)) {
                items(commentsAndUserData ?: emptyList()) {
                    if (it.first.postId == postId) {
                        CommentCard(
                            comment = it.first,
                            userModel = it.second,
                            bottomNavController = bottomNavController
                        )
                        Divider(color = Color.DarkGray)
                    }
                }

            }
        }
    }
}