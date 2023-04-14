package com.codeitsolo.communityconnect.auth.ui

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import com.codeitsolo.communityconnect.utils.PhoneTextField
import com.codeitsolo.communityconnect.utils.getFullPhoneNumber
import com.codeitsolo.communityconnect.utils.getOnlyPhoneNumber
import com.codeitsolo.communityconnect.utils.isPhoneNumber
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

@Composable
fun LoginScreen(onGetOTPClicked: (number: String, verificationId: String) -> Unit) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val phoneNumber = rememberSaveable { mutableStateOf("") }
        val fullPhoneNumber = rememberSaveable { mutableStateOf("") }
        val onlyPhoneNumber = rememberSaveable { mutableStateOf("") }
        var verificationId by rememberSaveable { mutableStateOf("") }
        val context = LocalContext.current

        PhoneTextField(text = phoneNumber.value, onValueChange = { phoneNumber.value = it })
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            if (!isPhoneNumber()) {
                fullPhoneNumber.value = getFullPhoneNumber()
                onlyPhoneNumber.value = getOnlyPhoneNumber()

                lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
                callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        Log.d("TAG", "onVerificationCompleted: Phone verification successful: ${p0.smsCode}")
                        Toast.makeText(context, "${p0.smsCode}", Toast.LENGTH_SHORT).show()
                    }

                    override fun onVerificationFailed(exception: FirebaseException) {
                        Toast.makeText(
                            context,
                            "Phone verification failed: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("TAG", "onVerificationFailed: ${exception.message}")
                    }

                    override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                        super.onCodeSent(p0, p1)
                        verificationId = p0
                        Log.d("TAG", "onCodeSent: $verificationId")
                        onGetOTPClicked(fullPhoneNumber.value, verificationId)
                    }
                }

                sendVerificationCode(
                    phoneNumber = fullPhoneNumber.value,
                    auth = FirebaseAuth.getInstance(),
                    activity = context as Activity,
                    callbacks = callbacks
                )
            } else {
                fullPhoneNumber.value = "Error"
                onlyPhoneNumber.value = "Error"
                Log.d("TAG", "LoginScreen: invalid phone number")
                Toast.makeText(context, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "Login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen {
        number, verificationId ->
        Log.d("TAG", "LoginScreenPreview: $number $verificationId")
    }
}

private fun sendVerificationCode(
    phoneNumber: String,
    auth: FirebaseAuth,
    activity: Activity,
    callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
) {
    val options = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber(phoneNumber)
        .setTimeout(60L, TimeUnit.SECONDS)
        .setActivity(activity)
        .setCallbacks(callbacks)
        .build()

    PhoneAuthProvider.verifyPhoneNumber(options)
}