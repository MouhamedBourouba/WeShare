package com.example.weshare.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.NoAccounts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.weshare.ui.theme.spacing

@Composable
fun Profile(navController: NavController, viewModel: ProfileViewModel = hiltViewModel()) {
    if (!viewModel.loading) MainContent(navController, viewModel)
    else Text(text = "Loading ...")
}

@Composable
private fun MainContent(navController: NavController, viewModel: ProfileViewModel) {
    Column(
        modifier = Modifier
            .padding(MaterialTheme.spacing.extraSmall)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        AccountState(navController, viewModel)
    }
}

@Composable
private fun AccountState(navController: NavController, viewModel: ProfileViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ProfileImageAndUserName(
            navController,
            username = viewModel.user.username,
            profileImageUrl = viewModel.user.imageUrl,
        )
        Spacer(modifier = Modifier.padding(top = MaterialTheme.spacing.medium))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium)
                .clip(RoundedCornerShape(15))
        ) {
            FollowerAndPosts(viewModel)
        }
    }
}

@Composable
private fun FollowerAndPosts(viewModel: ProfileViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = MaterialTheme.spacing.small),
    ) {
        Spacer(modifier = Modifier.padding(top = MaterialTheme.spacing.medium))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.small),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShowStateText(value = viewModel.user.followers.size.toString(), text = "Followers") {
                // todo: Show Followers List
            }
            ShowStateText(value = viewModel.user.following.size.toString(), text = "Following") {
                // todo: Show Following List
            }
            ShowStateText(value = viewModel.user.posts.size.toString(), text = "Posts") {}
        }
        UserBio(viewModel.user.bio)
    }
}

@Composable
private fun ColumnScope.UserBio(bio: String) {
    Spacer(modifier = Modifier.padding(top = MaterialTheme.spacing.medium))
    Text(
        text = bio, modifier = Modifier
            .padding(
                horizontal = MaterialTheme.spacing.small,
                vertical = MaterialTheme.spacing.small
            )
            .align(Alignment.Start)
    )

}

@Composable
private fun ShowStateText(value: String, text: String, onClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 17.5.sp)
        Text(text = text, fontSize = 17.5.sp)
    }
}

@Composable
fun ProfileImageAndUserName(
    navController: NavController,
    username: String,
    profileImageUrl: String?
) {
    Spacer(modifier = Modifier.padding(top = MaterialTheme.spacing.medium))
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        AsyncImage(
            model = profileImageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(110.dp),
            error = rememberVectorPainter(image = Icons.Filled.NoAccounts),
            placeholder = rememberVectorPainter(image = Icons.Filled.Downloading),
        )
        Spacer(modifier = Modifier.padding(top = MaterialTheme.spacing.small))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = username, fontSize = 24.sp)
        }
    }
}