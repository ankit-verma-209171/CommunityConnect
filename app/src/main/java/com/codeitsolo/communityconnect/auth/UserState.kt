package com.codeitsolo.communityconnect.auth

sealed interface UserState {
    object NotLoggedIn: UserState
    object LoggedIn: UserState
    object SavedUser: UserState
}