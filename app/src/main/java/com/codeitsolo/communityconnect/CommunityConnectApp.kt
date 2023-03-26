package com.codeitsolo.communityconnect

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.codeitsolo.communityconnect.ui.theme.CommunityConnectTheme

@Composable
fun CommunityConnectApp() {
    CommunityConnectTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommunityConnectAppPreview() {
    CommunityConnectApp()
}