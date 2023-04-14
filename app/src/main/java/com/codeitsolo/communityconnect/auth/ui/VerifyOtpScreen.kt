package com.codeitsolo.communityconnect.auth.ui

import android.app.Activity
import android.content.Context
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
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyOtpScreen(
    phoneNumber: String? = null,
    onVerificationSuccessful: () -> Unit = {},
    onVerificationFailed: () -> Unit = {}
) {
    var otp by rememberSaveable { mutableStateOf("") }
    var verificationId by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    var auth = FirebaseAuth.getInstance()
    lateinit var callbacks: OnVerificationStateChangedCallbacks

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
            placeholder = { Text(text = "Enter otp") },
            label = { Text(text = "Otp") }
        )
        Button(
            onClick = {
                if (otp.isEmpty() || otp.isBlank()) {
                    Toast.makeText(context, "Please enter otp to verify!", Toast.LENGTH_SHORT)
                        .show()
                    return@Button
                }

                val credential = PhoneAuthProvider.getCredential(verificationId, otp)
                signInWitPhoneAuthCredentials(
                    credential = credential,
                    auth = auth,
                    activity = context as Activity,
                    context = context,
                    message = message,
                    onVerificationSuccessful = onVerificationSuccessful,
                    onVerificationFailed = onVerificationFailed
                )
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Verify OTP")
        }
    }

    callbacks = object : OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            Toast.makeText(context, "Successful verification", Toast.LENGTH_SHORT).show()
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Toast.makeText(context, "Failed verification ${p0.message}", Toast.LENGTH_SHORT).show()
            // TODO: navigate to login screen || popup the otp screen from navigation stack
            onVerificationFailed()
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            verificationId = p0
        }
    }

    sendVerificationCode(
        phoneNumber = phoneNumber ?: "1223334444",
        auth = auth,
        activity = context as Activity,
        callbacks = callbacks
    )
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
    message: String,
    onVerificationSuccessful: () -> Unit = {},
    onVerificationFailed: () -> Unit = {}
) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Verification successful!", Toast.LENGTH_SHORT).show()
                // TODO: navigate to home screen
                onVerificationSuccessful()
                return@addOnCompleteListener
            }

            if (task.exception is FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(
                    context,
                    "Verification failed! " +
                            "${(task.exception as FirebaseAuthInvalidCredentialsException).message}",
                    Toast.LENGTH_SHORT
                ).show()
                 onVerificationFailed()
            }
        }
}

private fun sendVerificationCode(
    phoneNumber: String,
    auth: FirebaseAuth,
    activity: Activity,
    callbacks: OnVerificationStateChangedCallbacks
) {
    val options = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber(phoneNumber)
        .setTimeout(60L, TimeUnit.SECONDS)
        .setActivity(activity)
        .setCallbacks(callbacks)
        .build()

    PhoneAuthProvider.verifyPhoneNumber(options)
}