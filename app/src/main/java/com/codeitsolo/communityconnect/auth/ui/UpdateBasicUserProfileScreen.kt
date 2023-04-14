package com.codeitsolo.communityconnect.auth.ui

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.codeitsolo.communityconnect.R
import com.codeitsolo.communityconnect.utils.DatePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateBasicUserProfileScreen() {
    var firstName by rememberSaveable {
        mutableStateOf("")
    }
    var lastName by rememberSaveable {
        mutableStateOf("")
    }
    var dob by rememberSaveable {
        mutableStateOf("")
    }
    var work by rememberSaveable {
        mutableStateOf("")
    }
    var address by rememberSaveable {
        mutableStateOf("")
    }
    var imageUri by rememberSaveable {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val bitmap = rememberSaveable {
        mutableStateOf<Bitmap?>(null)
    }
    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri -> imageUri = uri }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)

    ) {

        item {
            if (imageUri == null) {
                Image(
                    painter = painterResource(id = R.drawable.profile_pic),
                    contentDescription = null,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .clickable {
                            photoLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        })
            } else {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .clickable {
                            photoLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                )
            }
        }

        item {
            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text(text = "Enter first name") }
            )
        }

        item {
            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(text = "Enter last name") }
            )
        }

        item {
            DatePicker(
                date = dob,
                onDateChange = { dob = it },
                label = { Text(text = "Enter your birthdate") }
            )
        }

        item {
            TextField(
                value = work,
                onValueChange = { work = it },
                label = { Text(text = "Enter your current work") }
            )
        }

        item {
            TextField(
                value = address,
                onValueChange = { address = it },
                label = { Text(text = "Enter your address") },
                singleLine = false
            )
        }

        item {
            Button(onClick = {}) {
                Text(text = "Save Profile")
            }
        }
    }
}
