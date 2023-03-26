package com.codeitsolo.communityconnect.auth.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onGetOTPClicked: (String) -> Unit = { /* Do something */}) {
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            placeholder = { Text(text = "Enter a valid phone number") },
            label = { Text(text = "Phone Number") }
        )
        Button(
            onClick = {
                if (phoneNumber.isEmpty() || phoneNumber.isBlank()) {
                    Toast.makeText(context, "Please enter valid phone number!", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                onGetOTPClicked(phoneNumber)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "GET OTP")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}