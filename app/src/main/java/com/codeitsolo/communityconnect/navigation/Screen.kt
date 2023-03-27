package com.codeitsolo.communityconnect.navigation

sealed class Screen(val route: String) {
    object Home: Screen(route = "home_screen")
    object Login: Screen(route = "login_screen")
    object VerifyOtp: Screen(route = "verify_otp_screen")
}