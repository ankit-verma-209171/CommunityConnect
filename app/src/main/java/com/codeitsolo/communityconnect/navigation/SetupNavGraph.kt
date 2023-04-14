package com.codeitsolo.communityconnect.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.codeitsolo.communityconnect.auth.ui.LoginScreen
import com.codeitsolo.communityconnect.auth.ui.UpdateBasicUserProfileScreen
import com.codeitsolo.communityconnect.auth.ui.VerifyOtpScreen
import com.codeitsolo.communityconnect.core.ui.HomeScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Screen.Login.route,
        ) {
            LoginScreen(onGetOTPClicked = { phoneNumber, verificationId ->
                navController.navigate(Screen.VerifyOtp.route + "/$phoneNumber/$verificationId")
            })
        }

        composable(
            route = Screen.VerifyOtp.route + "/{phoneNumber}/{verificationId}",
        ) {
            val phoneNumber = it.arguments?.getString("phoneNumber")
            val verificationId = it.arguments?.getString("verificationId")
            VerifyOtpScreen(
                phoneNumber = phoneNumber,
                verificationId = verificationId,
                onVerificationSuccessful = {
                    navController.navigate(Screen.UpdateBasicUserProfile.route) {
                        popUpTo(0)
                    }
                },
                onVerificationFailed = {
                    navController.popBackStack()
                })
        }

        composable(
            route = Screen.Home.route,
        ) {
            HomeScreen()
        }

        composable(
            route = Screen.UpdateBasicUserProfile.route,
        ) {
            UpdateBasicUserProfileScreen {
                navController.navigate(Screen.Home.route) {
                    popUpTo(0)
                }
            }
        }
    }
}