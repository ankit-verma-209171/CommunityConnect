package com.codeitsolo.communityconnect

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeitsolo.communityconnect.auth.UserState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppStartupViewModel: ViewModel() {
    private var _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private var _userState = MutableStateFlow<UserState>(UserState.NotLoggedIn)
    val userState = _userState.asStateFlow()

    init {
        viewModelScope.launch {
            val user = Firebase.auth.currentUser

            if (user == null) {
                Log.d("community_connect", "user not logged in!")
                _userState.value = UserState.NotLoggedIn
            }

            else {
                Log.d("community_connect", "user logged in! $user")
                _userState.value = UserState.LoggedIn
                user.let {
                    Log.d("community_connect", it.phoneNumber.toString())
                    Log.d("community_connect", "${it.displayName?.isEmpty()}")
                }

                if (user.displayName?.isNotEmpty() == true) {
                    _userState.value = UserState.SavedUser
                }
            }
            delay(100)
            _isLoading.value = false
        }
    }
}