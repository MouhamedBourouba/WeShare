package com.example.weshare.ui.screens.search

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.NoAccounts
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.weshare.domain.model.User
import com.example.weshare.ui.component.AnimatedShimmer
import com.example.weshare.ui.component.MTextFiled
import com.example.weshare.ui.theme.spacing
import es.dmoral.toasty.Toasty
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun Search(navController: NavController, searchViewModel: SearchViewModel = hiltViewModel()) {
    HandelCallBacks(viewModel = searchViewModel, context = LocalContext.current)
    Scaffold(
        topBar = { TopBar(navController) }
    ) { paddingValues ->
        Log.d("users", "Search: ${searchViewModel.users}")
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.spacing.small)
        ) {
            MTextFiled(
                value = searchViewModel.textFieldValue,
                onChangeListener = { textField ->
                    searchViewModel.textFieldValue = textField
                    searchViewModel.usersSearchList = searchViewModel.users.filter { it.username.lowercase().contains(textField.lowercase()) }
                },
                placeholder = "User Name",
                leadingIcon = Icons.Filled.PersonSearch,
                maxLines = 1,
                clearFocus = true
            )
            Spacer(modifier = Modifier.padding(top = MaterialTheme.spacing.small))
            ShowUsersList(searchViewModel, searchViewModel.usersSearchList.toMutableStateList())
        }
    }

}

@Composable
private fun ShowUsersList(viewModel: SearchViewModel, users: MutableList<User>) {
    if (viewModel.loading) {
        repeat(10) {
            AnimatedShimmer()
        }
    } else {
        LazyColumn() {
            items(users, key = { it.uid }) {
                UserItem(imageUrl = it.imageUrl, username = it.username)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyItemScope.UserItem(imageUrl: String?, username: String) {

    val noImage = rememberVectorPainter(image = Icons.Filled.NoAccounts)
    Log.d("image", "UserItem: $imageUrl")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(10.dp)
            .animateItemPlacement(
                animationSpec = TweenSpec(
                    durationMillis = 200
                )
            ),
    ) {
        AsyncImage(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            model = imageUrl,
            error = noImage,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            placeholder = rememberVectorPainter(image = Icons.Filled.Downloading)
        )
        Spacer(modifier = Modifier.padding(start = 10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 30.dp),
                text = username,
            )
        }
    }
}

@Composable
private fun TopBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = "Search")
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        }
    )
}

@Composable
private fun HandelCallBacks(viewModel: SearchViewModel, context: Context) {
    LaunchedEffect(key1 = true, block = {
        viewModel.errorChannel.collect {
            Toasty.error(context, it, Toasty.LENGTH_LONG).show()
        }
    })
}