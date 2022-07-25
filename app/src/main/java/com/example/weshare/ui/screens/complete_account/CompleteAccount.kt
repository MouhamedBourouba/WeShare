package com.example.weshare.ui.screens.complete_account

import android.content.Context
import android.content.Intent
import android.widget.NumberPicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.weshare.common.Gender
import com.example.weshare.data.Result
import com.example.weshare.ui.component.Loading
import com.example.weshare.ui.component.MButton
import com.example.weshare.ui.navigation.Graph
import com.example.weshare.ui.theme.facebookBlue
import com.example.weshare.ui.theme.spacing
import es.dmoral.toasty.Toasty

@Composable
fun CompleteAccount(
    navigator: NavController,
    viewModel: CompleteAccountViewModel = hiltViewModel(),
) {
    if (viewModel.createUi.value) {
        MaterialTheme {
            MainContent(viewModel = viewModel, navigator)

            if (viewModel.loading) {
                Loading(loadingText = "updating Account ...")
            }

            HandelCallBacks(
                viewModel = viewModel,
                context = LocalContext.current,
            )
        }
    }
}

@Composable
private fun MainContent(
    viewModel: CompleteAccountViewModel,
    navigator: NavController
) {
    if (viewModel.user.imageUrl == null) {
        ImagePicker(viewModel = viewModel)
    } else if (viewModel.user.age == null) {
        AgePicker(viewModel = viewModel)
    } else if (viewModel.user.gender == null) {
        GenderPicker(viewModel)
    } else {
        LaunchedEffect(key1 = true, block = {
            navigator.popBackStack()
            navigator.navigate(Graph.HOME)
        })
    }
}

@Composable
private fun ImagePicker(viewModel: CompleteAccountViewModel) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            viewModel.imageUri = it.data?.data
        })

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (viewModel.imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = viewModel.imageUri),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .clickable {
                            if (!viewModel.loading) launcher.launch(Intent(Intent.ACTION_PICK).also {
                                it.type = "image/*"
                            })
                        }
                )
            } else IconButton(onClick = {
                launcher.launch(Intent(Intent.ACTION_PICK).also {
                    it.type = "image/*"
                })
            }) {
                Icon(
                    imageVector = Icons.Filled.AddAPhoto,
                    contentDescription = null,
                    modifier = Modifier.size(260.dp)
                )
            }
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))

            Text(text = "Please Pick your Profile Image")

            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))

            MButton(buttonText = "Next", isEnabled = viewModel.imageUri != null) {
                viewModel.uploadImage()
            }
        }
    }
}

@Composable
private fun AgePicker(viewModel: CompleteAccountViewModel) {

    var pickedAge by remember {
        mutableStateOf<Int?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "Please Pick Your Age", fontSize = 24.sp)
        Spacer(modifier = Modifier.padding(top = MaterialTheme.spacing.medium))

        AndroidView(factory = { NumberPicker(it) }) {
            it.maxValue = 99
            it.minValue = 6
            it.setOnValueChangedListener { _, _, newValue ->
                pickedAge = newValue
            }
        }

        Spacer(modifier = Modifier.padding(top = MaterialTheme.spacing.medium))

        MButton(
            buttonText = "next",
            isEnabled = pickedAge != null
        ) {
            viewModel.updateAccount("age", pickedAge!!)
        }
    }
}

@Composable
private fun GenderPicker(viewModel: CompleteAccountViewModel) {
    var pickedGender by remember {
        mutableStateOf<Gender?>(null)
    }
    var femaleBorderColor by remember {
        mutableStateOf(Color.Black)
    }
    var maleBorderColor by remember {
        mutableStateOf<Color>(Color.Black)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Are You",
            fontSize = 24.sp,
            color = when (pickedGender) {
                Gender.Female -> Color.Red.copy(0.7f)
                Gender.Male -> facebookBlue
                null -> Color.Black
            }
        )
        Spacer(modifier = Modifier.padding(top = MaterialTheme.spacing.medium))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GenderBox(gender = Gender.Male, maleBorderColor) {
                maleBorderColor = facebookBlue
                femaleBorderColor = Color.Black
                pickedGender = Gender.Male
            }
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))

            GenderBox(gender = Gender.Female, femaleBorderColor) {
                maleBorderColor = Color.Black
                femaleBorderColor = Color.Red.copy(0.7f)
                pickedGender = Gender.Female
            }
        }

        Spacer(modifier = Modifier.padding(top = MaterialTheme.spacing.medium))

        MButton(
            buttonText = "next",
            isEnabled = pickedGender != null
        ) {
            when (pickedGender!!) {
                Gender.Female -> viewModel.updateAccount("gender", false)
                Gender.Male -> viewModel.updateAccount("gender", true)
            }
        }
    }
}

@Composable
private fun HandelCallBacks(
    viewModel: CompleteAccountViewModel,
    context: Context,
) {
    LaunchedEffect(key1 = true, block = {
        viewModel.taskChannel.collect {
            when (it) {
                is Result.Error -> {
                    Toasty.error(context, it.message ?: "Unknown Error", Toasty.LENGTH_LONG).show()
                }
                is Result.Success -> {}
            }
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenderBox(gender: Gender, borderColor: Color, onClick: () -> Unit) {

    val boxWidth = LocalConfiguration.current.screenWidthDp / 3


    Surface(
        modifier = Modifier
            .padding(MaterialTheme.spacing.small),
        border = BorderStroke(1.dp, borderColor),
        shape = RoundedCornerShape(16.dp),
        onClick = {
            onClick()
        }) {
        Column(
            modifier = Modifier
                .padding(MaterialTheme.spacing.small),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = if (gender is Gender.Male) Icons.Filled.Male else Icons.Filled.Female,
                contentDescription = null,
                tint = borderColor,
                modifier = Modifier.width(boxWidth.dp).height(130.dp)
            )
            Text(
                text = if (gender is Gender.Male) "Male" else "Female",
                fontSize = 19.sp,
                modifier = Modifier.padding(bottom = MaterialTheme.spacing.small),
                color = borderColor
            )
        }
    }
}