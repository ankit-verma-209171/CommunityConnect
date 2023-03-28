package com.codeitsolo.communityconnect

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.codeitsolo.communityconnect.auth.UserState
import com.codeitsolo.communityconnect.navigation.Screen
import com.codeitsolo.communityconnect.navigation.SetupNavGraph
import com.codeitsolo.communityconnect.ui.theme.CommunityConnectTheme

@Composable
fun CommunityConnectApp(
    navController: NavHostController = rememberNavController(),
    userState: UserState = UserState.NotLoggedIn
) {
    CommunityConnectTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SetupNavGraph(
                navController = navController,
                startDestination = when (userState) {
                    UserState.LoggedIn -> Screen.UpdateBasicUserProfile.route
                    UserState.NotLoggedIn -> Screen.Login.route
                    UserState.SavedUser -> Screen.Home.route
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommunityConnectAppPreview() {
    CommunityConnectApp()
}