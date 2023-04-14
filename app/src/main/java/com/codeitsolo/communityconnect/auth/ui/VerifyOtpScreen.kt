package com.codeitsolo.communityconnect.auth.ui

import android.app.Activity
import android.content.Context
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyOtpScreen(
    phoneNumber: String? = null,
    verificationId: String? = null,
    onVerificationSuccessful: () -> Unit = {},
    onVerificationFailed: () -> Unit = {}
) {
    var otp by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Phone Number: $phoneNumber")
        TextField(
            value = otp,
            onValueChange = { otp = it },
            placeholder = { Text(text = "Enter otp code") },
            label = { Text(text = "OTP") }
        )
        Button(
            onClick = {
                if (otp.isEmpty() || otp.isBlank()) {
                    Toast.makeText(context, "Please enter otp to verify!", Toast.LENGTH_SHORT)
                        .show()
                    return@Button
                }

                val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
                signInWitPhoneAuthCredentials(
                    credential = credential,
                    auth = auth,
                    activity = context as Activity,
                    context = context,
                    onVerificationSuccessful = onVerificationSuccessful,
                    onVerificationFailed = onVerificationFailed
                )
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Verify OTP")
        }
    }
}

@Preview
@Composable
fun VerifyOtpScreenPreview() {
    VerifyOtpScreen()
}

private fun signInWitPhoneAuthCredentials(
    credential: PhoneAuthCredential,
    auth: FirebaseAuth,
    activity: Activity,
    context: Context,
    onVerificationSuccessful: () -> Unit = {},
    onVerificationFailed: () -> Unit = {}
) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Signed In successful!", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "signInWitPhoneAuthCredentials: signed in successful")
                // TODO: navigate to home screen
                onVerificationSuccessful()
                return@addOnCompleteListener
            }

            if (task.exception is FirebaseAuthInvalidCredentialsException) {
                val errorMessage = (task.exception as FirebaseAuthInvalidCredentialsException).message
                Toast.makeText(
                    context,
                    "Signed In failed! " +
                            "$errorMessage",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("TAG", "signInWitPhoneAuthCredentials: $errorMessage")
                onVerificationFailed()
            }
        }
}
