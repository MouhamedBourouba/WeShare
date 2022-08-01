package com.example.weshare.ui.screens.edit_account

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.weshare.data.Result
import com.example.weshare.ui.component.Loading
import com.example.weshare.ui.component.MTextFiled
import com.example.weshare.ui.component.TopBarTitle
import com.example.weshare.ui.navigation.ScreensRoutes
import com.example.weshare.ui.theme.WeShareTheme
import com.example.weshare.ui.theme.spacing
import es.dmoral.toasty.Toasty
import java.util.*

@Composable
fun EditAccount(navController: NavController, viewModel: EditProfileViewModel = hiltViewModel()) {
    HandelCallBacks(viewModel, navController)

    WeShareTheme {
        Box(Modifier.fillMaxSize())  {
            if (viewModel.loading)  Loading("Updating Profile ...")
            MainContent(navController, viewModel)
        }
    }
}

@Composable
private fun HandelCallBacks(viewModel: EditProfileViewModel, navController: NavController) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true, block = {
        viewModel.channel.collect {
            when (it) {
                is Result.Error -> {
                    Toasty.error(context, it.message!!, Toasty.LENGTH_LONG).show()
                }
                is Result.Success -> {
                    navController.navigate(ScreensRoutes.Profile.route)
                }
            }
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(navController: NavController, viewModel: EditProfileViewModel) {
    Scaffold(
        topBar = {
            TopBar(navController = navController, viewModel)
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Spacer(modifier = Modifier.padding(top = MaterialTheme.spacing.large))

            ImagePicker(viewModel)

            UpdatingTextFields(viewModel)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun UpdatingTextFields(viewModel: EditProfileViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.medium)
    ) {
        MTextFiled(
            value = viewModel.userName,
            onChangeListener = {
                if (it.toCharArray().size < 30) viewModel.userName = it
            },
            label = "Name",
            placeholder = viewModel.user.username,
            leadingIcon = Icons.Filled.Person,
            focusDirection = FocusDirection.Down,
        )
        MTextFiled(
            value = viewModel.bio,
            placeholder = viewModel.user.bio,
            onChangeListener = {
                if (it.toCharArray().size < 300) {
                    viewModel.bio = it
                }
            },
            label = "Bio",
            leadingIcon = Icons.Filled.Note,
            clearFocus = true,
            maxLines = 5
        )
    }
}


@Composable
fun ImagePicker(viewModel: EditProfileViewModel) {
    val imageIntent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            if (it.data != null) {
                viewModel.imageUri = it.data?.data
            }
        }
    )
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (viewModel.imageUri != null || viewModel.profilePhotoUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(model = if (viewModel.imageUri != null) viewModel.imageUri else viewModel.profilePhotoUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable {
                        if (!viewModel.loading) imageIntent.launch(Intent(Intent.ACTION_PICK).also {
                            it.type = "image/*"
                        })
                    }
            )
        } else IconButton(onClick = {
            imageIntent.launch(Intent(Intent.ACTION_PICK).also {
                it.type = "image/*"
            })
        }) {
            Icon(
                imageVector = Icons.Filled.AddAPhoto,
                contentDescription = null,
                modifier = Modifier.size(260.dp)
            )
        }
        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))

        Text(text = "Change Profile Photo", color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun TopBar(navController: NavController, viewModel: EditProfileViewModel) {
    val keyboard = LocalSoftwareKeyboardController.current
    Column {
        SmallTopAppBar(
            title = {
                TopBarTitle(title = "Edit Profile")
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        navController.navigateUp()
                    },
                ) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        keyboard?.hide()
                        viewModel.updateAccount()
                    },
                    enabled = (viewModel.imageUri != null || viewModel.userName != viewModel.user.username || viewModel.bio != viewModel.user.bio),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Save,
                        contentDescription = null,
                        tint = if ((viewModel.imageUri != null || viewModel.userName != viewModel.user.username || viewModel.bio != viewModel.user.bio)) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.primary.copy(0.5f)
                    )
                }
            }
        )
        Divider(thickness = 1.dp, color = Color.LightGray.copy(0.7f))
    }
}