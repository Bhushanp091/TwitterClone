package com.example.twitterclone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.social_media_app.util.Screens
import com.example.twitterclone.main.presentation.screen.AddCommentScreen
import com.example.twitterclone.main.presentation.screen.AddPostScreen
import com.example.twitterclone.main.presentation.screen.HomeScreen
import com.example.twitterclone.main.presentation.screen.OtherUserScreen
import com.example.twitterclone.main.presentation.screen.PostScreen
import com.example.twitterclone.main.presentation.screen.ProfileScreen
import com.example.twitterclone.main.presentation.screen.SearchScreen
import com.example.twitterclone.ui.theme.BlueTheme

@Composable
fun HomeNavScreen(
    navController: NavHostController
) {

    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(bottomNavController = bottomNavController)}
    ) { paddingValues ->

        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            NavHost(
                navController = bottomNavController,
                startDestination = Screens.Home.route
            ) {
                composable(Screens.Home.route) {
                    HomeScreen(bottomNavController)
                }
                composable(Screens.Search.route) {
                    SearchScreen()
                }
                composable(Screens.AddPost.route) {
                    AddPostScreen(navController, bottomNavController)
                }
                composable(Screens.Profile.route) {
                    ProfileScreen(navController, bottomNavController)
                }
                composable(Screens.AddComment.route) { backstack ->
                    val postId = backstack.arguments?.getString("postId")?: ""
                    AddCommentScreen(postId, navController, bottomNavController)
                }
                composable(Screens.OtherUserProfile.route) {
                    OtherUserScreen(navController, bottomNavController)
                }
                composable(Screens.Posts.route){backstack->
                    val postId = backstack.arguments?.getString("postId")?: ""
                    PostScreen(postId,bottomNavController)
                }

            }
        }
    }
}


@Composable
fun BottomNavigationBar(
    bottomNavController: NavHostController
) {

    val items = listOf(
        BottomItem(
            title = "Home",
            icon = Icons.Rounded.Home
        ),
        BottomItem(
            title = "Search",
            icon = Icons.Rounded.Search
        ), BottomItem(
            title = "Add Post",
            icon = Icons.Rounded.Add
        ), BottomItem(
            title = "Profile",
            icon = Icons.Rounded.Person
        )
    )

    val selected = rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar (

    ){
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            items.forEachIndexed { index, bottomItem ->
                NavigationBarItem(selected = selected.intValue == index, onClick = {
                    selected.intValue = index
                    when (selected.intValue) {
                        0 -> {
                            bottomNavController.popBackStack()
                            bottomNavController.navigate(Screens.Home.route)
                        }

                        1 -> {
                            bottomNavController.popBackStack()
                            bottomNavController.navigate(Screens.Search.route)
                        }

                        2 -> {
                            bottomNavController.popBackStack()
                            bottomNavController.navigate(Screens.AddPost.route)
                        }

                        3 -> {
                            bottomNavController.popBackStack()
                            bottomNavController.navigate(Screens.Profile.route)
                        }
                    }
                }, icon = {
                    Icon(
                        imageVector = bottomItem.icon,
                        contentDescription = bottomItem.title,
                        tint = if (selected.intValue == index){
                            BlueTheme
                        }else{
                            Color.LightGray
                        }
                    )
                }, label = {
                    Text(
                        text = bottomItem.title, color = if (selected.intValue == index){
                            BlueTheme
                        }else{
                            Color.LightGray
                        }
                    )
                })
            }
        }
    }

}

data class BottomItem(
    val title: String, val icon: ImageVector
)