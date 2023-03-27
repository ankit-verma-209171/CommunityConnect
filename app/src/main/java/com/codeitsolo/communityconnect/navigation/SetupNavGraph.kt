package com.codeitsolo.communityconnect.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.codeitsolo.communityconnect.auth.ui.LoginScreen
import com.codeitsolo.communityconnect.auth.ui.VerifyOtpScreen
import com.codeitsolo.communityconnect.core.ui.HomeScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(
            route = Screen.Login.route,
        ) {
            LoginScreen(onGetOTPClicked = {phoneNumber ->
                navController.navigate(Screen.VerifyOtp.route + "/${phoneNumber}")
            })
        }

        composable(
            route = Screen.VerifyOtp.route + "/{phoneNumber}",
        ) {
            val phoneNumber = it.arguments?.getString("phoneNumber")
            VerifyOtpScreen(phoneNumber)
        }

        composable(
            route = Screen.Home.route,
        ) {
            HomeScreen()
        }
    }
}