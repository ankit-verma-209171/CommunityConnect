package com.codeitsolo.communityconnect.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.codeitsolo.communityconnect.auth.ui.LoginScreen
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
                navController.navigate(Screen.Home.route + "/${phoneNumber}")
            })
        }

        composable(
            route = Screen.Home.route + "/{phoneNumber}",
        ) {
            val phoneNumber = it.arguments?.getString("phoneNumber")
            HomeScreen(phoneNumber)
        }
    }
}