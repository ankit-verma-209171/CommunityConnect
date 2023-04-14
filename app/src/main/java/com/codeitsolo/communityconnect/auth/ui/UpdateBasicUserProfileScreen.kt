package com.codeitsolo.communityconnect.auth.ui

import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.codeitsolo.communityconnect.R
import com.codeitsolo.communityconnect.core.data.UserDetails
import com.codeitsolo.communityconnect.utils.DatePicker
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateBasicUserProfileScreen(onUpdateProfileSuccess: () -> Unit = {}) {
    var firstName by rememberSaveable {
        mutableStateOf("")
    }
    var lastName by rememberSaveable {
        mutableStateOf("")
    }
    val dob = rememberSaveable {
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
    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri -> imageUri = uri }

    val context = LocalContext.current

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
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(96.dp)
                        .clickable {
                            photoLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                )
            } else {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(96.dp)
                        .clickable {
                            photoLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
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
                onDateChange = {
                    dob.value = it
                    Log.d("TAG", "UpdateBasicUserProfileScreen: $dob $it")
                },
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
            Button(onClick = {
                val user = Firebase.auth.currentUser
                val profileUpdates = userProfileChangeRequest {
                    displayName = "$firstName $lastName"
                    photoUri = imageUri
                }
                user?.run {
                    updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(
                                    "TAG",
                                    "UpdateBasicUserProfileScreen: Successful profile saved!"
                                )
                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                                onUpdateProfileSuccess()
                            }
                        }

                    val userDetails = UserDetails(
                        uid = user.uid,
                        firstName = firstName,
                        lastName = lastName,
                        dob = dob.value,
                        work = work,
                        address = address
                    )
                    val database = FirebaseFirestore.getInstance()
                    val collection = database.collection("userDetails")
                    collection.add(userDetails)
                        .addOnSuccessListener {
                            Log.d("TAG", "UpdateBasicUserProfileScreen: Data added to fire store")
                        }
                        .addOnFailureListener {
                            Log.d("TAG", "UpdateBasicUserProfileScreen: Failed!")
                        }
                }

            }) {
                Text(text = "Save Profile")
            }
        }
    }
}

@Preview
@Composable
fun UpdateBasicUserProfilePreview() {
    UpdateBasicUserProfileScreen()
}